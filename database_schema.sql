-- XposeAPI Database Schema


-- =====================================================
-- Core Tables (no foreign key dependencies)
-- =====================================================

-- Address table
CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
);

-- =====================================================
-- Tables with foreign key dependencies
-- =====================================================

-- Contact Information table (depends on Address)
CREATE TABLE contact_information (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    address_id BIGINT,
    FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE
);

-- Artist table (depends on ContactInformation)
CREATE TABLE artist (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    artistic_name VARCHAR(255),
    about TEXT,
    contact_information_id BIGINT,
    FOREIGN KEY (contact_information_id) REFERENCES contact_information(id) ON DELETE CASCADE
);

-- Serie table
CREATE TABLE serie (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Asset table
CREATE TABLE asset (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(255),
    comment TEXT,
    url VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Website Settings table (depends on ContactInformation)
CREATE TABLE website_settings (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    website_name VARCHAR(255) NOT NULL,
    contact_information_id BIGINT,
    fav_icon_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (contact_information_id) REFERENCES contact_information(id) ON DELETE CASCADE
);

-- =====================================================
-- Junction Tables for Many-to-Many Relationships
-- =====================================================

-- Junction table for Asset-Artist many-to-many relationship
CREATE TABLE asset_authors (
    asset_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
    PRIMARY KEY (asset_id, artist_id),
    FOREIGN KEY (asset_id) REFERENCES asset(id) ON DELETE CASCADE,
    FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);

-- Junction table for Asset-Serie many-to-many relationship
CREATE TABLE asset_series (
    asset_id BIGINT NOT NULL,
    serie_id BIGINT NOT NULL,
    PRIMARY KEY (asset_id, serie_id),
    FOREIGN KEY (asset_id) REFERENCES asset(id) ON DELETE CASCADE,
    FOREIGN KEY (serie_id) REFERENCES serie(id) ON DELETE CASCADE
);

-- Junction table for Serie-Artist many-to-many relationship
CREATE TABLE serie_artists (
    serie_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
    PRIMARY KEY (serie_id, artist_id),
    FOREIGN KEY (serie_id) REFERENCES serie(id) ON DELETE CASCADE,
    FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);

-- =====================================================
-- Indexes for better performance
-- =====================================================

-- Indexes on foreign key columns
CREATE INDEX idx_contact_information_address_id ON contact_information(address_id);
CREATE INDEX idx_artist_contact_information_id ON artist(contact_information_id);
CREATE INDEX idx_website_settings_contact_information_id ON website_settings(contact_information_id);

-- Indexes on frequently queried columns
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_asset_active ON asset(active);
CREATE INDEX idx_serie_active ON serie(active);
CREATE INDEX idx_artist_artistic_name ON artist(artistic_name);

-- =====================================================
-- Notes:
-- =====================================================
-- 1. CollectionsPublicRS and LoginRequest are not JPA entities, so no tables created
-- 2. All foreign keys include ON DELETE CASCADE for referential integrity
-- 3. BIGINT used for IDs to match Java Long type
-- 4. VARCHAR(255) used as default string length (can be adjusted based on requirements)
-- 5. TEXT used for longer text fields like description and comment
-- 6. BOOLEAN fields default to TRUE where appropriate
-- 7. created_at uses TIMESTAMP with DEFAULT CURRENT_TIMESTAMP to match Java @PrePersist behavior
