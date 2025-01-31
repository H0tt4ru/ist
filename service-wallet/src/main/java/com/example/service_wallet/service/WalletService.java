package com.example.service_wallet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.base_domain.dtos.WalletDTO;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.WalletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public ResponseEntity<Object> getWallets() {
        return ResponseEntity.ok(walletRepository.findAll());
    }

    public ResponseEntity<Object> createWallet(WalletDTO walletDTO) throws Exception {
        try {
            Wallet wallet = Wallet.builder()
                    .id(walletDTO.getId())
                    .balance(walletDTO.getBalance())
                    .build();
            walletRepository.save(wallet);
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }
}
