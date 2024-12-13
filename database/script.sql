--Tablas para la base de datos
-- Crear la tabla de artistas
CREATE TABLE artist (
                        id BIGSERIAL PRIMARY KEY,
                        discogs_id BIGSERIAL UNIQUE NOT NULL,
                        name VARCHAR(255) NOT NULL
);

-- Crear la tabla de lanzamientos
CREATE TABLE release (
                         id BIGSERIAL PRIMARY KEY,
                         artist_id BIGINT NOT NULL,
                         title VARCHAR(255) NOT NULL,
                         release_year VARCHAR(5),
                         FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);

-- Crear la tabla de géneros
CREATE TABLE genre (
                       id BIGSERIAL PRIMARY KEY,
                       release_id BIGINT NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       FOREIGN KEY (release_id) REFERENCES release(id) ON DELETE CASCADE
);

-- Crear la tabla de etiquetas
CREATE TABLE tag (
                     id BIGSERIAL PRIMARY KEY,
                     release_id BIGINT NOT NULL,
                     name VARCHAR(255) NOT NULL,
                     FOREIGN KEY (release_id) REFERENCES release(id) ON DELETE CASCADE
);