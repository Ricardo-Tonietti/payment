package com.ead.payment.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=5, fraction = 2)
    private BigDecimal valuePaid;
    @NotBlank
    private String cardHolderFullName;
    @NotBlank
    @CPF
    private String cardHolderCpf;
    @NotBlank
    @Size(min = 16, max=20)
    private String creditCardNumber;
    @NotBlank
    @Size(min = 4, max=10)
    private String expirationDate;
    @NotBlank
    @Size(min = 3, max=3)
    private String cvvCode;
}
