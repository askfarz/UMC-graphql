package ru.mts.media.platform.umc.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;
import ru.mts.media.platform.umc.AbstractIntegrationTest;
import ru.mts.media.platform.umc.TestObjectMother;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgRepository;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgRepository;
import ru.mts.media.platform.umc.domain.gql.types.Venue;

import java.time.LocalDateTime;
import java.util.List;

class VenueWithLatestEventsQueryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected EventPgRepository eventPgRepository;
    @Autowired
    protected VenuePgRepository venuePgRepository;

    @AfterEach
    void tearDown() {
        eventPgRepository.deleteAll();
        venuePgRepository.deleteAll();
    }

    private GraphQlTester.Response executeSaveEvent() {
        return graphQlTester.documentName("venueWithLatestEvents").execute();
    }

    @Test
    void testVenueWithLatestEventsQueryNotFoundData() {
        venuePgRepository.deleteAll();

        var response = executeSaveEvent();

        response.path("venueWithLatestEvents")
                .entityList(Venue.class)
                .hasSize(0);
    }

    @Test
    void testVenueWithLatestEventsQuerySuccessfully() {
        var existVenue = TestObjectMother.createVenueEntity();
        var event = TestObjectMother.createEventEntity();
        var eventPgEntity = eventPgRepository.saveAndFlush(event);
        existVenue.setEvents(List.of(eventPgEntity));
        venuePgRepository.saveAndFlush(existVenue);

        var response = executeSaveEvent();

        response.path("venueWithLatestEvents")
                .entityList(Venue.class)
                .hasSize(1)
                .satisfies(venues -> {
                    Venue first = venues.get(0);
                    Assertions.assertEquals("Камп Ноу", first.getName());
                    Assertions.assertEquals("13", first.getExternalId().getExternalId());
                    Assertions.assertEquals("12", first.getExternalId().getProviderId());
                    Assertions.assertEquals("11", first.getExternalId().getBrandId());
                    Assertions.assertEquals(1, first.getEvents().size());
                    Assertions.assertEquals("FIFA World CUP", first.getEvents().getFirst().getName());
                    Assertions.assertEquals(LocalDateTime.parse("2025-07-30T18:00:00"), first.getEvents().getFirst().getStartTime());
                    Assertions.assertEquals(LocalDateTime.parse("2025-07-30T20:00:00"), first.getEvents().getFirst().getEndTime());
                });
    }
}
