package com.example.service_wallet.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.base_domain.dtos.WalletDTO;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;
import com.example.service_wallet.response.WalletResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Object> createWallet(WalletDTO walletDTO) throws Exception {
        try {
            Wallet wallet = Wallet.builder()
                    .id(walletDTO.getId())
                    .balance(walletDTO.getBalance())
                    .build();
            walletRepository.save(wallet);
            WalletResponse walletResponse = WalletResponse.builder()
                    .email(userRepository.findById(walletDTO.getId()).get().getEmail())
                    .balance(wallet.getBalance())
                    .build();
            return ResponseEntity.ok(walletResponse);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> getWallets() {
        List<WalletResponse> walletResponses = walletRepository.findAll(Sort.by(Sort.Direction.DESC, "email")).stream()
                .map(wallet -> WalletResponse.builder()
                        .email(userRepository.findById(wallet.getId()).get().getEmail())
                        .balance(wallet.getBalance())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(walletResponses);
    }

    public ResponseEntity<Object> getWallet(UUID uuid) {
        WalletResponse walletResponse = WalletResponse.builder()
                .email(userRepository.findById(uuid).get().getEmail())
                .balance(walletRepository.findById(uuid).get().getBalance())
                .build();
        return ResponseEntity.ok(walletResponse);
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
