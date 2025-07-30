package ru.mts.media.platform.umc.dao.postgres.event;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventPgRepository extends JpaRepository<EventPgEntity, Long> {

    @EntityGraph(attributePaths = {"venues"})
    List<EventPgEntity> findAllByIdIn(List<Long> ids);

    @Query("""
                SELECT e FROM EventPgEntity e
                JOIN FETCH e.venues v
                WHERE (v.brand, v.provider, v.externalId) IN :keys
                ORDER BY v.brand, v.provider, v.externalId, e.startTime DESC
            """)
    List<EventPgEntity> findAllByVenueKeys(@Param("keys") List<Object[]> keys);
}
