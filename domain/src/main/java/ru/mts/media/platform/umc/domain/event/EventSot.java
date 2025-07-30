package ru.mts.media.platform.umc.domain.event;

import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.FullExternalId;

import java.util.List;

public interface EventSot {

    List<Event> getEvents();

    List<Event> getAllByIdIn(List<Long> ids);

    List<List<Event>> getLatestEvents(List<FullExternalId> keys);
}
