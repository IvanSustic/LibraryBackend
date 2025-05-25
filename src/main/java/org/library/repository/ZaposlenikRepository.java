package org.library.repository;

import org.library.model.Zaposlenik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZaposlenikRepository extends JpaRepository<Zaposlenik, Integer> {
    Optional<Zaposlenik> findByEmail(String email);
}