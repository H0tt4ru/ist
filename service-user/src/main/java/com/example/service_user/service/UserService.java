package com.example.service_user.service;

import java.util.List;
import java.util.Optional;
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

    public ResponseEntity<Object> createUser(UserDetailDTO userDetailDTO) throws Exception {
        try {
            UserDetail userDetail = UserDetail.builder()
                    .id(userDetailDTO.getId())
                    .fullName(userDetailDTO.getFullName())
                    .gender(userDetailDTO.getGender())
                    .dob(userDetailDTO.getDob())
                    .phoneNumber(userDetailDTO.getPhoneNumber())
                    .address(userDetailDTO.getAddress())
                    .build();
            userDetailRepository.save(userDetail);
            return ResponseEntity.ok("2000");
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> getUsers() throws Exception {
        try {
            List<UserDetail> userDetail = userDetailRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
            if (userDetail.size() > 0) {
                return ResponseEntity.ok(userDetail.stream().map(user -> UserResponse.builder()
                        .username(userRepository.findById(user.getId()).get().getUsername())
                        .email(userRepository.findById(user.getId()).get().getEmail())
                        .fullName(user.getFullName())
                        .gender(user.getGender())
                        .dob(user.getDob().toString())
                        .phoneNumber(user.getPhoneNumber())
                        .address(user.getAddress())
                        .build()).collect(Collectors.toList()));
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> getUser(UUID id) throws Exception {
        try {
            Optional<UserDetail> userDetail = userDetailRepository.findById(id);
            if (userDetail.isPresent()) {
                return new ResponseEntity<>(UserResponse.builder()
                        .username(userRepository.findById(id).get().getUsername())
                        .email(userRepository.findById(id).get().getEmail())
                        .fullName(userDetail.get().getFullName())
                        .gender(userDetail.get().getGender())
                        .dob(userDetail.get().getDob().toString())
                        .phoneNumber(userDetail.get().getPhoneNumber())
                        .address(userDetail.get().getAddress())
                        .build(), HttpStatus.OK);
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> updateUser(UserDetailDTO userDetailDTO) throws Exception {
        try {
            Optional<UserDetail> optionalUserDetail = userDetailRepository.findById(userDetailDTO.getId());
            if (optionalUserDetail.isPresent()) {
                UserDetail userDetail = UserDetail.builder()
                        .id(userDetailDTO.getId())
                        .fullName(userDetailDTO.getFullName())
                        .gender(userDetailDTO.getGender())
                        .dob(userDetailDTO.getDob())
                        .phoneNumber(userDetailDTO.getPhoneNumber())
                        .address(userDetailDTO.getAddress())
                        .build();
                userDetailRepository.save(userDetail);
                return ResponseEntity.ok("2000");
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> deleteUser(UUID id) throws Exception {
        try {
            Optional<UserDetail> userDetail = userDetailRepository.findById(id);
            if (userDetail.isPresent()) {
                userDetailRepository.deleteById(id);
                return ResponseEntity.ok("2000");
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> getProfile() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                Optional<UserDetail> userDetail = userDetailRepository.findById(user.get().getId());
                if (userDetail.isPresent()) {
                    return getUser(user.get().getId());
                } else {
                    throw new Exception("4000");
                }
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> updateProfile(UserDetailDTO userDetailDTO) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<UserDetail> userDetail = userDetailRepository
                    .findById(userRepository.findByEmail(email).get().getId());
            if (userDetail.isPresent()) {
                updateUser(userDetailDTO);
                return ResponseEntity.ok("2000");
            } else {
                throw new Exception("4000");
            }
        } catch (

        Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> deleteProfile() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                Optional<UserDetail> userDetail = userDetailRepository.findById(user.get().getId());
                if (userDetail.isPresent()) {
                    return deleteUser(user.get().getId());
                } else {
                    throw new Exception("4000");
                }
            } else {
                throw new Exception("4000");
            }
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }
}
