package org.library.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.model.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnjiznicaDto {
    private Integer idKnjiznica;

    private String naziv;

    private String adresa;

    private String slika;

    private Mjesto mjesto;

}
