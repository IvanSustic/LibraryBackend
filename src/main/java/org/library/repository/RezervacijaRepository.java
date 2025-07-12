package org.library.repository;

import jakarta.transaction.Transactional;
import org.library.model.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Integer> {
    List<Rezervacija> findByKorisnikEmail(String email);

    @Query(value = """
    SELECT rezervacije.*,knjiga.naslov as nazivKnjige,knjiznica.naziv as nazivKnjiznice, korisnik.email as korisnikEmail
    FROM rezervacije
	JOIN knjiga ON knjiga.idKnjiga = rezervacije.idknjiga
	JOIN korisnik ON korisnik.idkorisnik = rezervacije.idkorisnik
    JOIN knjiznica ON knjiznica.idknjiznica = rezervacije.idknjiznica
    JOIN zaposlenik_ima_knjiznicu ON zaposlenik_ima_knjiznicu.idknjiznica = knjiznica.idKnjiznica
    JOIN zaposlenik ON zaposlenik_ima_knjiznicu.idzaposlenik = zaposlenik.idzaposlenik
    WHERE zaposlenik.email = :email
    """, nativeQuery = true)
    List<Object[]> findPosudbeByZaposlenikEmail(@Param("email") String email);

    List<Rezervacija> findAllByKnjigaIdKnjigaAndKnjiznicaIdKnjiznica(Integer idKnjiga, Integer idKnjiznica);

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM rezervacije
    WHERE (idKorisnik, idKnjiznica) IN (
        SELECT c.idKorisnik, c.idKnjiznica
        FROM clanstvo c
        WHERE c.krajUclanjivanja < CURRENT_DATE
    )
""", nativeQuery = true)
    void deleteRezervacijeFromIstekliClanovi();

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM rezervacije
    WHERE krajRezervacije < CURRENT_DATE
""", nativeQuery = true)
    void deleteIstekleRezervacije();
}


