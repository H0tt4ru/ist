package com.example.service_transaction.service;

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
import com.example.base_domain.repositories.TransactionRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.service_transaction.request.GetRequest;
import com.example.service_transaction.request.TransactionRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
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

    public ResponseEntity<Object> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            return ResponseEntity.ok("No transactions found");
        }
        List<TransactionDTO> transactionDTOs = transactions.stream().map(transaction -> TransactionDTO.builder()
                .id(transaction.getId())
                .senderId(transaction.getSenderId())
                .receiverId(transaction.getReceiverId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(transactionDTOs);
    }

    public ResponseEntity<Object> getTransaction(GetRequest getRequest) {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(transaction -> (getRequest.getSenderId() == null
                        || transaction.getSenderId().equals(getRequest.getSenderId())) &&
                        (getRequest.getReceiverId() == null
                                || transaction.getReceiverId().equals(getRequest.getReceiverId())))
                .collect(Collectors.toList());

        if (filteredTransactions.isEmpty()) {
            return ResponseEntity.ok("No transactions found");
        }
        return ResponseEntity.ok(filteredTransactions);
    }

    public ResponseEntity<Object> createTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Optional<User> receiver = userRepository.findByEmail(transactionRequest.getReceiverEmail());
            if (receiver.isPresent()) {
                TransactionDTO transactionDTO = TransactionDTO.builder()
                        .senderId(user.get().getId())
                        .receiverId(receiver.get().getId())
                        .amount(transactionRequest.getAmount())
                        .description(transactionRequest.getDescription())
                        .build();
                jmsTemplate.convertAndSend("transaction-queue", transactionDTO);
                return ResponseEntity.ok(transactionRequest);
            } else {
                return ResponseEntity.status(404).body("Receiver not found");
            }
        } else {
            return ResponseEntity.status(404).body("Sender not found");
        }
    }

    public ResponseEntity<Object> deleteTransaction(UUID id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return ResponseEntity.ok("Transaction deleted");
        } else {
            return ResponseEntity.status(404).body("Transaction not found");
        }
    }
}
