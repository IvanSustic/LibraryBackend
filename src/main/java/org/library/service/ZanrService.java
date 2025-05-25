package org.library.service;

import org.library.model.Zanr;

import java.util.List;
import java.util.Optional;

public interface ZanrService {
    List<Zanr> getAllZanrovi();
    Optional<Zanr> getZanrById(Integer id);
    Zanr saveZanr(Zanr zanr);
    void deleteZanr(Integer id);
}
