package com.example.service_wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.base_domain.dtos.WalletDTO;
import com.example.service_wallet.service.WalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<Object> createWallet(@RequestBody WalletDTO walletDTO) throws Exception {
        return walletService.createWallet(walletDTO);
    }
}
