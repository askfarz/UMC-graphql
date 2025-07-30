package ru.mts.media.platform.umc.it;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;
import ru.mts.media.platform.umc.AbstractIntegrationTest;
import ru.mts.media.platform.umc.TestObjectMother;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgRepository;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgRepository;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;

import java.time.LocalDateTime;

class SaveEventMutationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected EventPgRepository eventPgRepository;
    @Autowired
    protected VenuePgRepository venuePgRepository;

    private static SaveEventInput INPUT_REQUEST;

    @BeforeAll
    static void beforeAll() {
        String eventName = "FIFA World CUP";
        LocalDateTime startTime = LocalDateTime.parse("2025-07-30T18:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2025-07-30T20:00:00");
        String venueReferenceId = "12121212";

        INPUT_REQUEST = SaveEventInput.newBuilder()
                                      .name(eventName)
                                      .startTime(startTime)
                                      .endTime(endTime)
                                      .venueReferenceId(venueReferenceId)
                                      .build();
    }

    private GraphQlTester.Response executeSaveEvent() {
        return graphQlTester.documentName("saveEvent")
                            .variable("input", INPUT_REQUEST)
                            .execute();
    }

    @Test
    void testSaveEventNotFoundVenueByReferenceId() {
        var response = executeSaveEvent();

        response.errors()
                .satisfy(errors -> {
                    Assertions.assertTrue(errors.get(0).getMessage().contains("Venue with referenceId 12121212 not found"));
                });
    }

    @Test
    void testSaveEventSuccessfully() {
        var existVenue = TestObjectMother.createVenueEntity();
        venuePgRepository.save(existVenue);

        var response = executeSaveEvent();

        response.path("saveEvent")
                .entity(Event.class)
                .satisfies(event -> {
                    Assertions.assertEquals("FIFA World CUP", event.getName());
                    Assertions.assertEquals(LocalDateTime.parse("2025-07-30T18:00:00"), event.getStartTime());
                    Assertions.assertEquals(LocalDateTime.parse("2025-07-30T20:00:00"), event.getEndTime());
                });
    }
}
