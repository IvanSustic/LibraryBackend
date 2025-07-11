package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.model.Autor;
import org.library.model.TipKnjige;
import org.library.service.AutorService;
import org.library.service.TipKnjigeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipKnjige")
@RequiredArgsConstructor
public class TipKnjigeController {
    private final TipKnjigeService tipKnjigeService;

    @GetMapping("/all")
    public List<TipKnjige> getAll() {
        return tipKnjigeService.getAllTipoviKnjiga().stream().toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipKnjige> getById(@PathVariable Integer id) {
        return tipKnjigeService.getTipKnjigeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/dodaj")
    public TipKnjige saveAutor(@RequestBody TipKnjige tipKnjige) {
        return tipKnjigeService.saveTipKnjige(tipKnjige);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try{
            tipKnjigeService.deleteTipKnjige(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
