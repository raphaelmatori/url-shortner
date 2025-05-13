# URL Shortener

A simple and scalable URL shortening service inspired by systems like Bitly.  
This application allows users to create short links, which expire after 30 days, and get redirected quickly to the original URLs.  
It also tracks the number of clicks asynchronously.

## 🧱 Tech Stack (Dockerized Services)

| Component   | Technology                        | Purpose                                       |
|-------------|-----------------------------------|-----------------------------------------------|
| Backend     | Kotlin + Spring Boot              | Core logic and RESTful APIs                   |
| Database    | MongoDB                           | Persistent storage of shortened URLs          |
| Cache       | Redis                             | Fast lookup of redirection keys               |
| Web Server  | NGINX                             | Acts as reverse proxy for routing             |

---

## 🚀 How to Run Locally

Make sure you have Docker and Docker Compose installed.

```bash
docker-compose up --build
```

Access the system at: [http://localhost](http://localhost)

---

## 🏗️ Architecture Overview

This system follows a containerized microservice architecture with clear separation of concerns.

### 🔗 URL Creation Flow

- **Endpoint**: `POST /shorten`
- A logged-in user submits a long URL.
- Backend generates a 6-character random Base62 key.
- If a collision occurs in MongoDB (`_id` already exists), it retries.
- The entry is stored in MongoDB with TTL (30 days).
- The mapping is cached in Redis for fast retrieval.
- Response returns a shortened URL.

### ↪️ Redirection Flow

- **Endpoint**: `GET /{shortId}`
- System checks Redis first for the shortId.
- If not found, it queries MongoDB.
- If still not found, returns 404.
- If found, redirects using HTTP 302.
- Click counter is updated asynchronously.

### 🔒 Abuse Protection

- Future support for **Hashcash** or similar proof-of-work may be added to prevent abuse during link creation.

---

## 🗃️ Data Model (MongoDB)

**Collection: `short_urls`**

```json
{
  "_id": "aB9xZ2",           // Short code (Base62)
  "originalUrl": "https://example.com/long/url",
  "createdAt": ISODate("..."),
  "expiresAt": ISODate("..."),
  "clicks": 0
}
```

TTL index is applied on `expiresAt`, so documents expire automatically.

---

## 💾 Data Persistence

- **MongoDB** uses a named volume `mongo-data` to persist data across container restarts.
- **Redis** is configured with `appendonly yes` and stores data in the `redis-data` volume.

```yaml
volumes:
  mongo-data:
    driver: local
  redis-data:
    driver: local
```

To avoid data loss, **do not use `docker-compose down -v`**, which deletes volumes.

---

## 🧪 Future Improvements

- Rate limiting with Redis.
- User authentication and dashboard.
- Analytics dashboard for click stats.
- Proof-of-work during link creation.
- Distributed ID generation for horizontal scaling.

---

## 📂 Project Structure

```
.
├── src/main/kotlin/com/urlshortener
│   ├── controller        # REST endpoints
│   ├── model             # Data classes like ShortUrl
│   ├── repository        # MongoDB repositories
│   ├── service           # Business logic
│   └── config            # Redis and MongoDB config
├── Dockerfile
├── docker-compose.yml
├── nginx/
│   └── default.conf
└── README.md
```
