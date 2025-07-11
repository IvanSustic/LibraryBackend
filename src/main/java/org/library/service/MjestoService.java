package org.library.service;

import org.library.model.Mjesto;
import org.library.model.Mjesto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MjestoService {
    List<Mjesto> getAllMjesto();
    Optional<Mjesto> getMjestoById(Integer id);
    Mjesto saveMjesto(Mjesto mjesto);
    void deleteMjesto(Integer id) throws SQLException;
}
