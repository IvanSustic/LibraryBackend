package org.library.service;
import org.library.model.Clanstvo;

import java.util.List;
import java.util.Optional;

public interface ClanstvoService {
    List<Clanstvo> getAllClanstva();
    Optional<Clanstvo> getClanstvoById(Integer id);
    Clanstvo saveClanstvo(Clanstvo clanstvo);
    void deleteClanstvo(Integer id);
}