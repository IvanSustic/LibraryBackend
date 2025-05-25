package org.library.repository;

import org.library.model.Autor;
import org.library.model.Knjiga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface KnjigaRepository extends JpaRepository<Knjiga, Integer> {
}