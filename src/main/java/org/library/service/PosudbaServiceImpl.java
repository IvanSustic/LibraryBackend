package org.library.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
public class PosudbaServiceImpl implements PosudbaService {


    private final PosudbaRepository posudbaRepository;
    private final KorisnikRepository korisnikRepository;
    private final KnjiznicaRepository knjiznicaRepository;
    private final RacunRepository racunRepository;
    private final ZaposlenikRepository zaposlenikRepository;
    private final TipRacunaRepository tipRacunaRepository;
    private final ClanstvoRepository clanstvoRepository;
    @Override
    public List<Posudba> getAllPosudbe() {
        return posudbaRepository.findAll();
    }

    @Override
    public Optional<Posudba> getPosudbaById(Integer id) {
        return posudbaRepository.findById(id);
    }

    @Override
    public PosudbaDto savePosudba(PosudbaDto posudba) throws IllegalAccessException {
        Optional<Clanstvo> clanstvo =
                clanstvoRepository.findByKnjiznicaIdKnjiznicaAndKorisnikEmail(
                        posudba.getIdKnjiznica(),posudba.getKorisnikEmail());
        if (clanstvo.isPresent()) {
            if (clanstvo.get().getKrajUclanjenja().isBefore(LocalDate.now())) {
                throw new IllegalAccessException("Korisnikovo članstvo je isteklo.");
            }
            if (getPosudbeForKorisnik(posudba.getKorisnikEmail()).size() >= 3
                    || (knjiznicaRepository.findKolicina(posudba.getIdKnjiga(), posudba.getIdKnjiznica()) <= 0)) {
                throw new IllegalAccessException("Previše posudba.");
            } else {
                if (knjiznicaRepository.subtractKolicina(posudba.getIdKnjiga(), posudba.getIdKnjiznica()) == 1) {
                    return mapToDTO(posudbaRepository.save(mapToRezervacija(posudba)));
                } else {
                    throw new IllegalAccessException("Nema trenutno dostupnih knjiga");
                }

            }
        }else {
            throw new IllegalAccessException("Korisnik nema članstva.");
        }
    }



    @Override
    public List<PosudbaDto> getPosudbeForKorisnik(String email) {
        return posudbaRepository.findByKorisnikEmail(email).stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<PosudbaDto> getPosudbeForZaposlenik(String email) {
        return posudbaRepository.findPosudbeByZaposlenikEmail(email).stream().map(this::objectToDto).toList();
    }


    @Override
    public Optional<RacunDto> deletePosudba(Integer id, String email) throws IllegalAccessException {
        Racun racun = null;
        Optional<Posudba> posudba = posudbaRepository.findById(id);
        if (posudba.isPresent()){
            knjiznicaRepository.addKolicina(posudba.get().getKnjiga().getIdKnjiga(),
                    posudba.get().getKnjiznica().getIdKnjiznica());
            if (LocalDate.now().isAfter(posudba.get().getKrajPosudbe())){
                Zaposlenik zaposlenik = zaposlenikRepository.findByEmail(email).get();
                racun = Racun.builder()
                        .datum(LocalDate.now())
                        .cijena(BigDecimal.valueOf(ChronoUnit.DAYS.between(posudba.get().getKrajPosudbe(), LocalDate.now()) * 0.10))
                        .opis("Zakasnina za knjigu " + posudba.get().getKnjiga().getNaslov() + ".")
                        .tipRacuna(tipRacunaRepository.findById(2).get())
                        .korisnik(posudba.get().getKorisnik())
                        .zaposlenik(zaposlenik)
                        .build();
                racunRepository.save(racun);
            }
            posudbaRepository.deleteById(id);

            if (racun != null){
                return Optional.of(RacunServiceImpl.mapRacunToDto(racun));
            } else {
                return Optional.empty();
            }

        } else {
            throw new IllegalAccessException("Posudba nije nađena");
        }
    }

    @Override
    public Optional<RacunDto> ostecenaPosudba(Integer id, String email) throws IllegalAccessException {
        Racun racun = null;
        Optional<Posudba> posudba = posudbaRepository.findById(id);
        if (posudba.isPresent()){
                Zaposlenik zaposlenik = zaposlenikRepository.findByEmail(email).get();
                racun = Racun.builder()
                        .datum(LocalDate.now())
                        .cijena(posudba.get().getKnjiga().getCijena())
                        .opis("Knjiga " + posudba.get().getKnjiga().getNaslov() + " je oštećena ili izgubljena.")
                        .tipRacuna(tipRacunaRepository.findById(3).get())
                        .korisnik(posudba.get().getKorisnik())
                        .zaposlenik(zaposlenik)
                        .build();
            racunRepository.save(racun);
            posudbaRepository.deleteById(id);

            return Optional.of(RacunServiceImpl.mapRacunToDto(racun));
        } else {
            throw new IllegalAccessException("Posudba nije nađena");
        }
    }

    @Override
    public void deletePosudbaBezDodavanja(Integer id) throws IllegalAccessException {

        Optional<Posudba> posudba = posudbaRepository.findById(id);
        if (posudba.isPresent()){
            posudbaRepository.deleteById(id);
        } else {
            throw new IllegalAccessException("Posudba nije nađena");
        }
    }

    private Posudba mapToRezervacija(PosudbaDto dto){
        return Posudba.builder()
                .datumPosudbe(LocalDate.now())
                .krajPosudbe(LocalDate.now().plusWeeks(2))
                .knjiznica(Knjiznica.builder().idKnjiznica(dto.getIdKnjiznica()).build())
                .korisnik(korisnikRepository.findByEmail(dto.getKorisnikEmail()).get())
                .knjiga(Knjiga.builder().idKnjiga(dto.getIdKnjiga()).build())
                .build();
    }

    private PosudbaDto mapToDTO(Posudba posudba){
        return PosudbaDto.builder()
                .datumPosudbe(posudba.getDatumPosudbe())
                .krajPosudbe(posudba.getKrajPosudbe())
                .korisnikEmail(posudba.getKorisnik().getEmail())
                .idKnjiga(posudba.getKnjiga().getIdKnjiga())
                .idKnjiznica(posudba.getKnjiznica().getIdKnjiznica())
                .nazivKnjige(posudba.getKnjiga().getNaslov())
                .nazivKnjiznice(posudba.getKnjiznica().getNaziv())
                .idPosudba(posudba.getIdPosudba())
                .build();
    }

    private PosudbaDto objectToDto(Object[] posudba){
        return PosudbaDto.builder()
                .idPosudba(((Number) posudba[0]).intValue())
                .idKorisnik(((Number) posudba[1]).intValue())
                .idKnjiznica(((Number) posudba[2]).intValue())
                .idKnjiga(((Number) posudba[3]).intValue())
                .datumPosudbe(convertToLocalDateViaMilisecond((Date) posudba[4]))
                .krajPosudbe(convertToLocalDateViaMilisecond((Date) posudba[5]))
                .nazivKnjige((String) posudba[6])
                .nazivKnjiznice((String) posudba[7])
                .korisnikEmail((String) posudba[8])
                .build();
    }

    public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}