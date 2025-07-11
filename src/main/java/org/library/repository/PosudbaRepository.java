package org.library.repository;

import org.library.dto.PosudbaDto;
import org.library.model.Posudba;
import org.library.model.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PosudbaRepository extends JpaRepository<Posudba, Integer> {
    List<Posudba> findByKorisnikEmail(String email);

    @Query(value = """
    SELECT posudbe.*,knjiga.naslov as nazivKnjige,knjiznica.naziv as nazivKnjiznice, korisnik.email as korisnikEmail
    FROM posudbe
	JOIN knjiga ON knjiga.idKnjiga = posudbe.idknjiga
	JOIN korisnik ON korisnik.idkorisnik = posudbe.idkorisnik
    JOIN knjiznica ON knjiznica.idknjiznica = posudbe.idknjiznica
    JOIN zaposlenik_ima_knjiznicu ON zaposlenik_ima_knjiznicu.idknjiznica = knjiznica.idKnjiznica
    JOIN zaposlenik ON zaposlenik_ima_knjiznicu.idzaposlenik = zaposlenik.idzaposlenik
    WHERE zaposlenik.email = :email
    """, nativeQuery = true)
    List<Object[]> findPosudbeByZaposlenikEmail(@Param("email") String email);

    Optional<Posudba> findPosudbaByKnjigaIdKnjigaAndKnjiznicaIdKnjiznica(Integer idKnjiga, Integer idKnjiznica);
}