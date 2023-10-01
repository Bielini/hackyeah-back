package com.capgemini.hackyeah;

import com.capgemini.hackyeah.auth.model.RegisterRequest;
import com.capgemini.hackyeah.auth.service.AuthenticationService;
import com.capgemini.hackyeah.domain.model.Role;
import com.capgemini.hackyeah.domain.model.User;
import com.capgemini.hackyeah.domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class HashokApplication {

    public static void main(String[] args) {
        SpringApplication.run(HashokApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service,
            UserRepository userRepository
    ) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstname("Admin")
                    .lastname("Admin")
                    .email("admin@mail.com")
                    .password("password")
                    .role(Role.ADMIN)
                    .build();
            Optional<User> adminOpt = userRepository.findFirstByEmail(admin.getEmail());
            if (adminOpt.isEmpty()) {
                System.out.println("Admin token: " + service.register(admin).getAccessToken());
            }

            var manager = RegisterRequest.builder()
                    .firstname("Admin")
                    .lastname("Admin")
                    .email("manager@mail.com")
                    .password("password")
                    .role(Role.MANAGER)
                    .build();

            Optional<User> managerOpt = userRepository.findFirstByEmail(manager.getEmail());
            if (managerOpt.isEmpty()) {
                System.out.println("Manager token: " + service.register(manager).getAccessToken());
            }
        };
    }
}
