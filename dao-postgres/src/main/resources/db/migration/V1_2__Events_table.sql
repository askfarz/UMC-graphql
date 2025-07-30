-- Author: fzaskarov
-- Task: Create table for Events
-- Date: 2025-07-29

CREATE TABLE events
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    start_time TIMESTAMP    NOT NULL,
    end_time   TIMESTAMP    NOT NULL
);

-- DROP TABLE IF EXISTS events;
