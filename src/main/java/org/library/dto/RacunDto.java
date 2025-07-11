package org.library.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.model.TipRacuna;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RacunDto {
    private String imeZaposlenik;
    private String prezimeZaposlenika;
    private String imeKorisnika;
    private String prezimeKorisnika;
    private BigDecimal cijena;
    private String opis;
    private LocalDate datum;
    private String tipRacuna;
}
