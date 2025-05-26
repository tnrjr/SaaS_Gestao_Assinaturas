package com.tary.saas.controller;

import com.tary.saas.config.JwtUtil;
import com.tary.saas.entity.User;
import com.tary.saas.repository.UserRepository;
import com.tary.saas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    private record LoginRequest(String email, String password) {}
    private record AuthResponse(String token) {}
}
