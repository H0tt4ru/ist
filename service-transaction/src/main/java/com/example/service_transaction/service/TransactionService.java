package com.example.service_transaction.service;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.base_domain.dtos.TransactionDTO;
import com.example.base_domain.entities.Transaction;
import com.example.base_domain.entities.User;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.TransactionRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;
import com.example.service_transaction.request.GetRequest;
import com.example.service_transaction.request.TransactionRequest;
import com.example.service_transaction.response.ListTransactionResponse;
import com.example.service_transaction.response.TransactionResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = "transaction-queue")
    public void createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .senderId(transactionDTO.getSenderId())
                .receiverId(transactionDTO.getReceiverId())
                .amount(transactionDTO.getAmount())
                .description(transactionDTO.getDescription()).build();
        transactionRepository.save(transaction);
    }

    public ResponseEntity<Object> getTransaction(GetRequest getRequest) {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionResponse> filteredTransactions = transactions.stream()
                .filter(transaction -> {
                    if ((getRequest.getSenderEmail() == null || getRequest.getSenderEmail().isBlank())
                            && (getRequest.getReceiverEmail() == null || getRequest.getReceiverEmail().isBlank())) {
                        return true;
                    } else if (getRequest.getSenderEmail() != null && !getRequest.getSenderEmail().isBlank()
                            && (getRequest.getReceiverEmail() == null || getRequest.getReceiverEmail().isBlank())) {
                        return userRepository.findById(transaction.getSenderId()).get().getEmail()
                                .equals(getRequest.getSenderEmail());
                    } else if ((getRequest.getSenderEmail() == null || getRequest.getSenderEmail().isBlank())
                            && getRequest.getReceiverEmail() != null && !getRequest.getReceiverEmail().isBlank()) {
                        return userRepository.findById(transaction.getReceiverId()).get().getEmail()
                                .equals(getRequest.getReceiverEmail());
                    } else {
                        return userRepository.findById(transaction.getSenderId()).get().getEmail()
                                .equals(getRequest.getSenderEmail())
                                && userRepository.findById(transaction.getReceiverId()).get().getEmail()
                                        .equals(getRequest.getReceiverEmail());
                    }
                })
                .map(transaction -> TransactionResponse.builder()
                        .senderEmail(userRepository.findById(transaction.getSenderId()).get().getEmail())
                        .receiverEmail(userRepository.findById(transaction.getReceiverId()).get().getEmail())
                        .amount(transaction.getAmount())
                        .description(transaction.getDescription())
                        .date(transaction.getDate().atZone(ZoneId.systemDefault()).toLocalDate())
                        .time(transaction.getDate().atZone(ZoneId.systemDefault()).toLocalTime())
                        .build())
                .collect(Collectors.toList());

        ListTransactionResponse listTransactionResponse = ListTransactionResponse.builder()
                .code("2000")
                .message("Success")
                .transactions(filteredTransactions)
                .build();

        return ResponseEntity.ok(listTransactionResponse);
    }

    public ResponseEntity<Object> createTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Optional<User> receiver = userRepository.findByEmail(transactionRequest.getReceiverEmail());
            if (receiver.isPresent()) {
                Wallet senderWallet = walletRepository.findById(user.get().getId()).get();
                Wallet receiverWallet = walletRepository.findById(receiver.get().getId()).get();
                if (senderWallet.getBalance() < transactionRequest.getAmount()) {
                    return ResponseEntity.status(404).body("4001");
                }
                senderWallet.setBalance(senderWallet.getBalance() - transactionRequest.getAmount());
                receiverWallet.setBalance(receiverWallet.getBalance() + transactionRequest.getAmount());
                walletRepository.save(senderWallet);
                walletRepository.save(receiverWallet);
                TransactionDTO transactionDTO = TransactionDTO.builder()
                        .senderId(user.get().getId())
                        .receiverId(receiver.get().getId())
                        .amount(transactionRequest.getAmount())
                        .description(transactionRequest.getDescription())
                        .build();
                jmsTemplate.convertAndSend("transaction-queue", transactionDTO);
                return ResponseEntity.ok(transactionRequest);
            } else {
                return ResponseEntity.status(404).body("4000");
            }
        } else {
            return ResponseEntity.status(404).body("4000");
        }
    }

    public ResponseEntity<Object> deleteTransaction(UUID id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return ResponseEntity.ok("2000");
        } else {
            return ResponseEntity.status(404).body("4000");
        }
    }
}
