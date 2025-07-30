package ru.mts.media.platform.umc.api.gql.venue;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.FullExternalId;
import ru.mts.media.platform.umc.domain.gql.types.Venue;
import ru.mts.media.platform.umc.domain.venue.VenueSot;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class VenueDgsQuery {
    private final VenueSot sot;

    @DgsQuery
    public Venue venueByReferenceId(@InputArgument String id) {
        return Optional.of(id).flatMap(sot::getVenueByReferenceId).orElse(null);
    }

    @DgsQuery
    public List<Venue> venueWithLatestEvents() {
        return sot.getVenues();
    }

    @DgsData(parentType = "Venue", field = "events")
    public CompletableFuture<List<Event>> resolveEvents(DgsDataFetchingEnvironment dfe) {
        Venue venue = dfe.getSource();
        DataLoader<FullExternalId, List<Event>> loader = dfe.getDataLoader("venueLatestEvent");
        FullExternalId key = Optional.ofNullable(venue)
                                           .map(Venue::getExternalId)
                                           .orElse(null);
        return loader.load(key);
    }
}
