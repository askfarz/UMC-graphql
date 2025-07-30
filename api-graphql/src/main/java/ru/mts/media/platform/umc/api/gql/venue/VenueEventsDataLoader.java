package ru.mts.media.platform.umc.api.gql.venue;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import ru.mts.media.platform.umc.domain.event.EventSot;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.FullExternalId;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "venueLatestEvent")
@RequiredArgsConstructor
public class VenueEventsDataLoader implements BatchLoader<FullExternalId, List<Event>> {

    private final EventSot sot;

    @Override
    public CompletionStage<List<List<Event>>> load(List<FullExternalId> keys) {
        var allEvents = sot.getLatestEvents(keys);
        return CompletableFuture.completedFuture(allEvents);
    }
}
