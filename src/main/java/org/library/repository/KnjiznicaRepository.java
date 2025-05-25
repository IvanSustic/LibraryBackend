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



    @Modifying
    @Transactional
    @Query(value = """
 UPDATE knjiznica_ima_knjige
    SET kolicina = kolicina - 1
    WHERE idknjiga = :idKnjiga
      AND idknjiznica = :idKnjiznica
      AND kolicina > 0
    """, nativeQuery = true)
    int updateKolicina(
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
}