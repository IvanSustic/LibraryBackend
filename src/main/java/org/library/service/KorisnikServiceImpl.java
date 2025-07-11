package org.library.service;


import lombok.RequiredArgsConstructor;
import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.KorisnikDto;
import org.library.model.*;
import org.library.repository.KnjigaRepository;
import org.library.repository.KorisnikRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
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
    public List<KorisnikDto> getAllKorisnici() {
        return korisnikRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public Optional<Korisnik> getKorisnikById(Integer id) {
        return korisnikRepository.findById(id);
    }

    @Override
    public Korisnik saveKorisnik(KorisnikDto korisnik) throws SQLException {
        if (korisnikRepository.findByEmail(korisnik.getEmail()).isPresent()){
            throw new SQLException("Korisnik već postoji");
        }
        return korisnikRepository.save(korisnikDtoToKorisnik(korisnik));
    }

    @Override
    public Korisnik saveKorisnik(Korisnik korisnik) {
        return korisnikRepository.save(korisnik);
    }

    @Override
    public void deleteKorisnik(Integer id) {
        korisnikRepository.deleteById(id);
    }

    @Override
    public Optional<Korisnik> findByEmail(String email) {
        return korisnikRepository.findByEmail(email);
    }


    @Override
    public boolean checkRefreshToken(String refreshToken) {
        Optional<Korisnik> korisnik = korisnikRepository.findByRefreshToken(refreshToken);
        return korisnik.map(value -> value.getRefreshTokenDatum().isAfter(LocalDate.now())).orElse(false);

    }

    @Override
    public void saveRefreshToken(String refreshToken, LocalDate refreshTokenDatum, String email) {
    korisnikRepository.updateRefreshToken(refreshToken,refreshTokenDatum,email);
    }


    @Override
    public Korisnik saveKorisnikAdmin(Korisnik korisnik) throws SQLException {
        Optional<Korisnik> oldZaposlenik = korisnikRepository.findByEmail(korisnik.getEmail());
        if (oldZaposlenik.isPresent()){
            if (korisnik.getIdKorisnik()== null){
                throw new SQLException("Zaposlenik s ovim emailom već postoji");
            }
            if (korisnik.getIdKorisnik().equals(oldZaposlenik.get().getIdKorisnik())){
                if (korisnik.getLozinka()==null){
                    korisnik.setLozinka(oldZaposlenik.get().getLozinka());
                }
                return korisnikRepository.save(korisnik);
            }else throw new SQLException("Došlo je do pogreške kod editiranja zaposlenika");
        } else {
            return korisnikRepository.save(korisnik);
        }
    }

    @Override
    public Korisnik disableKorisnik(Korisnik korisnik) throws SQLException {

        if (korisnikRepository.findByEmail(korisnik.getEmail()).isPresent()){
            korisnik.setLozinka("iskljucen");
            korisnik.setRefreshToken(null);
            korisnik.setRefreshTokenDatum(null);
            return korisnikRepository.save(korisnik);
        } else {
            throw new SQLException("Zaposlenik ne postoji.");
        }
    }

    Korisnik korisnikDtoToKorisnik(KorisnikDto korisnikDto){
        return Korisnik.builder().email(korisnikDto.getEmail())
                .prezime(korisnikDto.getPrezime())
                .ime(korisnikDto.getIme())
                .lozinka(korisnikDto.getLozinka()).build();
    }

    KorisnikDto mapToDto(Korisnik korisnik){

        return KorisnikDto.builder()
                .idKorisnik(korisnik.getIdKorisnik())
                .email(korisnik.getEmail())
                .ime(korisnik.getIme())
                .prezime(korisnik.getPrezime())
                .iskljucen(korisnik.getLozinka().equals("iskljucen"))
                .build();

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
