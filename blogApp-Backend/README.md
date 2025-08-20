### BlogApp-RestAPI Documentation

**Overview:**  
BlogApp-RestAPI is a RESTful API that enables users to interact with blog posts, comments, and likes. It provides comprehensive CRUD operations for users, posts, comments, and likes, offering a fully-featured blog experience with secure authentication, role-based authorization, email verification, and caching for enhanced performance.

---

### **1. Features:**
- **User Management:**  
  User registration, update, delete, and listing.
- **Post Management:**  
  Create, read, update, and delete blog posts.
- **Comments & Likes:**  
  Users can comment on and like posts.
- **JWT Authentication & Security:**  
  Secured endpoints with JWT-based authentication and role-based access control.
- **Email Verification:**  
  New users must verify their email addresses to activate their accounts. A verification link is sent upon registration.
- **Caching with Caffeine:**  
  Frequently accessed data (such as blog posts and comments) is cached using Caffeine Cache, reducing database load and improving response times.

---

### **2. Tech Stack:**
- **Java 21 & Spring Boot:**  
  API development, dependency injection, and MVC architecture.
- **Spring Data JPA:**  
  Simplifies database interactions.
- **Maven:**  
  Project management and dependency tool.
- **Spring Security & JWT:**  
  Provides secure authentication and authorization.
- **Caffeine Cache:**  
  High-performance caching library integrated to cache frequently accessed endpoints.

---

### **Logging (SLF4J)**
- The project uses SLF4J with Logback (provided by Spring Boot) for application-wide logging. Many service classes include Lombok's `@Slf4j` annotation.
- Adjust log levels in `application.properties`. Minimal examples:
  - `logging.level.root=INFO`
  - `logging.level.com.yusufziyrek=DEBUG`

---

### **Architecture:**
- **Modular Monolith:**  
  The application follows a simple modular monolith architecture, organizing code into distinct modules within a single deployment unit to maintain modularity and ease of development.

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
- **JWT Tokens:**  
  Used for authentication and authorization.
- **Email Verification Tokens:**  
  Stores tokens used for verifying user email addresses.

---

### **4. API Endpoints:**

#### **Authentication & Security:**
- **`POST /api/v1/auth/register`**:  
  Registers a new user and triggers email verification.
- **`POST /api/v1/auth/login`**:  
  Authenticates a user and returns a JWT token.
- **`GET /api/v1/auth/verify?token={token}`**:  
  Verifies the user’s email using the provided token.
- **`POST /api/v1/auth/refresh`**:  
  Refreshes the JWT token.

#### **User Management:**
- **`GET /api/v1/users`**:  
  Lists all users.
- **`GET /api/v1/users/me`**:  
  Returns the user profile for the authenticated user.
- **`GET /api/v1/users/by-username/{username}`**:  
  Returns information for a specific user by username.
- **`GET /api/v1/users/{id}`**:  
  Returns information for a specific user by ID.
- **`PUT /api/v1/users/{id}`**:  
  Updates user details.
- **`DELETE /api/v1/users/{id}`**:  
  Deletes a user.

#### **Post Management:**
- **`GET /api/v1/posts`**:  
  Lists all blog posts.
- **`GET /api/v1/posts/me`**:  
  Returns blog posts for the authenticated user.
- **`GET /api/v1/posts/{id}`**:  
  Returns details for a specific post.
- **`POST /api/v1/posts`**:  
  Creates a new blog post (Requires authentication).
- **`PUT /api/v1/posts/{id}`**:  
  Updates a post (Requires authentication).
- **`DELETE /api/v1/posts/{id}`**:  
  Deletes a post (Requires authentication).

#### **Comment Management:**
- **`GET /api/v1/comments/post/{postId}`**:  
  Returns comments for a specific post.
- **`GET /api/v1/comments/user`**:  
  Returns comments for the authenticated user.
