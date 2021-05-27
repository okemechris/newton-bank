package com.djbabs.demobank.newtonbank.pojos;

import com.djbabs.demobank.newtonbank.enums.AccountType;
import lombok.Data;

@Data
public class NewAccountRequest {
    private AccountType accountType;
    private Long customerId;
}
