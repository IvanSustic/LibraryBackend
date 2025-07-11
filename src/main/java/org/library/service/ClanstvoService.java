package org.library.service;
import org.library.dto.ClanstvoDto;
import org.library.dto.RacunDto;
import org.library.model.Clanstvo;

import java.util.List;
import java.util.Optional;

public interface ClanstvoService {
    List<ClanstvoDto> getAllClanstva();
    List<ClanstvoDto> getClanstvoByKorisnik(String email);
    List<ClanstvoDto> getClanstvaForZaposlenik(String email);
    Optional<Clanstvo> getClanstvoById(Integer id);
    Clanstvo saveClanstvo(Clanstvo clanstvo);

    RacunDto  dodajClanstvo(String email, Integer idKnjiznica, String zaposlenikEmail);

    RacunDto dodajSvaClanstva(String email, String zaposlenikEmail);
    void deleteClanstvo(Integer id);
}