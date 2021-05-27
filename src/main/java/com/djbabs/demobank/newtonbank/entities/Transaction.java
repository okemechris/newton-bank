package com.djbabs.demobank.newtonbank.entities;

import com.djbabs.demobank.newtonbank.enums.PaymentChannel;
import com.djbabs.demobank.newtonbank.enums.TransactionDirection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 19, scale = 2,nullable = false)
    private BigDecimal amount;
    @Column(precision = 19, scale = 2,nullable = false)
    private BigDecimal accountBalance;
    @Column(nullable = false)
    private String accountNumber;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date valueDate;
    @Enumerated(value = EnumType.STRING)
    private PaymentChannel paymentChannel;
    private String channelName;
    @Column(length = 15)
    private String destinationBankCode;
    @Column(length = 10)
    private String destinationAccountNumber;
    private String destinationAccountName;
    @Column(length = 50)
    private String paymentReference;
    private String narration;
    @Enumerated(value = EnumType.STRING)
    private TransactionDirection direction;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;


    @PrePersist
    private void setCreatedAt() {
        dateAdded = new Date();
    }

    @PreUpdate
    private void setUpdatedAt() {
        dateUpdated = new Date();
    }

    @Override
    public boolean equals(Object transaction) {
        return this.id.equals(((Transaction)transaction).getId()) ;

    }

}
