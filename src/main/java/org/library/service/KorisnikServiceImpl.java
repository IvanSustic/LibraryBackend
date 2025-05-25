package org.library.service;


import lombok.RequiredArgsConstructor;
import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KorisnikDto;
import org.library.model.*;
import org.library.repository.AutorRepository;
import org.library.repository.KnjigaRepository;
import org.library.repository.KorisnikRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class KorisnikServiceImpl implements KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KnjigaRepository knjigaRepository;

    @Override
    public List<Korisnik> getAllKorisnici() {
        return korisnikRepository.findAll();
    }

    @Override
    public Optional<Korisnik> getKorisnikById(Integer id) {
        return korisnikRepository.findById(id);
    }

    @Override
    public Korisnik saveKorisnik(KorisnikDto korisnik) {
        return korisnikRepository.save(korisnikDtoToKorisnik(korisnik));
    }

    @Override
    public void deleteKorisnik(Integer id) {
        korisnikRepository.deleteById(id);
    }

    @Override
    public Optional<Korisnik> findByEmail(String email) {
        return korisnikRepository.findByEmail(email);
    }

    Korisnik korisnikDtoToKorisnik(KorisnikDto korisnikDto){
        return Korisnik.builder().email(korisnikDto.getEmail())
                .prezime(korisnikDto.getPrezime())
                .ime(korisnikDto.getIme())
                .lozinka(korisnikDto.getLozinka()).build();
    }

    public List<DozvoljenaKnjigaDto> findKnjigeByKorisnik(String email) {
        List<Object[]> rows =   korisnikRepository.findKnjigeByKorisik(email);
        return rows.stream().map(row -> {
           Set<Autor> autors = knjigaRepository.findById(((Number) row[0]).intValue()).get().getAutori();

            return DozvoljenaKnjigaDto.builder()
                    .idKnjiga(((Number) row[0]).longValue())
                    .naslov((String) row[1])
                    .datumIzdanja((Date) row[2])
                    .slika((String) row[3])
                    .nazivKnjiznice((String) row[4])
                    .kolicina(((Number) row[5]).intValue())
                    .zanr((String) row[6])
                    .tipKnjige((String) row[7])
                    .idKnjiznica(((Number) row[8]).longValue())
                    .autori(autors)
                    .build();
        }).toList();
    }
}
