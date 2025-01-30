package com.example.base_domain.dtos;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO implements Serializable {

    private UUID senderId;
    private UUID receiverId;
    private Integer amount;
    private String description;
}
