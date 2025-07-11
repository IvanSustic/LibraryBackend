package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "knjiga")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Knjiga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idKnjiga")
    @EqualsAndHashCode.Include

    private Integer idKnjiga;

    @Column(name = "naslov", nullable = false)
    private String naslov;

    @Column(name = "datumIzdanja")
    private LocalDate datumIzdanja;

    @Column(name = "slika")
    private String slika;

    @Column(name = "cijena", precision = 10, scale = 2)
    private BigDecimal cijena;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idZanr", nullable = false)
    private Zanr zanr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTipKnjige", nullable = false)
    private TipKnjige tipKnjige;


    @OneToMany(mappedBy = "knjiga")
    private Set<Posudba> posudbe;


    @OneToMany(mappedBy = "knjiga", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KnjiznicaImaKnjige> knjige;


    @ManyToMany
    @JoinTable(
            name = "knjiga_ima_autore",
            joinColumns = @JoinColumn(name = "idKnjiga"),
            inverseJoinColumns = @JoinColumn(name = "idAutor"))
    Set<Autor> autori;
}