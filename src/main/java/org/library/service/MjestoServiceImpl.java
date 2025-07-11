package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Mjesto;
import org.library.model.TipKnjige;
import org.library.repository.MjestoRepository;
import org.library.repository.TipKnjigeRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MjestoServiceImpl implements MjestoService {

    private final MjestoRepository mjestoRepository;

    @Override
    public List<Mjesto> getAllMjesto() {
        return mjestoRepository.findAll();
    }

    @Override
    public Optional<Mjesto> getMjestoById(Integer id) {
        return mjestoRepository.findById(id);
    }

    @Override
    public Mjesto saveMjesto(Mjesto mjesto) {
        return mjestoRepository.save(mjesto);
    }

    @Override
    public void deleteMjesto(Integer id) throws SQLException {
        if (mjestoRepository.findById(id).isEmpty()){
            throw new SQLException("Mjesto ne postoji.");
        }
        try {
            mjestoRepository.deleteById(id);
        } catch (Exception e){
            throw new SQLIntegrityConstraintViolationException("Mjesto je povezano s knjižnicama.");
        }
    }
}