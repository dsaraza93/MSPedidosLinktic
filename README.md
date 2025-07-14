# 📦 Servicio Gestión de Pedidos

Este microservicio permite gestionar el inventario de productos, permitiendo consultas y ajustes de stock mediante delta. Se comunica con el microservicio de catálogo y expone una API REST documentada con Swagger.

---

## 🚀 Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Data JPA**
- **H2 Database (en memoria)**
- **Swagger/OpenAPI (springdoc-openapi)**
- **Lombok**
- **Gradle**

---

## 📂 Estructura del proyecto

- `controller` – Endpoints REST (`/api/inventory`)
- `service` – Lógica de negocio
- `dto` – Objetos de solicitud y respuesta para ajuste de stock
- `model` – Entidad `Inventory`
- `repository` – Persistencia con JPA
- `resources/application.yml` – Configuración general, conexión y dependencias externas

---

## ⚙️ Configuración por defecto

Archivo: `src/main/resources/application.yml`

```yaml
server:
  port: 8082

spring:
  application:
    name: servicioGestionDePedidos

  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  h2:
    console:
      enabled: true
      path: /h2-console

catalogo:
  base-url: http://localhost:8081
  api-key  : A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6
```

---

## 🧪 Endpoints disponibles

| Método | Endpoint               | Descripción                                      |
|--------|------------------------|--------------------------------------------------|
| GET    | `/api/inventory`       | Consultar stock completo                         |
| PUT    | `/api/inventory/adjust/{id}` | Ajustar cantidad (requiere body con delta) |

---

## 🔐 Seguridad

Este servicio no implementa autenticación propia, pero incluye la configuración para comunicarse con el microservicio de catálogo mediante una API Key.

---

## 📚 Swagger / OpenAPI

Accede a la documentación automática en Swagger UI:

```
http://localhost:8082/swagger-ui.html
```

Especificación OpenAPI (JSON):

```
http://localhost:8082/v3/api-docs
```

---

## ▶️ Cómo ejecutar el proyecto

```bash
./gradlew bootRun
```

---

## 🧪 Consola H2 (opcional)

Puedes acceder a la base de datos en memoria desde:

```
http://localhost:8082/h2-console
```

- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`

---


## 👨‍💻 Autor

Desarrollado por **Daniel Saraza**  
GitHub: [@dsaraza93](https://github.com/dsaraza93)
