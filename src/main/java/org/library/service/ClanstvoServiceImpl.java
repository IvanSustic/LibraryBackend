package org.library.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.library.dto.ClanstvoDto;
import org.library.dto.PosudbaDto;
import org.library.dto.RacunDto;
import org.library.model.*;
import org.library.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClanstvoServiceImpl implements ClanstvoService {

    private final ClanstvoRepository clanstvoRepository;
    private final KorisnikRepository korisnikRepository;
    private final ZaposlenikRepository zaposlenikRepository;
    private final TipRacunaRepository tipRacunaRepository;
    private final RacunRepository racunRepository;
    private final KnjiznicaRepository knjiznicaRepository;

    @Override
    public List<ClanstvoDto> getAllClanstva() {
        return clanstvoRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public List<ClanstvoDto> getClanstvoByKorisnik(String email) {
        return clanstvoRepository.findAllByKorisnikEmail(email).stream().map(this::mapToDto).toList();
    }

    @Override
    public List<ClanstvoDto> getClanstvaForZaposlenik(String email) {

        return clanstvoRepository.findAllByZapEmail(email)
                .stream().map(this::objectToDto).toList();
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
    @Transactional
    public RacunDto dodajClanstvo(String email, Integer idKnjiznica, String zaposlenikEmail) {
        Korisnik korisnik = korisnikRepository.findByEmail(email).get();
        Zaposlenik zaposlenik = zaposlenikRepository.findByEmail(zaposlenikEmail).get();
        Racun racun;
        Optional<Clanstvo> clanstvo = clanstvoRepository.findByKnjiznicaIdKnjiznicaAndKorisnikEmail(idKnjiznica,email);
        Clanstvo novoClanstvo= Clanstvo.builder()
                .korisnik(korisnik)
                .knjiznica(Knjiznica.builder().idKnjiznica(idKnjiznica).build())
                .datumUclanjenja(LocalDate.now())
                .krajUclanjenja(LocalDate.now().plusYears(1))
                .build();

        clanstvo.ifPresent(value -> {
            if (value.getKrajUclanjenja().isAfter(LocalDate.now())) {
                novoClanstvo.setKrajUclanjenja(value.getKrajUclanjenja().plusYears(1));
            }
        });

        clanstvoRepository.save(
                    novoClanstvo);
            racun = Racun.builder()
                    .datum(LocalDate.now())
                    .cijena(BigDecimal.valueOf(5))
                    .opis("Pojedinačna članarina za korisnika "+email+".")
                    .tipRacuna(tipRacunaRepository.findById(1).get())
                    .korisnik(korisnik)
                    .zaposlenik(zaposlenik)
                    .build();
            racunRepository.save(racun);
            return RacunServiceImpl.mapRacunToDto(racun);

    }

    @Override
    @Transactional
    public RacunDto dodajSvaClanstva(String email, String zaposlenikEmail) {
        Korisnik korisnik = korisnikRepository.findByEmail(email).get();
        Zaposlenik zaposlenik = zaposlenikRepository.findByEmail(zaposlenikEmail).get();
        Racun racun;
        List<Integer> knjizniceIds= knjiznicaRepository.getAllIds();
        for (Integer knjizniceId : knjizniceIds) {
            clanstvoRepository.save(Clanstvo.builder()
                    .korisnik(korisnik)
                    .knjiznica(Knjiznica.builder().idKnjiznica(knjizniceId).build())
                    .datumUclanjenja(LocalDate.now())
                    .krajUclanjenja(LocalDate.now().plusYears(1))
                    .build());

        }
        racun = Racun.builder()
                    .datum(LocalDate.now())
                    .cijena(BigDecimal.valueOf(10))
                    .opis("Članarina za korisnika " + email + " za sve knjižnice.")
                    .tipRacuna(tipRacunaRepository.findById(4).get())
                    .korisnik(korisnik)
                    .zaposlenik(zaposlenik)
                    .build();
        racunRepository.save(racun);
        return RacunServiceImpl.mapRacunToDto(racun);

    }

    @Override
    public void deleteClanstvo(Integer id) {
        clanstvoRepository.deleteById(id);
    }

    private ClanstvoDto mapToDto(Clanstvo clanstvo){
        return ClanstvoDto.builder()
                .datumUclanjenja(clanstvo.getDatumUclanjenja())
                .krajUclanjenja(clanstvo.getKrajUclanjenja())
                .nazivKnjiznice(clanstvo.getKnjiznica().getNaziv())
                .email(clanstvo.getKorisnik().getEmail())
                .idKnjiznica(clanstvo.getKnjiznica().getIdKnjiznica())
                .idKorisnik(clanstvo.getKorisnik().getIdKorisnik())
                .build();
    }

    private ClanstvoDto objectToDto(Object[] clanstvo){
        return ClanstvoDto.builder()
                .idKorisnik(((Number) clanstvo[0]).intValue())
                .idKnjiznica(((Number) clanstvo[1]).intValue())
                .datumUclanjenja(convertToLocalDateViaMilisecond((Date) clanstvo[2]))
                .krajUclanjenja(convertToLocalDateViaMilisecond((Date) clanstvo[3]))
                .nazivKnjiznice((String) clanstvo[4])
                .email((String) clanstvo[5])
                .build();
    }

    private LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}