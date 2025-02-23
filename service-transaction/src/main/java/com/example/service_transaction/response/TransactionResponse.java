package com.example.service_transaction.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.base_domain.response.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TransactionResponse extends BaseResponse {

    private String senderEmail;
    private String receiverEmail;
    private Integer amount;
    private String description;
    private LocalDate date;
    private LocalTime time;
}
