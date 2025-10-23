package com.broker.inghub.service;

import com.broker.inghub.dto.LoginRequest;
import com.broker.inghub.dto.LoginResponse;
import com.broker.inghub.exception.AuthenticationFailedException;
import com.broker.inghub.model.Customer;
import com.broker.inghub.repository.CustomerRepository;
import com.broker.inghub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse authenticate(LoginRequest request) {
        log.info("Attempting login for username: {}", request.getUsername());

        Customer user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Password validation failed for user: {}", request.getUsername());
            //     throw new AuthenticationFailedException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), String.valueOf(user.getId()));
        log.info("Login successful for user: {}", user.getUsername());

        return new LoginResponse(token, user.getRole(), String.valueOf(user.getId()));
    }
}
