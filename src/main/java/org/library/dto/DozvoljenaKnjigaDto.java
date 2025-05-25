package org.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.model.Autor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DozvoljenaKnjigaDto {
        private Long idKnjiga;
        private String naslov;
        private Date datumIzdanja;
        private String slika;
        private Long idKnjiznica;
        private String nazivKnjiznice;
        private Integer kolicina;
        private String tipKnjige;
        private String zanr;
        private Set<Autor> autori;

}
