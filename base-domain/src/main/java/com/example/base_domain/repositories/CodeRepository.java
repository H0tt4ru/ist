package com.example.base_domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.base_domain.entities.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code, String> {

    Optional<Code> findByCode(String code);
}
