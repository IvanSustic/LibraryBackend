package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.ZaposlenikDto;
import org.library.model.*;
import org.library.repository.KnjigaRepository;
import org.library.repository.ZaposlenikRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZaposlenikServiceImpl implements ZaposlenikService {

    private final ZaposlenikRepository zaposlenikRepository;
    private final KnjigaRepository knjigaRepository;
    @Override
    public List<ZaposlenikDto> getAllZaposlenici() {
        return zaposlenikRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public List<ZaposlenikDto> getKnjiznicariForVoditelj(String email) {
        return zaposlenikRepository.findKnjiznicariForVoditelj(email).stream().map(this::mapToDto).toList();
    }

    @Override
    public Optional<Zaposlenik> getZaposlenikById(Integer id) {
        return zaposlenikRepository.findById(id);
    }

    @Override
    public Zaposlenik saveZaposlenik(Zaposlenik zaposlenik) throws SQLException {
        Optional<Zaposlenik> oldZaposlenik = zaposlenikRepository.findByEmail(zaposlenik.getEmail());
        if (oldZaposlenik.isPresent()){
            if (zaposlenik.getIdZaposlenik()== null){
                throw new SQLException("Zaposlenik s ovim emailom već postoji");
            }
           if (zaposlenik.getIdZaposlenik().equals(oldZaposlenik.get().getIdZaposlenik())){
               if (zaposlenik.getLozinka()==null){
                   zaposlenik.setLozinka(oldZaposlenik.get().getLozinka());
               }
               return zaposlenikRepository.save(zaposlenik);
           }else throw new SQLException("Došlo je do pogreške kod editiranja zaposlenika");
        } else {
            return zaposlenikRepository.save(zaposlenik);
        }
    }

    @Override
    public Zaposlenik disableZaposlenik(Zaposlenik zaposlenik) throws SQLException {

        if (zaposlenikRepository.findByEmail(zaposlenik.getEmail()).isPresent()){
            zaposlenik.setLozinka("iskljucen");
            zaposlenik.setRefreshToken(null);
            zaposlenik.setRefreshTokenDatum(null);
            return zaposlenikRepository.save(zaposlenik);
        } else {
            throw new SQLException("Zaposlenik ne postoji.");
        }
    }

    @Override
    public void deleteZaposlenik(Integer id) {
        zaposlenikRepository.deleteById(id);
    }

    @Override
    public Optional<Zaposlenik> findByEmail(String email) {
        return zaposlenikRepository.findByEmail(email);
    }

    @Override
    public boolean checkRefreshToken(String refreshToken) {
        Optional<Zaposlenik> korisnik = zaposlenikRepository.findByRefreshToken(refreshToken);
        return korisnik.map(value -> value.getRefreshTokenDatum().isAfter(LocalDate.now())).orElse(false);

    }

    @Override
    public void saveRefreshToken(String refreshToken, LocalDate refreshTokenDatum, String email) {
        zaposlenikRepository.updateRefreshToken(refreshToken,refreshTokenDatum,email);
    }

    ZaposlenikDto mapToDto(Zaposlenik zaposlenik){
        ZaposlenikDto zaposlenikDto = ZaposlenikDto.builder()
                .idZaposlenik(zaposlenik.getIdZaposlenik())
                .ime(zaposlenik.getIme())
                .email(zaposlenik.getEmail())
                .prezime(zaposlenik.getPrezime())
                .radnaMjesta(zaposlenik.getRadnaMjesta())
                .knjiznice(zaposlenik.getKnjiznice().stream().map(knjiznica -> Knjiznica.builder().idKnjiznica(knjiznica.getIdKnjiznica()).naziv(knjiznica.getNaziv()).build()).collect(Collectors.toSet()))
                .build();
        if (zaposlenik.getLozinka().equals("iskljucen")){
            zaposlenikDto.setIskljucen(true);
        }
        return zaposlenikDto;
    }

    @Override
    public List<DozvoljenaKnjigaDto> findKnjigeByZaposlenik(String email) {
        List<Object[]> rows =   zaposlenikRepository.findKnjigeByZaposlenik(email);
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
