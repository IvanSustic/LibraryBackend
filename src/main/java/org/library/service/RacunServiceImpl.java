package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.dto.RacunDto;
import org.library.model.Racun;
import org.library.repository.RacunRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RacunServiceImpl implements RacunService {

    private final RacunRepository racunRepository;

    @Override
    public List<RacunDto> getAllRacuni() {
        return racunRepository.findAll().stream().map(RacunServiceImpl::mapRacunToDto).toList();
    }

    @Override
    public Optional<RacunDto> getRacunById(Integer id) {
        return Optional.ofNullable(mapRacunToDto(racunRepository.findById(id).get()));
    }

    @Override
    public Racun saveRacun(Racun racun) {
        return racunRepository.save(racun);
    }

    @Override
    public void deleteRacun(Integer id) {
        racunRepository.deleteById(id);
    }

    protected static RacunDto mapRacunToDto(Racun racun){
        return RacunDto.builder()
                .prezimeZaposlenika(racun.getZaposlenik().getPrezime())
                .imeZaposlenik(racun.getZaposlenik().getIme())
                .imeKorisnika(racun.getKorisnik().getIme())
                .prezimeKorisnika(racun.getKorisnik().getPrezime())
                .opis(racun.getOpis())
                .datum(racun.getDatum())
                .tipRacuna(racun.getTipRacuna().getNaziv())
                .cijena(racun.getCijena())
                .build();
    }
}