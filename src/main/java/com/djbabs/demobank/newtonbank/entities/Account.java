package com.djbabs.demobank.newtonbank.entities;

import com.djbabs.demobank.newtonbank.enums.AccountType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10, unique = true)
    private String accountNumber;
    @Enumerated(value = EnumType.STRING)
    private AccountType accountType;
    private Long customerId;
    @Column(precision = 19, scale = 2,nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
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
    public boolean equals(Object account) {
        return this.id.equals(((Account)account).getId()) ;

    }
}
