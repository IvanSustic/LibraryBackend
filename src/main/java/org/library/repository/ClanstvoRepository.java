package org.library.repository;

import jakarta.transaction.Transactional;
import org.library.model.Clanstvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClanstvoRepository extends JpaRepository<Clanstvo, Integer> {
    public List<Clanstvo> findAllByKorisnikEmail(String email);

    @Query(value = """
    SELECT clanstvo.*,knjiznica.naziv as nazivKnjiznice, korisnik.email as korisnikEmail
    FROM clanstvo
	JOIN korisnik ON korisnik.idkorisnik = clanstvo.idkorisnik
    JOIN knjiznica ON knjiznica.idknjiznica = clanstvo.idknjiznica
    JOIN zaposlenik_ima_knjiznicu ON zaposlenik_ima_knjiznicu.idknjiznica = knjiznica.idKnjiznica
    JOIN zaposlenik ON zaposlenik_ima_knjiznicu.idzaposlenik = zaposlenik.idzaposlenik
    WHERE zaposlenik.email = :email
    """, nativeQuery = true)
    List<Object[]> findAllByZapEmail(@Param("email") String email);

    Optional<Clanstvo> findByKnjiznicaIdKnjiznicaAndKorisnikEmail(Integer idKnjiznica, String email);


    @Modifying
    @Transactional
    @Query(value = """
  DELETE FROM clanstvo
  WHERE krajUclanjivanja < CURRENT_DATE
    AND NOT EXISTS (
      SELECT 1
      FROM posudbe p
      WHERE p.idKorisnik = clanstvo.idKorisnik
        AND p.idKnjiznica = clanstvo.idKnjiznica
    )
  """, nativeQuery = true)
    void deleteIsteklaClanstvaWithoutPosudbe();

}