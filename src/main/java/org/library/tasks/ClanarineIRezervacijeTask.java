package org.library.tasks;

import lombok.AllArgsConstructor;
import org.library.repository.ClanstvoRepository;
import org.library.repository.RezervacijaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClanarineIRezervacijeTask {
    private final RezervacijaRepository rezervacijaRepository;
    private final ClanstvoRepository clanstvoRepository;
    public ClanarineIRezervacijeTask(RezervacijaRepository rezervacijaRepository,
                                     ClanstvoRepository clanstvoRepository) {
        this.rezervacijaRepository = rezervacijaRepository;
        this.clanstvoRepository = clanstvoRepository;
    }
    @Scheduled(cron = "0 0 2 * * *")
    public void runJob() {
        rezervacijaRepository.deleteIstekleRezervacije();
        rezervacijaRepository.deleteRezervacijeFromIstekliClanovi();
        clanstvoRepository.deleteIsteklaClanstvaWithoutPosudbe();
    }   
}
