package ru.mts.media.platform.umc.api.gql.event;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import ru.mts.media.platform.umc.domain.event.EventSot;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.Venue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "eventVenues")
@RequiredArgsConstructor
public class EventVenuesDataLoader implements BatchLoader<Long, List<Venue>> {

    private final EventSot sot;

    @Override
    public CompletionStage<List<List<Venue>>> load(List<Long> eventIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<Event> events = sot.getAllByIdIn(eventIds);
            return events.stream()
                         .map(Event::getVenues)
                         .toList();
        });
    }
}
