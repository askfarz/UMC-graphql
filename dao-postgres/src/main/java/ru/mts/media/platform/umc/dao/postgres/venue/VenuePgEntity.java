package ru.mts.media.platform.umc.dao.postgres.venue;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.mts.media.platform.umc.dao.postgres.common.FullExternalIdPk;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgEntity;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FullExternalIdPk.class)
@Table(name = "venue",
        indexes = {
                @Index(name = "idx_venue_referenceId",
                        columnList = "referenceId",
                        unique = true)
        })
public class VenuePgEntity {
    @Id
    private String brand;

    @Id
    private String provider;

    @Id
    @Column(name = "external_id", nullable = false)
    private String externalId;

    private String referenceId;

    private String name;

    @ManyToMany(mappedBy = "venues", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<EventPgEntity> events;

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
        VenuePgEntity that = (VenuePgEntity) object;
        return getBrand() != null && Objects.equals(getBrand(), that.getBrand())
                && getProvider() != null && Objects.equals(getProvider(), that.getProvider())
                && getExternalId() != null && Objects.equals(getExternalId(), that.getExternalId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(brand, provider, externalId);
    }
}
