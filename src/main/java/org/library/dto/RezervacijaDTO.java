package org.library.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.model.Knjiga;
import org.library.model.Knjiznica;
import org.library.model.Korisnik;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RezervacijaDTO {
    private Integer idRezervacija;
    private Integer idKnjiznica;
    private Integer idKnjiga;
    private Integer idKorisnik;
    private String korisnikEmail;
    private String nazivKnjiznice;
    private String nazivKnjige;
    private LocalDate datumRezervacije;
    private LocalDate krajRezervacije;
}
