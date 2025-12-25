# Fund Search Engine

Backend service for managing and searching investment funds. This project upload Excel data to **PostgreSQL** and **Elasticsearch**, providing a high-performance search API with dynamic filtering and sorting capabilities.

---

## 🚀 1. Quick Start

### Prerequisites
* **Docker & Docker Desktop** (Required for Database & Elasticsearch)
* **Java 17** (Spring)

### Step 1: Start Infrastructure
Run the following command in the project root to start PostgreSQL, Elasticsearch, and Kibana:

```bash
docker-compose up -d
```

---

## 🚀 2. Run Application

Once the containers are up, start the Spring Boot application:

The app will start at http://localhost:8080

```bash
./mvnw spring-boot:run
```

---

## 3. API Usage Guide

You must upload the Excel file to fill the database and search index.

Endpoint: POST /api/funds/upload

Body: form-data -> key: file (Select your .xlsx file, there is an example file on docs directory)

```bash
curl --location 'http://localhost:8080/api/funds/upload' \
--form 'file=@"/path/to/funds.xlsx"'
```

The search endpoint is dynamic. You can combine any of the parameters below.


Parameter       Type			                  Description								                 Example
----------      ------              ---------------------------                --------------------------                              
quote           string          Partial match for Fund Code or Fund Name   quote=AK		
umbrellaType    string          Exact match for the umbrella category      umbrellaType=Hisse Senedi Şemsiye Fonu
minReturn       number          Minimum return rate filter                 minReturn=50 (returns > %50)
returnPeriod    string          Period to apply the return filter          return1Year
sort            string          Sorting field and direction                sort=return1Year,desc
page            number          Page number (starts from 0)                page=0
size            number          Page size (default 10)                     size=20

Full Example Query: "Find funds containing 'AK', within 'Hisse Senedi Şemsiye Fonu' category, with >50% yearly return, sorted by highest return, page starts from 0 and each page have 20 entry"

---

## 4. Testing with Postman

A ready-to-use Postman Collection is included in this repository.

1. Go to the docs/ folder in the project root.
2. Import oneriver.postman_collection.json into Postman.
3. Use the pre-configured requests to test various scenarios.

---

## 5. Architecture & Tech Stack

Strategy Pattern: Used for dynamic search filters (Open/Closed Principle).

CQRS-Like Separation: Write operations are handled transactionally via PostgreSQL, while read operations are offloaded to Elasticsearch.

Data Facade: A dedicated FundDataManager service manages data synchronization across multiple data sources.

Tech Stack:

Java 17 & Spring Boot 3.5.9
Elasticsearch (Search Engine)
PostgreSQL (DB)
Docker Compose
Apache POI (Excel Processing)
