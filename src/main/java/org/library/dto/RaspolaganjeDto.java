package org.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.model.Autor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaspolaganjeDto {
    private Integer idKnjiga;
    private Integer idKnjiznica;
    private Integer kolicina;
}
