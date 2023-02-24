package com.ead.payment.services.impl;

import com.ead.payment.dtos.PaymentRequestDto;
import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.models.UserModel;
import com.ead.payment.repositories.CreditCardRepository;
import com.ead.payment.repositories.PaymentRepository;
import com.ead.payment.services.PaymentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final CreditCardRepository creditCardRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardRepository creditCardRepository) {
        this.paymentRepository = paymentRepository;
        this.creditCardRepository = creditCardRepository;
    }
    @Transactional
    @Override
    public PaymentModel requestPayment(PaymentRequestDto paymentRequestDto, UserModel userModel) {

        var creditCardModel = new CreditCardModel();
        var creditCardModelOptional = creditCardRepository.findByUser(userModel);

        if(creditCardModelOptional.isPresent()){
            creditCardModel = creditCardModelOptional.get();
        }

        BeanUtils.copyProperties(paymentRequestDto, creditCardModel);
        creditCardModel.setUser(userModel);
        creditCardRepository.save(creditCardModel);

        var paymentModel = new PaymentModel();
        paymentModel.setPaymentControl(PaymentControl.REQUESTED);
        paymentModel.setPaymentRequestDate(LocalDateTime.now(ZoneId.of("UTC")));
        paymentModel.setPaymentExpirationDate(LocalDateTime.now(ZoneId.of("UTC")).plusDays(30));
        paymentModel.setLastDigitsCreditCard(paymentRequestDto.getCreditCardNumber().substring(paymentRequestDto.getCreditCardNumber().length()-4));
        paymentModel.setValuePaid(paymentRequestDto.getValuePaid());
        paymentModel.setPaymentMessage("Payment Requested");
        paymentModel.setUser(userModel);
        paymentRepository.save(paymentModel);
        //send request to queue
        return paymentModel;
    }

    @Override
    public Optional<PaymentModel> findLastPaymentByUser(UserModel userModel) {
        return paymentRepository.findTopByUserOrderByPaymentRequestDateDesc(userModel);
    }
}
