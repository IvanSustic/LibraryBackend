package org.library.service;


import org.library.dto.KnjiznicaDto;
import org.library.dto.RacunDto;
import org.library.dto.RaspolaganjeDto;
import org.library.model.Knjiznica;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface KnjiznicaService {
    List<KnjiznicaDto> getAllKnjiznice();

    Optional<KnjiznicaDto> getKnjiznicaById(Integer id);
    Knjiznica saveKnjiznica(KnjiznicaDto knjiznica);
    void deleteKnjiznica(Integer id) throws SQLException;

   List<KnjiznicaDto> findKnjizniceForZaposlenik(String email);

    int insertRaspolaganje(RaspolaganjeDto raspolaganjeDto) throws SQLException;

    int updateRaspolaganje(RaspolaganjeDto raspolaganjeDto) throws SQLException;

    int deleteRaspolaganje(RaspolaganjeDto raspolaganjeDto) throws SQLException;
}
