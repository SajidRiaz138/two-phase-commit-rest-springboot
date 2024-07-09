package com.twophasepayment;

import com.twophasepayment.dto.PaymentStatus;
import com.twophasepayment.dto.TransactionData;
import com.twophasepayment.exception.PaymentException;
import com.twophasepayment.mapper.PaymentMapper;
import com.twophasepayment.model.Payment;
import com.twophasepayment.repository.PaymentRepository;
import com.twophasepayment.serviceimpl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceImplTest
{
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPreparePaymentSuccess()
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

        when(paymentMapper.transactionDataToPayment(any(TransactionData.class))).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.preparePayment(transactionData);

        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING.name(), result.getPreparationStatus());
        verify(paymentMapper, times(1)).transactionDataToPayment(any(TransactionData.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void testPreparePaymentFailure()
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setOrderNumber("12345");
        transactionData.setItem("Laptop");
        transactionData.setPrice("1200.00");
        transactionData.setPaymentMode("CreditCard");

        when(paymentMapper.transactionDataToPayment(any(TransactionData.class))).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(PaymentException.class, () -> paymentService.preparePayment(transactionData));
        verify(paymentMapper, times(1)).transactionDataToPayment(any(TransactionData.class));
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    public void testCommitPaymentSuccess()
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

        when(paymentRepository.findByItem(transactionData.getItem())).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.commitPayment(transactionData);

        assertNotNull(result);
        assertEquals(PaymentStatus.APPROVED.name(), result.getPreparationStatus());
        verify(paymentRepository, times(1)).findByItem(transactionData.getItem());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void testCommitPaymentFailure()
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setItem("Laptop");

        when(paymentRepository.findByItem(transactionData.getItem())).thenReturn(null);

        assertThrows(PaymentException.class, () -> paymentService.commitPayment(transactionData));
        verify(paymentRepository, times(1)).findByItem(transactionData.getItem());
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    public void testRollbackPaymentSuccess()
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

        when(paymentRepository.findByItem(transactionData.getItem())).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.rollbackPayment(transactionData);

        assertNotNull(result);
        assertEquals(PaymentStatus.ROLLBACK.name(), result.getPreparationStatus());
        verify(paymentRepository, times(1)).findByItem(transactionData.getItem());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void testRollbackPaymentFailure()
    {
        TransactionData transactionData = new TransactionData();
        transactionData.setItem("Laptop");

        when(paymentRepository.findByItem(transactionData.getItem())).thenReturn(null);

        assertThrows(PaymentException.class, () -> paymentService.rollbackPayment(transactionData));
        verify(paymentRepository, times(1)).findByItem(transactionData.getItem());
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }
}
