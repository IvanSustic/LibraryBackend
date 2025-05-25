package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Clanstvo;
import org.library.repository.ClanstvoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClanstvoServiceImpl implements ClanstvoService {

    private final ClanstvoRepository clanstvoRepository;

    @Override
    public List<Clanstvo> getAllClanstva() {
        return clanstvoRepository.findAll();
    }

    @Override
    public Optional<Clanstvo> getClanstvoById(Integer id) {
        return clanstvoRepository.findById(id);
    }

    @Override
    public Clanstvo saveClanstvo(Clanstvo clanstvo) {
        return clanstvoRepository.save(clanstvo);
    }

    @Override
    public void deleteClanstvo(Integer id) {
        clanstvoRepository.deleteById(id);
    }
}