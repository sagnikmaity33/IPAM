# IP Address Manager (IPAM) – Phase 1

A **production-style backend system** for managing IP networks and address allocation, built using **Spring Boot, PostgreSQL, Redis, and Docker**.

This project is designed as a **learning + interview-ready system design project** to demonstrate:

* Backend architecture
* Networking algorithms
* Scalable service design
* Distributed locking
* Containerized environments

---

# 1. Project Goal

Organizations with large infrastructures manage thousands of IP addresses.
Without automation, they face problems like:

* Duplicate IP allocation
* Network conflicts
* Poor visibility of IP utilization
* Manual tracking via spreadsheets

This system provides a **backend service to manage networks, subnets, and IP allocations automatically**.

---

# 2. System Architecture

The project follows a **Clean Modular Architecture**.

```
Client
   │
REST API (Controllers)
   │
Application Services
   │
Domain Engines (Algorithms)
   │
Repository Layer
   │
Database (PostgreSQL)

+ Redis (Distributed Locking)
```

### Key Layers

| Layer      | Purpose               |
| ---------- | --------------------- |
| Controller | Handles HTTP requests |
| Service    | Business logic        |
| Engine     | Core algorithms       |
| Repository | Database access       |
| Database   | Persistent storage    |
| Redis      | Distributed locking   |

---

# 3. Tech Stack

| Technology      | Purpose              |
| --------------- | -------------------- |
| Java 17         | Programming Language |
| Spring Boot 3   | Backend framework    |
| Spring Data JPA | ORM                  |
| PostgreSQL      | Database             |
| Redis           | Distributed locking  |
| Docker          | Containerization     |
| Maven           | Build tool           |

---

# 4. Project Structure

```
src/main/java/com/ipam

config
common

engine
   cidr
      CidrCalculator.java
      CidrParser.java

   allocation
      IpAllocator.java

   validation
      CidrValidator.java
      OverlapDetector.java

network
   controller
   service
   repository
   entity
   dto

ip
   controller
   service
   repository
   entity
   dto

locking
   RedisLockService.java

IpamApplication.java
```

### Design Principles

* **Loosely Coupled Modules**
* **Single Responsibility**
* **Algorithm Isolation in Engines**

---

# 5. Database Design

## Networks Table

```
networks
---------------------------
id
cidr
network_address
broadcast_address
total_ips
description
created_at
```

## IP Addresses Table

```
ip_addresses
---------------------------
id
subnet_id
ip_address
status
hostname
mac_address
device_type
owner
allocated_at
```

### IP Status

```
ALLOCATED
FREE
RESERVED
```

---

# 6. Core Algorithms

## CIDR Calculation

CIDR is used to determine the network boundaries.

Example:

```
192.168.1.0/24
```

Results:

```
Network Address  → 192.168.1.0
Broadcast Address → 192.168.1.255
Total IPs        → 256
Usable Range     → 192.168.1.1 – 192.168.1.254
```

The system converts IP addresses into **32-bit integers** to perform efficient bitwise operations.

---

## Subnet Overlap Detection

Two networks overlap if their IP ranges intersect.

Example:

```
Subnet A
192.168.1.0/24
Range: 192.168.1.0 – 192.168.1.255

Subnet B
192.168.1.128/25
Range: 192.168.1.128 – 192.168.1.255
```

These networks overlap.

Algorithm:

```
startA ≤ endB
AND
startB ≤ endA
```

To perform this comparison efficiently, the system converts IPs to numeric values.

---

## IP Allocation Algorithm

When allocating an IP:

1. Convert usable IP range to numeric values
2. Retrieve allocated IPs from database
3. Iterate from start → end
4. Return first free IP

Example:

Allocated:

```
192.168.1.1
192.168.1.2
192.168.1.4
```

Next available:

```
192.168.1.3
```

---

# 7. Redis Distributed Lock

Concurrent requests may try to allocate the same IP.

Example race condition:

```
Request A → allocate 192.168.1.10
Request B → allocate 192.168.1.10
```

To prevent this:

```
Redis Lock
lock:subnet:{id}
```

Only one allocation operation can execute at a time.

---

# 8. Phase-1 Features

### Network Management

* Create subnet using CIDR
* Calculate network details
* List all subnets
* Update subnet metadata
* Delete subnet
* Subnet utilization statistics

### IP Allocation

* Allocate next available IP
* Allocate specific IP
* Release IP
* Bulk IP allocation
* List subnet IPs

### Conflict Detection

* CIDR validation
* Prevent subnet overlap
* Prevent duplicate IP allocation
* Reserve network and broadcast IPs

---

# 9. API Endpoints

## Network APIs

```
POST   /api/networks
GET    /api/networks
GET    /api/networks/{id}
DELETE /api/networks/{id}
```

## IP APIs

```
POST   /api/ip/allocate
POST   /api/ip/allocate-specific
POST   /api/ip/bulk-allocate
DELETE /api/ip/{ip}
GET    /api/ip/subnet/{id}
```

---

# 10. Running the Project with Docker

## Step 1 – Clone the Repository

```
git clone https://github.com/yourusername/ipam-backend.git

cd ipam-backend
```

---

## Step 2 – Create Docker Compose File

Create:

```
docker/docker-compose.yml
```

Content:

```
version: '3'

services:

  postgres:
    image: postgres:15
    container_name: ipam_postgres
    environment:
      POSTGRES_DB: ipam
      POSTGRES_USER: ipam
      POSTGRES_PASSWORD: ipam
    ports:
      - "5432:5432"

  redis:
    image: redis:7
    container_name: ipam_redis
    ports:
      - "6379:6379"
```

---

## Step 3 – Start Infrastructure

```
docker compose up -d
```

This starts:

```
PostgreSQL
Redis
```

---

## Step 4 – Run Spring Boot Application

```
./mvnw spring-boot:run
```

Application will run on:

```
http://localhost:8080
```

---

# 11. Testing the APIs

You can test using:

* Postman
* Curl
* HTTPie

---

## Create Subnet

```
POST /api/networks
```

Body:

```
192.168.1.0/24
```

---

## Allocate Next IP

```
POST /api/ip/allocate
```

Parameters:

```
subnetId=1
startIp=192.168.1.1
endIp=192.168.1.254
```

---

## Allocate Specific IP

```
POST /api/ip/allocate-specific
```

```
subnetId=1
ip=192.168.1.50
```

---

## Release IP

```
DELETE /api/ip/192.168.1.50
```

---

## List Subnet IPs

```
GET /api/ip/subnet/1
```

---

# 12. Future Improvements (Phase-2)

Next phase will introduce:

* Subnet hierarchy
* DHCP pools
* IP search engine
* Audit logging
* Pagination
* Prometheus monitoring
* Role based access control
* Kubernetes deployment

---

# 13. Learning Outcomes

This project demonstrates:

* CIDR mathematics
* Networking algorithms
* Backend architecture
* Distributed locking
* Database design
* Containerized development environments

---

# 14. License

MIT License
