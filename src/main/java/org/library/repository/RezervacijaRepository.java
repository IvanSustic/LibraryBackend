package org.library.repository;

import org.library.model.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Integer> {
    List<Rezervacija> findByKorisnikEmail(String email);
}


