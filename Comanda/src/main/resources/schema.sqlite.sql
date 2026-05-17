-- SQLite schema for Comanda backend
-- Execute this script on an empty SQLite database.

PRAGMA foreign_keys = ON;

-- Roles in JWT: administrador, personal, usuario

CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY,
  nombre TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  rol TEXT NOT NULL,
  restaurante TEXT NULL,
  avatar TEXT NULL,
  fecha_registro TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurants (
  id INTEGER PRIMARY KEY,
  nombre TEXT NOT NULL UNIQUE,
  tipo TEXT NOT NULL,
  distrito TEXT NOT NULL,
  direccion TEXT NOT NULL,
  mensaje_personalizado TEXT NOT NULL,
  mesas INTEGER NOT NULL DEFAULT 0,
  telefono TEXT NOT NULL,
  email TEXT NOT NULL,
  imagen TEXT NULL,
  horario_apertura TEXT NOT NULL,
  horario_cierre TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurant_requests (
  id INTEGER PRIMARY KEY,
  nombre TEXT NOT NULL,
  propietario TEXT NOT NULL,
  email TEXT NOT NULL,
  tipo TEXT NOT NULL,
  ciudad TEXT NOT NULL,
  telefono TEXT NOT NULL,
  descripcion TEXT NOT NULL,
  fecha TEXT NOT NULL,
  estado TEXT NOT NULL CHECK (estado IN ('pendiente','aceptado','rechazado'))
);

CREATE TABLE IF NOT EXISTS tables (
  id INTEGER PRIMARY KEY,
  restaurant_id INTEGER NOT NULL,
  numero INTEGER NOT NULL,
  capacidad INTEGER NOT NULL,
  estado TEXT NOT NULL CHECK (estado IN ('disponible','reservada')),
  zona TEXT NOT NULL CHECK (zona IN ('Terraza','Salón Interior','VIP')),
  UNIQUE (restaurant_id, numero),
  FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reservations (
  id INTEGER PRIMARY KEY,
  restaurant_id INTEGER NOT NULL,
  cliente TEXT NOT NULL,
  email TEXT NOT NULL,
  tel TEXT NOT NULL,
  fecha TEXT NOT NULL,
  hora TEXT NOT NULL,
  personas INTEGER NOT NULL,
  mesa_numero INTEGER NOT NULL,
  zona TEXT NOT NULL,
  notas TEXT NULL,
  estado TEXT NOT NULL CHECK (estado IN ('pendiente','confirmada','cancelada','cancelada_cliente')),
  FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
  id INTEGER PRIMARY KEY,
  restaurant_id INTEGER NULL,
  usuario TEXT NOT NULL,
  email TEXT NOT NULL,
  telefono TEXT NULL,
  tipo TEXT NOT NULL CHECK (tipo IN ('comentario','reclamo','experiencia')),
  asunto TEXT NOT NULL,
  mensaje TEXT NOT NULL,
  fecha TEXT NOT NULL,
  calificacion INTEGER NULL,
  leido INTEGER NOT NULL DEFAULT 0 CHECK (leido IN (0,1)),
  FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE SET NULL
);

