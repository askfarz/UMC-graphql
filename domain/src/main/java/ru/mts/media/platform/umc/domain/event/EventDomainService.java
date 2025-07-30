package ru.mts.media.platform.umc.domain.event;

import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;
import ru.mts.media.platform.umc.domain.venue.VenueSot;

@Service
@RequiredArgsConstructor
public class EventDomainService {
    private final ApplicationEventPublisher eventPublisher;
    private final VenueSot sot;
    private final EventDomainServiceMapper eventMapper;

    public EventSave save(SaveEventInput input) {
        var evt = sot.getVenueByReferenceId(input.getVenueReferenceId())
                     .map(venue -> eventMapper.mapEvent(input, venue))
                     .map(EventSave::new)
                     .orElseThrow(() -> new DgsEntityNotFoundException(String.format("Venue with referenceId %s not found", input.getVenueReferenceId())));

        eventPublisher.publishEvent(evt);

        return evt;
    }
}
