package com.example.api_core.config;

import java.time.LocalDate;
import java.util.List;
import java.time.ZoneOffset;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.base_domain.constant.Gender;
import com.example.base_domain.constant.Role;
import com.example.base_domain.entities.ErrorCode;
import com.example.base_domain.entities.Transaction;
import com.example.base_domain.entities.User;
import com.example.base_domain.entities.UserDetail;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.ErrorCodeRepository;
import com.example.base_domain.repositories.TransactionRepository;
import com.example.base_domain.repositories.UserDetailRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;

@Configuration
public class SeederConfig {

        @Bean
        CommandLineRunner commandLineRunner(UserRepository userRepository,
                        UserDetailRepository userDetailRepository,
                        TransactionRepository transactionRepository, ErrorCodeRepository errorCodeRepository,
                        WalletRepository walletRepository,
                        PasswordEncoder passwordEncoder) {
                return _ -> {
                        if (errorCodeRepository.count() == 0) {
                                errorCodeRepository.saveAll(List.of(
                                                ErrorCode.builder().code("4101").message("Broken request fields")
                                                                .build(),
                                                ErrorCode.builder().code("4102").message("Empty fields").build(),
                                                ErrorCode.builder().code("4103").message("Email already exists")
                                                                .build(),
                                                ErrorCode.builder().code("4104").message(
                                                                "Password must have at least 8 characters, 1 uppercase, 1 lowercase, and 1 number")
                                                                .build(),
                                                ErrorCode.builder().code("4105")
                                                                .message("Full name be no more than 100 characters")
                                                                .build(),
                                                ErrorCode.builder().code("4106")
                                                                .message("Gender must be either Male or Female")
                                                                .build(),
                                                ErrorCode.builder().code("4107").message("Invalid date of birth format")
                                                                .build(),
                                                ErrorCode.builder().code("4108")
                                                                .message("Major must be one of the provided majors")
                                                                .build(),
                                                ErrorCode.builder().code("4109")
                                                                .message("Phone number must be numbers only and minimum 7 digits")
                                                                .build(),
                                                ErrorCode.builder().code("4110")
                                                                .message("Username must be no more than 20 characters")
                                                                .build(),
                                                ErrorCode.builder().code("4200").message("Empty fields").build(),
                                                ErrorCode.builder().code("4201").message("Wrong email or password")
                                                                .build(),
                                                ErrorCode.builder().code("4300").message("Empty database").build(),
                                                ErrorCode.builder().code("4301").message("Student not found").build(),
                                                ErrorCode.builder().code("4302")
                                                                .message("Gender must be either Male or Female")
                                                                .build(),
                                                ErrorCode.builder().code("4303").message("Invalid date of birth format")
                                                                .build(),
                                                ErrorCode.builder().code("4304").message("Invalid major").build(),
                                                ErrorCode.builder().code("4400").message("Empty fields").build(),
                                                ErrorCode.builder().code("4401")
                                                                .message("Destination student not found").build(),
                                                ErrorCode.builder().code("4402")
                                                                .message("Amount does not follow the rule").build(),
                                                ErrorCode.builder().code("4403").message("Not enough points").build(),
                                                ErrorCode.builder().code("4500")
                                                                .message("Invalid type (must be one of: SentBy, ReceivedBy, All, or SentByReceivedBy)")
                                                                .build(),
                                                ErrorCode.builder().code("4501").message("SentBy student not found")
                                                                .build(),
                                                ErrorCode.builder().code("4502").message("ReceivedBy student not found")
                                                                .build(),
                                                ErrorCode.builder().code("4600").message("JWT token is missing")
                                                                .build(),
                                                ErrorCode.builder().code("4601").message("Invalid JWT token").build(),
                                                ErrorCode.builder().code("4602").message("Expired JWT token").build(),
                                                ErrorCode.builder().code("4603").message("Insufficient permissions")
                                                                .build(),
                                                ErrorCode.builder().code("4604").message("Account locked").build(),
                                                ErrorCode.builder().code("4700").message("Invalid email format")
                                                                .build(),
                                                ErrorCode.builder().code("4701").message("Invalid password format")
                                                                .build(),
                                                ErrorCode.builder().code("4702").message("Invalid phone number format")
                                                                .build(),
                                                ErrorCode.builder().code("4703").message("Invalid date of birth format")
                                                                .build(),
                                                ErrorCode.builder().code("4800").message("Duplicate key violation")
                                                                .build(),
                                                ErrorCode.builder().code("4801").message("Foreign key violation")
                                                                .build(),
                                                ErrorCode.builder().code("4802").message("Transaction failed").build(),
                                                ErrorCode.builder().code("4900").message("External API timeout")
                                                                .build(),
                                                ErrorCode.builder().code("4901")
                                                                .message("Invalid response from external service")
                                                                .build(),
                                                ErrorCode.builder().code("4902")
                                                                .message("Unauthorized request to external service")
                                                                .build(),
                                                ErrorCode.builder().code("4903").message("Inconsistent state").build(),
                                                ErrorCode.builder().code("4904").message("Missing required field")
                                                                .build(),
                                                ErrorCode.builder().code("5100")
                                                                .message("Rate limit exceeded – Too many requests")
                                                                .build(),
                                                ErrorCode.builder().code("5101").message("API rate limit exceeded")
                                                                .build(),
                                                ErrorCode.builder().code("5200").message("Feature not yet implemented")
                                                                .build(),
                                                ErrorCode.builder().code("5201").message("Unknown error occurred")
                                                                .build(),
                                                ErrorCode.builder().code("5000").message("Internal server error")
                                                                .build()));
                        }
                        if (userRepository.count() == 0) {
                                User admin = User.builder()
                                                .username("admin")
                                                .email("admin@admin.com")
                                                .password(passwordEncoder.encode("passwordAdmin1"))
                                                .role(Role.ADMIN)
                                                .build();

                                User user1 = User.builder()
                                                .username("User 1")
                                                .email("user1@gmail.com")
                                                .password(passwordEncoder.encode("passwordUser1"))
                                                .role(Role.USER)
                                                .build();

                                User user2 = User.builder()
                                                .username("User 2")
                                                .email("user2@gmail.com")
                                                .password(passwordEncoder.encode("passwordUser2"))
                                                .role(Role.USER)
                                                .build();

                                User user3 = User.builder()
                                                .username("User 3")
                                                .email("user3@gmail.com")
                                                .password(passwordEncoder.encode("passwordUser3"))
                                                .role(Role.USER)
                                                .build();

                                userRepository.saveAll(List.of(admin, user1, user2, user3));

                                UserDetail userDetail1 = UserDetail.builder()
                                                .id(userRepository.findByEmail("user1@gmail.com").get().getId())
                                                .fullName("User 1 Long")
                                                .gender(Gender.Female)
                                                .dob(LocalDate.of(1999, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC))
                                                .phoneNumber("1234567890")
                                                .address("123 Main St")
                                                .build();

                                UserDetail userDetail2 = UserDetail.builder()
                                                .id(userRepository.findByEmail("user2@gmail.com").get().getId())
                                                .fullName("User 2 Long")
                                                .gender(Gender.Male)
                                                .dob(LocalDate.of(1999, 2, 2).atStartOfDay().toInstant(ZoneOffset.UTC))
                                                .phoneNumber("9876543210")
                                                .address("123 Main St")
                                                .build();

                                UserDetail userDetail3 = UserDetail.builder()
                                                .id(userRepository.findByEmail("user3@gmail.com").get().getId())
                                                .fullName("User 3 Long")
                                                .gender(Gender.Female)
                                                .dob(LocalDate.of(1999, 3, 3).atStartOfDay().toInstant(ZoneOffset.UTC))
                                                .phoneNumber("1029384756")
                                                .address("123 Main St")
                                                .build();

                                userDetailRepository.saveAll(List.of(userDetail1, userDetail2, userDetail3));

                                Wallet wallet1 = Wallet.builder()
                                                .id(userRepository.findByEmail("user1@gmail.com").get().getId())
                                                .balance(1000)
                                                .build();

                                Wallet wallet2 = Wallet.builder()
                                                .id(userRepository.findByEmail("user2@gmail.com").get().getId())
                                                .balance(2000)
                                                .build();

                                Wallet wallet3 = Wallet.builder()
                                                .id(userRepository.findByEmail("user3@gmail.com").get().getId())
                                                .balance(3000)
                                                .build();

                                walletRepository.saveAll(List.of(wallet1, wallet2, wallet3));

                                Transaction transaction1 = Transaction.builder()
                                                .senderId(userRepository.findByEmail("user1@gmail.com").get().getId())
                                                .receiverId(userRepository.findByEmail("user2@gmail.com").get().getId())
                                                .amount(100)
                                                .build();

                                Transaction transaction2 = Transaction.builder()
                                                .senderId(userRepository.findByEmail("user1@gmail.com").get().getId())
                                                .receiverId(userRepository.findByEmail("user3@gmail.com").get().getId())
                                                .amount(200)
                                                .build();

                                Transaction transaction3 = Transaction.builder()
                                                .senderId(userRepository.findByEmail("user2@gmail.com").get().getId())
                                                .receiverId(userRepository.findByEmail("user3@gmail.com").get().getId())
                                                .amount(300)
                                                .build();

                                transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3));
                        }
                };
        };
}
