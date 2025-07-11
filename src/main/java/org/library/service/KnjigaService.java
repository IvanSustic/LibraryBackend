package org.library.service;

import org.library.dto.KnjigaDto;
import org.library.model.Knjiga;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface KnjigaService {
    List<KnjigaDto> getAllKnjige();
    Optional<KnjigaDto> getKnjigaById(Integer id);
    Knjiga saveKnjiga(KnjigaDto dto);
    void deleteKnjiga(Integer id) throws SQLException;
}
