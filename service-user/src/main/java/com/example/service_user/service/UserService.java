package com.example.service_user.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
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
import com.example.base_domain.repositories.CodeRepository;
import com.example.base_domain.repositories.UserDetailRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;
import com.example.base_domain.response.BaseResponse;
import com.example.service_user.request.GetTopRequest;
import com.example.service_user.request.GetUserRequest;
import com.example.service_user.response.ListUserResponse;
import com.example.service_user.response.UserResponse;
import com.example.service_user.response.UsersResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final CodeRepository codeRepository;

    @Transactional
    public ResponseEntity<Object> createUser(UserDetailDTO userDetailDTO) throws Exception {
        try {
            Optional<UserDetail> userDetailOptional = userDetailRepository
                    .findByPhoneNumber(userDetailDTO.getPhoneNumber());
            if (userDetailOptional.isPresent()) {
                throw new Exception("4100");
            }

            UserDetail userDetail = UserDetail.builder()
                    .id(userDetailDTO.getId())
                    .fullName(userDetailDTO.getFullName())
                    .gender(userDetailDTO.getGender())
                    .dob(LocalDate.parse(userDetailDTO.getDob()).atStartOfDay(ZoneId.of("UTC")).toInstant())
                    .phoneNumber(userDetailDTO.getPhoneNumber())
                    .address(userDetailDTO.getAddress())
                    .build();

            userDetailRepository.save(userDetail);

            UserResponse userResponse = UserResponse.builder()
                    .code("2201")
                    .message(codeRepository.findByCode("2201").get().getMessage())
                    .username(userRepository.findById(userDetailDTO.getId()).get().getUser())
                    .email(userRepository.findById(userDetailDTO.getId()).get().getEmail())
                    .fullName(userDetail.getFullName())
                    .gender(userDetail.getGender())
                    .dob(userDetail.getDob().toString())
                    .phoneNumber(userDetail.getPhoneNumber())
                    .address(userDetail.getAddress())
                    .build();

            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getUsers() throws Exception {
        try {
            List<UserDetail> userDetails = userDetailRepository.findAll(Sort.by(Sort.Direction.DESC, "fullName"));
            if (userDetails.isEmpty()) {
                BaseResponse baseResponse = BaseResponse.builder()
                        .code("2202")
                        .message(codeRepository.findByCode("2202").get().getMessage())
                        .build();
                return new ResponseEntity<>(baseResponse, HttpStatus.NO_CONTENT);
            }

            List<UsersResponse> userResponses = userDetails.stream()
                    .map(detail -> {
                        User user = userRepository.findById(detail.getId()).get();
                        return UsersResponse.builder()
                                .username(user.getUser())
                                .email(user.getEmail())
                                .fullName(detail.getFullName())
                                .gender(detail.getGender())
                                .dob(detail.getDob().toString())
                                .phoneNumber(detail.getPhoneNumber())
                                .address(detail.getAddress())
                                .balance(walletRepository.findById(detail.getId()).get().getBalance())
                                .build();
                    })
                    .collect(Collectors.toList());

            ListUserResponse listUserResponse = ListUserResponse.builder()
                    .code("2202")
                    .message(codeRepository.findByCode("2202").get().getMessage())
                    .users(userResponses)
                    .build();
            return ResponseEntity.ok(listUserResponse);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getUser(GetUserRequest userRequest) throws Exception {
        try {

            if (userRequest.getEmail().isBlank()) {
                throw new Exception("4201");
            }

            Optional<UserDetail> userDetailOptional = userDetailRepository
                    .findById(userRepository.findByEmail(userRequest.getEmail()).get().getId());
            if (userDetailOptional.isEmpty()) {
                throw new Exception("4202");
            }

            UserDetail userDetail = userDetailOptional.get();

            UserResponse userResponse = UserResponse.builder()
                    .code("2203")
                    .message(codeRepository.findByCode("2203").get().getMessage())
                    .username(userRepository.findById(userDetail.getId()).get().getUser())
                    .email(userRepository.findById(userDetail.getId()).get().getEmail())
                    .fullName(userDetail.getFullName())
                    .gender(userDetail.getGender())
                    .dob(userDetail.getDob().toString())
                    .phoneNumber(userDetail.getPhoneNumber())
                    .address(userDetail.getAddress())
                    .balance(walletRepository.findById(userDetail.getId()).get().getBalance())
                    .build();

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> updateUser(UserDetailDTO userDetailDTO) throws Exception {
        try {
            if (userDetailDTO.getId() == null ||
                    userDetailDTO.getFullName() == null || userDetailDTO.getFullName().isEmpty() ||
                    userDetailDTO.getGender() == null ||
                    userDetailDTO.getDob() == null || userDetailDTO.getDob().isEmpty() ||
                    userDetailDTO.getPhoneNumber() == null || userDetailDTO.getPhoneNumber().isEmpty() ||
                    userDetailDTO.getAddress() == null || userDetailDTO.getAddress().isEmpty()) {
                throw new Exception("4201");
            }

            UserDetail userDetail = userDetailRepository.findById(userDetailDTO.getId())
                    .orElseThrow(() -> new Exception("4202"));

            userDetail.setFullName(userDetailDTO.getFullName());
            userDetail.setGender(userDetailDTO.getGender());
            userDetail.setDob(LocalDate.parse(userDetailDTO.getDob()).atStartOfDay(ZoneId.of("UTC")).toInstant());
            userDetail.setPhoneNumber(userDetailDTO.getPhoneNumber());
            userDetail.setAddress(userDetailDTO.getAddress());

            userDetailRepository.save(userDetail);

            UserResponse userResponse = UserResponse.builder()
                    .code("2204")
                    .message(codeRepository.findByCode("2204").get().getMessage())
                    .username(userRepository.findById(userDetailDTO.getId()).get().getUser())
                    .email(userRepository.findById(userDetailDTO.getId()).get().getEmail())
                    .fullName(userDetail.getFullName())
                    .gender(userDetail.getGender())
                    .dob(userDetail.getDob().toString())
                    .phoneNumber(userDetail.getPhoneNumber())
                    .address(userDetail.getAddress())
                    .balance(walletRepository.findById(userDetail.getId()).get().getBalance())
                    .build();

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteUser(GetUserRequest userGetRequest) throws Exception {
        try {
            UserDetail userDetail = userDetailRepository
                    .findById(userRepository.findByEmail(userGetRequest.getEmail()).get().getId())
                    .orElseThrow(() -> new Exception("4202"));
            userDetailRepository.deleteById(userDetail.getId());
            if (userRepository.getReferenceById(userDetail.getId()) == null) {
                throw new Exception("4202");
            }
            if (walletRepository.getReferenceById(userDetail.getId()) == null) {
                throw new Exception("4202");
            }
            userRepository.deleteById(userDetail.getId());
            walletRepository.deleteById(userDetail.getId());
            BaseResponse baseResponse = BaseResponse.builder()
                    .code("2205")
                    .message(codeRepository.findByCode("2205").get().getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> getProfile() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            GetUserRequest userRequest = GetUserRequest.builder()
                    .email(user.getEmail())
                    .build();
            return getUser(userRequest);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> updateProfile(UserDetailDTO userDetailDTO) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            userDetailDTO.setId(user.getId());
            return updateUser(userDetailDTO);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> deleteProfile() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            GetUserRequest userRequest = GetUserRequest.builder()
                    .email(user.getEmail())
                    .build();
            return deleteUser(userRequest);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> getTopUsers(GetTopRequest topRequest) throws Exception {
        try {
            List<Wallet> wallets = walletRepository.findAll(Sort.by(Sort.Direction.DESC, "balance"));
            if (wallets.isEmpty()) {
                BaseResponse baseResponse = BaseResponse.builder()
                        .code("2202")
                        .message(codeRepository.findByCode("2202").get().getMessage())
                        .build();

                return new ResponseEntity<>(baseResponse, HttpStatus.NO_CONTENT);
            }
            List<UsersResponse> userResponses = wallets.stream()
                    .limit(topRequest.getTop())
                    .map(wallet -> {
                        try {
                            UserDetail userDetail = userDetailRepository.findById(wallet.getId()).orElseThrow();
                            return UsersResponse.builder()
                                    .username(userRepository.findById(wallet.getId()).get().getUser())
                                    .email(userRepository.findById(wallet.getId()).get().getEmail())
                                    .fullName(userDetail.getFullName())
                                    .gender(userDetail.getGender())
                                    .dob(userDetail.getDob().toString())
                                    .phoneNumber(userDetail.getPhoneNumber())
                                    .address(userDetail.getAddress())
                                    .balance(wallet.getBalance())
                                    .build();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
            ListUserResponse listUserResponse = ListUserResponse.builder()
                    .code("2202")
                    .message(codeRepository.findByCode("2202").get().getMessage())
                    .users(userResponses)
                    .build();

            return new ResponseEntity<>(listUserResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
