package org.library.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.model.Knjiznica;
import org.library.model.Korisnik;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClanstvoDto {
    private Integer idKnjiznica;
    private Integer idKorisnik;
    private String nazivKnjiznice;
    private String email;
    private LocalDate datumUclanjenja;
    private LocalDate krajUclanjenja;
}
