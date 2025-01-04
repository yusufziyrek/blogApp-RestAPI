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
- **`GET /likes/post/{postId}`**: Returns likes for specific post.
- **`GET /likes/comment/{commentId}`**: Returns likes for specific comment.
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
- **Pagination & Sorting:** To handle large datasets for posts and comments. (Added)
- **Improved Error Handling:** Better validation and custom error messages. (Added)
- **Database Support:** Integration with other databases like MySQL and PostgreSQL.

For more information, check the [GitHub repository](https://github.com/yusufziyrek/blogApp-RestAPI).

#

 # Preview
- User operations

![image](https://github.com/user-attachments/assets/7b0e6c41-89f5-400e-8b3f-434860689268)
![image](https://github.com/user-attachments/assets/8147842a-ae8a-4105-ba89-1a202329e8eb)



- Post operations

![image](https://github.com/user-attachments/assets/89504de4-3dec-486f-bf37-ad5426320a44)
![image](https://github.com/user-attachments/assets/5f5912f1-cac1-41d5-9232-a212b15a5eee)



- Comment operations

![image](https://github.com/user-attachments/assets/327e81c4-d692-498f-a4ab-fa1245c6350a)


- Like operations

![image](https://github.com/user-attachments/assets/17eb5e5d-3980-4016-8c4d-a37f9153c1a1)
![image](https://github.com/user-attachments/assets/a942590b-80ae-498e-8f3e-ca450f268e40)


# Exceptions

![image](https://github.com/user-attachments/assets/4945327f-d2e4-4de4-acfd-c375c4eb5389)
![image](https://github.com/user-attachments/assets/4247b747-a1e3-4ce9-a27c-29c7b5ab4fe4)
![image](https://github.com/user-attachments/assets/ebc8f309-fe61-48b8-a8f5-82c652b20a60)
![image](https://github.com/user-attachments/assets/384e82b4-c77a-470b-9b35-6b227b0f89ca)
