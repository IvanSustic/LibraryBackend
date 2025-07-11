package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "racun")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Racun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRacun")
    @EqualsAndHashCode.Include
    private Integer idRacun;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKorisnik", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idZaposlenik")
    private Zaposlenik zaposlenik;

    @Column(name = "cijena", precision = 10, scale = 2)
    private BigDecimal cijena;

    @Column(name = "opis")
    private String opis;

    @Column(name = "datum")
    private LocalDate datum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTipRacuna", nullable = false)
    private TipRacuna tipRacuna;
}