package org.library.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.KnjigaDto;
import org.library.model.Knjiga;
import org.library.model.Zaposlenik;
import org.library.service.KnjigaService;
import org.library.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/knjige")
@RequiredArgsConstructor
public class KnjigaController {
    private final KnjigaService knjigaService;

    @GetMapping("/all")
    public List<KnjigaDto> getAll() {
        return knjigaService.getAllKnjige().stream().toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KnjigaDto> getById(@PathVariable Integer id) {
        return knjigaService.getKnjigaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping
    public Knjiga create(@RequestBody KnjigaDto dto) {
        return knjigaService.saveKnjiga(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Knjiga> update(@PathVariable Integer id, @RequestBody KnjigaDto dto) {
        return knjigaService.getKnjigaById(id).map(existing -> {
            KnjigaDto updated = dto;
            updated.setIdKnjiga(id);
            return ResponseEntity.ok(knjigaService.saveKnjiga(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        knjigaService.deleteKnjiga(id);
        return ResponseEntity.noContent().build();
    }
}
