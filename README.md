### BlogApp-RestAPI Documentation

**Overview:**
BlogApp-RestAPI is a RESTful API enabling users to interact with blog posts, comments, and likes. It allows comprehensive CRUD operations on users, posts, comments and likes, offering a fully-featured blog experience.

---

### **1. Features:**
- **User Management:** User registration, update, delete, and listing.
- **Post Management:** Create, read, update, delete blog posts.
- **Comments & Likes:** Users can comment on and like posts.

---

### **2. Tech Stack:**
- **Java 21 & Spring Boot:** API development, dependency injection, MVC structure.
- **Spring Data JPA:** For simplifying database interactions.
- **Maven:** Project management and dependency tool.

---

### **3. Database Structure:**

- **Users:** Stores user details (ID, username, department, etc.).
- **Posts:** Stores post details (ID, title, content, commentCount, likeCount, timestamps, etc.).
- **Comments:** Stores comments about the post.(ID, userId, postId, likeCount, timestamps, etc.)
- **Likes:** Stores likes of posts and comments.(ID, userId, postId, commentId, timestamps, etc.)

---

### **4. API Endpoints:**

#### **User Management:**
- **`GET /users`**: List all users.
- **`GET /users/{id}`**: Returns all information for a specific user.
- **`POST /users`**: Create a new user.
- **`PUT /users/{id}`**: Update a user.
- **`DELETE /users/{id}`**: Delete a user.

#### **Post Management:**
- **`GET /posts`**: List all blog posts.
- **`GET /posts/{id}`**: Returns all information for a specific post.
- **`POST /posts`**: Create a new blog post.
- **`PUT /posts/{id}`**: Update a post.
- **`DELETE /posts/{id}`**: Delete a post.

#### **Comment Management:**
- **`GET /comments/post/{postId}`**: Returns comments for a specific post.
- **`POST /comments/post/{postId}`**: Add a new comment to a post.
- **`DELETE /comments/{id}`**: Delete a comment.

#### **Like Management:**
- **`POST /likes/post/{postId}`**: Like a post.
- **`POST /likes/comment/{commentId}`**: Like a comment.
- **`DELETE /likes/{likeId}`**: Deletes likes for a post or comment.

---

### **5. Project Setup & Execution:**

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

You can configure the database connection by updating the `application.properties` file to support external databases like MySQL or PostgreSQL.

---

### **6. Future Improvements:**

- **JWT Authentication & Authorization:** For secure user login and access control.
- **Pagination & Sorting:** To handle large datasets for posts and comments.
- **Improved Error Handling:** Better validation and custom error messages.
- **Database Support:** Integration with other databases like MySQL and PostgreSQL.

For more information, check the [GitHub repository](https://github.com/yusufziyrek/blogApp-RestAPI).

#

 # Preview
- User operations

![image](https://github.com/user-attachments/assets/a8a09b5a-94e5-4e8b-ade1-f8bac036179c)
![image](https://github.com/user-attachments/assets/4d66e2d6-0960-47fc-b13c-b38e7c5d6398)


- Post operations

![image](https://github.com/user-attachments/assets/441e6a13-492a-4ef0-b976-0d3581c3b2ff)
![image](https://github.com/user-attachments/assets/40fd4bb3-1add-4d2f-9ec0-8ff357b93482)


- Comment operations

![image](https://github.com/user-attachments/assets/3fc2acf3-7364-4b43-bff1-a34a512528e7)


- Like operations

![image](https://github.com/user-attachments/assets/5c12f27f-108f-42d5-b0b7-870712f5082a)
![image](https://github.com/user-attachments/assets/f9352ad7-c895-49e9-836a-78ecace5cc81)


# Exceptions

![image](https://github.com/user-attachments/assets/5db2353d-f1c6-40f9-a8d0-f46aff37e883)
![image](https://github.com/user-attachments/assets/89a12d46-acdb-4154-90c4-5ac832e6f09c)
![image](https://github.com/user-attachments/assets/4a4a887c-c8be-49ae-91b7-7332972710b5)











