package org.library.service;

import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KorisnikDto;
import org.library.model.Korisnik;
import org.library.model.Zaposlenik;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KorisnikService {
    List<KorisnikDto> getAllKorisnici();
    Optional<Korisnik> getKorisnikById(Integer id);
    Korisnik saveKorisnik(KorisnikDto korisnik) throws SQLException;
    Korisnik saveKorisnik(Korisnik korisnik) throws SQLException;
    void deleteKorisnik(Integer id);
    Optional<Korisnik> findByEmail(String email);

    boolean checkRefreshToken(String refreshToken);


    void saveRefreshToken(String refreshToken, LocalDate refreshTokenDatum, String email);


    Korisnik saveKorisnikAdmin(Korisnik korisnik) throws SQLException;

    Korisnik disableKorisnik(Korisnik korisnik) throws SQLException;

    List<DozvoljenaKnjigaDto> findKnjigeByKorisnik(String email);
}
