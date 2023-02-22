DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_to_events CASCADE;
DROP TABLE IF EXISTS participations CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(2048)                           NOT NULL,
    email VARCHAR(2048)                           NOT NULL,

    CONSTRAINT pk_user PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(2048)  UNIQUE                   NOT NULL,

    CONSTRAINT pk_category PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2048)                           NOT NULL,
    category_id        BIGINT REFERENCES categories (id),
    description        VARCHAR(2048)                           NOT NULL,
    eventDate          TIMESTAMP                               NOT NULL,
    created_on         TIMESTAMP                               NOT NULL,
    published_on       TIMESTAMP                               NOT NULL,
    state              INTEGER                                 NOT NULL,
    initiator          BIGINT                                  NOT NULL,
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INT                                     NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    title              VARCHAR(2048)                           NOT NULL,

    CONSTRAINT pk_event PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS compilations
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT REFERENCES events (id),
    pinned   BOOLEAN                                 NOT NULL,
    title    VARCHAR(2048)                           NOT NULL,

    CONSTRAINT pk_compilation PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS compilation_to_events
(
    event_id BIGINT REFERENCES events (id),
    comp_id  BIGINT REFERENCES compilations (id)
);
CREATE TABLE IF NOT EXISTS participations(
        id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
        event_id BIGINT REFERENCES events (id),
        requester_id BIGINT REFERENCES users(id),
        created   TIMESTAMP                                 NOT NULL,
        status    INTEGER                          NOT NULL,

        CONSTRAINT pk_participation PRIMARY KEY (id)


)