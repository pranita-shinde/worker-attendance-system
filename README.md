🚀 Project: Worker Attendance & Overtime Management System
📌 Overview

This project implements a backend system for managing construction site workers, their attendance, overtime calculation, and settlement workflows.

It is built using Java Spring Boot, with PostgreSQL (Supabase) for persistence and Redis for real-time caching.

⚙️ Tech Stack
Java 17
Spring Boot
Spring Data JPA (Hibernate)
PostgreSQL (Supabase)
Redis (Caching)
Maven

📦 Setup Instructions
1. Clone Repo
git clone <your-repo-url>
cd <project>

2. PostgreSQL Setup

Update application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=postgres
spring.datasource.password=your_password

3. Redis Setup
   
Run Redis locally:
sudo service redis-server start

🧩 Features Implemented

👷 Worker Management
Create, delete workers
Validation and duplicate checks

🏗️ Site Management
Create and manage construction sites

⏱️ Attendance System
Clock-in / Clock-out
Prevent duplicate clock-ins
Real-time active workers via Redis

💰 Overtime Engine
Auto calculation after 8 hours
1.5x for first 2 hours
2x beyond that
Monthly cap: 60 hours

💸 Overtime Settlement
Monthly settlement API
Atomic transactions (all-or-nothing)
Prevents partial updates
Cannot settle current month

⚡ Redis Caching
Active workers stored in Redis
TTL: 16 hours (auto-expiry)
Cache invalidation on clock-out

🛡️ Fault Tolerance
Redis failure handled gracefully
Application works without Redis


🧪 APIs (Postman Tested)

Attendance

POST /api/v1/attendance/clock-in

POST /api/v1/attendance/clock-out

GET /api/v1/attendance/active

GET /api/v1/attendance/log


Overtime

POST /api/v1/overtime/settle/{workerId}?month=YYYY-MM

🐞 Ticket Fixes

LF-201	CORS configured

LF-202	Redis failure handled gracefully

LF-203	Pagination + N+1 fixed using JOIN FETCH

LF-204	Atomic settlement using @Transactional + event listener

LF-205	HikariCP connection pool tuning

🤖 AI Usage

AI tools (ChatGPT, Copilot) were used for:

Designing entity relationships
Structuring service-layer logic
Debugging errors and edge cases

Manual validation was done to:

Verify business rules
Prevent incorrect logic (AI hallucination)
Ensure alignment with assignment requirements

💡 Design Decisions
Used Redis for real-time active workers to reduce DB load
Implemented transactional boundaries for settlement to ensure data integrity
Applied JOIN FETCH to avoid N+1 query problem
Separated business logic from controller for clean architecture
