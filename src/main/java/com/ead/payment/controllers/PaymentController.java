package com.ead.payment.controllers;

import com.ead.payment.dtos.PaymentRequestDto;
import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.models.UserModel;
import com.ead.payment.services.PaymentService;
import com.ead.payment.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;
    public PaymentController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/users/{userId}/payments")
    public ResponseEntity<Object> requestPayment(@PathVariable(value = "userId")UUID userId,
                                                 @RequestBody @Valid PaymentRequestDto paymentRequestDto){

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if(userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        }
        Optional<PaymentModel> paymentModelOptional = paymentService.findLastPaymentByUser(userModelOptional.get());
        if(paymentModelOptional.isPresent()){
            if(paymentModelOptional.get().getPaymentControl().equals(PaymentControl.REQUESTED)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Payment already requested.");
            }
            if (paymentModelOptional.get().getPaymentControl().equals(PaymentControl.EFFECTED)) {
                paymentModelOptional.get();
                LocalDateTime.now(ZoneId.of("UTC"));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Payment already made.");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(paymentService.requestPayment(paymentRequestDto, userModelOptional.get()));
    }
}
