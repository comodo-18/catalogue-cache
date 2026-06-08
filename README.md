# CatalogCache 🗂️

A Spring Boot microservice implementing the **Redis cache-aside pattern** for high-performance product catalog serving. Cache-aside behaviour verified in production — repeated requests served from Redis without hitting PostgreSQL.

[![Live](https://img.shields.io/badge/Live-Render-46E3B7?style=flat-square&logo=render)](https://catalogue-cache.onrender.com/swagger-ui.html)
[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)

---

## 🚀 Live Demo

**API Docs (Swagger):** https://catalogue-cache.onrender.com/swagger-ui.html

> Note: Free tier — first request may take 50+ seconds to wake up.

---

## 🏗️ Architecture

```
Client
  ↓
CatalogCache (Spring Boot)
  ↓              ↓
Redis Cache   PostgreSQL
(Upstash)     (Supabase)
  ↓
Kafka Producer → product-cache-invalidation topic
                       ↓
               InventorySync (consumer)
```

---

## ✨ Key Features

- **Cache-aside pattern** — Redis checked before every DB query; repeated requests served from Redis without hitting PostgreSQL
- **Observable caching** — `/api/cache/stats` endpoint tracks hit/miss rates in real-time
- **Kafka event publishing** — publishes cache invalidation events on product update/delete
- **Distributed-ready** — stateless design supports horizontal scaling
- **Observable** — hit/miss tracking with `AtomicLong` counters (thread-safe)
- **Global error handling** — consistent error responses across all endpoints

---

## 🔑 Trade-off Decisions

| Decision | Choice | Why |
|----------|--------|-----|
| Caching | Redis cache-aside | 100k+ reads/sec, sub-millisecond latency |
| Database | PostgreSQL (Supabase) | ACID compliance for product data |
| Events | Kafka | Loose coupling — CatalogCache doesn't know about InventorySync |
| Consistency | Eventual (10 min TTL) | Product browsing tolerates slight staleness |
| Kafka in prod | Disabled via `@ConditionalOnProperty` | No free Kafka broker on Render free tier |

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID (cached) |
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products/{id}` | Update product (evicts cache) |
| `DELETE` | `/api/products/{id}` | Delete product (evicts cache) |
| `GET` | `/api/cache/stats` | Cache hit/miss rate statistics |

---

## 📊 Cache Stats Response

```json
{
  "totalRequests": 10,
  "cacheHits": 8,
  "cacheMisses": 2,
  "hitRate": "80.0%"
}
```

> Hit rate increases as the same products are requested repeatedly within the TTL window (10 minutes). Resets on service restart.

---

## 🧪 Tests

- **Redis cache unit test** — verifies DB called once on cache miss, not on cache hit
- Uses `@SpyBean` to verify `productRepository.findById()` call count

```bash
./mvnw test
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 3.2.5, Java 21 |
| Cache | Redis (Upstash) — `@Cacheable`, `@CacheEvict` |
| Database | PostgreSQL (Supabase Session Pooler) |
| Messaging | Apache Kafka — cache invalidation events |
| Deployment | Render (Docker) |

---

## ⚙️ Running Locally

**Prerequisites:** Java 21, Docker

```bash
# Clone the repo
git clone https://github.com/comodo-18/catalogue-cache.git
cd catalogue-cache

# Start Kafka + Zookeeper
docker-compose up -d

# Run the app
./mvnw spring-boot:run
```

App runs on `http://localhost:8080`

---

## 🔧 Environment Variables (Production)

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://<supabase-pooler-host>:5432/postgres
DB_USER=<supabase-user>
DB_PASSWORD=<password>
REDIS_HOST=<upstash-host>
REDIS_PORT=6379
REDIS_PASSWORD=<upstash-password>
```

---

## 🔗 Related Projects

- [**InventorySync**](https://github.com/comodo-18/inventory-sync) — Kafka consumer + Redisson distributed locking for stock reservation