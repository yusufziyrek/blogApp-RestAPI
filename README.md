### BlogApp-RestAPI Documentation

**Overview:**  
BlogApp-RestAPI is a RESTful API that enables users to interact with blog posts, comments, and likes. It provides comprehensive CRUD operations for users, posts, comments, and likes, offering a fully-featured blog experience with secure authentication and role-based authorization.

---

### **1. Features:**
- **User Management:**  
  User registration, update, delete, and listing.
- **Post Management:**  
  Create, read, update, and delete blog posts.
- **Comments & Likes:**  
  Users can comment on and like posts.
- **JWT Authentication & Refresh Token Security:**  
  Secured endpoints with JWT-based authentication, refresh token rotation, device tracking, and role-based access control.
- **Advanced Token Management:**  
  Persistent refresh tokens with automatic cleanup, session limits, and secure logout functionality.

---

### **2. Tech Stack:**
- **Java 25 & Spring Boot 3.5.6:**  
  API development, dependency injection, and MVC architecture.
- **Spring Data JPA:**  
  Simplifies database interactions.
- **Maven:**  
  Project management and dependency tool.
- **Spring Security & JWT:**  
  Provides secure authentication and authorization.
- **PostgreSQL:**  
  Primary database for persistent storage.

---

### **Logging (SLF4J)**
- The project uses SLF4J with Logback (provided by Spring Boot) for application-wide logging. Many service classes include Lombok's `@Slf4j` annotation.
- Adjust log levels in `application.properties`. Minimal examples:
  - `logging.level.root=INFO`
  - `logging.level.com.yusufziyrek=DEBUG`

---

### **Architecture**

The project follows Clean Architecture in a Modular Monolith setup:

- Domain-centric modules: `auth/`, `user/`, `post/`, `comment/`, `like/` (+ `shared/` for cross-cutting concerns)
- Layers per module:
  - `domain/` → Entities and domain logic
  - `application/`
    - `usecases/` → Orchestrates business rules per action
    - `ports/` → Interfaces (e.g., repositories) consumed by use cases
  - `infrastructure/`
    - `web/` → REST controllers (adapters)
    - `persistence/` → JPA adapters (port implementations)
    - `config/` → Module wiring
- Request flow: Controller → UseCase → Port → Adapter (JPA) → DB
- CQRS-lite: Read/Write use cases are separated where meaningful

Cross-cutting concerns live under `shared/` (Security/JWT, CORS/Cache config, exceptions, common DTOs).

---

### **3. Database Structure:**

- **Users:**  
  Stores user details (ID, username, department, email, role, etc.).
- **Posts:**  
  Stores post details (ID, title, content, commentCount, likeCount, timestamps, etc.).
- **Comments:**  
  Stores comments related to posts (ID, userId, postId, likeCount, timestamps, etc.).
- **Likes:**  
  Stores likes on posts and comments (ID, userId, postId, commentId, timestamps, etc.).
- **Refresh Tokens:**  
  Persistent refresh tokens with device tracking for secure session management.

---

### **4. API Endpoints:**

#### **Authentication & Security:**
- **`POST /api/v1/auth/register`**:  
  Register a new user and immediately authenticate (returns access + refresh token).
- **`POST /api/v1/auth/login`**:  
  Authenticate with email or username + password (returns access + refresh token).
- **`POST /api/v1/auth/refresh`**:  
  Issue a new access token using a valid (non‑revoked) refresh token.

#### **User Management:**
- **`GET /api/v1/users`** (ADMIN):  
  Paginated list of users.
- **`GET /api/v1/users/me`**:  
  Current authenticated user profile.
- **`PUT /api/v1/users/me`**:  
  Update own profile data.
- **`GET /api/v1/users/{id}`** (ADMIN or owner):  
  Fetch user by ID.
- **`PUT /api/v1/users/{id}`** (ADMIN or owner):  
  Update user by ID.

#### **Post Management:**
- **`GET /api/v1/posts`**:  
  Paginated list of posts.
