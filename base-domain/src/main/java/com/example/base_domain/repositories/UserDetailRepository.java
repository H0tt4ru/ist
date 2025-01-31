package com.example.base_domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.base_domain.entities.UserDetail;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, UUID> {

    Optional<UserDetail> findByPhoneNumber(String phoneNumber);
}
