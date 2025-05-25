package org.library.service;

import org.library.model.TipKnjige;

import java.util.List;
import java.util.Optional;

public interface TipKnjigeService {
    List<TipKnjige> getAllTipoviKnjiga();
    Optional<TipKnjige> getTipKnjigeById(Integer id);
    TipKnjige saveTipKnjige(TipKnjige tipKnjige);
    void deleteTipKnjige(Integer id);
}
