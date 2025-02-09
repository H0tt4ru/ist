package com.example.base_domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.base_domain.entities.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<List<Transaction>> findAllBySenderId(UUID senderId);

    Optional<List<Transaction>> findAllByReceiverId(UUID receiverId);
}
