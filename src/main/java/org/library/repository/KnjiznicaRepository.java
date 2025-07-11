package org.library.repository;

import jakarta.transaction.Transactional;
import org.library.model.Knjiznica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KnjiznicaRepository extends JpaRepository<Knjiznica, Integer> {
    @Query(value = """
    SELECT kolicina
    FROM knjiznica_ima_knjige
    WHERE idKnjiznica = :idKnjiznica
      AND idKnjiga = :idKnjiga
    """, nativeQuery = true)
    Integer findKolicina(@Param("idKnjiga") Integer idKnjiga, @Param("idKnjiznica") Integer idKnjiznica );
    @Query(value = """
    SELECT knjiznica.*
    FROM knjiznica
    JOIN zaposlenik_ima_knjiznicu ON zaposlenik_ima_knjiznicu.idknjiznica = knjiznica.idKnjiznica
    JOIN zaposlenik ON zaposlenik_ima_knjiznicu.idzaposlenik = zaposlenik.idzaposlenik
    WHERE zaposlenik.email = :email
    """, nativeQuery = true)
    List<Knjiznica> findForZaposlenik(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = """
 UPDATE knjiznica_ima_knjige
    SET kolicina = kolicina - 1
    WHERE idknjiga = :idKnjiga
      AND idknjiznica = :idKnjiznica
      AND kolicina > 0
    """, nativeQuery = true)
    int subtractKolicina(
            @Param("idKnjiga") int idKnjiga,
            @Param("idKnjiznica") int idKnjiznica
    );

    @Modifying
    @Transactional
    @Query(value = """
 UPDATE knjiznica_ima_knjige
    SET kolicina = kolicina + 1
    WHERE idknjiga = :idKnjiga
      AND idknjiznica = :idKnjiznica
      AND kolicina > 0
    """, nativeQuery = true)
    int addKolicina(
            @Param("idKnjiga") int idKnjiga,
            @Param("idKnjiznica") int idKnjiznica
    );

    @Modifying
    @Transactional
    @Query(value = """
 UPDATE knjiznica_ima_knjige
    SET kolicina = :kolicina
    WHERE idknjiga = :idKnjiga
      AND idknjiznica = :idKnjiznica
    """, nativeQuery = true)
    int updateRaspolaganje(
            @Param("idKnjiga") int idKnjiga,
            @Param("idKnjiznica") int idKnjiznica,
            @Param("kolicina") int kolicina
    );

    @Modifying
    @Transactional
    @Query(value = """
 INSERT INTO knjiznica_ima_knjige
    VALUES (:idKnjiga,:idKnjiznica,:kolicina)
    """, nativeQuery = true)
    int insertRaspolaganje(
            @Param("idKnjiga") Integer idKnjiga,
            @Param("idKnjiznica") Integer idKnjiznica,
            @Param("kolicina") Integer kolicina
    );

    @Query(value = """
 SELECT * FROM knjiznica_ima_knjige
        WHERE idKnjiga = :idKnjiga 
        and idKnjiznica= :idKnjiznica
    """, nativeQuery = true)
    Object getRaspolaganje(
            @Param("idKnjiga") Integer idKnjiga,
            @Param("idKnjiznica") Integer idKnjiznica
    );

    @Modifying
    @Transactional
    @Query(value = """
 DELETE FROM knjiznica_ima_knjige
        WHERE idKnjiga = :idKnjiga 
        and idKnjiznica = :idKnjiznica
    """, nativeQuery = true)
    int deleteRaspolaganje(
            @Param("idKnjiga") Integer idKnjiga,
            @Param("idKnjiznica") Integer idKnjiznica
    );
    @Query(value = """
    SELECT idKnjiznica
    FROM knjiznica
    """, nativeQuery = true)
    List<Integer> getAllIds();
}