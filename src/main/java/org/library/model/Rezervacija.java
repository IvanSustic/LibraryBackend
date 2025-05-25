package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "rezervacije")
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rezervacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRezervacija")
    @EqualsAndHashCode.Include
    private Integer idRezervacija;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idKorisnik", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idKnjiga", nullable = false)
    private Knjiga knjiga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idKnjiznica", nullable = false)
    private Knjiznica knjiznica;

    @Column(name = "datumRezervacije", nullable = false)
    private LocalDate datumRezervacije;

    @Column(name = "krajRezervacije")
    private LocalDate krajRezervacije;
}
