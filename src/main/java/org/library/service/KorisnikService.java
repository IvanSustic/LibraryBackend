package org.library.service;

import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KorisnikDto;
import org.library.model.Korisnik;
import org.library.model.Zaposlenik;

import java.util.List;
import java.util.Optional;

public interface KorisnikService {
    List<Korisnik> getAllKorisnici();
    Optional<Korisnik> getKorisnikById(Integer id);
    Korisnik saveKorisnik(KorisnikDto korisnik);
    void deleteKorisnik(Integer id);
    Optional<Korisnik> findByEmail(String email);

    List<DozvoljenaKnjigaDto> findKnjigeByKorisnik(String email);
}
