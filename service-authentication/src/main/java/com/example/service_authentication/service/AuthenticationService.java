package com.example.service_authentication.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.api_core.service.JwtService;
import com.example.base_domain.constant.Role;
import com.example.base_domain.dtos.UserDTO;
import com.example.base_domain.dtos.UserDetailDTO;
import com.example.base_domain.dtos.WalletDTO;
import com.example.base_domain.entities.User;
import com.example.base_domain.entities.UserDetail;
import com.example.base_domain.entities.Wallet;
import com.example.service_authentication.client.UserDetailClient;
import com.example.service_authentication.client.WalletClient;
import com.example.service_authentication.request.LoginRequest;
import com.example.service_authentication.request.RegisterRequest;
import com.example.service_authentication.response.LoginResponse;
import com.example.service_authentication.response.RegisterResponse;
import com.example.base_domain.repositories.UserDetailRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final WalletRepository walletRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailClient userDetailClient;
    private final WalletClient walletClient;

    public ResponseEntity<Object> register(RegisterRequest registerRequest) throws Exception {
        try {
            Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());
            if (userOptional.isPresent()) {
                throw new Exception("4001");
            }
            if (true /* validation */) {
                UserDTO userDTO = UserDTO.builder().username(registerRequest.getUsername())
                        .email(registerRequest.getEmail())
                        .password(registerRequest.getPassword()).role(Role.USER).build();

                User user = User.builder().username(userDTO.getUsername()).email(userDTO.getEmail())
                        .password(passwordEncoder.encode(userDTO.getPassword())).role(userDTO.getRole()).build();

                userRepository.save(user);

                UserDetailDTO userDetailDTO = UserDetailDTO.builder()
                        .id(userRepository.findByEmail(registerRequest.getEmail()).get().getId())
                        .fullName(registerRequest.getFullName())
                        .gender(registerRequest.getGender())
                        .dob(LocalDate.parse(registerRequest.getDob()).atStartOfDay(ZoneId.of("UTC")).toInstant())
                        .phoneNumber(registerRequest.getPhoneNumber())
                        .address(registerRequest.getAddress()).build();

                ResponseEntity<String> userDetailResponse = userDetailClient.createUserDetail(userDetailDTO);
                if (!userDetailResponse.getStatusCode().is2xxSuccessful()) {
                    throw new Exception("4000");
                }

                WalletDTO walletDTO = WalletDTO.builder()
                        .id(userRepository.findByEmail(registerRequest.getEmail()).get().getId())
                        .balance(1000000).build();

                ResponseEntity<String> walletResponse = walletClient.createWallet(walletDTO);
                if (!walletResponse.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Wallet creation failed");
                }

                RegisterResponse registerResponse = RegisterResponse.builder().code("2000")
                        .message("Register successful").username(userDTO.getUsername()).email(userDTO.getEmail())
                        .fullName(userDetailDTO.getFullName())
                        .gender(userDetailDTO.getGender()).dob(userDetailDTO.getDob().toString())
                        .phoneNumber(userDetailDTO.getPhoneNumber()).address(userDetailDTO.getAddress())
                        .balance(walletDTO.getBalance()).build();

                return new ResponseEntity<>("ok", HttpStatus.OK);
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            Optional<User> userToDelete = userRepository.findByEmail(registerRequest.getEmail());
            if (userToDelete.isPresent()) {
                userRepository.delete(userToDelete.get());
            }

            Optional<UserDetail> userDetailToDelete = userDetailRepository.findById(userToDelete.get().getId());
            if (userDetailToDelete.isPresent()) {
                userDetailRepository.delete(userDetailToDelete.get());
            }

            Optional<Wallet> walletToDelete = walletRepository.findById(userToDelete.get().getId());
            if (walletToDelete.isPresent()) {
                walletRepository.delete(walletToDelete.get());
            }
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