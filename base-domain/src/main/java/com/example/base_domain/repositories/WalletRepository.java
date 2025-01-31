package com.example.base_domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.base_domain.entities.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
