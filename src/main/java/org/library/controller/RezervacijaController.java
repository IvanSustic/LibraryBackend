package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KnjigaDto;
import org.library.dto.KorisnikDto;
import org.library.dto.RezervacijaDTO;
import org.library.model.Rezervacija;
import org.library.service.RezervacijaService;
import org.library.service.ZaposlenikService;
import org.library.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rezervacije")
@RequiredArgsConstructor
public class RezervacijaController {
    private final JwtUtil jwtUtil;
    private final RezervacijaService rezervacijaService;

    @GetMapping("/forUser")
    public List<RezervacijaDTO> getForUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return rezervacijaService.getRezervacijeForKorisnik(jwtUtil.extractUsername(token));
    }


    @PostMapping("/rezerviraj")
    public ResponseEntity<?> rezerviraj(@RequestBody RezervacijaDTO request, HttpServletRequest tokenRequest) {
        String header = tokenRequest.getHeader("Authorization");
        String token = header.substring(7);
        request.setKorisnikEmail(jwtUtil.extractUsername(token));
        System.out.println(request);
        try {
            RezervacijaDTO rezervacija = rezervacijaService.saveRezervacija(request);
            return ResponseEntity.ok().body(rezervacija);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nemoguče rezervirati knjigu");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        try{
            this.rezervacijaService.deleteRezervacija(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }
}
