package com.example.base_domain.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.example.base_domain.constant.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailDTO implements Serializable {

    private UUID id;
    private String fullName;
    private Gender gender;
    private Instant dob;
    private String phoneNumber;
    private String address;
}
