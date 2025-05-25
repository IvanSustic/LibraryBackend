package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.model.Zaposlenik;
import org.library.service.ZaposlenikService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zaposlenici")
@RequiredArgsConstructor
public class ZaposlenikController {

    private final ZaposlenikService zaposlenikService;

    @GetMapping("/all")
    public List<Zaposlenik> getAll() {
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

    @PostMapping
    public Zaposlenik create(@RequestBody Zaposlenik dto) {

        return zaposlenikService.saveZaposlenik(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Zaposlenik> update(@PathVariable Integer id, @RequestBody Zaposlenik dto) {
        return zaposlenikService.getZaposlenikById(id).map(existing -> {
            Zaposlenik updated = dto;
            updated.setIdZaposlenik(id);
            return ResponseEntity.ok(zaposlenikService.saveZaposlenik(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        zaposlenikService.deleteZaposlenik(id);
        return ResponseEntity.noContent().build();
    }


}