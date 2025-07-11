package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KnjigaDto;
import org.library.dto.KorisnikDto;
import org.library.dto.ZaposlenikDto;
import org.library.model.Korisnik;
import org.library.model.Zaposlenik;
import org.library.repository.KorisnikRepository;
import org.library.service.KorisnikService;
import org.library.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/korisnik")
@RequiredArgsConstructor
public class KorisnikController {
    private final JwtUtil jwtUtil;
    private final KorisnikService korisnikService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/all")
    public List<KorisnikDto> getAll() {
        return korisnikService.getAllKorisnici()
                .stream()
                .toList();
    }

    @GetMapping("/knjige")
    public List<DozvoljenaKnjigaDto> getForUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return korisnikService.findKnjigeByKorisnik(jwtUtil.extractUsername(token));
    }

    @GetMapping("/emails")
    public List<String> getEmails() {
        return korisnikService.getAllKorisnici().stream().map(KorisnikDto::getEmail).toList();
    }


    @PostMapping("/register/korisnik")
    public ResponseEntity<?> register(@RequestBody Korisnik korisnik) {
        try {
            if (korisnik.getLozinka()!=null && !korisnik.getLozinka().isEmpty()){
                korisnik.setLozinka(passwordEncoder.encode(korisnik.getLozinka()));
            }

            korisnikService.saveKorisnikAdmin(korisnik);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/disableKorisnik")
    public ResponseEntity<?> disableKorisnik(@RequestBody Korisnik korisnik) {
        try {
            korisnikService.disableKorisnik(korisnik);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }



}
