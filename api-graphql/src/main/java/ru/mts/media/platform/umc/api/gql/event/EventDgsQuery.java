package ru.mts.media.platform.umc.api.gql.event;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import ru.mts.media.platform.umc.domain.event.EventSot;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.Venue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class EventDgsQuery {

    private final EventSot sot;

    @DgsQuery
    public List<Event> events() {
        return sot.getEvents();
    }

    @DgsData(parentType = "Event", field = "venues")
    public CompletableFuture<List<Venue>> getVenues(DgsDataFetchingEnvironment dfe) {
        Event event = dfe.getSource();
        DataLoader<Long, List<Venue>> dataLoader = dfe.getDataLoader("eventVenues");
        return dataLoader.load(Long.valueOf(event.getId()));
    }
}