- **`GET /api/v1/posts/me`**:  
  Posts created by the authenticated user.
- **`GET /api/v1/posts/{id}`**:  
  Post details.
- **`POST /api/v1/posts`**:  
  Create a post (auth required).
- **`PUT /api/v1/posts/{id}`**:  
  Update a post (owner only).
- **`DELETE /api/v1/posts/{id}`**:  
  Delete a post (owner only).

#### **Nested Comments (per Post):**
- **`GET /api/v1/posts/{postId}/comments`**:  
  Paginated comments for a post.
- **`POST /api/v1/posts/{postId}/comments`**:  
  Add comment to post (auth required).

#### **Individual Comment Management:**
- **`GET /api/v1/comments/{id}`**:  
  Comment details.
- **`PUT /api/v1/comments/{id}`**:  
  Update comment (owner only).
- **`DELETE /api/v1/comments/{id}`**:  
  Delete comment (owner only).

#### **Post Likes (Nested):**
- **`POST /api/v1/posts/{postId}/likes`**:  
  Like a post (auth required).
- **`DELETE /api/v1/posts/{postId}/likes`**:  
  Unlike a post (auth required).
- **`GET /api/v1/posts/{postId}/likes`**:  
  Paginated likes for a post.
- **`GET /api/v1/posts/{postId}/likes/count`**:  
  Total like count for a post.

#### **Comment Likes (Nested):**
- **`POST /api/v1/comments/{commentId}/likes`**:  
  Like a comment (auth required).
- **`DELETE /api/v1/comments/{commentId}/likes`**:  
  Unlike a comment (auth required).
- **`GET /api/v1/comments/{commentId}/likes`**:  
  Paginated likes for a comment.

#### **Applied REST Conventions:**
✅ **Nested Resources** (comments & likes under posts / comments)  
✅ **Ownership Enforcement** (update/delete restricted)  
✅ **Stateless Auth** (JWT in Authorization header)  
✅ **Consistent Plural Nouns & Hierarchical URLs**

---

### **5. JWT Authentication & Refresh Token Security**

- **JWT-Based Authentication:**  
  Each request to a protected endpoint requires a valid JWT token. Users receive both access and refresh tokens upon successful login/registration:
  ```http
  Authorization: Bearer <ACCESS_TOKEN>
  ```

- **Refresh Token Mechanism:**  
  - **Persistent Storage:** Refresh tokens are stored in database with expiration tracking
  - **Device Tracking:** Each refresh token includes device info and IP address for security
  - **Token Rotation:** New refresh token generated on each refresh request
  - **Automatic Cleanup:** Expired tokens are automatically cleaned up via scheduled tasks
  - **Session Limits:** Maximum 5 active refresh tokens per user (configurable)

- **Security Features:**  
  - **Token Revocation:** Logout revokes refresh tokens immediately
  - **Role-Based Access:** Spring Security integration with method-level permissions
  - **Expiration Handling:** Separate expiration times for access (1h) and refresh tokens (24h)
  - **Device Management:** Track and limit active sessions per user



---

### **6. Project Setup & Execution**

1. **Clone the Repository:**  
   ```bash
   git clone https://github.com/yusufziyrek/blogApp-RestAPI.git
   ```
2. **Navigate to the Project Directory:**  
   ```bash
   cd blogApp-RestAPI
   ```
3. **Run the Application Using Maven:**  
   ```bash
   mvn spring-boot:run
   ```
4. **Configure JWT, Email, and Cache Settings:**  
   In your `application.properties`, set up the following:
   ```properties
   # JWT Configuration
   jwt.secret-key=YourSecureSecretKey
   jwt.expiration-time=3600000  # Access token: 1 hour
   jwt.refresh-token.expiration-ms=86400000  # Refresh token: 24 hours

   # Refresh Token Settings
   jwt.refresh.max-tokens-per-user=5  # Max active sessions per user
   jwt.refresh.cleanup.enabled=true  # Auto cleanup expired tokens

   # Email Configuration (using Gmail SMTP with TLS)
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   spring.mail.properties.mail.smtp.starttls.required=true
   ```
