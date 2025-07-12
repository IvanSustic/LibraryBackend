package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.dto.KnjigaDto;
import org.library.model.Knjiga;
import org.library.repository.KnjigaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KnjigaServiceImpl implements KnjigaService {

    private final KnjigaRepository knjigaRepository;

    @Override
    public List<KnjigaDto> getAllKnjige() {
        return knjigaRepository.findAll().stream().map(this::mapToKnjigaDto).toList();
    }

    @Override
    public Optional<KnjigaDto> getKnjigaById(Integer id) {
        return knjigaRepository.findById(id).map(this::mapToKnjigaDto);
    }

    @Override
    public Knjiga saveKnjiga(KnjigaDto dto) {
        return knjigaRepository.save(mapToKnjiga(dto));
    }

    @Override
    public void deleteKnjiga(Integer id) throws SQLException {
        if (knjigaRepository.findById(id).isEmpty()){
            throw new SQLException("Knjiga ne postoji.");
        }
        try {
            knjigaRepository.deleteById(id);
        } catch (Exception e){
            throw new SQLIntegrityConstraintViolationException("Knjiga je povezana s knjižnicom i ne može biti obrisana.");
        }
    }

    private Knjiga mapToKnjiga(KnjigaDto dto){
        return Knjiga.builder().idKnjiga(dto.getIdKnjiga())
                .cijena(dto.getCijena())
                .datumIzdanja(dto.getDatumIzdanja())
                .slika(dto.getSlika())
                .zanr(dto.getZanr())
                .tipKnjige(dto.getTipKnjige())
                .autori(dto.getAutori())
                .naslov(dto.getNaslov())
                .build();
    }

    private KnjigaDto mapToKnjigaDto(Knjiga knjiga){
        return new KnjigaDto(knjiga.getIdKnjiga(),knjiga.getNaslov(),knjiga.getDatumIzdanja(),knjiga.getSlika(),knjiga.getZanr(),knjiga.getTipKnjige(), knjiga.getCijena(),knjiga.getAutori());
    }
}