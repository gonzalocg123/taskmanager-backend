# ⚙️ Task Manager Backend

Backend del proyecto **Task Manager**, una aplicación de gestión de tareas con **Spring Boot 3**, **MySQL** y **Spring Data JPA**.  
Proporciona una API REST segura y escalable para el frontend desarrollado con **React + Vite**.

---

## 🌍 Demo API

API desplegada en **Render**  
👉 [https://taskmanager-backend-megy.onrender.com](https://taskmanager-backend-megy.onrender.com/api)

---

## ✨ Características Principales

- ✅ CRUD completo de tareas 
- ✅ Registro e inicio de sesión de usuarios  
- ✅ Validación de datos y manejo de errores  
- ✅ Persistencia en MySQL con **JPA + Hibernate**  
- ✅ Configuración de **CORS** para el frontend  
- ✅ Despliegue en **Render** con base de datos en **Railway**

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**  
- **Spring Boot 3**  
- **Spring Data JPA**  
- **Spring Security (básico)**  
- **MySQL 8**  
- **Maven**  
- **Render** (deploy)  
- **Railway** (base de datos)  

---

## 🧱 Estructura del Proyecto

```
📦 taskmanager-backend
├── src/
│   ├── main/java/com/taskmanager/
│   │   ├── controller/     # Controladores REST
│   │   ├── service/        # Lógica de negocio
│   │   ├── repository/     # Repositorios JPA
│   │   ├── model/          # Entidades
│   │   ├── config/         # Configuración CORS / Seguridad
│   │   └── TaskManagerApplication.java
│   └── main/resources/
│       ├── application.properties
│       └── schema.sql / data.sql (opcional)
└── pom.xml
```

---

## ⚙️ Configuración

### Archivo `application.properties`

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

---

## 🔐 Endpoints Principales

### Usuarios
| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| POST | `/api/users/register` | Registro de nuevo usuario |
| POST | `/api/users/login` | Inicio de sesión |

### Tareas
| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| GET | `/api/tasks?userEmail={email}` | Obtener tareas del usuario |
| POST | `/api/tasks?userEmail={email}` | Crear nueva tarea |
| PUT | `/api/tasks/{id}?userEmail={email}` | Actualizar tarea |
| DELETE | `/api/tasks/{id}?userEmail={email}` | Eliminar tarea |

---

## 🧩 Ejecución Local

### Requisitos
- Java 17+
- Maven
- MySQL 8

### Pasos
```bash
# Clonar el repositorio
git clone https://github.com/gonzalocg123/taskmanager-backend.git
cd taskmanager-backend

# Configurar base de datos
mysql -u root -p
CREATE DATABASE taskmanager;

# Ejecutar la aplicación
./mvnw spring-boot:run
```

La API estará disponible en:  
👉 http://localhost:8080

---

## ☁️ Despliegue en Render

1. Conecta tu repositorio con Render.  
2. Crea un nuevo servicio web:  
   - **Build Command:** `./mvnw clean package -DskipTests`  
   - **Start Command:** `java -jar target/*.jar`  
3. Añade las variables de entorno de Railway (MySQL).  

---

## 🧠 Seguridad

- Contraseñas cifradas con **BCrypt**  
- Validación de datos en el backend  
- Protección de endpoints por usuario  
- Configuración CORS para dominios específicos

---

## 🧰 Solución de Problemas

### Error de CORS
Verifica que el frontend esté permitido:
```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:5173",
    "https://taskmanager-frontend-azure.vercel.app"
));
```

### Error de Conexión MySQL
Asegúrate de crear la base de datos:
```bash
mysql -u root -p
CREATE DATABASE taskmanager;
```

---

## 👨‍💻 Autor

**Gonzalo C.G.** - Desarrollador Full Stack  
📧 [chicagodinogonzalo@gmail.com](chicagodinogonzalo@gmail.com)  
🐙 [GitHub](https://github.com/gonzalocg123)  
💼 [LinkedIn](https://www.linkedin.com/in/gonzalo-chica-godino-27710a33a/)

---

## 📝 Licencia

Distribuido bajo la licencia **MIT**.  
Consulta el archivo `LICENSE` para más información.

---

<div align="center">

⭐ Si te gusta este proyecto, ¡dale una estrella en GitHub!  

### ⚙️ Backend desarrollado con pasión por [Gonzalo C.G.](https://github.com/gonzalocg123)

</div>
