## API Endpoints

| Method | Endpoint                     | Description                      |
|--------|------------------------------|----------------------------------|
| POST   | /payments/prepare_payment    | Prepare a payment                |
| POST   | /payments/commit_payment     | Commit a payment                 |
| POST   | /payments/rollback_payment   | Rollback a payment               |
| GET    | /payments/{orderNumber}      | Get a payment by order number    |
| DELETE | /payments/{id}               | Delete a payment by ID           |


### Prepare Payment

curl -X POST http://localhost:8082/payments/prepare_payment \
-H "Content-Type: application/json" \
-d '{
  "orderNumber": "12345",
  "item": "Laptop",
  "price": "1200.00",
  "paymentMode": "CreditCard"
}'

### Commit Payment
curl -X POST http://localhost:8082/payments/commit_payment \
-H "Content-Type: application/json" \
-d '{
  "orderNumber": "12345",
  "item": "Laptop"
}'

### Rollback 

curl -X POST http://localhost:8082/payments/rollback_payment \
-H "Content-Type: application/json" \
-d '{
  "orderNumber": "12345",
  "item": "Laptop"
}'
