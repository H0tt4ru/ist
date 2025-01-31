package com.example.service_authentication.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.base_domain.dtos.WalletDTO;

@FeignClient(name = "wallet-service", url = "http://localhost:8082")
public interface WalletClient {
    @PostMapping("/wallet")
    ResponseEntity<String> createWallet(@RequestBody WalletDTO walletDTO);
}
