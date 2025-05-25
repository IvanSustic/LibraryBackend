package org.library.model;

import jakarta.persistence.*;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "posudbe")
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posudba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPosudba")
    @EqualsAndHashCode.Include
    private Integer idPosudba;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKorisnik", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKnjiga", nullable = false)
    private Knjiga knjiga;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKnjiznica", nullable = false)
    private Knjiznica knjiznica;

    @Column(name = "datumPosudbe", nullable = false)
    private LocalDate datumPosudbe;

    @Column(name = "krajPosudbe")
    private LocalDate krajPosudbe;

}