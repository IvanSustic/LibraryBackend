package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.PosudbaDto;
import org.library.dto.RezervacijaDTO;
import org.library.model.Posudba;
import org.library.model.Rezervacija;
import org.library.service.PosudbaService;
import org.library.service.RezervacijaService;
import org.library.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posudba")
@RequiredArgsConstructor
public class PosudbaController {
    private final JwtUtil jwtUtil;
    private final PosudbaService posudbaService;

    @GetMapping("/forUser")
    public List<PosudbaDto> getForUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return posudbaService.getPosudbeForKorisnik(jwtUtil.extractUsername(token));
    }

    @GetMapping("/forZaposlenik")
    public List<PosudbaDto> getForZaposlenik(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return posudbaService.getPosudbeForZaposlenik(jwtUtil.extractUsername(token));
    }

    @PostMapping("/rezerviraj")
    public ResponseEntity<?> rezerviraj(@RequestBody PosudbaDto request, HttpServletRequest tokenRequest) {
        String header = tokenRequest.getHeader("Authorization");
        String token = header.substring(7);
        request.setKorisnikEmail(jwtUtil.extractUsername(token));
        System.out.println(request);
        try {
            PosudbaDto rezervacija = posudbaService.savePosudba(request);
            return ResponseEntity.ok().body(rezervacija);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nemoguče rezervirati knjigu");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        try{
            this.posudbaService.deletePosudba(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
