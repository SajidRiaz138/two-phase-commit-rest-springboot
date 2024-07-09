package com.twophasecoordinator.controller;

import com.twophasecoordinator.dto.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CoordinatorController
{

    private static final Logger logger = LoggerFactory.getLogger(CoordinatorController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value ("${order.service.prepare.url}")
    private String orderPrepareUrl;

    @Value ("${order.service.commit.url}")
    private String orderCommitUrl;

    @Value ("${order.service.rollback.url}")
    private String orderRollbackUrl;

    @Value ("${payment.service.prepare.url}")
    private String paymentPrepareUrl;

    @Value ("${payment.service.commit.url}")
    private String paymentCommitUrl;

    @Value ("${payment.service.rollback.url}")
    private String paymentRollbackUrl;

    @PostMapping ("/initiate_transaction")
    public String initiateTwoPhaseCommit(@RequestBody Transaction transaction)
    {
        logger.info("Initiating 2PC for transaction: {}", transaction);
        try
        {
            if (callPreparePhase(transaction))
            {
                if (callCommitPhase(transaction))
                {
                    logger.info("Transaction committed successfully for order number: {}", transaction.getOrderNumber());
                    return "Transaction committed successfully.";
                }

                callRollback(transaction);
                logger.warn("Transaction rollback for order number: {}", transaction.getOrderNumber());
                return "Transaction Rollback";
            }

            callRollback(transaction);
            logger.warn("Transaction rollback for order number: {}", transaction.getOrderNumber());
            return "Transaction Rollback";
        }
        catch (Exception e)
        {
            logger.error("Error during 2PC for transaction: {}", transaction, e);
            callRollback(transaction);
            return "Transaction Rollback due to an error.";
        }
    }

    private boolean callPreparePhase(Transaction transaction)
    {
        logger.info("Calling prepare phase for transaction: {}", transaction);
        try
        {
            boolean isOrderSuccess = callService(orderPrepareUrl, transaction);
            boolean isPaymentSuccess = callService(paymentPrepareUrl, transaction);

            return isOrderSuccess && isPaymentSuccess;
        }
        catch (Exception e)
        {
            logger.error("Error during prepare phase for transaction: {}", transaction, e);
            return false;
        }
    }

    private boolean callCommitPhase(Transaction transaction)
    {
        logger.info("Calling commit phase for transaction: {}", transaction);
        try
        {
            boolean isOrderSuccess = callService(orderCommitUrl, transaction);
            boolean isPaymentSuccess = callService(paymentCommitUrl, transaction);

            return isOrderSuccess && isPaymentSuccess;
        }
        catch (Exception e)
        {
            logger.error("Error during commit phase for transaction: {}", transaction, e);
            return false;
        }
    }

    private boolean callService(String url, Transaction transaction)
    {
        ResponseEntity<String> response = restTemplate.postForEntity(url, transaction, String.class);
        return response != null && response.getStatusCode().is2xxSuccessful();
    }

    private void callRollback(Transaction transaction)
    {
        logger.info("Calling rollback phase for transaction: {}", transaction);
        callServiceRollback(orderRollbackUrl, transaction);
        callServiceRollback(paymentRollbackUrl, transaction);
    }

    private void callServiceRollback(String serviceUrl, Transaction transaction)
    {
        try
        {
            restTemplate.postForEntity(serviceUrl, transaction, Void.class);
        }
        catch (Exception e)
        {
            logger.error("Error during rollback for URL: {} and transaction: {}", serviceUrl, transaction, e);
        }
    }
}
