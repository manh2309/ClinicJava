package org.example.clinicjava;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@SpringBootApplication
public class ClinicJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinicJavaApplication.class, args);
    }
}
