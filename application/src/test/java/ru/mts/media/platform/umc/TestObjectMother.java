package ru.mts.media.platform.umc;

import ru.mts.media.platform.umc.dao.postgres.event.EventPgEntity;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgEntity;

import java.time.LocalDateTime;

public class TestObjectMother {

    public static VenuePgEntity createVenueEntity() {
        return VenuePgEntity.builder()
                            .brand("11")
                            .provider("12")
                            .externalId("13")
                            .name("Камп Ноу")
                            .referenceId("12121212")
                            .build();
    }

    public static EventPgEntity createEventEntity() {
        return EventPgEntity.builder()
                            .startTime(LocalDateTime.parse("2025-07-30T18:00:00"))
                            .endTime(LocalDateTime.parse("2025-07-30T20:00:00"))
                            .name("FIFA World CUP")
                            .build();
    }
}
