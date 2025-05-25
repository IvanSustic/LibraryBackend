package org.library.service;
import lombok.RequiredArgsConstructor;
import org.library.dto.RezervacijaDTO;
import org.library.model.Knjiga;
import org.library.model.Knjiznica;
import org.library.model.Rezervacija;
import org.library.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RezervacijaServiceImpl implements RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final KorisnikRepository korisnikRepository;
    private final KnjiznicaRepository knjiznicaRepository;

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

        if (getRezervacijeForKorisnik(rezervacija.getKorisnikEmail()).size() >= 3
        || (knjiznicaRepository.findKolicina(rezervacija.getIdKnjiga(), rezervacija.getIdKnjiznica())<=0)) {
            throw new IllegalAccessException("Too many reservations already.");
        } else{
            if (knjiznicaRepository.updateKolicina(rezervacija.getIdKnjiga(),rezervacija.getIdKnjiznica())==1){
                return mapToDTO(rezervacijaRepository.save(mapToRezervacija(rezervacija)));
            } else{
                throw new IllegalAccessException("No books available");
            }

        }
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
                .build();
    }

}
