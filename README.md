# BlogApp - High Performance Spring Boot Application

## 🚀 Performance Optimizations

### Database Optimizations
- **Connection Pool**: HikariCP with optimized settings
- **Batch Processing**: Configured for optimal batch operations
- **Indexing**: Strategic database indexes for faster queries
- **Query Optimization**: Custom queries with JOIN FETCH

### Caching Strategy
- **Multi-tier Caching**: Different cache configurations for different data types
- **Caffeine Cache**: High-performance in-memory caching
- **Cache Eviction**: Smart cache invalidation strategies
- **Weak References**: Memory-efficient caching

### Application Performance
- **Async Processing**: Thread pool configuration for non-blocking operations
- **Connection Pooling**: Optimized database connection management
- **JPA Optimization**: Batch operations and query optimization
- **Security**: Enhanced password encoding and JWT optimization

## 🐳 Docker Setup

### Quick Start with Docker

#### Option 1: Database Only (Recommended for Development)
```bash
# Start only PostgreSQL database
docker-compose up -d postgres pgadmin

# Run Spring Boot application locally
mvn spring-boot:run -Dspring.profiles.active=docker
```

#### Option 2: Full Stack (Production Ready)
```bash
# Start all services (database + application)
docker-compose -f docker-compose-full.yml up -d
```

### Docker Services

#### PostgreSQL Database
- **Port**: 5432
- **Database**: blogApp
- **Username**: postgres
- **Password**: 12345
- **Health Check**: Enabled

#### PgAdmin (Database Management)
- **URL**: http://localhost:8081
- **Email**: admin@blogapp.com
- **Password**: admin123

#### Spring Boot Application
- **Port**: 8080
- **Health Check**: http://localhost:8080/actuator/health
- **API Base**: http://localhost:8080/api/v1

### Docker Commands

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild and start
docker-compose up -d --build

# Clean up volumes
docker-compose down -v

# Check service status
docker-compose ps
```

### Database Connection

#### Local Development
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogApp
spring.datasource.username=postgres
spring.datasource.password=12345
```

#### Docker Environment
```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/blogApp
spring.datasource.username=postgres
spring.datasource.password=12345
```

## 📊 Monitoring & Metrics

### Actuator Endpoints
- `/actuator/health` - Application health status
- `/actuator/metrics` - Performance metrics
- `/actuator/prometheus` - Prometheus metrics
- `/actuator/caches` - Cache statistics

### Key Metrics
- JVM metrics (memory, threads, garbage collection)
- Database connection pool metrics
- Cache hit/miss ratios
- HTTP request/response times
- Custom business metrics

## 🏗️ Architecture

### Clean Architecture
```
src/main/java/com/yusufziyrek/blogApp/
├── content/           # Blog content domain
│   ├── domain/       # Entities and business rules
│   ├── dto/          # Data Transfer Objects
│   ├── repo/         # Repository layer
│   ├── service/      # Business logic
│   └── web/          # REST controllers
├── identity/         # User management domain
│   ├── domain/       # User entities
│   ├── dto/          # User DTOs
│   ├── repo/         # User repositories
│   ├── service/      # User services
│   └── web/          # Auth controllers
└── shared/           # Shared components
    ├── config/       # Configuration classes
    ├── dto/          # Shared DTOs
    ├── exception/    # Global exception handling
    └── security/     # Security components
```

## 🔧 Configuration

### Database Configuration
```properties
# Connection Pool
spring.datasource.hikari.maximum-pool-size=30
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000

# JPA Performance
spring.jpa.properties.hibernate.jdbc.batch_size=25
spring.jpa.properties.hibernate.order_inserts=true
```

### Cache Configuration
```properties
# Cache TTL
default-cache-ttl=15 minutes
long-lived-cache-ttl=60 minutes
short-lived-cache-ttl=5 minutes
```

## 🚀 Getting Started

### Prerequisites
- Java 24
- Docker & Docker Compose
- Maven 3.8+ (for local development)

### Installation

#### Using Docker (Recommended)
```bash
# Clone repository
git clone <repository-url>
cd blogApp

# Start database
docker-compose up -d postgres pgadmin

# Run application
mvn spring-boot:run -Dspring.profiles.active=docker
```

#### Local Development
```bash
# Install PostgreSQL locally
# Configure application.properties
# Run application
mvn spring-boot:run
```

### API Endpoints
- `GET /api/v1/posts` - Get all posts
- `POST /api/v1/posts` - Create new post
- `GET /api/v1/search?q=keyword&type=blog` - Search functionality
- `POST /api/v1/auth/login` - User authentication

## 📈 Performance Benchmarks

### Expected Performance
- **Response Time**: < 100ms for cached operations
- **Throughput**: 1000+ requests/second
- **Database**: Optimized queries with proper indexing
- **Memory**: Efficient caching with weak references

### Monitoring
- Real-time metrics via Actuator
- Prometheus integration for metrics collection
- Cache statistics and hit ratios
- Database connection pool monitoring

## 🔒 Security Features

- JWT-based authentication
- Role-based access control
- Password encryption (BCrypt with strength 12)
- CORS configuration
- Input validation

## 🛠️ Development

### Code Quality
- Clean Architecture principles
- SOLID principles
- Comprehensive logging
- Exception handling
- Unit testing

### Best Practices
- Repository pattern
- Service layer abstraction
- DTO pattern for API responses
- Proper transaction management
- Async processing for non-blocking operations

## 📝 License

This project is licensed under the MIT License. 