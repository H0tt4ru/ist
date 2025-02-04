package com.example.service_user.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.base_domain.dtos.UserDetailDTO;
import com.example.base_domain.entities.User;
import com.example.base_domain.entities.UserDetail;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.UserDetailRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;
import com.example.service_user.request.TopRequest;
import com.example.service_user.request.UserGetRequest;
import com.example.service_user.request.UserRequest;
import com.example.service_user.response.ListUserResponse;
import com.example.service_user.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public ResponseEntity<Object> createUser(UserDetailDTO userDetailDTO) throws Exception {
        try {
            UserDetail userDetail = buildUserDetail(userDetailDTO);
            userDetailRepository.save(userDetail);
            return ResponseEntity.ok("2000");
        } catch (Exception e) {
            if (userDetailRepository.existsById(userDetailDTO.getId())) {
                userDetailRepository.deleteById(userDetailDTO.getId());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> getUsers() throws Exception {
        try {
            List<UserDetail> userDetails = userDetailRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
            if (!userDetails.isEmpty()) {
                List<UserResponse> userResponses = userDetails.stream()
                        .map(userDetail -> {
                            try {
                                return buildUserResponse(userDetail);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
                ListUserResponse listUserResponse = ListUserResponse.builder()
                        .code("2000")
                        .message("Success")
                        .users(userResponses)
                        .build();
                return ResponseEntity.ok(listUserResponse);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> getUser(UserGetRequest userRequest) throws Exception {
        try {
            UserDetail userDetail = userDetailRepository
                    .findById(userRepository.findByEmail(userRequest.getEmail()).get().getId())
                    .orElseThrow(() -> new Exception("4000"));
            UserResponse userResponse = buildUserResponse(userDetail);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> updateUser(UserRequest userRequest) throws Exception {
        try {
            User user = userRepository.findById(userRequest.getId())
                    .orElseThrow(() -> new Exception("User not found"));
            userDetailRepository.findById(userRequest.getId())
                    .orElseThrow(() -> new Exception("User not found"));
            UserDetail userDetail = buildUserDetail(UserDetailDTO.builder()
                    .id(userRequest.getId())
                    .fullName(userRequest.getFullName())
                    .gender(userRequest.getGender())
                    .dob(LocalDate.parse(userRequest.getDob()).atStartOfDay(ZoneId.of("UTC")).toInstant())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .address(userRequest.getAddress())
                    .build());
            user.setUsername(userRequest.getUsername());
            userDetailRepository.save(userDetail);
            userRepository.save(user);
            UserResponse userResponse = buildUserResponse(userDetail);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> deleteUser(UserGetRequest userGetRequest) throws Exception {
        try {
            UserDetail userDetail = userDetailRepository
                    .findById(userRepository.findByEmail(userGetRequest.getEmail()).get().getId())
                    .orElseThrow(() -> new Exception("User not found"));
            userDetailRepository.deleteById(userDetail.getId());
            return ResponseEntity.ok("2000");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> getProfile() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            UserGetRequest userRequest = new UserGetRequest();
            userRequest.setEmail(user.getEmail());
            return getUser(userRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> updateProfile(UserRequest userRequest) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            userRequest.setId(user.getId());
            return updateUser(userRequest);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> deleteProfile() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            UserGetRequest userRequest = UserGetRequest.builder()
                    .email(user.getEmail())
                    .build();
            return deleteUser(userRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> getTopUsers(TopRequest topRequest) throws Exception {
        try {
            List<Wallet> wallets = walletRepository.findAll(Sort.by(Sort.Direction.DESC, "balance"));
            if (!wallets.isEmpty()) {
                List<UserResponse> userResponses = wallets.stream()
                        .limit(topRequest.getTop())
                        .map(wallet -> {
                            try {
                                UserDetail userDetail = userDetailRepository.findById(wallet.getId()).orElseThrow();
                                return buildUserResponse(userDetail);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
                ListUserResponse listUserResponse = ListUserResponse.builder()
                        .code("2000")
                        .message("Success")
                        .users(userResponses)
                        .build();
                return ResponseEntity.ok(listUserResponse);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    private UserDetail buildUserDetail(UserDetailDTO userDetailDTO) throws Exception {
        return UserDetail.builder()
                .id(userDetailDTO.getId())
                .fullName(userDetailDTO.getFullName())
                .gender(userDetailDTO.getGender())
                .dob(userDetailDTO.getDob())
                .phoneNumber(userDetailDTO.getPhoneNumber())
                .address(userDetailDTO.getAddress())
                .build();
    }

    private UserResponse buildUserResponse(UserDetail userDetail) throws Exception {
        User user = userRepository.findById(userDetail.getId()).orElseThrow();
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(userDetail.getFullName())
                .gender(userDetail.getGender())
                .dob(userDetail.getDob().toString())
                .phoneNumber(userDetail.getPhoneNumber())
                .address(userDetail.getAddress())
                .balance(walletRepository.findById(user.getId()).orElseThrow().getBalance())
                .build();
    }
}
