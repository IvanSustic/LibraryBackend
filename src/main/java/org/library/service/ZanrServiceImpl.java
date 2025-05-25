package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Zanr;
import org.library.repository.ZanrRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZanrServiceImpl implements ZanrService {

    private final ZanrRepository zanrRepository;

    @Override
    public List<Zanr> getAllZanrovi() {
        return zanrRepository.findAll();
    }

    @Override
    public Optional<Zanr> getZanrById(Integer id) {
        return zanrRepository.findById(id);
    }

    @Override
    public Zanr saveZanr(Zanr zanr) {
        return zanrRepository.save(zanr);
    }

    @Override
    public void deleteZanr(Integer id) {
        zanrRepository.deleteById(id);
    }
}