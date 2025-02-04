package com.example.service_user.request;

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
public class UserRequest {

    private UUID id;
    private String username;
    private String fullName;
    private Gender gender;
    private String dob;
    private String phoneNumber;
    private String address;
}
