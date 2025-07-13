# BlogApp

A modern blog application built with Spring Boot, featuring user authentication, content management, and social interactions.

## Features

- **User Authentication**: JWT-based authentication with registration and login
- **Content Management**: Create, read, update, and delete blog posts
- **Social Features**: Comment system and like functionality
- **Search**: Full-text search across posts and comments
- **Caching**: Redis-based caching for improved performance
- **Security**: Role-based access control and input validation

## Technology Stack

- **Backend**: Spring Boot 3.x
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Tokens)
- **Caching**: Caffeine Cache
- **Build Tool**: Maven
- **Java Version**: 24

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.8+
- PostgreSQL

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd blogApp
   ```

2. **Configure database**
   ```sql
   CREATE DATABASE blogapp;
   ```

3. **Update application.properties**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/blogapp
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh JWT token
- `GET /api/v1/auth/verify?token={token}` - Verify account with token

### Users
- `GET /api/v1/users` - Get all users (paginated)
- `GET /api/v1/users/me` - Get current user profile
- `GET /api/v1/users/by-username/{username}` - Get user by username
- `GET /api/v1/users/{id}` - Get user by ID
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Posts
- `GET /api/v1/posts` - Get all posts (paginated)
- `GET /api/v1/posts/me` - Get current user's posts (paginated)
- `GET /api/v1/posts/{id}` - Get post by ID
- `POST /api/v1/posts` - Create new post
- `PUT /api/v1/posts/{id}` - Update post
- `DELETE /api/v1/posts/{id}` - Delete post

### Comments
- `GET /api/v1/comments/post/{postId}` - Get comments for a post
- `GET /api/v1/comments/user` - Get current user's comments
- `GET /api/v1/comments/{id}` - Get comment by ID
- `POST /api/v1/comments/post/{postId}` - Add comment to post
- `PUT /api/v1/comments/{id}` - Update comment
- `DELETE /api/v1/comments/{id}` - Delete comment

### Likes
- `GET /api/v1/likes/post/{postId}` - Get likes for a post
- `GET /api/v1/likes/comment/{commentId}` - Get likes for a comment
- `GET /api/v1/likes/{id}` - Get like by ID
- `POST /api/v1/likes/post/{postId}` - Like a post
- `POST /api/v1/likes/comment/{commentId}` - Like a comment
- `DELETE /api/v1/likes/post/{likeId}` - Unlike a post
- `DELETE /api/v1/likes/comment/{likeId}` - Unlike a comment

### Search
- `GET /api/v1/search?query={searchTerm}&type={all|posts|comments}` - Search content (paginated)

## Project Structure

```
src/main/java/com/yusufziyrek/blogApp/
├── content/           # Blog content management
│   ├── domain/       # Domain models (Post, Comment, Like)
│   ├── dto/          # Data transfer objects
│   ├── repo/         # Repository interfaces
│   ├── service/      # Business logic
│   └── web/          # REST controllers
├── identity/         # User management and authentication
│   ├── domain/       # User and security models
│   ├── dto/          # Auth DTOs
│   ├── repo/         # User repositories
│   ├── service/      # Auth and user services
│   └── web/          # Auth controllers
└── shared/           # Common components
    ├── config/       # Application configuration
    ├── exception/    # Global exception handling
    └── security/     # JWT and security components
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request