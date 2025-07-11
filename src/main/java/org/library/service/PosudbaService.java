package org.library.service;

import org.library.dto.PosudbaDto;
import org.library.dto.RacunDto;
import org.library.dto.RezervacijaDTO;
import org.library.model.Posudba;

import java.util.List;
import java.util.Optional;

public interface PosudbaService {
    List<Posudba> getAllPosudbe();
    Optional<Posudba> getPosudbaById(Integer id);
    PosudbaDto savePosudba(PosudbaDto posudba) throws IllegalAccessException;
    Optional<RacunDto> deletePosudba(Integer id, String email) throws IllegalAccessException;
    Optional<RacunDto> ostecenaPosudba(Integer id, String email) throws IllegalAccessException;

    List<PosudbaDto> getPosudbeForKorisnik(String email);
    List<PosudbaDto> getPosudbeForZaposlenik(String email);

    void deletePosudbaBezDodavanja(Integer id) throws IllegalAccessException;
}