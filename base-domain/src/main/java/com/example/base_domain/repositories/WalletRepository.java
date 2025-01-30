package com.example.base_domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.base_domain.entities.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
