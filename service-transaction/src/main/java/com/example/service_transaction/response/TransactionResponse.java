package com.example.service_transaction.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private String senderEmail;
    private String receiverEmail;
    private Integer amount;
    private String description;
    private LocalDate date;
    private LocalTime time;
}
