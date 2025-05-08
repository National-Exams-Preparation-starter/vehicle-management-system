CREATE TABLE roles
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    role_name  VARCHAR(255),
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    role_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id                               UUID    NOT NULL,
    created_at                       TIMESTAMP WITHOUT TIME ZONE,
    updated_at                       TIMESTAMP WITHOUT TIME ZONE,
    first_name                       VARCHAR(255),
    last_name                        VARCHAR(255),
    phone_number                     VARCHAR(255),
    national_id                      VARCHAR(255),
    email                            VARCHAR(255),
    password                         VARCHAR(255),
    account_status                   VARCHAR(255),
    is_verified                      BOOLEAN NOT NULL,
    password_reset_code              VARCHAR(255),
    password_reset_code_generated_at TIMESTAMP WITHOUT TIME ZONE,
    verification_code                VARCHAR(255),
    verification_code_created_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_nationalid UNIQUE (national_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_phonenumber UNIQUE (phone_number);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);