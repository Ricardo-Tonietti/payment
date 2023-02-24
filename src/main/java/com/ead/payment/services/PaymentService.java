package com.ead.payment.services;

import com.ead.payment.dtos.PaymentRequestDto;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.models.UserModel;

import java.util.Optional;

public interface PaymentService {

    PaymentModel requestPayment(PaymentRequestDto paymentRequestDto, UserModel userModel);
    Optional<PaymentModel> findLastPaymentByUser(UserModel userModel);
}
