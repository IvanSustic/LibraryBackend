package org.library.service;

import org.library.dto.DozvoljenaKnjigaDto;
import org.library.dto.ZaposlenikDto;
import org.library.model.Knjiznica;
import org.library.model.Zaposlenik;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ZaposlenikService {
    List<ZaposlenikDto> getAllZaposlenici();
    List<ZaposlenikDto> getKnjiznicariForVoditelj(String email);
    Optional<Zaposlenik> getZaposlenikById(Integer id);
    Zaposlenik saveZaposlenik(Zaposlenik zaposlenik) throws SQLException;

    Zaposlenik disableZaposlenik(Zaposlenik zaposlenik) throws SQLException;

    void deleteZaposlenik(Integer id);
    Optional<Zaposlenik> findByEmail(String email);

    boolean checkRefreshToken(String refreshToken);

    void saveRefreshToken(String refreshToken, LocalDate refreshTokenDatum, String email);


    List<DozvoljenaKnjigaDto> findKnjigeByZaposlenik(String email);
}