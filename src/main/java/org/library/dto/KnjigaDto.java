package org.library.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.library.model.Autor;
import org.library.model.TipKnjige;
import org.library.model.Zanr;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnjigaDto {

    private Integer idKnjiga;

    private String naslov;

    private LocalDate datumIzdanja;

    private String slika;

    private Zanr zanr;

    private TipKnjige tipKnjige;

    private Set<Autor> autori;
}
