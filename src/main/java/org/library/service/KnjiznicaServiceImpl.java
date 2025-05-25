package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.dto.KnjigaDto;
import org.library.dto.KnjiznicaDto;
import org.library.model.Knjiznica;
import org.library.repository.KnjiznicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KnjiznicaServiceImpl implements KnjiznicaService {

    private final KnjiznicaRepository knjiznicaRepository;

    @Override
    public List<KnjiznicaDto> getAllKnjiznice() {
        return knjiznicaRepository.findAll().stream().map(this::mapToKnjiznicaDto).toList();
    }

    @Override
    public Optional<KnjiznicaDto> getKnjiznicaById(Integer id) {
        return knjiznicaRepository.findById(id).map(this::mapToKnjiznicaDto);
    }

    @Override
    public Knjiznica saveKnjiznica(KnjiznicaDto knjiznica) {
        return knjiznicaRepository.save(mapToKnjiznica(knjiznica));
    }

    @Override
    public void deleteKnjiznica(Integer id) {
        knjiznicaRepository.deleteById(id);
    }

    private KnjiznicaDto mapToKnjiznicaDto(Knjiznica knjiznica){
        return KnjiznicaDto.builder()
                .idKnjiznica(knjiznica.getIdKnjiznica())
                .slika(knjiznica.getSlika())
                .adresa(knjiznica.getAdresa())
                .naziv(knjiznica.getNaziv())
                .mjesto(knjiznica.getMjesto())
                .build();
    }

    private Knjiznica mapToKnjiznica(KnjiznicaDto knjiznica){
        return Knjiznica.builder()
                .idKnjiznica(knjiznica.getIdKnjiznica())
                .slika(knjiznica.getSlika())
                .adresa(knjiznica.getAdresa())
                .naziv(knjiznica.getNaziv())
                .mjesto(knjiznica.getMjesto())
                .build();
    }
}