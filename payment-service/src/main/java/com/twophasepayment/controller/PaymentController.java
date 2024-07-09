package com.twophasepayment.controller;

import com.twophasepayment.dto.TransactionData;
import com.twophasepayment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/payments")
public class PaymentController
{

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;


    @Autowired
    public PaymentController(PaymentService paymentService)
    {
        this.paymentService = paymentService;
    }

    @PostMapping ("/prepare_payment")
    public ResponseEntity<String> preparePayment(@Validated @RequestBody TransactionData transactionData)
    {
        logger.info("Received prepare payment request: {}", transactionData);
        paymentService.preparePayment(transactionData);
        return ResponseEntity.ok("Payment prepared successfully");
    }

    @PostMapping ("/commit_payment")
    public ResponseEntity<String> commitPayment(@Validated @RequestBody TransactionData transactionData)
    {
        logger.info("Received commit payment request: {}", transactionData);
        paymentService.commitPayment(transactionData);
        return ResponseEntity.ok("Payment committed successfully");
    }

    @PostMapping ("/rollback_payment")
    public ResponseEntity<String> rollbackPayment(@Validated @RequestBody TransactionData transactionData)
    {
        logger.info("Received rollback payment request: {}", transactionData);
        paymentService.rollbackPayment(transactionData);
        return ResponseEntity.ok("Payment rolled back successfully");
    }

    @GetMapping ("/{orderNumber}")
    public ResponseEntity<TransactionData> getPayment(@PathVariable String orderNumber)
    {
        logger.info("Received get payment request for order number: {}", orderNumber);
        TransactionData transactionData = paymentService.findPaymentByOrderNumber(orderNumber);
        if (transactionData != null)
        {
            return ResponseEntity.ok(transactionData);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
