package org.library.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.library.dto.PosudbaDto;
import org.library.dto.RezervacijaDTO;
import org.library.model.Clanstvo;
import org.library.model.Knjiga;
import org.library.model.Knjiznica;
import org.library.model.Rezervacija;
import org.library.repository.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RezervacijaServiceImpl implements RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final PosudbaRepository posudbaRepository;
    private final KorisnikRepository korisnikRepository;
    private final KnjiznicaRepository knjiznicaRepository;
    private final ClanstvoRepository clanstvoRepository;
    private final PosudbaService posudbaService;
    @Override
    public List<Rezervacija> getAllRezervacije() {
        return rezervacijaRepository.findAll();
    }

    @Override
    public Optional<Rezervacija> getRezervacijaById(Integer id) {
        return rezervacijaRepository.findById(id);
    }

    @Override
    public RezervacijaDTO saveRezervacija(RezervacijaDTO rezervacija) throws IllegalAccessException {
        Optional<Clanstvo> clanstvo =
                clanstvoRepository.findByKnjiznicaIdKnjiznicaAndKorisnikEmail(
                        rezervacija.getIdKnjiznica(),rezervacija.getKorisnikEmail());
        if (clanstvo.isPresent()){
            if (clanstvo.get().getKrajUclanjenja().isBefore(LocalDate.now())){
                throw new IllegalAccessException("Korisnikovo članstvo je isteklo.");
            }

        if (getRezervacijeForKorisnik(rezervacija.getKorisnikEmail()).size() >= 3
        || (knjiznicaRepository.findKolicina(rezervacija.getIdKnjiga(), rezervacija.getIdKnjiznica())<=0)) {
            throw new IllegalAccessException("Previše rezervacija.");
        } else{
            if (knjiznicaRepository.subtractKolicina(rezervacija.getIdKnjiga(),rezervacija.getIdKnjiznica())==1){
                return mapToDTO(rezervacijaRepository.save(mapToRezervacija(rezervacija)));
            } else{
                throw new IllegalAccessException("Nema trenutno dostupnih knjiga");
            }

        }
        } else {
            throw new IllegalAccessException("Korisnik nema članstva.");
        }
    }

    @Override
    @Transactional(rollbackOn = IllegalAccessException.class)
    public PosudbaDto saveRezerviranaPosudba(RezervacijaDTO rezervacijaDTO) throws IllegalAccessException {
        deleteRezervacija(rezervacijaDTO.getIdRezervacija());
        return posudbaService.savePosudba( PosudbaDto.builder()
                .idKnjiznica(rezervacijaDTO.getIdKnjiznica())
                .korisnikEmail(rezervacijaDTO.getKorisnikEmail())
                .nazivKnjiznice(rezervacijaDTO.getNazivKnjiznice())
                .nazivKnjige(rezervacijaDTO.getNazivKnjige())
                .krajPosudbe(LocalDate.now().plusWeeks(2))
                .datumPosudbe(LocalDate.now())
                .idKnjiga(rezervacijaDTO.getIdKnjiga())
                .idKorisnik(rezervacijaDTO.getIdKorisnik())
                .build());

    }

    @Override
    public void deleteRezervacija(Integer id) throws IllegalAccessException {
        Optional<Rezervacija> rezervacija = rezervacijaRepository.findById(id);
        if (rezervacija.isPresent()){
            knjiznicaRepository.addKolicina(rezervacija.get().getKnjiga().getIdKnjiga(),
                    rezervacija.get().getKnjiznica().getIdKnjiznica());
            rezervacijaRepository.deleteById(id);
        } else {
            throw new IllegalAccessException("Posudba nije nađena");
        }
    }



    @Override
    public List<RezervacijaDTO> getRezervacijeForZaposlenik(String email) {
        return rezervacijaRepository.findPosudbeByZaposlenikEmail(email).stream().map(this::objectToDto).toList();
    }

    @Override
    public List<RezervacijaDTO> getRezervacijeForKorisnik(String email) {
        return rezervacijaRepository.findByKorisnikEmail(email).stream().map(this::mapToDTO).toList();
    }

    private Rezervacija mapToRezervacija(RezervacijaDTO dto){
        return Rezervacija.builder()
                .datumRezervacije(LocalDate.now())
                .krajRezervacije(LocalDate.now().plusWeeks(2))
                .knjiznica(Knjiznica.builder().idKnjiznica(dto.getIdKnjiznica()).build())
                .korisnik(korisnikRepository.findByEmail(dto.getKorisnikEmail()).get())
                .knjiga(Knjiga.builder().idKnjiga(dto.getIdKnjiga()).build())
                .build();
    }

    private RezervacijaDTO mapToDTO(Rezervacija rezervacija){
        return RezervacijaDTO.builder()
                .datumRezervacije(rezervacija.getDatumRezervacije())
                .krajRezervacije(rezervacija.getKrajRezervacije())
                .korisnikEmail(rezervacija.getKorisnik().getEmail())
                .idKnjiga(rezervacija.getKnjiga().getIdKnjiga())
                .idKnjiznica(rezervacija.getKnjiznica().getIdKnjiznica())
                .nazivKnjige(rezervacija.getKnjiga().getNaslov())
                .nazivKnjiznice(rezervacija.getKnjiznica().getNaziv())
                .idRezervacija(rezervacija.getIdRezervacija())
                .idKorisnik(rezervacija.getKorisnik().getIdKorisnik())
                .build();
    }

    private RezervacijaDTO objectToDto(Object[] posudba){
        return RezervacijaDTO.builder()
                .idRezervacija(((Number) posudba[0]).intValue())
                .idKorisnik(((Number) posudba[1]).intValue())
                .idKnjiznica(((Number) posudba[2]).intValue())
                .idKnjiga(((Number) posudba[3]).intValue())
                .datumRezervacije(convertToLocalDateViaMilisecond((Date) posudba[4]))
                .krajRezervacije(convertToLocalDateViaMilisecond((Date) posudba[5]))
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
