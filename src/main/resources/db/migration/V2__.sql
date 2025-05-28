CREATE TABLE owner
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    profile_id UUID,
    address    VARCHAR(255),
    CONSTRAINT pk_owner PRIMARY KEY (id)
);

CREATE TABLE ownership_records
(
    id              UUID             NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    purchase_price  DOUBLE PRECISION NOT NULL,
    transfer_date   TIMESTAMP WITHOUT TIME ZONE,
    vehicle_id      UUID,
    owner_id        UUID,
    plate_number_id UUID,
    CONSTRAINT pk_ownership_records PRIMARY KEY (id)
);

CREATE TABLE plate_numbers
(
    id           UUID NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    plate_number VARCHAR(255),
    issued_date  TIMESTAMP WITHOUT TIME ZONE,
    plate_status VARCHAR(255),
    owner_id     UUID,
    CONSTRAINT pk_plate_numbers PRIMARY KEY (id)
);

CREATE TABLE vehicles
(
    id                UUID             NOT NULL,
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    updated_at        TIMESTAMP WITHOUT TIME ZONE,
    chassis_number    VARCHAR(255)     NOT NULL,
    manufacturer      VARCHAR(255),
    manufactured_year INTEGER          NOT NULL,
    model             VARCHAR(255),
    price             DOUBLE PRECISION NOT NULL,
    owner_id          UUID,
    current_plate_id  UUID,
    CONSTRAINT pk_vehicles PRIMARY KEY (id)
);

ALTER TABLE owner
    ADD CONSTRAINT uc_owner_profile UNIQUE (profile_id);

ALTER TABLE plate_numbers
    ADD CONSTRAINT uc_plate_numbers_platenumber UNIQUE (plate_number);

ALTER TABLE vehicles
    ADD CONSTRAINT uc_vehicles_chassisnumber UNIQUE (chassis_number);

ALTER TABLE vehicles
    ADD CONSTRAINT uc_vehicles_current_plate UNIQUE (current_plate_id);

ALTER TABLE ownership_records
    ADD CONSTRAINT FK_OWNERSHIP_RECORDS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES owner (id);

ALTER TABLE ownership_records
    ADD CONSTRAINT FK_OWNERSHIP_RECORDS_ON_PLATE_NUMBER FOREIGN KEY (plate_number_id) REFERENCES plate_numbers (id);

ALTER TABLE ownership_records
    ADD CONSTRAINT FK_OWNERSHIP_RECORDS_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicles (id);

ALTER TABLE owner
    ADD CONSTRAINT FK_OWNER_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES users (id);

ALTER TABLE plate_numbers
    ADD CONSTRAINT FK_PLATE_NUMBERS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES owner (id);

ALTER TABLE vehicles
    ADD CONSTRAINT FK_VEHICLES_ON_CURRENT_PLATE FOREIGN KEY (current_plate_id) REFERENCES plate_numbers (id);

ALTER TABLE vehicles
    ADD CONSTRAINT FK_VEHICLES_ON_OWNER FOREIGN KEY (owner_id) REFERENCES owner (id);