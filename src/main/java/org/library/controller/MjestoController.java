package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.KnjigaDto;
import org.library.model.Knjiga;
import org.library.model.Mjesto;
import org.library.service.KnjigaService;
import org.library.service.MjestoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mjesto")
@RequiredArgsConstructor
public class MjestoController {
    private final MjestoService mjestoServicce;

    @GetMapping("/all")
    public List<Mjesto> getAll() {
        return mjestoServicce.getAllMjesto().stream().toList();
    }



    @GetMapping("/{id}")
    public ResponseEntity<Mjesto> getById(@PathVariable Integer id) {
        return mjestoServicce.getMjestoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping("/dodaj")
    public Mjesto create(@RequestBody Mjesto mjesto) {
        return mjestoServicce.saveMjesto(mjesto);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try{
            mjestoServicce.deleteMjesto(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
