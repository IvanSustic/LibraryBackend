package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.model.TipKnjige;
import org.library.model.Zanr;
import org.library.service.TipKnjigeService;
import org.library.service.ZanrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zanr")
@RequiredArgsConstructor
public class ZanrController {
    private final ZanrService zanrService;

    @GetMapping("/all")
    public List<Zanr> getAll() {
        return zanrService.getAllZanrovi().stream().toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zanr> getById(@PathVariable Integer id) {
        return zanrService.getZanrById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/dodaj")
    public Zanr saveZanr(@RequestBody Zanr zanr) {
        return zanrService.saveZanr(zanr);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try{
            zanrService.deleteZanr(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
