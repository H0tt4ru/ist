package com.example.base_domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "error_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorCode {

    @Id
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String message;
}