5. **Test Endpoints:**  
   - **Authentication:**  
     ```bash
     # Register new user
     curl -X POST http://localhost:8080/api/v1/auth/register \
          -H "Content-Type: application/json" \
          -d '{"firstname":"John","lastname":"Doe","username":"johndoe","email":"john@example.com","password":"password123","department":"IT","age":25}'

     # Login
     curl -X POST http://localhost:8080/api/v1/auth/login \
          -H "Content-Type: application/json" \
          -d '{"email":"john@example.com","password":"password123"}'

     # Refresh token
     curl -X POST http://localhost:8080/api/v1/auth/refresh \
          -H "Content-Type: application/json" \
          -d '{"refreshToken":"your_refresh_token_here"}'

     # Logout
     curl -X POST http://localhost:8080/api/v1/auth/logout \
          -H "Content-Type: application/json" \
          -d '{"refreshToken":"your_refresh_token_here"}'
     ```
6. **Database Configuration:**  
   Update the relevant properties in `application.properties` (or `application.yml`) to point to your preferred database (MySQL, PostgreSQL, etc.).

---

### **7. Future Improvements:**
- **Email Verification:** Add email verification for new user registrations.
- **Caffeine Cache:** Integrate caching for frequently accessed endpoints.
- **OAuth2 Support:** Enable social login via Google and GitHub.
- **2FA (Two-Factor Authentication):** Enhance security with OTP-based login.
- **Device Management:** Web interface for users to manage their active sessions.
- **Advanced Token Analytics:** Track token usage patterns and suspicious activities.
- **Admin Panel:** Provide a web interface for managing users, posts, and comments.

For more information, check the [GitHub repository](https://github.com/yusufziyrek/blogApp-RestAPI).

---
# **Preview**
- **Auth operations**
  
  ![image](https://github.com/user-attachments/assets/8381ac64-f820-40a5-923a-6346b743b523)  
  ![image](https://github.com/user-attachments/assets/162a6931-77cf-4f64-b37d-e4bf1788bfe5)  
  ![image](https://github.com/user-attachments/assets/a039a244-f15a-4827-9e99-722903d041aa)  
  ![image](https://github.com/user-attachments/assets/f38bff4e-10f3-4e74-8a4a-fb5358042a7c)  
  ![image](https://github.com/user-attachments/assets/7904d994-5cd2-4153-a8b7-087cb172c124)

- **Post operations**

  ![image](https://github.com/user-attachments/assets/89504de4-3dec-486f-bf37-ad5426320a44)  
  ![image](https://github.com/user-attachments/assets/5f5912f1-cac1-41d5-9232-a212b15a5eee)

- **Comment operations**

  ![image](https://github.com/user-attachments/assets/327e81c4-d692-498f-a4ab-fa1245c6350a)

- **Like operations**

  ![image](https://github.com/user-attachments/assets/17eb5e5d-3980-4016-8c4d-a37f9153c1a1)  
  ![image](https://github.com/user-attachments/assets/a942590b-80ae-498e-8f3e-ca450f268e40)

---

### **Exceptions**

  ![image](https://github.com/user-attachments/assets/4945327f-d2e4-4de4-acfd-c375c4eb5389)  
  ![image](https://github.com/user-attachments/assets/4247b747-a1e3-4ce9-a27c-29c7b5ab4fe4)  
  ![image](https://github.com/user-attachments/assets/ebc8f309-fe61-48b8-a8f5-82c652b20a60)  
  ![image](https://github.com/user-attachments/assets/384e82b4-c77a-470b-9b35-6b227b0f89ca)

---

### **Contributing**

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### **Author**

- **Yusuf Ziyrek** - [GitHub Profile](https://github.com/yusufziyrek)

### **Acknowledgments**

- Spring Boot team for the excellent framework
- Open source community for the tools and libraries used in this project

---

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build Status](https://img.shields.io/badge/Build-Passing-success.svg)]()
