package org.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.model.Knjiznica;
import org.library.model.RadnoMjesto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZaposlenikDto {
    private Integer idZaposlenik;
    private String lozinka;
    private String email;
    private String ime;
    private String prezime;
    private Set<Knjiznica> knjiznice;
    private Set<RadnoMjesto> radnaMjesta;
    private Boolean iskljucen = false;
}
