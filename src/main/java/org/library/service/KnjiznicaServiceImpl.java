package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.dto.KnjiznicaDto;
import org.library.dto.RaspolaganjeDto;
import org.library.model.Clanstvo;
import org.library.model.Knjiznica;
import org.library.model.Korisnik;
import org.library.repository.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KnjiznicaServiceImpl implements KnjiznicaService {

    private final KnjiznicaRepository knjiznicaRepository;
    private final PosudbaRepository posudbaRepository;
    private final RezervacijaRepository rezervacijaRepository;
    private final RacunRepository racunRepository;
    private final ClanstvoRepository clanstvoRepository;

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
        Knjiznica knjiznica1 = knjiznicaRepository.save(mapToKnjiznica(knjiznica));
        if (knjiznica.getIdKnjiznica()!= null){
            racunRepository.findAllByTipRacunaIdTipRacunaAndDatumAfter(4,LocalDate.now().minusYears(1)).forEach(racun -> {
                clanstvoRepository.save(Clanstvo.builder()
                        .datumUclanjenja(racun.getDatum())
                        .krajUclanjenja(racun.getDatum().plusYears(1))
                        .knjiznica(Knjiznica.builder().idKnjiznica(knjiznica1.getIdKnjiznica()).build())
                        .korisnik(Korisnik.builder().idKorisnik(racun.getKorisnik().getIdKorisnik()).build())
                        .build());
            });
        }
        return knjiznica1;
    }

    @Override
    public void deleteKnjiznica(Integer id) throws SQLException {
        if (knjiznicaRepository.findById(id).isEmpty()){
            throw new SQLException("Knjiznica ne postoji.");
        }
        try {
            knjiznicaRepository.deleteById(id);
        } catch (Exception e){
            throw new SQLIntegrityConstraintViolationException("Knjiznica je povezana s ostalim članovima i ne može " +
                    "biti obrisana.");
        }
    }

    @Override
    public List<KnjiznicaDto> findKnjizniceForZaposlenik(String email) {
        return knjiznicaRepository.findForZaposlenik(email).stream().map(this::mapToKnjiznicaDto).toList();
    }

    @Override
    public int insertRaspolaganje(RaspolaganjeDto raspolaganjeDto) throws SQLException {
        if (knjiznicaRepository.getRaspolaganje(raspolaganjeDto.getIdKnjiga(),raspolaganjeDto.getIdKnjiznica())!=null){
            throw new SQLException("Već postoji ovo raspolaganje");
        }
        return knjiznicaRepository.insertRaspolaganje(raspolaganjeDto.getIdKnjiga(),raspolaganjeDto.getIdKnjiznica(),
                raspolaganjeDto.getKolicina());
    }

    @Override
    public int updateRaspolaganje(RaspolaganjeDto raspolaganjeDto) throws SQLException {
        if (knjiznicaRepository.getRaspolaganje(raspolaganjeDto.getIdKnjiga(),raspolaganjeDto.getIdKnjiznica())==null){
            throw new SQLException("Ne postoji ovo raspolaganje");
        }
        return knjiznicaRepository.updateRaspolaganje(raspolaganjeDto.getIdKnjiga(),raspolaganjeDto.getIdKnjiznica(),
                raspolaganjeDto.getKolicina());
    }

    @Override
    public int deleteRaspolaganje(RaspolaganjeDto raspolaganjeDto) throws SQLException {
        if (!posudbaRepository.findAllByKnjigaIdKnjigaAndKnjiznicaIdKnjiznica(raspolaganjeDto.getIdKnjiga(),raspolaganjeDto.getIdKnjiznica()).isEmpty()
        || !rezervacijaRepository.findAllByKnjigaIdKnjigaAndKnjiznicaIdKnjiznica(raspolaganjeDto.getIdKnjiga(),raspolaganjeDto.getIdKnjiznica()).isEmpty()){
            throw new SQLException("Nemoguće obrisati raspolaganje jer trenutno postoje knjige koje su rezervirane ili posuđene.");
        }
        return knjiznicaRepository.deleteRaspolaganje(raspolaganjeDto.getIdKnjiga(),raspolaganjeDto.getIdKnjiznica());
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