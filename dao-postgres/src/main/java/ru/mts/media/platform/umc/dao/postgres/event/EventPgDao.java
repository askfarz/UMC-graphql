package ru.mts.media.platform.umc.dao.postgres.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgEntity;
import ru.mts.media.platform.umc.domain.event.EventSave;
import ru.mts.media.platform.umc.domain.event.EventSot;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.FullExternalId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
class EventPgDao implements EventSot {
    private final EventPgRepository repository;
    private final EventPgMapper mapper;

    @Override
    public List<Event> getEvents() {
        return repository.findAll().stream()
                         .map(mapper::asModel)
                         .toList();
    }

    @Override
    public List<Event> getAllByIdIn(List<Long> ids) {
        return repository.findAllByIdIn(ids).stream()
                         .map(mapper::asModel)
                         .toList();
    }

    @Override
    public List<List<Event>> getLatestEvents(List<FullExternalId> keys) {
        List<Object[]> keyTuples = mapKeys(keys);

        var allEvents = repository.findAllByVenueKeys(keyTuples);

        var latestEventPerVenue = allEvents.stream()
                                           .flatMap(event -> event.getVenues().stream()
                                                                  .map(venue -> Map.entry(mapPk(venue), mapper.asModel(event))))
                                           .collect(
                                                   Collectors.toMap(
                                                           Map.Entry::getKey,
                                                           Map.Entry::getValue,
                                                           (existing, replacement) -> existing
                                                   ));

        return keys.stream()
                   .map(key -> {
                       var e = latestEventPerVenue.get(key);
                       return e != null ? List.of(e) : new ArrayList<Event>();
                   })
                   .toList();
    }

    @EventListener
    public void handleVenueCreatedEvent(EventSave evt) {
        evt.unwrap()
           .map(mapper::asEntity)
           .ifPresent(repository::save);
    }

    private FullExternalId mapPk(VenuePgEntity venue) {
        return new FullExternalId(venue.getExternalId(), venue.getProvider(), venue.getExternalId());
    }

    private List<Object[]> mapKeys(List<FullExternalId> keys) {
        return keys.stream()
                   .map(key -> new Object[] {key.getBrandId(), key.getProviderId(), key.getExternalId()})
                   .collect(Collectors.toCollection(ArrayList::new));
    }
}
