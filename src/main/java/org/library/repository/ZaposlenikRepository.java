package org.library.repository;

import jakarta.transaction.Transactional;
import org.library.model.Korisnik;
import org.library.model.Zaposlenik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZaposlenikRepository extends JpaRepository<Zaposlenik, Integer> {
    Optional<Zaposlenik> findByEmail(String email);

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
       FROM zaposlenik
       join zaposlenik_ima_knjiznicu zk on zk.idZaposlenik = zaposlenik.idZaposlenik
       JOIN knjiznica kn ON zk.idknjiznica = kn.idknjiznica
       JOIN knjiznica_ima_knjige kik ON kn.idknjiznica = kik.idknjiznica
       JOIN knjiga k ON kik.idknjiga = k.idknjiga
       JOIN zanr z ON k.idzanr = z.idzanr
       JOIN tip_knjige tk ON k.idtipknjige = tk.idtipknjige
       WHERE zaposlenik.email = :email
         AND kik.kolicina > 0
    """, nativeQuery = true)
    List<Object[]> findKnjigeByZaposlenik(@Param("email") String email);

    Optional<Zaposlenik> findByRefreshToken(String refreshToken);

    @Query("""
SELECT DISTINCT z
FROM Zaposlenik z
JOIN z.radnaMjesta rm
JOIN z.knjiznice k
WHERE rm.naziv = 'Knjižničar'
  AND NOT EXISTS (
    SELECT 1 FROM z.radnaMjesta rm2 WHERE rm2.naziv <> 'Knjižničar'
  )
  AND k.idKnjiznica IN (
    SELECT k2.idKnjiznica
    FROM Zaposlenik z2
    JOIN z2.knjiznice k2
    WHERE z2.email = :email
  )
  AND z.email <> :email
""")
    List<Zaposlenik> findKnjiznicariForVoditelj(@Param("email") String email);


    @Modifying
    @Transactional
    @Query(value = """
    UPDATE zaposlenik
    SET refreshToken=:refreshToken,
    refreshTokenDatum=:refreshTokenDatum
    WHERE email= :email
    """, nativeQuery = true)
    void updateRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("refreshTokenDatum") LocalDate refreshTokenDatum,
                            @Param("email") String email);

}