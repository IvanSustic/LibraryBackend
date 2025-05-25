package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KnjigaDto;
import org.library.repository.KorisnikRepository;
import org.library.service.KorisnikService;
import org.library.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/korisnik")
@RequiredArgsConstructor
public class KorisnikController {
    private final JwtUtil jwtUtil;
    private final KorisnikService korisnikService;

    @GetMapping("/knjige")
    public List<DozvoljenaKnjigaDto> getForUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return korisnikService.findKnjigeByKorisnik(jwtUtil.extractUsername(token));
    }
}
