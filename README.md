# BlogApp

Spring Boot ve PostgreSQL ile geliştirilmiş blog API'si. JWT authentication, kullanıcı yönetimi, blog yazıları, yorumlar ve beğeniler içerir.

## Özellikler
- JWT tabanlı authentication
- Kullanıcı kayıt ve giriş
- Blog yazısı CRUD işlemleri
- Yorum sistemi
- Beğeni sistemi
- Arama fonksiyonu
- Caching desteği

## Kurulum

### Gereksinimler
- Java 21+
- Maven 3.8+
- PostgreSQL

### Adımlar
1. PostgreSQL'de veritabanı oluşturun:
   ```sql
   CREATE DATABASE blogApp;
   ```

2. `application.properties` dosyasındaki veritabanı ayarlarını güncelleyin

3. Projeyi çalıştırın:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## API Endpointleri

### Authentication
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| POST | `/api/v1/auth/register` | Kullanıcı kaydı | - |
| POST | `/api/v1/auth/login` | Giriş yap | - |
| POST | `/api/v1/auth/refresh` | Token yenile | - |

### Users
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| GET | `/api/v1/users` | Tüm kullanıcılar | JWT |
| GET | `/api/v1/users/profile` | Kullanıcı profili | JWT |
| GET | `/api/v1/users/{id}` | Kullanıcı detayı | JWT |
| PUT | `/api/v1/users/{id}` | Kullanıcı güncelle | JWT |
| DELETE | `/api/v1/users/{id}` | Kullanıcı sil | JWT |

### Posts
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| GET | `/api/v1/posts` | Tüm postlar | - |
| GET | `/api/v1/posts/{id}` | Post detayı | - |
| POST | `/api/v1/posts` | Post oluştur | JWT |
| PUT | `/api/v1/posts/{id}` | Post güncelle | JWT |
| DELETE | `/api/v1/posts/{id}` | Post sil | JWT |

### Comments
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| GET | `/api/v1/comments/post/{postId}` | Post yorumları | - |
| POST | `/api/v1/comments/{postId}` | Yorum ekle | JWT |
| PUT | `/api/v1/comments/{id}` | Yorum güncelle | JWT |
| DELETE | `/api/v1/comments/{id}` | Yorum sil | JWT |

### Likes
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| POST | `/api/v1/likes/post/{postId}` | Post beğen | JWT |
| POST | `/api/v1/likes/comment/{commentId}` | Yorum beğen | JWT |
| DELETE | `/api/v1/likes/post/{likeId}` | Post beğenisini kaldır | JWT |
| DELETE | `/api/v1/likes/comment/{likeId}` | Yorum beğenisini kaldır | JWT |

### Search
| Method | Endpoint | Açıklama | Auth |
|--------|----------|----------|------|
| GET | `/api/v1/search?query=...&type=all` | Arama | - |

## Örnek Kullanım

### Kayıt Ol
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "123456",
    "firstname": "Test",
    "lastname": "User"
  }'
```

### Giriş Yap
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

### Post Oluştur
```bash
curl -X POST http://localhost:8080/api/v1/posts \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "İlk Blog Yazım",
    "text": "Bu benim ilk blog yazım."
  }'
``` 