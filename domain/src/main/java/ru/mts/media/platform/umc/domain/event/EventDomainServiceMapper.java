package ru.mts.media.platform.umc.domain.event;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;
import ru.mts.media.platform.umc.domain.gql.types.Venue;

import java.util.ArrayList;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
interface EventDomainServiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venues", ignore = true)
    @Mapping(target = "name", source = "input.name")
    Event mapEvent(SaveEventInput input, Venue venue);

    @AfterMapping
    default void addVenueToEvent(@MappingTarget Event event, Venue venue) {
        if (event.getVenues() == null) {
            event.setVenues(new ArrayList<>());
        }
        event.getVenues().add(venue);
    }
}
