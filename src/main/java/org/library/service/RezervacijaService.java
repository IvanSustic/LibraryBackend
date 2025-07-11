package org.library.service;

import org.library.dto.PosudbaDto;
import org.library.dto.RezervacijaDTO;
import org.library.model.Rezervacija;

import java.util.List;
import java.util.Optional;

public interface RezervacijaService {
    List<Rezervacija> getAllRezervacije();
    Optional<Rezervacija> getRezervacijaById(Integer id);
    RezervacijaDTO saveRezervacija(RezervacijaDTO rezervacija) throws IllegalAccessException;

    PosudbaDto saveRezerviranaPosudba(RezervacijaDTO rezervacija) throws IllegalAccessException;
    void deleteRezervacija(Integer id) throws IllegalAccessException;
    List<RezervacijaDTO> getRezervacijeForZaposlenik(String email);

    List<RezervacijaDTO> getRezervacijeForKorisnik(String email);
}