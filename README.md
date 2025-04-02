### BlogApp-RestAPI Documentation

**Overview:**  
BlogApp-RestAPI is a RESTful API enabling users to interact with blog posts, comments, and likes. It allows comprehensive CRUD operations on users, posts, comments, and likes, offering a fully-featured blog experience with secure authentication, authorization, and email verification.

---

### **1. Features:**
- **User Management:** User registration, update, delete, and listing.
- **Post Management:** Create, read, update, delete blog posts.
- **Comments & Likes:** Users can comment on and like posts.
- **JWT Authentication & Security:** Secured endpoints with JWT-based authentication and role-based access control.
- **Email Verification:** New users must verify their email addresses to activate their accounts. A verification link is sent to the user’s email upon registration.

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
- **Email Verification Tokens:** Stores tokens used for verifying user email addresses.

---

### **4. API Endpoints:**

#### **Authentication & Security:**
- **`POST /auth/register`**: Registers a new user and triggers email verification.
- **`POST /auth/login`**: Authenticates a user and returns a JWT token.
- **`GET /auth/verify-email?token={token}`**: Verifies the user’s email address using the token sent to their inbox.

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

## **5. JWT Authentication & Security Updates**

- **JWT-Based Authentication**  
  Each request to a protected endpoint requires a valid JWT token. Users obtain this token upon successful login and must include it in the `Authorization` header:

  ```http
  Authorization: Bearer <JWT_TOKEN>
  ```

- **Spring Security Integration**  
  Uses role-based permissions to control access to various endpoints.

- **Token Expiration Handling**  
  Includes token validity checks to ensure sessions are managed securely.

- **Email Verification Flow**  
  1. **User Registration**: A new user registers via `POST /auth/register`.  
  2. **Verification Link**: The system sends a verification link to the user’s email.  
  3. **Account Activation**: The user clicks the link (`GET /auth/verify-email?token=<TOKEN>`) to activate their account.  
  4. **Authentication**: Once verified, the user can log in and receive a JWT token.

---

## **6. Project Setup & Execution**

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/yusufziyrek/blogApp-RestAPI.git
   ```

2. **Navigate to the Project Directory**  
   ```bash
   cd blogApp-RestAPI
   ```

3. **Run the Application Using Maven**  
   ```bash
   mvn spring-boot:run
   ```

4. **Configure JWT and Email Settings**  
   In your `application.properties`, set the following (as an example):

   ```properties
   # JWT Secret Key
   jwt.secret-key=YourSecureSecretKey

   # Email Configuration (e.g., Gmail SMTP)
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-email-password
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

5. **Test Endpoints**  
   - **Authentication**  
     ```bash
     curl -X POST http://localhost:8080/auth/login \
          -H "Content-Type: application/json" \
          -d '{"username":"user1","password":"password"}'
     ```
   - **Email Verification**  
     Check your inbox for a verification link and open it in a browser (e.g., `GET /auth/verify-email?token=<TOKEN>`).

6. **Database Configuration**  
   Update the relevant properties in `application.properties` (or `application.yml`) to point to your preferred database (MySQL, PostgreSQL, etc.).

---

### **7. Future Improvements:**
- **OAuth2 Support:** Enable social login via Google and GitHub.
- **2FA (Two-Factor Authentication):** Enhance security with OTP-based login.
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

# **Exceptions**

  ![image](https://github.com/user-attachments/assets/4945327f-d2e4-4de4-acfd-c375c4eb5389)
  ![image](https://github.com/user-attachments/assets/4247b747-a1e3-4ce9-a27c-29c7b5ab4fe4)
  ![image](https://github.com/user-attachments/assets/ebc8f309-fe61-48b8-a8f5-82c652b20a60)
  ![image](https://github.com/user-attachments/assets/384e82b4-c77a-470b-9b35-6b227b0f89ca)
