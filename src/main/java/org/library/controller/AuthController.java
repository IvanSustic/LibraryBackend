package org.library.controller;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.model.Korisnik;
import org.library.service.EmailService;
import org.library.service.KorisnikService;
import org.library.service.ZaposlenikService;
import org.library.utils.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final ZaposlenikService zaposlenikService;

    private final KorisnikService korisnikService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;


    @PostMapping("/login/user")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getLozinka())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = korisnikService.findByEmail(authRequest.getEmail()).get();
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        korisnikService.saveRefreshToken(refreshToken,LocalDate.now().plusWeeks(1),authRequest.getEmail());
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/register/user")
   public ResponseEntity<?> register(@RequestBody KorisnikDto request) {
            try {
                request.setLozinka(passwordEncoder.encode(request.getLozinka()));
                korisnikService.saveKorisnik(request);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(500).body(e.getMessage());
            }
    }


    @PostMapping("/refresh/user")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) return ResponseEntity.badRequest().build();
        try {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = korisnikService.findByEmail(username).get();
            if (korisnikService.checkRefreshToken(refreshToken)) {
                String newAccessToken = jwtUtil.generateAccessToken(userDetails);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/login/zaposlenik")
    public ResponseEntity<?> loginZaposlenik(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getLozinka())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = zaposlenikService.findByEmail(authRequest.getEmail()).get();
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        zaposlenikService.saveRefreshToken(refreshToken,LocalDate.now().plusWeeks(1),authRequest.getEmail());
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/refresh/zaposlenik")
    public ResponseEntity<?> refreshZaposlenik(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) return ResponseEntity.badRequest().build();

        try {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = zaposlenikService.findByEmail(username).get();
            if (zaposlenikService.checkRefreshToken(refreshToken)) {
                String newAccessToken = jwtUtil.generateAccessToken(userDetails);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok().body(Map.of("message", "Logged out successfully"));
    }
    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestBody EmailDto email) {
        Optional<Korisnik> user = korisnikService.findByEmail(email.getEmail());
        if (user.isPresent()) {
            String token = jwtUtil.generateAccessToken(user.get());
            String resetLink = "http://localhost:4200/reset-password/" + token;
            emailService.sendResetLink(user.get().getEmail(), resetLink);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email nije nađen");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetLozinkaRequest request) {

        if (request.getToken() == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nevažeči token");
        }
        Optional<Korisnik> user = korisnikService.findByEmail(jwtUtil.extractUsername(request.getToken()));
        if (user.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nepostojeći korisnik");
        }
        user.get().setLozinka(passwordEncoder.encode(request.getNewLozinka()));
        try {
            korisnikService.saveKorisnik(user.get());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
