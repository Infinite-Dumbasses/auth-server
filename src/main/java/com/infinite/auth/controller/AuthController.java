package com.infinite.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {

    @Value("${jwt.test}")
    private String test1;
    @Value("${JWT_TEST}")
    private String test2;
    @Autowired
    private final Environment env;

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok(env.getProperty("JWT_TEST") + "\n" + test1 + "\n" + test2 + "\n" + "hi");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register() {
        return ResponseEntity.ok("hi");
    }

    @GetMapping("/issue")
    public ResponseEntity<?> issue(@AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok("hi");
    }
}
