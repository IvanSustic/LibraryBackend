package org.library.service;

import org.library.model.Zaposlenik;

import java.util.List;
import java.util.Optional;

public interface ZaposlenikService {
    List<Zaposlenik> getAllZaposlenici();
    Optional<Zaposlenik> getZaposlenikById(Integer id);
    Zaposlenik saveZaposlenik(Zaposlenik zaposlenik);
    void deleteZaposlenik(Integer id);
    Optional<Zaposlenik> findByEmail(String email);
}