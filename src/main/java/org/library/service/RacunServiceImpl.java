package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Racun;
import org.library.repository.RacunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.library.model.Racun;
import org.library.repository.RacunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RacunServiceImpl implements RacunService {

    private final RacunRepository racunRepository;

    @Override
    public List<Racun> getAllRacuni() {
        return racunRepository.findAll();
    }

    @Override
    public Optional<Racun> getRacunById(Integer id) {
        return racunRepository.findById(id);
    }

    @Override
    public Racun saveRacun(Racun racun) {
        return racunRepository.save(racun);
    }

    @Override
    public void deleteRacun(Integer id) {
        racunRepository.deleteById(id);
    }
}