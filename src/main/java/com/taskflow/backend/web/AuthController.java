package com.taskflow.backend.web;

//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // MUST match the key in JwtAuthenticationFilter
    private final String SECRET_KEY = "YourSuperSecretKeyForSigningJWTsMustBeLongEnough";

    // Quick DTO for login request
    public record LoginRequest(String username, String tenantId) {}

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        // In a real app, you would validate password here.
        // For this POC, we just generate the token immediately.

        String token = Jwts.builder()
                .setSubject(request.username())
                .claim("tenantId", request.tenantId()) // Embed Tenant ID in Token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();

        return Map.of("token", token);
    }
}