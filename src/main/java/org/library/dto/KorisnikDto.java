package org.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KorisnikDto {
    private Integer idKorisnik;
    private String email;
    private String lozinka;
    private String ime;
    private String prezime;
    private Boolean iskljucen = false;
}
