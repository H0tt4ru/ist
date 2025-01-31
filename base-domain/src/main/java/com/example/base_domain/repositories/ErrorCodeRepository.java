package com.example.base_domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.base_domain.entities.ErrorCode;

@Repository
public interface ErrorCodeRepository extends JpaRepository<ErrorCode, String> {

    Optional<ErrorCode> findByCode(String code);
}
