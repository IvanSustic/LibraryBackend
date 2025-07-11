package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.ClanstvoDto;
import org.library.dto.KnjiznicaDto;
import org.library.dto.RacunDto;
import org.library.dto.RezervacijaDTO;
import org.library.model.Clanstvo;
import org.library.repository.ClanstvoRepository;
import org.library.service.ClanstvoService;
import org.library.utils.JwtUtil;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clanstvo")
@RequiredArgsConstructor
public class ClanstvoController {
    private final ClanstvoService clanstvoService;
    private final JwtUtil jwtUtil;
    @GetMapping("/all")
    public List<ClanstvoDto> getAll() {
        return clanstvoService.getAllClanstva();
    }

    @GetMapping("/forUser")
    public List<ClanstvoDto> getForUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return clanstvoService.getClanstvoByKorisnik(jwtUtil.extractUsername(token));
    }

    @GetMapping("/forZaposlenik")
    public List<ClanstvoDto> getForZaposlenik(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return clanstvoService.getClanstvaForZaposlenik(jwtUtil.extractUsername(token));
    }

    @PostMapping("/dodaj")
    public RacunDto dodajClanstvo(@Param("email") String email, @Param("idKnjiznica") String idKnjiznica, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

       return clanstvoService.dodajClanstvo(email,Integer.parseInt(idKnjiznica),jwtUtil.extractUsername(token));
    }

    @PostMapping("/dodajSva")
    public RacunDto dodajSvaClanstva(@Param("email") String email, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return clanstvoService.dodajSvaClanstva(email,jwtUtil.extractUsername(token));
    }


}
