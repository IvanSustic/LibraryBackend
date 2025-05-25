package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Racun;
import org.library.model.Zaposlenik;
import org.library.repository.RacunRepository;
import org.library.repository.ZaposlenikRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.library.model.Racun;

import java.util.List;
import java.util.Optional;

public interface RacunService {
    List<Racun> getAllRacuni();
    Optional<Racun> getRacunById(Integer id);
    Racun saveRacun(Racun racun);
    void deleteRacun(Integer id);
}