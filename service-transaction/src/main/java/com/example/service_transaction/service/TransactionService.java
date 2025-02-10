package com.example.service_transaction.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.base_domain.dtos.TransactionDTO;
import com.example.base_domain.entities.Transaction;
import com.example.base_domain.entities.User;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.CodeRepository;
import com.example.base_domain.repositories.TransactionRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;
import com.example.base_domain.response.BaseResponse;
import com.example.service_transaction.request.GetRequest;
import com.example.service_transaction.request.TransactionRequest;
import com.example.service_transaction.response.ListTransactionResponse;
import com.example.service_transaction.response.TransactionResponse;
import com.example.service_transaction.response.TransactionsResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final JmsTemplate jmsTemplate;
    private final CodeRepository codeRepository;

    @Transactional
    @JmsListener(destination = "transaction-queue")
    public void createTransaction(TransactionDTO transactionDTO) throws Exception {
        try {
            Transaction transaction = Transaction.builder()
                    .senderId(transactionDTO.getSenderId())
                    .receiverId(transactionDTO.getReceiverId())
                    .amount(transactionDTO.getAmount())
                    .description(transactionDTO.getDescription()).build();
            transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new Exception("4401");
        }
    }

    public ResponseEntity<Object> getTransaction(GetRequest getRequest) throws Exception {
        try {
            List<Transaction> transactions = transactionRepository.findAll();
            List<TransactionsResponse> transactionResponses = transactions.stream()
                    .filter(transaction -> {
                        if (getRequest.getSenderEmail() != null && !getRequest.getSenderEmail().isBlank() &&
                                getRequest.getReceiverEmail() != null && !getRequest.getReceiverEmail().isBlank()) {
                            return userRepository.findById(transaction.getSenderId()).get().getEmail()
                                    .equals(getRequest.getSenderEmail()) &&
                                    userRepository.findById(transaction.getReceiverId()).get().getEmail()
                                            .equals(getRequest.getReceiverEmail());
                        } else if (getRequest.getSenderEmail() != null && !getRequest.getSenderEmail().isBlank() &&
                                (getRequest.getReceiverEmail() == null || getRequest.getReceiverEmail().isBlank())) {
                            return userRepository.findById(transaction.getSenderId()).get().getEmail()
                                    .equals(getRequest.getSenderEmail());
                        } else if ((getRequest.getSenderEmail() == null || getRequest.getSenderEmail().isBlank()) &&
                                getRequest.getReceiverEmail() != null && !getRequest.getReceiverEmail().isBlank()) {
                            return userRepository.findById(transaction.getReceiverId()).get().getEmail()
                                    .equals(getRequest.getReceiverEmail());
                        } else {
                            return true;
                        }
                    })
                    .map(transaction -> TransactionsResponse.builder()
                            .senderEmail(userRepository.findById(transaction.getSenderId()).get().getEmail())
                            .receiverEmail(userRepository.findById(transaction.getReceiverId()).get().getEmail())
                            .amount(transaction.getAmount())
                            .description(transaction.getDescription())
                            .date(transaction.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate())
                            .time(transaction.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalTime())
                            .build())
                    .collect(Collectors.toList());

            ListTransactionResponse listTransactionResponse = ListTransactionResponse.builder()
                    .code("2402")
                    .message(codeRepository.findByCode("2402").get().getMessage())
                    .transactions(transactionResponses)
                    .build();

            return new ResponseEntity<>(listTransactionResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("4402");
        }
    }

    public ResponseEntity<Object> getAllTransaction() throws Exception {
        try {
            List<Transaction> transactions = transactionRepository.findAll();
            List<TransactionsResponse> transactionResponses = transactions.stream()
                    .map(transaction -> TransactionsResponse.builder()
                            .senderEmail(userRepository.findById(transaction.getSenderId()).get().getEmail())
                            .receiverEmail(userRepository.findById(transaction.getReceiverId()).get().getEmail())
                            .amount(transaction.getAmount())
                            .description(transaction.getDescription())
                            .date(transaction.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate())
                            .time(transaction.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalTime())
                            .build())
                    .collect(Collectors.toList());
            ListTransactionResponse listTransactionResponse = ListTransactionResponse.builder()
                    .code("2402")
                    .message(codeRepository.findByCode("2402").get().getMessage())
                    .transactions(transactionResponses)
                    .build();
            return new ResponseEntity<>(listTransactionResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("4402");
        }
    }

    public ResponseEntity<Object> createTransaction(TransactionRequest transactionRequest) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<User> user = userRepository.findByEmail(email);
            if (!user.isPresent()) {
                throw new Exception("4401");
            }
            Optional<User> receiver = userRepository.findByEmail(transactionRequest.getReceiverEmail());
            if (!receiver.isPresent()) {
                throw new Exception("4401");
            }
            Wallet senderWallet = walletRepository.findById(user.get().getId()).get();
            Wallet receiverWallet = walletRepository.findById(receiver.get().getId()).get();
            if (senderWallet.getBalance() < transactionRequest.getAmount()) {
                throw new Exception("4401");
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
            TransactionResponse transactionResponse = TransactionResponse.builder()
                    .code("2404")
                    .message(codeRepository.findByCode("2404").get().getMessage())
                    .senderEmail(user.get().getEmail())
                    .receiverEmail(receiver.get().getEmail())
                    .amount(transactionRequest.getAmount())
                    .description(transactionRequest.getDescription())
                    .date(LocalDate.now())
                    .time(LocalTime.now())
                    .build();

            return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteTransaction(UUID id) throws Exception {
        try {
            if (transactionRepository.existsById(id)) {
                transactionRepository.deleteById(id);
                BaseResponse baseResponse = BaseResponse.builder()
                        .code("2403")
                        .message(codeRepository.findByCode("2403").get().getMessage())
                        .build();

                return new ResponseEntity<>(baseResponse, HttpStatus.OK);

            } else {
                return ResponseEntity.status(404).body("4000");
            }
        } catch (Exception e) {
            throw new Exception("4403");
        }
    }

    public ResponseEntity<Object> getIncome() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        GetRequest getRequest = GetRequest.builder()
                .receiverEmail(user.get().getEmail())
                .build();
        System.out.println(getRequest);
        return getTransaction(getRequest);
    }

    public ResponseEntity<Object> getOutcome() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        GetRequest getRequest = GetRequest.builder()
                .senderEmail(user.get().getEmail())
                .build();
        return getTransaction(getRequest);
    }
}
