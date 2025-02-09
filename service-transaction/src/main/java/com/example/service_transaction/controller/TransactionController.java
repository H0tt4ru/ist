package com.example.service_transaction.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service_transaction.request.GetRequest;
import com.example.service_transaction.request.TransactionRequest;
import com.example.service_transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get")
    public ResponseEntity<Object> getTransaction(@RequestBody GetRequest getRequest)
            throws Exception {
        return transactionService.getTransaction(getRequest);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllTransaction()
            throws Exception {
        return transactionService.getAllTransaction();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTransaction(@RequestBody UUID uid)
            throws Exception {
        return transactionService.deleteTransaction(uid);
    }

    @PostMapping("/transaction")
    public ResponseEntity<Object> createTransaction(@RequestBody TransactionRequest transactionRequest)
            throws Exception {
        return transactionService.createTransaction(transactionRequest);
    }

    @GetMapping("/income")
    public ResponseEntity<Object> getIncome()
            throws Exception {
        return transactionService.getIncome();
    }

    @GetMapping("/outcome")
    public ResponseEntity<Object> getOutcome()
            throws Exception {
        return transactionService.getOutcome();
    }
}
