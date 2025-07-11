package org.library.service;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.StandardForeignKeyExporter;
import org.library.model.Autor;
import org.library.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
    public void deleteAutor(Integer id) throws SQLException {
        if (autorRepository.findById(id).isEmpty()){
            throw new SQLException("Autor ne postoji.");
        }
        try {
            autorRepository.deleteById(id);
        } catch (Exception e){
            throw new SQLIntegrityConstraintViolationException("Autor je povezan sa knjigama i ne može biti obrisan.");
        }

    }
}
