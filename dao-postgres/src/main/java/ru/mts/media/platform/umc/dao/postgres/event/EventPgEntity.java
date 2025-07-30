package ru.mts.media.platform.umc.dao.postgres.event;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class EventPgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "events_venue",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "venue_brand", referencedColumnName = "brand"),
                    @JoinColumn(name = "venue_provider", referencedColumnName = "provider"),
                    @JoinColumn(name = "venue_external_id", referencedColumnName = "external_id")
            }
    )
    @ToString.Exclude
    private List<VenuePgEntity> venues;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Override
    public final boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        Class<?> oEffectiveClass =
                object instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        EventPgEntity that = (EventPgEntity) object;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
