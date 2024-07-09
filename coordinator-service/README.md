
### Explanation of Coordinator Functionality

The Coordinator Service uses the two-phase commit protocol to ensure consistency across the Order and Payment Services. It works in two phases:

1. **Prepare Phase**:
    - The Coordinator sends a prepare request to both the Order and Payment Services.
    - Both services prepare the transaction and respond with a success status if they are ready to commit.

2. **Commit Phase**:
    - If both services respond positively to the prepare request, the Coordinator sends a commit request to both services to finalize the transaction.
    - If any service fails during the prepare phase, the Coordinator sends a rollback request to both services to abort the transaction.

By following this protocol, the Coordinator ensures that both services either commit or rollback the transaction together, maintaining data consistency.


### /initiate_transaction

http://localhost:8080/initiate_transaction