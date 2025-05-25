package org.library.service;

import org.library.dto.PosudbaDto;
import org.library.dto.RezervacijaDTO;
import org.library.model.Posudba;

import java.util.List;
import java.util.Optional;

public interface PosudbaService {
    List<Posudba> getAllPosudbe();
    Optional<Posudba> getPosudbaById(Integer id);
    PosudbaDto savePosudba(PosudbaDto posudba) throws IllegalAccessException;
    void deletePosudba(Integer id) throws IllegalAccessException;

    List<PosudbaDto> getPosudbeForKorisnik(String email);
    List<PosudbaDto> getPosudbeForZaposlenik(String email);

    void deletePosudbaBezDodavanja(Integer id) throws IllegalAccessException;
}