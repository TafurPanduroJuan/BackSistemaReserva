# Comanda — Backend

API REST del sistema de reservas **Comanda**, desarrollada con Spring Boot 3 y PostgreSQL. Gestiona autenticación JWT, restaurantes, mesas, reservas, comentarios y notificaciones.

Repositorio del frontend: [FrontSistemaReserva](https://github.com/TafurPanduroJuan/FrontSistemaReserva.git)

---

## Tecnologías

- Java 21 + Spring Boot 3.4.5
- Spring Security + JWT (jjwt)
- Spring Data JPA + Hibernate
- PostgreSQL 17
- SendGrid (envío de emails)
- Springdoc OpenAPI (Swagger UI)
- Maven 3

---

## Estructura de paquetes

```
com.grupo6.Comanda/
├── auth/               # Login, registro, recuperación de contraseña, EmailService
├── controller/         # 6 REST controllers (restaurants, tables, reservations, comments, users, notifications)
├── service/            # Interfaces + implementaciones de la lógica de negocio
├── repository/         # Repositorios JPA (uno por entidad)
├── model/entities/     # 7 entidades: User, Restaurant, Table, Reservation, Comment, Notification, RestaurantRequest
├── security/           # JwtService, JwtFilter, SecurityConfig, CustomUserDetailsService
└── config/             # SwaggerConfig
```

---

## Requisitos previos

- Java 21
- Maven 3
- PostgreSQL 17 con una base de datos creada (ej: `comanda`)
- Cuenta de SendGrid para el envío de emails (opcional en desarrollo)

---

## Instalación

```bash
# 1. Clonar el repositorio
git clone https://github.com/TafurPanduroJuan/BackSistemaReserva.git
cd BackSistemaReserva/Comanda
```

Configurar `src/main/resources/application.properties` con las variables de entorno o directamente con tus datos locales:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/comanda
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

comanda.security.jwt.secret=tu_jwt_secret
comanda.security.jwt.expiration-ms=28800000

# CORS — URL del frontend
comanda.cors.allowed-origins=http://localhost:5173
```

```bash
# 2. Crear las tablas con el script incluido
psql -U tu_usuario -d comanda -f ComandaDB.sql

# 3. Compilar y ejecutar
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## Endpoints principales

### Auth — `/api/auth`
| Método | Ruta | Acceso |
|---|---|---|
| POST | `/login` | Público |
| POST | `/register` | Público |
| POST | `/forgot-password` | Público |
| POST | `/reset-password` | Público |

### Restaurantes — `/api/restaurants`
| Método | Ruta | Acceso |
|---|---|---|
| GET | `/` | Público |
| GET | `/{id}` | Público |
| GET | `/{id}/stats` | Público |
| POST | `/` | Admin |
| PUT | `/{id}` | Admin / Personal |
| DELETE | `/{id}` | Admin |
| GET | `/requests` | Admin |
| POST | `/requests` | Público |
| PUT | `/requests/{id}/accept` | Admin |
| PUT | `/requests/{id}/reject` | Admin |

### Mesas — `/api/tables`
| Método | Ruta | Acceso |
|---|---|---|
| GET | `/` | Personal / Admin |
| POST | `/` | Personal / Admin |
| GET | `/available` | Público |
| POST | `/reserve` | Público |
| DELETE | `/{id}` | Personal / Admin |

### Reservas — `/api/reservations`
| Método | Ruta | Acceso |
|---|---|---|
| GET | `/` | Personal / Admin |
| GET | `/me` | Usuario autenticado |
| PATCH | `/{id}/status` | Personal / Admin |

### Comentarios — `/api/comments`
| Método | Ruta | Acceso |
|---|---|---|
| GET | `/` | Público |
| POST | `/` | Público |
| GET | `/my-restaurant` | Personal |
| PUT | `/{id}/read` | Personal |
| PUT | `/{id}/respond` | Personal |
| GET | `/unread-count` | Personal |
| DELETE | `/{id}` | Admin |

### Usuarios — `/api/users`
| Método | Ruta | Acceso |
|---|---|---|
| GET | `/me` | Autenticado |
| PUT | `/me` | Autenticado |
| GET | `/` | Admin |
| PUT | `/{id}/role` | Admin |
| DELETE | `/{id}` | Admin |

---

## Despliegue

El backend está desplegado en Render:
`https://backsistemareserva-1fzv.onrender.com`

Swagger UI en producción:
`https://backsistemareserva-1fzv.onrender.com/comanda/api-docs`

Colección Apidog (documentación y pruebas):
`https://ms7yixnffg.apidog.io`

Para desplegar en Render, las variables de entorno que necesita el servicio son:

```
DATABASE_URL
JWT_SECRET
CORS_ORIGINS
SENDGRID_API_KEY
PORT
```

---

## Credenciales de prueba

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | admin@comanda.com | admin123 |
| Personal | PFogon@comanda.com | fogon12345 |
| Usuario | diego12@comanda.com | diego12345 |
