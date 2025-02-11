package com.example.service_authentication.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api_core.service.JwtService;
import com.example.base_domain.constant.Role;
import com.example.base_domain.dtos.UserDetailDTO;
import com.example.base_domain.dtos.WalletDTO;
import com.example.base_domain.entities.User;
import com.example.service_authentication.client.UserDetailClient;
import com.example.service_authentication.client.WalletClient;
import com.example.service_authentication.request.LoginRequest;
import com.example.service_authentication.request.RegisterRequest;
import com.example.service_authentication.response.LoginResponse;
import com.example.service_authentication.response.RegisterResponse;
import com.example.base_domain.repositories.CodeRepository;
import com.example.base_domain.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CodeRepository codeRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailClient userDetailClient;
    private final WalletClient walletClient;

    @Transactional
    public ResponseEntity<Object> register(RegisterRequest registerRequest) throws Exception {
        try {
            Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());
            if (userOptional.isPresent()) {
                throw new Exception("4100");
            }

            // Validation
            if (registerRequest.getUsername().isBlank() || registerRequest.getEmail().isBlank()
                    || registerRequest.getPassword().isBlank() || registerRequest.getFullName().isBlank()
                    || registerRequest.getGender() == null || registerRequest.getDob().isBlank()
                    || registerRequest.getPhoneNumber().isBlank() || registerRequest.getAddress().isBlank()) {
                throw new Exception("4101");
            }

            if (registerRequest.getUsername().length() < 6 || registerRequest.getUsername().length() > 20) {
                throw new Exception("4102");
            }

            if (!registerRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new Exception("4103");
            }

            if (registerRequest.getPassword().length() < 6 || registerRequest.getPassword().length() > 20) {
                throw new Exception("4104");
            }

            if (registerRequest.getFullName().length() < 6 || registerRequest.getFullName().length() > 50) {
                throw new Exception("4105");
            }

            if (!(registerRequest.getGender().toString().equalsIgnoreCase("Male")
                    || registerRequest.getGender().toString().equalsIgnoreCase("Female"))) {
                throw new Exception("4106");
            }

            if (!registerRequest.getDob().matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
                throw new Exception("4107");
            }

            if (!registerRequest.getPhoneNumber().matches("^[0-9]{10,12}$")) {
                throw new Exception("4108");
            }

            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(Role.USER)
                    .build();

            User savedUser = userRepository.save(user);

            UserDetailDTO userDetailDTO = UserDetailDTO.builder()
                    .id(savedUser.getId())
                    .fullName(registerRequest.getFullName())
                    .gender(registerRequest.getGender())
                    .dob(registerRequest.getDob())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .address(registerRequest.getAddress())
                    .build();

            userDetailClient.createUserDetail(userDetailDTO);

            WalletDTO walletDTO = WalletDTO.builder()
                    .id(savedUser.getId())
                    .balance(1000)
                    .build();

            walletClient.createWallet(walletDTO);

            RegisterResponse registerResponse = RegisterResponse.builder()
                    .code("2101")
                    .message(codeRepository.findByCode("2101").get().getMessage())
                    .username(savedUser.getUser())
                    .email(savedUser.getEmail())
                    .fullName(userDetailDTO.getFullName())
                    .gender(userDetailDTO.getGender())
                    .dob(userDetailDTO.getDob())
                    .phoneNumber(userDetailDTO.getPhoneNumber())
                    .address(userDetailDTO.getAddress())
                    .balance(walletDTO.getBalance())
                    .build();

            return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            throw new Exception("4020");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> login(LoginRequest loginRequest) throws Exception {
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (!userOptional.isPresent()) {
                throw new Exception("4111");
            }

            if (loginRequest.getPassword().isEmpty() || loginRequest.getPassword().isBlank()) {
                throw new Exception("4110");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userOptional.get();
            var jwtToken = jwtService.generateToken(user);
            LoginResponse loginResponse = LoginResponse.builder()
                    .code("2102")
                    .message(codeRepository.findByCode("2102").get().getMessage())
                    .token(jwtToken)
                    .build();
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
