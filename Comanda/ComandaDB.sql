-- =============================================================
--  ComandaDB  –  Script de base de datos PostgreSQL
--  Proyecto: Sistema de Reservas / Comanda  |  Grupo 6
--  Generado a partir de las entidades JPA del backend

-- Pasos para Conectar la BD a pgAdmin 

--HOSTNAME 
-- dpg-d8acrbj7uimc73aaiid0-a

--PORT 
---5432

---DATABASE
---comandadb

--USERNAME
--comandadb_user

--PASSWORD 
--mTjkwtP24bMlS8d56WGbdvPZulJQ5Hzd
-- =============================================================

-- -------------------------------------------------------------
--  1. CREAR Y CONECTARSE A LA BASE DE DATOS
-- -------------------------------------------------------------
CREATE DATABASE "ComandaDB"
    ENCODING    'UTF8'
    LC_COLLATE  'es_PE.UTF-8'
    LC_CTYPE    'es_PE.UTF-8'
    TEMPLATE    template0;

\connect "ComandaDB"

-- -------------------------------------------------------------
--  2. TIPOS ENUMERADOS
-- -------------------------------------------------------------

-- Roles de usuario
CREATE TYPE user_role AS ENUM (
    'ADMINISTRADOR',
    'PERSONAL',
    'USUARIO'
);

-- Estado de reserva
CREATE TYPE reservation_status AS ENUM (
    'pendiente',
    'confirmada',
    'cancelada',
    'cancelada_cliente'
);

-- Tipo de comentario
CREATE TYPE comment_type AS ENUM (
    'comentario',
    'reclamo',
    'experiencia'
);

-- Estado de solicitud de restaurante
CREATE TYPE request_status AS ENUM (
    'pendiente',
    'aceptado',
    'rechazado'
);

-- Estado de mesa
CREATE TYPE table_status AS ENUM (
    'disponible',
    'reservada'
);

-- Zona de mesa
CREATE TYPE table_zone AS ENUM (
    'Terraza',
    'Salón Interior',
    'VIP'
);

-- -------------------------------------------------------------
--  3. TABLA: users
-- -------------------------------------------------------------
CREATE TABLE users (
    id             BIGSERIAL       PRIMARY KEY,
    nombre         VARCHAR(150)    NOT NULL,
    email          VARCHAR(200)    NOT NULL UNIQUE,
    password_hash  VARCHAR(255)    NOT NULL,
    rol            user_role       NOT NULL,
    restaurante    VARCHAR(150),
    avatar         VARCHAR(500),
    fecha_registro DATE            NOT NULL DEFAULT CURRENT_DATE
);

-- -------------------------------------------------------------
--  4. TABLA: restaurants
-- -------------------------------------------------------------
CREATE TABLE restaurants (
    id                    BIGSERIAL       PRIMARY KEY,
    nombre                VARCHAR(200)    NOT NULL UNIQUE,
    tipo                  VARCHAR(100)    NOT NULL,
    distrito              VARCHAR(100)    NOT NULL,
    direccion             VARCHAR(300)    NOT NULL,
    mensaje_personalizado TEXT            NOT NULL,
    mesas                 INTEGER         NOT NULL CHECK (mesas > 0),
    telefono              VARCHAR(20)     NOT NULL,
    email                 VARCHAR(200)    NOT NULL,
    imagen                VARCHAR(500),
    horario_apertura      VARCHAR(10)     NOT NULL,
    horario_cierre        VARCHAR(10)     NOT NULL
);

-- -------------------------------------------------------------
--  5. TABLA: tables  (mesas del restaurante)
-- -------------------------------------------------------------
CREATE TABLE tables (
    id            BIGSERIAL       PRIMARY KEY,
    restaurant_id BIGINT          NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    numero        INTEGER         NOT NULL,
    capacidad     INTEGER         NOT NULL CHECK (capacidad > 0),
    estado        table_status    NOT NULL DEFAULT 'disponible',
    zona          table_zone      NOT NULL,
    CONSTRAINT uk_tables_restaurant_numero UNIQUE (restaurant_id, numero)
);

