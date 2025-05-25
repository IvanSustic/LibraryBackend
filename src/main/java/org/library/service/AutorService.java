package org.library.service;

import org.library.model.Autor;

import java.util.List;
import java.util.Optional;

public interface AutorService {
    List<Autor> getAllAutori();
    Optional<Autor> getAutorById(Integer id);
    Autor saveAutor(Autor autor);
    void deleteAutor(Integer id);
}
