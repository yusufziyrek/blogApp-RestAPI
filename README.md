### BlogApp-RestAPI Documentation

**Overview:**
BlogApp-RestAPI is a RESTful API that enables users to interact with blog posts, comments, and likes. It provides comprehensive CRUD operations for users, posts, comments, and likes, offering a fully-featured blog experience with secure authentication, role-based authorization, email verification, and caching for enhanced performance.

---

### **1. Features:**

* **User Management:**
  User registration, update, deletion, and listing.
* **Post Management:**
  Create, read, update, and delete blog posts.
* **Comments & Likes:**
  Users can comment on and like posts.
* **JWT Authentication & Security:**
  Secured endpoints with JWT-based authentication and role-based access control.
* **Email Verification:**
  New users must verify their email addresses to activate their accounts. A verification link is sent upon registration.
* **Caching with Caffeine:**
  Frequently accessed data (such as blog posts and comments) is cached using Caffeine Cache, reducing database load and improving response times.

---

### **2. Tech Stack:**

* **Java 21 & Spring Boot:**
  API development, dependency injection, and MVC architecture.
* **Spring Data JPA:**
  Simplifies database interactions.
* **Maven:**
  Project management and dependency tool.
* **Spring Security & JWT:**
  Provides secure authentication and authorization.
* **Caffeine Cache:**
  High-performance caching library integrated to cache frequently accessed endpoints.

---

### **3. Architecture:**

* **Modular Monolith:**
  The application follows a modular monolith architecture, organizing code into distinct modules within a single deployment unit to maintain modularity and ease of development.

---

### **4. Database Structure:**

* **Users:**
  Stores user details (ID, username, department, email, role, etc.).
* **Posts:**
  Stores post details (ID, title, content, commentCount, likeCount, timestamps, etc.).
* **Comments:**
  Stores comments related to posts (ID, userId, postId, likeCount, timestamps, etc.).
* **Likes:**
  Stores likes on posts and comments (ID, userId, postId, commentId, timestamps, etc.).
* **JWT Tokens:**
  Used for authentication and authorization.
* **Email Verification Tokens:**
  Stores tokens used for verifying user email addresses.

---

### **5. API Endpoints:**

#### **Authentication & Security:**

* **`POST /api/v1/auth/register`**
  Registers a new user and triggers email verification.
* **`POST /api/v1/auth/login`**
  Authenticates a user and returns a JWT token.
* **`GET  /api/v1/auth/verify?token={token}`**
  Verifies the user’s email using the provided token.
* **`POST /api/v1/auth/refresh`**
  Refreshes the JWT token.

#### **User Management:**

* **`GET    /api/v1/users`**
  Lists all users.
* **`GET    /api/v1/users/me`**
  Returns the user profile for the authenticated user.
* **`GET    /api/v1/users/by-username/{username}`**
  Returns information for a specific user by username.
* **`GET    /api/v1/users/{id}`**
  Returns information for a specific user by ID.
* **`PUT    /api/v1/users/{id}`**
  Updates user details.
* **`DELETE /api/v1/users/{id}`**
  Deletes a user.

#### **Post Management:**

* **`GET    /api/v1/posts`**
  Lists all blog posts.
* **`GET    /api/v1/posts/me`**
  Returns blog posts for the authenticated user.
* **`GET    /api/v1/posts/{id}`**
  Returns details for a specific post.
* **`POST   /api/v1/posts`**
  Creates a new blog post (Requires authentication).
* **`PUT    /api/v1/posts/{id}`**
  Updates a post (Requires authentication).
* **`DELETE /api/v1/posts/{id}`**
  Deletes a post (Requires authentication).

#### **Comment Management:**

* **`GET    /api/v1/comments/post/{postId}`**
  Returns comments for a specific post.
* **`GET    /api/v1/comments/user`**
  Returns comments for the authenticated user.
* **`GET    /api/v1/comments/{id}`**
  Returns details for a specific comment.
* **`POST   /api/v1/comments/post/{postId}`**
  Adds a new comment to a post (Requires authentication).
* **`PUT    /api/v1/comments/{id}`**
  Updates a comment (Requires authentication).
* **`DELETE /api/v1/comments/{id}`**
  Deletes a comment (Requires authentication).

#### **Like Management:**

* **`GET    /api/v1/likes/post/{postId}`**
  Returns likes for a specific post.
* **`GET    /api/v1/likes/comment/{commentId}`**
  Returns likes for a specific comment.
* **`GET    /api/v1/likes/{id}`**
  Returns details for a specific like.
* **`POST   /api/v1/likes/post/{postId}`**
  Likes a post (Requires authentication).
* **`POST   /api/v1/likes/comment/{commentId}`**
  Likes a comment (Requires authentication).
* **`DELETE /api/v1/likes/post/{likeId}`**
  Deletes a like from a post (Requires authentication).
* **`DELETE /api/v1/likes/comment/{likeId}`**
  Deletes a like from a comment (Requires authentication).

#### **Search Functionality:**

* **`GET /api/v1/search`**
  Searches posts, users, or all fields based on a query and an optional type parameter (default is "all").

---

### **6. JWT Authentication & Security Updates**

* **JWT-Based Authentication:**
  Each request to a protected endpoint requires a valid JWT token. Include in header:

  ```http
  Authorization: Bearer <JWT_TOKEN>
  ```
* **Spring Security Integration:**
  Role-based permissions control access.
* **Token Expiration Handling:**
  Validates token expiry for secure session management.
* **Email Verification Flow:**

  1. Register via `POST /api/v1/auth/register`
  2. Receive verification email
  3. Activate account via `GET /api/v1/auth/verify?token=<TOKEN>`
  4. Login to obtain JWT token

---

### **7. Caching with Caffeine Cache**

* **Purpose:**
  Cache frequently accessed data (posts, comments, likes) to improve performance.
* **Usage:**
  Service-layer GET methods are cached; POST/PUT/DELETE operations invalidate relevant caches.
* **Benefits:**

  * Reduced database load
  * Faster response times
  * Better scalability under high traffic

---

### **8. Project Setup & Execution**

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yusufziyrek/blogApp-RestAPI.git
   ```
2. **Navigate to Directory:**

   ```bash
   cd blogApp-RestAPI
   ```
3. **Run with Maven:**

   ```bash
   mvn spring-boot:run
   ```
4. **Configure `application.properties`:**

   ```properties
   jwt.secret-key=YourSecureSecretKey
   jwt.expiration-time=3600000

   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   spring.mail.properties.mail.smtp.starttls.required=true
   ```
5. **Test Endpoints (example):**

   ```bash
   curl -X POST http://localhost:8080/api/v1/auth/login \
        -H "Content-Type: application/json" \
        -d '{"username":"user1","password":"password"}'
   ```

---

### **9. Future Improvements:**

* **OAuth2 Support:** Enable social login via Google/GitHub.
* **2FA (Two-Factor Authentication):** Enhance security with OTP.
* **Admin Panel:** Web interface for managing users, posts, and comments.
* **Advanced Cache Strategies:** Explore custom eviction policies as scale grows.

---

### **10. Frontend**

* The BlogApp frontend was developed entirely using AI technologies.
* The project is open to community contributions and support.

---

For more information and to contribute, visit the [GitHub repository](https://github.com/yusufziyrek/blogApp-RestAPI).
