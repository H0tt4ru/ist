package com.example.service_transaction.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private String receiverEmail;
    private Integer amount;
    private String description;
}
