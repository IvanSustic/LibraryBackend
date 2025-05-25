package org.library.service;


import org.library.dto.KnjiznicaDto;
import org.library.model.Knjiznica;

import java.util.List;
import java.util.Optional;

public interface KnjiznicaService {
    List<KnjiznicaDto> getAllKnjiznice();

    Optional<KnjiznicaDto> getKnjiznicaById(Integer id);
    Knjiznica saveKnjiznica(KnjiznicaDto knjiznica);
    void deleteKnjiznica(Integer id);
}
