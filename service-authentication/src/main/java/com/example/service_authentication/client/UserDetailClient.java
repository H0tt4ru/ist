package com.example.service_authentication.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.base_domain.dtos.UserDetailDTO;

@FeignClient(name = "user-detail-service", url = "${feign.url.user-detail-service}")
public interface UserDetailClient {

    @PostMapping("/user-detail/create")
    ResponseEntity<String> createUserDetail(@RequestBody UserDetailDTO userDetailDTO);
}
