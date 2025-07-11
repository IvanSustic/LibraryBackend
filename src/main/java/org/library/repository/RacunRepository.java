package org.library.repository;

import org.library.model.Racun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RacunRepository extends JpaRepository<Racun, Integer> {

    List<Racun> findAllByTipRacunaIdTipRacunaAndDatumAfter(Integer id, LocalDate datum);

}