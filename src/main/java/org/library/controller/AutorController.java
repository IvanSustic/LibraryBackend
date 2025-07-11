package org.library.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.library.dto.KnjigaDto;
import org.library.model.Autor;
import org.library.model.Knjiga;
import org.library.service.AutorService;
import org.library.service.KnjigaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/api/autor")
@RequiredArgsConstructor
public class AutorController {
    private final AutorService autorService;

    @GetMapping("/all")
    public List<Autor> getAll() {
        return autorService.getAllAutori().stream().toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> getById(@PathVariable Integer id) {
        return autorService.getAutorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/dodaj")
    public Autor saveAutor(@RequestBody Autor autor) {
        System.out.println(autor);
        return autorService.saveAutor(autor);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try{
            autorService.deleteAutor(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }


    }
}