- **`GET /api/v1/comments/{id}`**:  
  Returns details for a specific comment.
- **`POST /api/v1/comments/post/{postId}`**:  
  Adds a new comment to a post (Requires authentication).
- **`PUT /api/v1/comments/{id}`**:  
  Updates a comment (Requires authentication).
- **`DELETE /api/v1/comments/{id}`**:  
  Deletes a comment (Requires authentication).

#### **Like Management:**
- **`GET /api/v1/likes/post/{postId}`**:  
  Returns likes for a specific post.
- **`GET /api/v1/likes/comment/{commentId}`**:  
  Returns likes for a specific comment.
- **`GET /api/v1/likes/{id}`**:  
  Returns details for a specific like.
- **`POST /api/v1/likes/post/{postId}`**:  
  Likes a post (Requires authentication).
- **`POST /api/v1/likes/comment/{commentId}`**:  
  Likes a comment (Requires authentication).
- **`DELETE /api/v1/likes/post/{likeId}`**:  
  Deletes a like from a post (Requires authentication).
- **`DELETE /api/v1/likes/comment/{likeId}`**:  
  Deletes a like from a comment (Requires authentication).

#### **Search Functionality:**
- **`GET /api/v1/search`**:  
  Searches posts, users, or all fields based on a query and an optional type parameter (default is "all").

---

### **5. JWT Authentication & Security Updates**

- **JWT-Based Authentication:**  
  Each request to a protected endpoint requires a valid JWT token. Users receive a token upon successful login, which must be included in the `Authorization` header:
  ```http
  Authorization: Bearer <JWT_TOKEN>
  ```
- **Spring Security Integration:**  
  Uses role-based permissions to control access.
- **Token Expiration Handling:**  
  Validates token expiration to ensure secure session management.
- **Email Verification Flow:**  
  1. **User Registration:** User registers via `POST /api/v1/auth/register`.  
  2. **Verification Link:** An email is sent with a verification link.  
  3. **Account Activation:** User activates their account by visiting `GET /api/v1/auth/verify?token=<TOKEN>`.  
  4. **Authentication:** Once verified, the user can log in and receive a JWT token.

---

### **6. Caching with Caffeine Cache**

To improve performance and reduce database load, BlogApp-RestAPI integrates **Caffeine Cache**:

- **Purpose:**  
  Frequently accessed data—such as blog posts, comments, and likes—is cached to speed up read operations.
- **Usage:**  
  Caching is applied on service methods (typically on GET operations) to store the results for a specified duration. Cache invalidation is triggered on data modifications (POST, PUT, DELETE) to prevent stale data.
- **Benefits:**  
  - **Reduced Database Load:** Minimizes repetitive database queries for frequently requested data.
  - **Faster Response Times:** Delivers cached results quickly, enhancing overall application performance.
  - **Scalability:** Supports high traffic by reducing expensive database operations.

---

### **7. Project Setup & Execution**

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
   # JWT Secret Key
   jwt.secret-key=YourSecureSecretKey
   jwt.expiration-time=3600000  # Example: 1 hour in milliseconds

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
     curl -X POST http://localhost:8080/auth/login \
          -H "Content-Type: application/json" \
          -d '{"username":"user1","password":"password"}'
     ```
   - **Email Verification:**  
     Check your inbox for a verification link and open it in a browser (e.g., `GET /auth/verify?token=<TOKEN>`).
6. **Database Configuration:**  
   Update the relevant properties in `application.properties` (or `application.yml`) to point to your preferred database (MySQL, PostgreSQL, etc.).

---

### **8. Future Improvements:**
- **OAuth2 Support:** Enable social login via Google and GitHub.
- **2FA (Two-Factor Authentication):** Enhance security with OTP-based login.
- **Admin Panel:** Provide a web interface for managing users, posts, and comments.
- **Advanced Cache Strategies:** Explore more sophisticated caching strategies and cache eviction policies as the project scales.

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
