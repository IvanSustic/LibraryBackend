package org.library.service;

import org.library.dto.RacunDto;
import org.library.model.Racun;

import java.util.List;
import java.util.Optional;

public interface RacunService {
    List<RacunDto> getAllRacuni();
    Optional<RacunDto> getRacunById(Integer id);
    Racun saveRacun(Racun racun);
    void deleteRacun(Integer id);
}