package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Autor;
import org.library.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    @Override
    public List<Autor> getAllAutori() {
        return autorRepository.findAll();
    }

    @Override
    public Optional<Autor> getAutorById(Integer id) {
        return autorRepository.findById(id);
    }

    @Override
    public Autor saveAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    @Override
    public void deleteAutor(Integer id) {
        autorRepository.deleteById(id);
    }
}
