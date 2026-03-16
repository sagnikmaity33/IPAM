Overview

IPAM (IP Address Management) is a backend system designed to manage network subnets and IP allocations efficiently.

This project implements a scalable backend architecture capable of managing large network infrastructures by combining:

CIDR-based subnet management

hierarchical network organization

high-performance IP allocation using Redis bitmaps

PostgreSQL persistence

modular engine-based architecture

Phase-2 extends the Phase-1 implementation by introducing:

Network hierarchy

Redis bitmap allocation

engine-based architecture

scalable validation and CIDR processing

Technology Stack
Layer	Technology
Backend	Spring Boot
Language	Java 17
Database	PostgreSQL
Cache	Redis
Build Tool	Maven
Containerization	Docker
System Architecture

The system follows a layered architecture with clear separation of responsibilities.

What is this?
Layered Architecture
Controller Layer

Controllers expose REST APIs to interact with the system.

Controllers:

NetworkController
IpController
NetworkHierarchyController

Example endpoints:

POST /api/networks
POST /api/networks/{id}/subnet
POST /api/ip/allocate
POST /api/ip/bulk-allocate
DELETE /api/ip/{ip}
GET /api/networks/tree

Responsibilities:

handle HTTP requests

validate inputs

call service layer

Service Layer

The service layer contains business logic and orchestrates interactions between engines and repositories.

Services:

NetworkService
NetworkHierarchyService
IpAllocationService
SubnetSplitService
NetworkTreeService

Responsibilities:

Network creation
Subnet hierarchy management
IP allocation orchestration
Validation coordination
Data persistence
Engine Layer

The engine layer performs core computational logic.

Separating computation into engines makes the architecture modular and scalable.

CIDR Engine

Components:

CidrParser
CidrCalculator
ParsedCidr

Responsibilities:

Parse CIDR notation
Calculate network address
Calculate broadcast address
Compute total IP addresses
Convert IP addresses to numeric format

Example:

CIDR: 10.1.1.0/24
Network: 10.1.1.0
Broadcast: 10.1.1.255
Total IPs: 256
Validation Engine

Components:

CidrValidator
OverlapDetector
HierarchyValidator

Responsibilities:

Validate CIDR format
Prevent overlapping subnets
Validate subnet hierarchy relationships

Overlap detection logic:

start1 <= end2 && start2 <= end1

This ensures no two networks overlap.

Allocation Engine

Component:

RedisBitmapAllocator

This engine provides high performance IP allocation.

Redis stores subnet allocation state as a bitmap.

Example Redis key:

ipam:bitmap:subnet:5

Each bit represents an IP address.

0 = free
1 = allocated

Example bitmap:

000010011

Redis operations used:

BITPOS
SETBIT
GETBIT

Time complexity:

O(1)

This allows extremely fast IP allocation even for large subnets.

Data Layer
PostgreSQL

Stores persistent metadata.

Tables:

networks
id
cidr
network_address
broadcast_address
total_ips
parent_network_id
created_at
ip_addresses
id
subnet_id
ip_address
status
hostname
mac_address
device_type
owner
allocated_at

PostgreSQL acts as the source of truth for all network data.

Redis

Redis stores allocation state for fast lookup.

Example key:

ipam:bitmap:subnet:{subnetId}

Example:

ipam:bitmap:subnet:5

Redis enables:

O(1) allocation
fast lookup
minimal memory usage
Network Hierarchy

Phase-2 introduces hierarchical subnet management.

Example hierarchy:

10.0.0.0/8
└── 10.1.0.0/16
└── 10.1.1.0/24

Hierarchy rules:

Child subnet must lie inside parent subnet
Child subnets must not overlap

Hierarchy validation ensures correct network organization.

Network Creation Flow
What is this?
IP Allocation Flow
What is this?
Subnet Hierarchy Flow
What is this?
Deployment Architecture

The application is deployed using Docker containers.

What is this?

Containers:

Spring Boot Application
PostgreSQL Database
Redis Cache
Performance Optimizations
Feature	Benefit
Redis Bitmap Allocation	O(1) IP allocation
CIDR Engine	Efficient subnet calculation
Hierarchy Engine	Structured network organization
Validation Engine	Prevent invalid networks
Example API Usage

Create network:

curl -X POST http://localhost:8080/api/networks \
-H "Content-Type: application/json" \
-d "10.0.0.0/8"

Create subnet:

curl -X POST http://localhost:8080/api/networks/3/subnet \
-H "Content-Type: application/json" \
-d "10.1.0.0/16"

Allocate IP:

curl -X POST \
"http://localhost:8080/api/ip/allocate?subnetId=5&startIp=10.1.1.1&endIp=10.1.1.254"
Future Improvements

Possible enhancements include:

Interval tree subnet lookup
Audit logging system
Swagger API documentation
Observability metrics
Kubernetes deployment
Summary

Phase-2 transforms the IPAM system into a scalable backend service by introducing:

Network hierarchy management
Redis bitmap IP allocation
Engine-based architecture
Layered backend design