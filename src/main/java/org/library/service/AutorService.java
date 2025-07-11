package org.library.service;

import jakarta.validation.ConstraintViolationException;
import org.library.model.Autor;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

public interface AutorService {
    List<Autor> getAllAutori();
    Optional<Autor> getAutorById(Integer id);
    Autor saveAutor(Autor autor);
    void deleteAutor(Integer id)  throws SQLException;
}
