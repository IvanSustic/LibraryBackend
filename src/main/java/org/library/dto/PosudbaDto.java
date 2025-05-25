package org.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PosudbaDto {
    private Integer idPosudba;
    private Integer idKorisnik;
    private Integer idKnjiga;
    private Integer idKnjiznica;
    private String korisnikEmail;
    private String nazivKnjiznice;
    private String nazivKnjige;
    private LocalDate datumPosudbe;
    private LocalDate krajPosudbe;
}
