package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.KnjigaDto;
import org.library.dto.RacunDto;
import org.library.model.Knjiga;
import org.library.service.KnjigaService;
import org.library.service.RacunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racun")
@RequiredArgsConstructor
public class RacunController {
    private final RacunService racunService;

    @GetMapping("/all")
    public List<RacunDto> getAll() {
        return racunService.getAllRacuni();
    }

}
