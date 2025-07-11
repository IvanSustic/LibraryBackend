package org.library.repository;

import jakarta.transaction.Transactional;
import org.library.dto.KnjigaDto;
import org.library.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {
    Optional<Korisnik> findByEmail(String email);
    @Query(value = """
    SELECT k.idknjiga AS idKnjiga,
           k.naslov AS naslov,
           k.datumizdanja AS datumIzdanja,
           k.slika AS slika,
           kn.naziv AS nazivKnjiznice,
           kik.kolicina AS kolicina,
           z.naziv AS zanr,
           tk.naziv AS tipKnjige,
           kn.idknjiznica AS idKnjiznice
    FROM korisnik ko
    JOIN clanstvo cl ON ko.idkorisnik = cl.idkorisnik
    JOIN knjiznica kn ON cl.idknjiznica = kn.idknjiznica
    JOIN knjiznica_ima_knjige kik ON kn.idknjiznica = kik.idknjiznica
    JOIN knjiga k ON kik.idknjiga = k.idknjiga
    JOIN zanr z ON k.idzanr = z.idzanr
    JOIN tip_knjige tk ON k.idtipknjige = tk.idtipknjige
    WHERE ko.email = :email
      AND kik.kolicina > 0
    """, nativeQuery = true)
    List<Object[]> findKnjigeByKorisik(@Param("email") String email);

    Optional<Korisnik> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE korisnik
    SET refreshToken=:refreshToken,
    refreshTokenDatum=:refreshTokenDatum
    WHERE email= :email
    """, nativeQuery = true)
    void updateRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("refreshTokenDatum") LocalDate refreshTokenDatum,
                            @Param("email") String email);

}