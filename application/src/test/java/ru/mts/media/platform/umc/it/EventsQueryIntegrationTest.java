package ru.mts.media.platform.umc.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;
import ru.mts.media.platform.umc.AbstractIntegrationTest;
import ru.mts.media.platform.umc.TestObjectMother;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgRepository;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgEntity;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgRepository;
import ru.mts.media.platform.umc.domain.gql.types.Event;

import java.time.LocalDateTime;
import java.util.List;

class EventsQueryIntegrationTest extends AbstractIntegrationTest {

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
        return graphQlTester.documentName("events").execute();
    }

    @Test
    void testEventsQueryNotFoundData() {
        var response = executeSaveEvent();

        response.path("events")
                .entityList(Event.class)
                .hasSize(0);
    }

    @Test
    void testEventsQuerySuccessfully() {
        var venue = TestObjectMother.createVenueEntity();
        var event = TestObjectMother.createEventEntity();
        VenuePgEntity venuePgEntity = venuePgRepository.saveAndFlush(venue);
        event.setVenues(List.of(venuePgEntity));
        eventPgRepository.saveAndFlush(event);

        var response = executeSaveEvent();

        response.path("events")
                .entityList(Event.class)
                .hasSize(1)
                .satisfies(events -> {
                    Event first = events.get(0);
                    Assertions.assertEquals("FIFA World CUP", first.getName());
                    Assertions.assertEquals(LocalDateTime.parse("2025-07-30T18:00:00"), first.getStartTime());
                    Assertions.assertEquals(LocalDateTime.parse("2025-07-30T20:00:00"), first.getEndTime());
                    Assertions.assertEquals(1, first.getVenues().size());
                    Assertions.assertEquals("Камп Ноу", first.getVenues().getFirst().getName());
                    Assertions.assertEquals("13", first.getVenues().getFirst().getExternalId().getExternalId());
                });
    }
}
