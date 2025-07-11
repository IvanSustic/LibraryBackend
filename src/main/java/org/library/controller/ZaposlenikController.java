package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KorisnikDto;
import org.library.dto.RezervacijaDTO;
import org.library.dto.ZaposlenikDto;
import org.library.model.Zaposlenik;
import org.library.service.ZaposlenikService;
import org.library.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zaposlenici")
@RequiredArgsConstructor
public class ZaposlenikController {
    private final JwtUtil jwtUtil;
    private final ZaposlenikService zaposlenikService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/all")
    public List<ZaposlenikDto> getAll() {
        return zaposlenikService.getAllZaposlenici()
                .stream()
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zaposlenik> getById(@PathVariable Integer id) {
        return zaposlenikService.getZaposlenikById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        zaposlenikService.deleteZaposlenik(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/knjige")
    public List<DozvoljenaKnjigaDto> getForUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return zaposlenikService.findKnjigeByZaposlenik(jwtUtil.extractUsername(token));
    }

    @GetMapping("/zaposleniciForKnjiznicar")
    public List<ZaposlenikDto> getZaposleniciForKnjiznicar(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return zaposlenikService.getKnjiznicariForVoditelj(jwtUtil.extractUsername(token));
    }


    @PostMapping("/register/zaposlenik")
    public ResponseEntity<?> register(@RequestBody Zaposlenik zaposlenik) {
        try {
            if (zaposlenik.getLozinka()!=null && !zaposlenik.getLozinka().isEmpty()){
                zaposlenik.setLozinka(passwordEncoder.encode(zaposlenik.getLozinka()));
            }

            zaposlenikService.saveZaposlenik(zaposlenik);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/disableZaposlenik")
    public ResponseEntity<?> disableZaposlenik(@RequestBody Zaposlenik zaposlenik) {
        try {
            zaposlenikService.disableZaposlenik(zaposlenik);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}