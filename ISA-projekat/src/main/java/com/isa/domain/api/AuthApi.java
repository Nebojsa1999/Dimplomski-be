package com.isa.domain.api;

import com.isa.config.CustomUserDetailsService;
import com.isa.config.JwtService;
import com.isa.domain.dto.LoginDTO;
import com.isa.domain.dto.LoginResponseDTO;
import com.isa.domain.model.User;
import com.isa.exception.NotFoundException;
import com.isa.exception.UnauthorizedException;
import com.isa.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api")
public class AuthApi {

    private final CustomUserDetailsService customUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthApi(CustomUserDetailsService customUserService, PasswordEncoder passwordEncoder, JwtService jwtService, UserService userService) {
        this.customUserService = customUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {

        final User user = customUserService.findUserByEmail(loginDTO.getUsername()).orElseThrow(()-> new UnauthorizedException("No user found with email: " + loginDTO.getUsername()));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid password for user: " + loginDTO.getUsername());
        }

        final String token = jwtService.createAuthenticationToken(user.getId(), Instant.now().plus(5, ChronoUnit.MINUTES), user.getRole());
        final String refreshToken = jwtService.createRefreshToken(user.getId(), Instant.now().plus(1, ChronoUnit.DAYS));
        final LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(token);
        responseDTO.setUser(user);
        responseDTO.setRefreshToken(refreshToken);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping(path = "/refreshToken")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody String refreshToken) {
        final long userId = jwtService.getUserId(refreshToken);
        final User user = userService.get(userId).orElseThrow(NotFoundException::new);

        final String token = jwtService.createAuthenticationToken(user.getId(), Instant.now().plus(5, ChronoUnit.MINUTES), user.getRole());
        final LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setRefreshToken(refreshToken);
        responseDTO.setUser(user);
        responseDTO.setToken(token);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
