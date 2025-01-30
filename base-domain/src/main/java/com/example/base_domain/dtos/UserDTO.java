package com.example.base_domain.dtos;

import java.io.Serializable;

import com.example.base_domain.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private String username;
    private String email;
    private String password;
    private Role role;
}
