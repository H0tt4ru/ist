package com.example.service_authentication.response;

import com.example.base_domain.constant.Gender;
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
public class RegisterResponse extends BaseResponse {

    private String username;
    private String email;

    private String fullName;
    private Gender gender;
    private String dob;
    private String phoneNumber;
    private String address;

    private Integer balance;
}
