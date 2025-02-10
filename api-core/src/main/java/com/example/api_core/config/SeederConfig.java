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
import com.example.base_domain.entities.Code;
import com.example.base_domain.entities.Transaction;
import com.example.base_domain.entities.User;
import com.example.base_domain.entities.UserDetail;
import com.example.base_domain.entities.Wallet;
import com.example.base_domain.repositories.CodeRepository;
import com.example.base_domain.repositories.TransactionRepository;
import com.example.base_domain.repositories.UserDetailRepository;
import com.example.base_domain.repositories.UserRepository;
import com.example.base_domain.repositories.WalletRepository;

@Configuration
public class SeederConfig {

        @Bean
        CommandLineRunner commandLineRunner(UserRepository userRepository,
                        UserDetailRepository userDetailRepository,
                        TransactionRepository transactionRepository, CodeRepository codeRepository,
                        WalletRepository walletRepository,
                        PasswordEncoder passwordEncoder) {
                return _ -> {
                        if (codeRepository.count() == 0) {
                                codeRepository.saveAll(List.of(
                                                // ✅ Success Codes (2000 - 2999)
                                                Code.builder().code("2000").message("Request completed successfully.")
                                                                .build(),
                                                Code.builder().code("2101").message("User registration successful.")
                                                                .build(),
                                                Code.builder().code("2102").message("User login successful.").build(),
                                                Code.builder().code("2201").message("User detail created successfully.")
                                                                .build(),
                                                Code.builder().code("2202").message("Users retrieved successfully.")
                                                                .build(),
                                                Code.builder().code("2203").message("User retrieved successfully.")
                                                                .build(),
                                                Code.builder().code("2204").message("User updated successfully.")
                                                                .build(),
                                                Code.builder().code("2205").message("User deleted successfully.")
                                                                .build(),
                                                Code.builder().code("2206")
                                                                .message("User profile retrieved successfully.")
                                                                .build(),
                                                Code.builder().code("2207")
                                                                .message("User profile updated successfully.").build(),
                                                Code.builder().code("2208")
                                                                .message("User profile deleted successfully.").build(),
                                                Code.builder().code("2301").message("Wallet created successfully.")
                                                                .build(),
                                                Code.builder().code("2302").message("Wallets retrieved successfully.")
                                                                .build(),
                                                Code.builder().code("2303").message("Wallet retrieved successfully.")
                                                                .build(),
                                                Code.builder().code("2304").message("Wallet updated successfully.")
                                                                .build(),
                                                Code.builder().code("2305").message("Wallet deleted successfully.")
                                                                .build(),
                                                Code.builder().code("2401").message("Transaction created successfully.")
                                                                .build(),
                                                Code.builder().code("2402")
                                                                .message("Transaction retrieved successfully.").build(),
                                                Code.builder().code("2403").message("Transaction deleted successfully.")
                                                                .build(),
                                                Code.builder().code("2404")
                                                                .message("Transaction processed successfully.").build(),

                                                // ❌ General Error Codes (4000 - 4099)
                                                Code.builder().code("4000").message("General error occurred.").build(),
                                                Code.builder().code("4010").message(
                                                                "Unauthorized")
                                                                .build(),
                                                Code.builder().code("4020")
                                                                .message("Bad request").build(),
                                                Code.builder().code("4030")
                                                                .message("Too many requests: Rate limit exceeded.")
                                                                .build(),
                                                Code.builder().code("4031").message(
                                                                "Request timeout: Server took too long to respond.")
                                                                .build(),
                                                Code.builder().code("4032")
                                                                .message("Bad request: Duplicate request detected.")
                                                                .build(),
                                                Code.builder().code("4040").message(
                                                                "Service unavailable: The requested service is down.")
                                                                .build(),
                                                Code.builder().code("4041").message(
                                                                "Gateway timeout: Service did not respond in time.")
                                                                .build(),
                                                Code.builder().code("4042").message(
                                                                "Bad gateway: Invalid response from upstream service.")
                                                                .build(),
                                                Code.builder().code("4050").message(
                                                                "Database error: Unable to process the request.")
                                                                .build(),
                                                Code.builder().code("4051")
                                                                .message("Resource conflict: Data already exists.")
                                                                .build(),
                                                Code.builder().code("4052").message(
                                                                "Resource not found: The requested item does not exist.")
                                                                .build(),
                                                Code.builder().code("4053").message(
                                                                "Precondition failed: Required conditions not met.")
                                                                .build(),
                                                Code.builder().code("4090").message(
                                                                "Internal server error: An unexpected error occurred.")
                                                                .build(),
                                                Code.builder().code("4091")
                                                                .message("Service error: Dependency service failure.")
                                                                .build(),
                                                Code.builder().code("4092").message(
                                                                "Configuration error: Misconfigured environment settings.")
                                                                .build(),

                                                // ❌ Service-Authentication Errors (4100 - 4199)
                                                Code.builder().code("4100")
                                                                .message("User registration failed: email already exists.")
                                                                .build(),
                                                Code.builder().code("4101")
                                                                .message("User registration failed: Empty field.")
                                                                .build(),
                                                Code.builder().code("4102")
                                                                .message("User registration failed: Invalid username.")
                                                                .build(),
                                                Code.builder().code("4103").message(
                                                                "User registration failed: Invalid email format.")
                                                                .build(),
                                                Code.builder().code("4104")
                                                                .message("User registration failed: Invalid password.")
                                                                .build(),
                                                Code.builder().code("4105")
                                                                .message("User registration failed: Invalid Full Name.")
                                                                .build(),
                                                Code.builder().code("4106")
                                                                .message("User registration failed: Invalid gender.")
                                                                .build(),
                                                Code.builder().code("4107").message(
                                                                "User registration failed: Invalid date of birth.")
                                                                .build(),
                                                Code.builder().code("4108").message(
                                                                "User registration failed: Invalid phone number.")
                                                                .build(),
                                                Code.builder().code("4110").message("User login failed: Empty field.")
                                                                .build(),
                                                Code.builder().code("4111").message(
                                                                "User login failed: Invalid email or password.")
                                                                .build(),

                                                // ❌ Service-User Errors (4200 - 4299)
                                                Code.builder().code("4201")
                                                                .message("User retrieval failed: Empty field.").build(),
                                                Code.builder().code("4202")
                                                                .message("User retrieval failed: Not Found.").build(),

                                                // ❌ Service-Wallet Errors (4300 - 4399)
                                                Code.builder().code("4301")
                                                                .message("Wallet creation failed: Empty field.")
                                                                .build(),
                                                Code.builder().code("4302")
                                                                .message("Wallet update failed: Invalid request.")
                                                                .build(),
                                                Code.builder().code("4303")
                                                                .message("Wallet deletion failed: Invalid request.")
                                                                .build(),

                                                // ❌ Service-Transaction Errors (4400 - 4499)
                                                Code.builder().code("4401")
                                                                .message("Transaction creation failed: Empty field.")
                                                                .build(),
                                                Code.builder().code("4402").message(
                                                                "Transaction retrieval failed: Invalid request.")
                                                                .build(),
                                                Code.builder().code("4403").message(
                                                                "Transaction deletion failed: Invalid request.")
                                                                .build(),
                                                Code.builder().code("4404").message(
                                                                "Transaction processing failed: Insufficient balance.")
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
