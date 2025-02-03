package com.example.service_wallet.service;

import java.util.UUID;

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

    public ResponseEntity<Object> getWallets() {
        return ResponseEntity.ok(walletRepository.findAll());
    }

    public ResponseEntity<Object> getWallet(UUID uuid) {
        return ResponseEntity.ok(walletRepository.findById(uuid));
    }

    public ResponseEntity<Object> updateWallet(WalletDTO walletDTO) throws Exception {
        try {
            Wallet wallet = walletRepository.findById(walletDTO.getId()).get();
            wallet.setBalance(walletDTO.getBalance());
            walletRepository.save(wallet);
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> deleteWallet(UUID uuid) throws Exception {
        try {
            Wallet wallet = walletRepository.findById(uuid).get();
            walletRepository.delete(wallet);
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }
}