-- -------------------------------------------------------------
--  6. TABLA: reservations
-- -------------------------------------------------------------
CREATE TABLE reservations (
    id            BIGSERIAL           PRIMARY KEY,
    restaurant_id BIGINT              NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    cliente       VARCHAR(150)        NOT NULL,
    email         VARCHAR(200)        NOT NULL,
    tel           VARCHAR(20)         NOT NULL,
    fecha         DATE                NOT NULL,
    hora          TIME                NOT NULL,
    personas      INTEGER             NOT NULL CHECK (personas > 0),
    mesa_numero   INTEGER             NOT NULL,
    zona          VARCHAR(100)        NOT NULL,
    notas         TEXT,
    estado        reservation_status  NOT NULL DEFAULT 'pendiente'
);

-- -------------------------------------------------------------
--  7. TABLA: comments
-- -------------------------------------------------------------
CREATE TABLE comments (
    id            BIGSERIAL       PRIMARY KEY,
    restaurant_id BIGINT          NOT NULL REFERENCES restaurants(id) ON DELETE CASCADE,
    usuario       VARCHAR(150)    NOT NULL,
    email         VARCHAR(200)    NOT NULL,
    telefono      VARCHAR(20),
    tipo          comment_type    NOT NULL,
    asunto        VARCHAR(300)    NOT NULL,
    mensaje       TEXT            NOT NULL,
    fecha         DATE            NOT NULL DEFAULT CURRENT_DATE,
    calificacion  INTEGER         CHECK (calificacion BETWEEN 1 AND 5),
    leido         BOOLEAN         NOT NULL DEFAULT FALSE
);

-- -------------------------------------------------------------
--  8. TABLA: restaurant_requests  (solicitudes de alta)
-- -------------------------------------------------------------
CREATE TABLE restaurant_requests (
    id          BIGSERIAL       PRIMARY KEY,
    nombre      VARCHAR(200)    NOT NULL,
    propietario VARCHAR(150)    NOT NULL,
    email       VARCHAR(200)    NOT NULL,
    tipo        VARCHAR(100)    NOT NULL,
    ciudad      VARCHAR(100)    NOT NULL,
    telefono    VARCHAR(20)     NOT NULL,
    descripcion TEXT            NOT NULL,
    fecha       DATE            NOT NULL DEFAULT CURRENT_DATE,
    estado      request_status  NOT NULL DEFAULT 'pendiente'
);

-- -------------------------------------------------------------
--  9. ÍNDICES  (mejoran búsquedas frecuentes)
-- -------------------------------------------------------------
CREATE INDEX idx_reservations_restaurant  ON reservations(restaurant_id);
CREATE INDEX idx_reservations_fecha       ON reservations(fecha);
CREATE INDEX idx_reservations_estado      ON reservations(estado);
CREATE INDEX idx_tables_restaurant        ON tables(restaurant_id);
CREATE INDEX idx_comments_restaurant      ON comments(restaurant_id);
CREATE INDEX idx_comments_leido           ON comments(leido);
CREATE INDEX idx_restaurant_requests_est  ON restaurant_requests(estado);

-- -------------------------------------------------------------
-- 10. DATOS INICIALES  (seed — equivalente al DataInitializer)
-- -------------------------------------------------------------

-- Usuario administrador por defecto
-- Contraseña: admin123  (hash BCrypt generado externamente)
-- En producción reemplaza el hash por uno generado con tu encoder
INSERT INTO users (nombre, email, password_hash, rol, restaurante, avatar, fecha_registro)
VALUES (
    'Administrador',
    'admin@comanda.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lBb2',  -- admin123
    'ADMINISTRADOR',
    NULL,
    NULL,
    CURRENT_DATE
);

-- =============================================================
--  FIN DEL SCRIPT  –  ComandaDB
-- =============================================================