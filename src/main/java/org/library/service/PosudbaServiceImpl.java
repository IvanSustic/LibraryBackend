package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.dto.PosudbaDto;
import org.library.model.Knjiga;
import org.library.model.Knjiznica;
import org.library.model.Posudba;
import org.library.repository.KnjiznicaRepository;
import org.library.repository.KorisnikRepository;
import org.library.repository.PosudbaRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PosudbaServiceImpl implements PosudbaService {


    private final PosudbaRepository posudbaRepository;
    private final KorisnikRepository korisnikRepository;
    private final KnjiznicaRepository knjiznicaRepository;

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
        if (getPosudbeForKorisnik(posudba.getKorisnikEmail()).size() >= 3
                || (knjiznicaRepository.findKolicina(posudba.getIdKnjiga(), posudba.getIdKnjiznica())<=0)) {
            throw new IllegalAccessException("Too many reservations already.");
        } else{
            if (knjiznicaRepository.updateKolicina(posudba.getIdKnjiga(),posudba.getIdKnjiznica())==1){
                return mapToDTO(posudbaRepository.save(mapToRezervacija(posudba)));
            } else{
                throw new IllegalAccessException("No books available");
            }

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
    public void deletePosudba(Integer id) throws IllegalAccessException {

        Optional<Posudba> posudba = posudbaRepository.findById(id);
        if (posudba.isPresent()){
            knjiznicaRepository.addKolicina(posudba.get().getKnjiga().getIdKnjiga(),
                    posudba.get().getKnjiznica().getIdKnjiznica());
            posudbaRepository.deleteById(id);
        } else {
            throw new IllegalAccessException("Rezervacija nije nađena");
        }
    }

    @Override
    public void deletePosudbaBezDodavanja(Integer id) throws IllegalAccessException {

        Optional<Posudba> posudba = posudbaRepository.findById(id);
        if (posudba.isPresent()){
            posudbaRepository.deleteById(id);
        } else {
            throw new IllegalAccessException("Rezervacija nije nađena");
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