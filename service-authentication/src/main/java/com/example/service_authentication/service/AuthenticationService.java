package com.example.service_authentication.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.api_core.service.JwtService;
import com.example.base_domain.constant.Role;
import com.example.base_domain.dtos.UserDTO;
import com.example.base_domain.dtos.UserDetailDTO;
import com.example.base_domain.dtos.WalletDTO;
import com.example.base_domain.entities.UserDetail;
import com.example.base_domain.entities.User;
import com.example.service_authentication.request.LoginRequest;
import com.example.service_authentication.request.RegisterRequest;
import com.example.service_authentication.response.LoginResponse;
import com.example.service_authentication.response.RegisterResponse;
import com.example.base_domain.repositories.UserRepository;

import jakarta.jms.ObjectMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JmsTemplate jmsTemplate;

    public ResponseEntity<Object> register(RegisterRequest registerRequest) throws Exception {
        try {
            if (true /* validation */) {
                UserDTO userDTO = UserDTO.builder().username(registerRequest.getUsername())
                        .email(registerRequest.getEmail())
                        .password(registerRequest.getPassword()).role(Role.USER).build();

                User user = User.builder().user(userDTO.getUsername()).email(userDTO.getEmail())
                        .password(passwordEncoder.encode(userDTO.getPassword())).role(userDTO.getRole()).build();

                userRepository.save(user);

                UserDetailDTO userDetailDTO = UserDetailDTO.builder()
                        .id(userRepository.findByEmail(registerRequest.getEmail()).get().getId())
                        .fullName(registerRequest.getFullName())
                        .gender(registerRequest.getGender())
                        .dob(Instant.parse(registerRequest.getDob())).phoneNumber(registerRequest.getPhoneNumber())
                        .address(registerRequest.getAddress()).build();

                jmsTemplate.send("createUserDetail", session -> {
                    ObjectMessage message = session.createObjectMessage(userDetailDTO);
                    return message;
                });

                WalletDTO walletDTO = WalletDTO.builder()
                        .id(userRepository.findByEmail(registerRequest.getEmail()).get().getId())
                        .balance(1000000).build();

                jmsTemplate.send("createWallet", session -> {
                    ObjectMessage message = session.createObjectMessage(walletDTO);
                    return message;
                });

                RegisterResponse registerResponse = RegisterResponse.builder().code("2000")
                        .message("Register successful").username(userDTO.getUsername()).email(userDTO.getEmail())
                        .fullName(userDetailDTO.getFullName())
                        .gender(userDetailDTO.getGender()).dob(userDetailDTO.getDob().toString())
                        .phoneNumber(userDetailDTO.getPhoneNumber()).address(userDetailDTO.getAddress())
                        .balance(walletDTO.getBalance()).build();

                return new ResponseEntity<>(registerResponse, HttpStatus.OK);
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> login(LoginRequest loginRequest) throws Exception {
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isPresent()) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                User user = userOptional.get();
                var jwtToken = jwtService.generateToken(user);
                LoginResponse loginResponse = LoginResponse.builder().code("2001").message("Login successful")
                        .token(jwtToken).build();
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            } else {
                throw new Exception("4201");
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}