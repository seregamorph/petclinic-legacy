CREATE TABLE IF NOT EXISTS owners (
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    address    VARCHAR(255) NOT NULL,
    city       VARCHAR(80)  NOT NULL,
    telephone  VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS pets (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    birth_date DATE        NOT NULL,
    type       VARCHAR(30) NOT NULL,
    owner_id   INT         NOT NULL REFERENCES owners (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vets (
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    specialty  VARCHAR(80)
);

CREATE TABLE IF NOT EXISTS visits (
    id          SERIAL PRIMARY KEY,
    pet_id      INT          NOT NULL REFERENCES pets (id) ON DELETE CASCADE,
    visit_date  DATE         NOT NULL,
    description VARCHAR(500) NOT NULL
);
