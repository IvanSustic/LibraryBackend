package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.TipKnjige;
import org.library.repository.TipKnjigeRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipKnjigeServiceImpl implements TipKnjigeService {

    private final TipKnjigeRepository tipKnjigeRepository;

    @Override
    public List<TipKnjige> getAllTipoviKnjiga() {
        return tipKnjigeRepository.findAll();
    }

    @Override
    public Optional<TipKnjige> getTipKnjigeById(Integer id) {
        return tipKnjigeRepository.findById(id);
    }

    @Override
    public TipKnjige saveTipKnjige(TipKnjige tipKnjige) {
        return tipKnjigeRepository.save(tipKnjige);
    }

    @Override
    public void deleteTipKnjige(Integer id) throws SQLException {
        if (tipKnjigeRepository.findById(id).isEmpty()){
            throw new SQLException("Tip knjige ne postoji.");
        }
        try {
            tipKnjigeRepository.deleteById(id);
        } catch (Exception e){
            throw new SQLIntegrityConstraintViolationException("Tip knjige je povezan sa knjigama i ne može biti obrisan.");
        }
    }
}