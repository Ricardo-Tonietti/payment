package com.ead.payment.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_CREDIT_CARDS")
public class CreditCardModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID cardId;
    @Column(nullable = false, length = 150)
    private String cardHolderFullName;
    @Column(nullable = false, length = 20)
    private String cardHolderCpf;
    @Column(nullable = false, length = 20)
    private String creditCardNumber;
    @Column(nullable = false, length = 10)
    private String expirationDate;
    @Column(nullable = false, length = 3)
    private String cvvCode;

    @OneToOne
    @JoinColumn(name = "user_id" , unique = true, nullable = false)
    private UserModel user;



}
