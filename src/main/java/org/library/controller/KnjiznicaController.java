package org.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.library.dto.KnjiznicaDto;
import org.library.dto.RaspolaganjeDto;
import org.library.model.Knjiznica;
import org.library.service.KnjiznicaService;
import org.library.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/knjiznica")
@RequiredArgsConstructor
public class KnjiznicaController {
    private final KnjiznicaService knjiznicaService;
    private final JwtUtil jwtUtil;
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

    @GetMapping("/forZaposlenik")
    public List<KnjiznicaDto> getKnjizniceForZaposleni(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(7);

        return knjiznicaService.findKnjizniceForZaposlenik(jwtUtil.extractUsername(token));
    }

    @PostMapping("/dodaj")
    public Knjiznica create(@RequestBody KnjiznicaDto dto) {
        return knjiznicaService.saveKnjiznica(dto);
    }

    @PutMapping("/dodaj/{id}")
    public ResponseEntity<Knjiznica> update(@PathVariable Integer id, @RequestBody KnjiznicaDto dto) {
        return knjiznicaService.getKnjiznicaById(id).map(existing -> {
            dto.setIdKnjiznica(id);
            return ResponseEntity.ok(knjiznicaService.saveKnjiznica(dto));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/addRaspolaganje")
    public ResponseEntity<String> insertRaspolaganje(@RequestBody RaspolaganjeDto dto) {
        try {
            knjiznicaService.insertRaspolaganje(dto);
            return ResponseEntity.ok().build();
        } catch (SQLException e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @PutMapping("/updateRaspolaganje")
    public ResponseEntity<String> updateRaspolaganje(@RequestBody RaspolaganjeDto dto) {
        try {
            knjiznicaService.updateRaspolaganje(dto);
            return ResponseEntity.ok().build();
        } catch (SQLException e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try{
            knjiznicaService.deleteKnjiznica(id);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteRaspolaganje")
    public ResponseEntity<String> delete(@RequestBody RaspolaganjeDto raspolaganjeDto) {
        try {
            knjiznicaService.deleteRaspolaganje(raspolaganjeDto);
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
}
