package com.example.service_wallet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.base_domain.dtos.WalletDTO;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.CodeRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;
import com.example.base_domain.response.BaseResponse;
import com.example.service_wallet.response.ListWalletResponse;
import com.example.service_wallet.response.WalletResponse;
import com.example.service_wallet.response.WalletsResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;

    @Transactional
    public ResponseEntity<Object> createWallet(WalletDTO walletDTO) throws Exception {
        try {
            Wallet wallet = Wallet.builder()
                    .id(walletDTO.getId())
                    .balance(walletDTO.getBalance())
                    .build();

            walletRepository.save(wallet);

            WalletResponse walletResponse = WalletResponse.builder()
                    .code("2301")
                    .message(codeRepository.findByCode("2301").get().getMessage())
                    .email(userRepository.findById(walletDTO.getId()).get().getEmail())
                    .balance(walletDTO.getBalance())
                    .build();

            return new ResponseEntity<>(walletResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> getWallets() throws Exception {
        try {
            List<WalletsResponse> walletResponses = walletRepository.findAll(Sort.by(Sort.Direction.DESC, "email"))
                    .stream()
                    .map(wallet -> WalletsResponse.builder()
                            .email(userRepository.findById(wallet.getId()).get().getEmail())
                            .balance(wallet.getBalance())
                            .build())
                    .collect(Collectors.toList());

            ListWalletResponse listWalletResponse = ListWalletResponse.builder()
                    .code("2302")
                    .message(codeRepository.findByCode("2302").get().getMessage())
                    .wallets(walletResponses)
                    .build();

            return new ResponseEntity<>(listWalletResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> getWallet(UUID uuid) throws Exception {
        try {
            WalletResponse walletResponse = WalletResponse.builder()
                    .code("2303")
                    .message(codeRepository.findByCode("2303").get().getMessage())
                    .email(userRepository.findById(uuid).get().getEmail())
                    .balance(walletRepository.findById(uuid).get().getBalance())
                    .build();
            return new ResponseEntity<>(walletResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> updateWallet(WalletDTO walletDTO) throws Exception {
        try {
            Wallet wallet = walletRepository
                    .findById(walletDTO.getId()).orElseThrow(() -> new Exception("4000"));
            wallet.setBalance(walletDTO.getBalance());
            walletRepository.save(wallet);

            WalletResponse walletResponse = WalletResponse.builder()
                    .code("2304")
                    .message(codeRepository.findByCode("2304").get().getMessage())
                    .email(userRepository.findById(walletDTO.getId()).get().getEmail())
                    .balance(wallet.getBalance())
                    .build();
            return new ResponseEntity<>(walletResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("4000");
        }
    }

    public ResponseEntity<Object> deleteWallet(UUID uuid) throws Exception {
        try {
            Optional<Wallet> wallet = walletRepository.findById(uuid);
            if (wallet.isEmpty()) {
                throw new Exception("4303");
            }
            walletRepository.deleteById(uuid);

            BaseResponse baseResponse = BaseResponse.builder()
                    .code("2305")
                    .message(codeRepository.findByCode("2305").get().getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.OK);

        } catch (Exception e) {
            throw new Exception("4000");
        }
    }
}
