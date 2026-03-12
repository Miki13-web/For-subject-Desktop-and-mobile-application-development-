# For-subject-Desktop-and-mobile-application-development-

<img width="424" height="233" alt="Zrzut ekranu 2026-03-12 132246" src="https://github.com/user-attachments/assets/2746e966-25ec-437f-9214-045dab85d787" />
<img width="1240" height="936" alt="Zrzut ekranu 2026-03-12 132320" src="https://github.com/user-attachments/assets/85cd0d13-14b4-4cba-8d54-3e5b0cbc69eb" />
<img width="1239" height="949" alt="Zrzut ekranu 2026-03-12 132303" src="https://github.com/user-attachments/assets/ebc6dd38-5bb5-4159-a10a-1e88992ccf74" />

## 🧠 Theoretical Background / Key Concepts Learned

During the development of this REST API, I solidified my understanding of several core backend concepts:

### 1. HTTP Basics & Status Codes
HTTP is the foundation of client-server communication. A client sends a **Request** (HTTP method like `GET`/`POST`, a URL, headers, and an optional body) and receives a **Response** (status code, headers, and data).
* **2xx (Success):** `200 OK` (request succeeded), `201 Created` (new resource successfully saved).
* **4xx (Client Error):** `400 Bad Request` (invalid payload/JSON), `404 Not Found` (wrong URL or missing ID).
* **5xx (Server Error):** `500 Internal Server Error` (unhandled exception or app crash).

<img width="1547" height="152" alt="Zrzut ekranu 2026-03-12 132206" src="https://github.com/user-attachments/assets/74c064f2-e3e6-42ee-91bd-d10c9c97c495" />

### 2. Spring vs. Spring Boot
**Spring** is a powerful core framework, but it requires heavy manual configuration. **Spring Boot** is an extension that auto-configures everything out of the box. It provides pre-configured templates (like an embedded web server) so you can skip the boilerplate and focus directly on business logic.

### 3. REST Controllers & POJOs
* **Controller returns:** A REST controller typically returns raw data (usually JSON) or a `ResponseEntity` object, which allows for manual tweaking of HTTP status codes and headers.
* **POJO (Plain Old Java Object):** A standard, simple Java class with private fields, getters, and setters. It doesn't inherit from any heavy framework classes or implement special framework interfaces (e.g., the `Horse` and `Stable` models).

### 4. Essential Annotations
* `@SpringBootApplication`: The starting point of the app. It enables auto-configuration and component scanning.
* `@RestController`: Marks the class as a web controller that returns data (JSON) rather than rendering HTML views.
* `@RequestMapping` / `@GetMapping` / `@PostMapping`: Maps incoming HTTP requests to specific methods based on the URL path.
* `@Autowired`: Instructs Spring to automatically inject necessary dependencies (beans) into a class.

### 5. Dependency Injection (DI)
Instead of a class creating its own dependencies manually (e.g., using the `new` keyword), the framework provides them from the outside. Spring acts as an IoC (Inversion of Control) container, managing the lifecycle of objects (beans) and injecting them where needed. This keeps the code decoupled, modular, and highly testable.

### 6. Embedded Web Servers (Tomcat, Jetty, Undertow)
These are servlet containers (web servers) that actively listen for incoming HTTP requests and execute the Java application. Spring Boot shines here because it comes with **Tomcat embedded by default**. There is no need to manually install or deploy the app to an external server—it runs as a standalone Java application.

### 7. Exception Handling in Spring Boot
While standard `try-catch` blocks work locally, the best practice in Spring Boot is global exception handling. Using `@ControllerAdvice` and `@ExceptionHandler`, you can catch specific exceptions (e.g., `StableNotFoundException`) across the entire application and translate them into clean, meaningful HTTP error responses instead of returning an ugly `500 Internal Server Error` stack trace to the client.
