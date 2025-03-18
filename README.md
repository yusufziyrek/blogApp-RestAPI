### BlogApp-RestAPI Documentation

**Overview:**  
BlogApp-RestAPI is a RESTful API enabling users to interact with blog posts, comments, and likes. It allows comprehensive CRUD operations on users, posts, comments, and likes, offering a fully-featured blog experience with secure authentication and authorization.

---

### **1. Features:**
- **User Management:** User registration, update, delete, and listing.
- **Post Management:** Create, read, update, delete blog posts.
- **Comments & Likes:** Users can comment on and like posts.
- **JWT Authentication & Security:** Secured endpoints with JWT-based authentication and role-based access control.

---

### **2. Tech Stack:**
- **Java 21 & Spring Boot:** API development, dependency injection, MVC structure.
- **Spring Data JPA:** Simplifying database interactions.
- **Maven:** Project management and dependency tool.
- **Spring Security & JWT:** Secure authentication and authorization.

---

### **3. Database Structure:**

- **Users:** Stores user details (ID, username, department, email, role, etc.).
- **Posts:** Stores post details (ID, title, content, commentCount, likeCount, timestamps, etc.).
- **Comments:** Stores comments about the post. (ID, userId, postId, likeCount, timestamps, etc.)
- **Likes:** Stores likes of posts and comments. (ID, userId, postId, commentId, timestamps, etc.)
- **JWT Tokens:** Used for authentication and authorization.

---

### **4. API Endpoints:**

#### **Authentication & Security:**
- **`POST /auth/register`**: Registers a new user.
- **`POST /auth/login`**: Authenticates a user and returns a JWT token.

#### **User Management:**
- **`GET /users`**: List all users.
- **`GET /users/{id}`**: Returns all information for a specific user.
- **`PUT /users/{id}`**: Update a user.
- **`DELETE /users/{id}`**: Delete a user.

#### **Post Management:**
- **`GET /posts`**: List all blog posts.
- **`GET /posts/{id}`**: Returns all information for a specific post.
- **`POST /posts`**: Create a new blog post (Requires authentication).
- **`PUT /posts/{id}`**: Update a post (Requires authentication).
- **`DELETE /posts/{id}`**: Delete a post (Requires authentication).

#### **Comment Management:**
- **`GET /comments/post/{postId}`**: Returns comments for a specific post.
- **`POST /comments/post/{postId}`**: Add a new comment to a post (Requires authentication).
- **`DELETE /comments/{id}`**: Delete a comment (Requires authentication).

#### **Like Management:**
- **`GET /likes/post/{postId}`**: Returns likes for a specific post.
- **`GET /likes/comment/{commentId}`**: Returns likes for a specific comment.
- **`POST /likes/post/{postId}`**: Like a post (Requires authentication).
- **`POST /likes/comment/{commentId}`**: Like a comment (Requires authentication).
- **`DELETE /likes/{likeId}`**: Deletes likes for a post or comment (Requires authentication).

---

### **5. JWT Authentication & Security Updates**
- **JWT-Based Authentication:** Each request to a protected endpoint requires a valid JWT token.
- **Spring Security Integration:** Secure access using role-based permissions.
- **Token Expiration Handling:** Ensures secure session management.

#### **How JWT Works**
1. A user registers via `POST /auth/register`.
2. The user logs in via `POST /auth/login` and receives a JWT token.
3. The JWT token is included in the `Authorization` header for future requests:
   ```
   Authorization: Bearer <JWT_TOKEN>
   ```
4. The API validates the token before granting access.

---

### **6. Project Setup & Execution:**

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yusufziyrek/blogApp-RestAPI.git
   ```

2. **Navigate to the project directory:**
   ```bash
   cd blogApp-RestAPI
   ```

3. **Run the application using Maven:**
   ```bash
   mvn spring-boot:run
   ```

4. **Configure JWT Secret Key in `application.properties`**
   ```
   jwt.secret-key=YourSecureSecretKey
   ```

5. **Test authentication using Postman or curl**
   ```bash
   curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username":"user1","password":"password"}'
   ```

You can configure the database connection by updating the `application.properties` file to support external databases like MySQL or PostgreSQL.

---

### **7. Future Improvements:**
- **OAuth2 Support:** Enable social login via Google and GitHub.
- **2FA (Two-Factor Authentication):** Enhance security with OTP-based login.
- **Email Verification:** Require email confirmation for new users.
- **Admin Panel:** Provide a web interface for managing users, posts, and comments.

For more information, check the [GitHub repository](https://github.com/yusufziyrek/blogApp-RestAPI).

---

# **Preview**
- **User operations**
  
  ![image](https://github.com/user-attachments/assets/7b0e6c41-89f5-400e-8b3f-434860689268)
  ![image](https://github.com/user-attachments/assets/8147842a-ae8a-4105-ba89-1a202329e8eb)

- **Post operations**

  ![image](https://github.com/user-attachments/assets/89504de4-3dec-486f-bf37-ad5426320a44)
  ![image](https://github.com/user-attachments/assets/5f5912f1-cac1-41d5-9232-a212b15a5eee)

- **Comment operations**

  ![image](https://github.com/user-attachments/assets/327e81c4-d692-498f-a4ab-fa1245c6350a)

- **Like operations**

  ![image](https://github.com/user-attachments/assets/17eb5e5d-3980-4016-8c4d-a37f9153c1a1)
  ![image](https://github.com/user-attachments/assets/a942590b-80ae-498e-8f3e-ca450f268e40)

---

# **Exceptions**

  ![image](https://github.com/user-attachments/assets/4945327f-d2e4-4de4-acfd-c375c4eb5389)
  ![image](https://github.com/user-attachments/assets/4247b747-a1e3-4ce9-a27c-29c7b5ab4fe4)
  ![image](https://github.com/user-attachments/assets/ebc8f309-fe61-48b8-a8f5-82c652b20a60)
  ![image](https://github.com/user-attachments/assets/384e82b4-c77a-470b-9b35-6b227b0f89ca)
