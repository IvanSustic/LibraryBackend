package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.KnjigaDto;
import org.library.dto.KnjiznicaDto;
import org.library.model.Knjiznica;
import org.library.service.KnjigaService;
import org.library.service.KnjiznicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knjiznica")
@RequiredArgsConstructor
public class KnjiznicaController {
    private final KnjiznicaService knjiznicaService;

    @GetMapping("/all")
    public List<KnjiznicaDto> getAll() {
        return knjiznicaService.getAllKnjiznice().stream().toList();
    }



    @GetMapping("/{id}")
    public ResponseEntity<KnjiznicaDto> getById(@PathVariable Integer id) {
        return knjiznicaService.getKnjiznicaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Knjiznica create(@RequestBody KnjiznicaDto dto) {
        return knjiznicaService.saveKnjiznica(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Knjiznica> update(@PathVariable Integer id, @RequestBody KnjiznicaDto dto) {
        return knjiznicaService.getKnjiznicaById(id).map(existing -> {
            KnjiznicaDto updated = dto;
            updated.setIdKnjiznica(id);
            return ResponseEntity.ok(knjiznicaService.saveKnjiznica(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        knjiznicaService.deleteKnjiznica(id);
        return ResponseEntity.noContent().build();
    }
}
