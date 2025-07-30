-- Author: fzaskarov
-- Task: Create table for Events-Venue relationships
-- Date: 2025-07-29

CREATE TABLE events_venue
(
    event_id          BIGINT       NOT NULL,
    venue_brand       VARCHAR(255) NOT NULL,
    venue_provider    VARCHAR(255) NOT NULL,
    venue_external_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (event_id, venue_brand, venue_provider, venue_external_id),
    CONSTRAINT fk_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_venue FOREIGN KEY (venue_brand, venue_provider, venue_external_id) REFERENCES venue (brand, provider, external_id) ON DELETE RESTRICT
);

-- DROP TABLE IF EXISTS events_venue;
