package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.PosudbaDto;
import org.library.dto.RacunDto;
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

    @PostMapping("/posudi")
    public ResponseEntity<String> posudi(@RequestBody PosudbaDto request) {
        try{
            this.posudbaService.savePosudba(request);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        try{
            return ResponseEntity.ok().body(this.posudbaService.deletePosudba(id, jwtUtil.extractUsername(token)));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/ostecena/{id}")
    public ResponseEntity<?> deleteOstecena(@PathVariable("id") Integer id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        try{
            return ResponseEntity.ok().body(this.posudbaService.ostecenaPosudba(id, jwtUtil.extractUsername(token)));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
