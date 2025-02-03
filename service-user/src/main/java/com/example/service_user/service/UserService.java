package com.example.service_user.service;

import java.util.List;
import java.util.UUID;
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
import com.example.base_domain.repositories.UserDetailRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.service_user.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Object> createUser(UserDetailDTO userDetailDTO) {
        try {
            UserDetail userDetail = buildUserDetail(userDetailDTO);
            userDetailRepository.save(userDetail);
            return ResponseEntity.ok("2000");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> getUsers() {
        try {
            List<UserDetail> userDetails = userDetailRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
            if (!userDetails.isEmpty()) {
                List<UserResponse> userResponses = userDetails.stream()
                        .map(this::buildUserResponse)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(userResponses);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("4000");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> getUser(UUID id) {
        try {
            UserDetail userDetail = userDetailRepository.findById(id)
                    .orElseThrow(() -> new Exception("4000"));
            UserResponse userResponse = buildUserResponse(userDetail);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("4000");
        }
    }

    public ResponseEntity<Object> updateUser(UserDetailDTO userDetailDTO) {
        try {
            UserDetail userDetail = userDetailRepository.findById(userDetailDTO.getId())
                    .orElseThrow(() -> new Exception("User not found"));
            updateUserDetail(userDetail, userDetailDTO);
            userDetailRepository.save(userDetail);
            return ResponseEntity.ok("2000");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> deleteUser(UUID id) {
        try {
            UserDetail userDetail = userDetailRepository.findById(id)
                    .orElseThrow(() -> new Exception("User not found"));
            userDetailRepository.delete(userDetail);
            return ResponseEntity.ok("2000");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("4000");
        }
    }

    public ResponseEntity<Object> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            return getUser(user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("4000");
        }
    }

    public ResponseEntity<Object> updateProfile(UserDetailDTO userDetailDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            userDetailDTO.setId(user.getId());
            return updateUser(userDetailDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("4000");
        }
    }

    public ResponseEntity<Object> deleteProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("4000"));
            return deleteUser(user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("4000");
        }
    }

    private UserDetail buildUserDetail(UserDetailDTO userDetailDTO) {
        return UserDetail.builder()
                .id(userDetailDTO.getId())
                .fullName(userDetailDTO.getFullName())
                .gender(userDetailDTO.getGender())
                .dob(userDetailDTO.getDob())
                .phoneNumber(userDetailDTO.getPhoneNumber())
                .address(userDetailDTO.getAddress())
                .build();
    }

    private void updateUserDetail(UserDetail userDetail, UserDetailDTO userDetailDTO) {
        userDetail.setFullName(userDetailDTO.getFullName());
        userDetail.setGender(userDetailDTO.getGender());
        userDetail.setDob(userDetailDTO.getDob());
        userDetail.setPhoneNumber(userDetailDTO.getPhoneNumber());
        userDetail.setAddress(userDetailDTO.getAddress());
    }

    private UserResponse buildUserResponse(UserDetail userDetail) {
        User user = userRepository.findById(userDetail.getId()).orElseThrow();
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(userDetail.getFullName())
                .gender(userDetail.getGender())
                .dob(userDetail.getDob().toString())
                .phoneNumber(userDetail.getPhoneNumber())
                .address(userDetail.getAddress())
                .build();
    }
}
