package com.isa.domain.api;

import com.isa.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
public class VerificationApi {

    private final VerificationTokenService verificationTokenService;

    @Autowired
    public VerificationApi(VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        verificationTokenService.verifyToken(token);
        return new ResponseEntity<>(Map.of("message", "Email successfully verified."), HttpStatus.OK);
    }
}
