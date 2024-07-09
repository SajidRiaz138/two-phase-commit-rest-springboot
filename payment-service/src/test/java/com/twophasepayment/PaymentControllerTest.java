package com.twophasepayment;

import com.twophasepayment.controller.PaymentController;
import com.twophasepayment.dto.PaymentStatus;
import com.twophasepayment.dto.TransactionData;
import com.twophasepayment.exception.GlobalExceptionHandler;
import com.twophasepayment.exception.PaymentException;
import com.twophasepayment.model.Payment;
import com.twophasepayment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class PaymentControllerTest
{

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(paymentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testPreparePaymentSuccess() throws Exception
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setOrderNumber("12345");
        transactionData.setItem("Laptop");
        transactionData.setPrice("1200.00");
        transactionData.setPaymentMode("CreditCard");

        Payment payment = new Payment();
        payment.setOrderNumber(transactionData.getOrderNumber());
        payment.setItem(transactionData.getItem());
        payment.setPreparationStatus(PaymentStatus.PENDING.name());
        payment.setPrice(transactionData.getPrice());
        payment.setPaymentMode(transactionData.getPaymentMode());

        when(paymentService.preparePayment(any(TransactionData.class))).thenReturn(payment);

        mockMvc.perform(post("/payments/prepare_payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderNumber\":\"12345\",\"item\":\"Laptop\",\"price\":\"1200.00\",\"paymentMode\":\"CreditCard\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment prepared successfully"));

        verify(paymentService, times(1)).preparePayment(any(TransactionData.class));
    }

    @Test
    public void testPreparePaymentFailure() throws Exception
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setOrderNumber("12345");
        transactionData.setItem("Laptop");
        transactionData.setPrice("1200.00");
        transactionData.setPaymentMode("CreditCard");

        when(paymentService.preparePayment(any(TransactionData.class))).thenThrow(new PaymentException("Test Exception"));

        mockMvc.perform(post("/payments/prepare_payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderNumber\":\"12345\",\"item\":\"Laptop\",\"price\":\"1200.00\",\"paymentMode\":\"CreditCard\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Test Exception"));

        verify(paymentService, times(1)).preparePayment(any(TransactionData.class));
    }

    @Test
    public void testCommitPaymentSuccess() throws Exception
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setOrderNumber("12345");
        transactionData.setItem("Laptop");
        transactionData.setPrice("1200.00");
        transactionData.setPaymentMode("CreditCard");

        Payment payment = new Payment();
        payment.setOrderNumber(transactionData.getOrderNumber());
        payment.setItem(transactionData.getItem());
        payment.setPreparationStatus(PaymentStatus.APPROVED.name());
        payment.setPrice(transactionData.getPrice());
        payment.setPaymentMode(transactionData.getPaymentMode());

        when(paymentService.commitPayment(any(TransactionData.class))).thenReturn(payment);

        mockMvc.perform(post("/payments/commit_payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderNumber\":\"12345\",\"item\":\"Laptop\",\"price\":\"1200.00\",\"paymentMode\":\"CreditCard\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment committed successfully"));

        verify(paymentService, times(1)).commitPayment(any(TransactionData.class));
    }

    @Test
    public void testCommitPaymentFailure() throws Exception
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setOrderNumber("12345");
        transactionData.setItem("Laptop");
        transactionData.setPrice("1200.00");
        transactionData.setPaymentMode("CreditCard");

        when(paymentService.commitPayment(any(TransactionData.class))).thenThrow(new PaymentException("Test Exception"));

        mockMvc.perform(post("/payments/commit_payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderNumber\":\"12345\",\"item\":\"Laptop\",\"price\":\"1200.00\",\"paymentMode\":\"CreditCard\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Test Exception"));

        verify(paymentService, times(1)).commitPayment(any(TransactionData.class));
    }

    @Test
    public void testRollbackPaymentSuccess() throws Exception
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setOrderNumber("12345");
        transactionData.setItem("Laptop");
        transactionData.setPrice("1200.00");
        transactionData.setPaymentMode("CreditCard");

        Payment payment = new Payment();
        payment.setOrderNumber(transactionData.getOrderNumber());
        payment.setItem(transactionData.getItem());
        payment.setPreparationStatus(PaymentStatus.ROLLBACK.name());
        payment.setPrice(transactionData.getPrice());
        payment.setPaymentMode(transactionData.getPaymentMode());

        when(paymentService.rollbackPayment(any(TransactionData.class))).thenReturn(payment);

        mockMvc.perform(post("/payments/rollback_payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderNumber\":\"12345\",\"item\":\"Laptop\",\"price\":\"1200.00\",\"paymentMode\":\"CreditCard\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment rolled back successfully"));

        verify(paymentService, times(1)).rollbackPayment(any(TransactionData.class));
    }

    @Test
    public void testRollbackPaymentFailure() throws Exception
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setOrderNumber("12345");
        transactionData.setItem("Laptop");
        transactionData.setPrice("1200.00");
        transactionData.setPaymentMode("CreditCard");

        when(paymentService.rollbackPayment(any(TransactionData.class))).thenThrow(new PaymentException("Test Exception"));

        mockMvc.perform(post("/payments/rollback_payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderNumber\":\"12345\",\"item\":\"Laptop\",\"price\":\"1200.00\",\"paymentMode\":\"CreditCard\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Test Exception"));

        verify(paymentService, times(1)).rollbackPayment(any(TransactionData.class));
    }
}
