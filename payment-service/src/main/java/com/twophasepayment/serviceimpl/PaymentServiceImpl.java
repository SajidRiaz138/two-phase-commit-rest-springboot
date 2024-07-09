package com.twophasepayment.serviceimpl;

import com.twophasepayment.dto.PaymentStatus;
import com.twophasepayment.dto.TransactionData;
import com.twophasepayment.exception.PaymentException;
import com.twophasepayment.mapper.PaymentMapper;
import com.twophasepayment.model.Payment;
import com.twophasepayment.repository.PaymentRepository;
import com.twophasepayment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService
{

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper)
    {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Payment preparePayment(TransactionData transactionData)
    {
        logger.info("Preparing payment for order: {}", transactionData.getOrderNumber());
        try
        {
            Payment payment = paymentMapper.transactionDataToPayment(transactionData);
            payment.setPreparationStatus(PaymentStatus.PENDING.name());
            payment = paymentRepository.save(payment);
            return payment;
        }
        catch (Exception e)
        {
            logger.error("Error during payment preparation", e);
            throw new PaymentException("Error during payment preparation", e);
        }
    }

    @Override
    public Payment commitPayment(TransactionData transactionData)
    {
        logger.info("Committing payment for item: {}", transactionData.getItem());
        try
        {
            Payment payment = paymentRepository.findByItem(transactionData.getItem());
            if (payment != null && payment.getPreparationStatus().equalsIgnoreCase(PaymentStatus.PENDING.name()))
            {
                payment.setPreparationStatus(PaymentStatus.APPROVED.name());
                paymentRepository.save(payment);
                return payment;
            }
            else
            {
                throw new PaymentException("Payment cannot be committed");
            }
        }
        catch (Exception e)
        {
            logger.error("Error during payment commit", e);
            throw new PaymentException("Error during payment commit", e);
        }
    }

    @Override
    public Payment rollbackPayment(TransactionData transactionData)
    {
        logger.info("Rolling back payment for item: {}", transactionData.getItem());
        try
        {
            Payment payment = paymentRepository.findByItem(transactionData.getItem());
            if (payment != null)
            {
                payment.setPreparationStatus(PaymentStatus.ROLLBACK.name());
                paymentRepository.save(payment);
                return payment;
            }
            else
            {
                throw new PaymentException("Error during payment rollback");
            }
        }
        catch (Exception e)
        {
            logger.error("Error during payment rollback", e);
            throw new PaymentException("Error during payment rollback", e);
        }
    }

    @Override
    public TransactionData findPaymentByOrderNumber(String orderNumber)
    {
        logger.info("Find Payment with order number : {}", orderNumber);
        Payment payment = paymentRepository.findPaymentByOrderNumber(orderNumber);
        logger.info("Found Payment : {}", payment);
        return paymentMapper.paymentToTransactionData(payment);
    }
}
