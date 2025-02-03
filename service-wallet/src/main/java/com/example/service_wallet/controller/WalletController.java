package com.example.service_wallet.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/get-all")
    public ResponseEntity<Object> getWallets() throws Exception {
        return walletService.getWallets();
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getWallet(@RequestBody UUID uuid) throws Exception {
        return walletService.getWallet(uuid);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateWallet(@RequestBody WalletDTO walletDTO) throws Exception {
        return walletService.updateWallet(walletDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteWallet(@RequestBody UUID uuid) throws Exception {
        return walletService.deleteWallet(uuid);
    }

}
