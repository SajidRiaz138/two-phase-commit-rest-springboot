# Order Service

This is a RESTful Order Service implemented using Spring Boot. The service handles the preparation, committing, and rollback of orders. It also provides endpoints to get and delete orders.

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [License](#license)

## Requirements

- Java 17
- Maven
- Spring Boot 3 or higher

## Installation

1. Clone the repository:

    ```sh
    clone service
    cd orderservice
    ```

2. Build the project:

    ```sh
    # Using Maven
    mvn clean install

3. Run the application:

    ```sh
    # Using Maven
    mvn spring-boot:run

## Usage

Once the application is running, you can interact with it using a tool like `curl` or Postman.

### Prepare Order

curl -X POST http://localhost:8081/orders/prepare_order \
-H "Content-Type: application/json" \
-d '{
  "orderNumber": "12345",
  "item": "Laptop",
  "price": "1200.00",
  "paymentMode": "CreditCard"
}'

### Commit Order

curl -X POST http://localhost:8081/orders/commit_order \
-H "Content-Type: application/json" \
-d '{
  "orderNumber": "12345",
  "item": "Laptop"
}'

### Rollback order

curl -X POST http://localhost:8081/orders/rollback_order \
-H "Content-Type: application/json" \
-d '{
"orderNumber": "12345",
"item": "Laptop"
}'

### Get Order

curl -X GET http://localhost:8081/orders/12345

curl -X DELETE http://localhost:8081/orders/1

## API Endpoints

| Method | Endpoint                | Description                      |
|--------|--------------------------|----------------------------------|
| POST   | /orders/prepare_order    | Prepare an order                 |
| POST   | /orders/commit_order     | Commit an order                  |
| POST   | /orders/rollback_order   | Rollback an order                |
| GET    | /orders/{orderNumber}    | Get an order by order number     |
| DELETE | /orders/{id}             | Delete an order by ID            |





