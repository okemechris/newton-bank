package com.djbabs.demobank.newtonbank.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private Boolean success;
    private int status;
    private T data;
}
