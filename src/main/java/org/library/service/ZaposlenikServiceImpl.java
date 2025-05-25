package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Knjiznica;
import org.library.model.RadnoMjesto;
import org.library.model.Zaposlenik;
import org.library.repository.ZaposlenikRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZaposlenikServiceImpl implements ZaposlenikService {

    private final ZaposlenikRepository zaposlenikRepository;

    @Override
    public List<Zaposlenik> getAllZaposlenici() {
        return zaposlenikRepository.findAll();
    }

    @Override
    public Optional<Zaposlenik> getZaposlenikById(Integer id) {
        return zaposlenikRepository.findById(id);
    }

    @Override
    public Zaposlenik saveZaposlenik(Zaposlenik zaposlenik) {
        return zaposlenikRepository.save(zaposlenik);
    }

    @Override
    public void deleteZaposlenik(Integer id) {
        zaposlenikRepository.deleteById(id);
    }

    @Override
    public Optional<Zaposlenik> findByEmail(String email) {
        return zaposlenikRepository.findByEmail(email);
    }

}
