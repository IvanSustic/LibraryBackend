package org.library.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "clanstvo")
@Data
@EqualsAndHashCode
@IdClass(Clanstvo.ClanstvoId.class)
public class Clanstvo {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKnjiznica", referencedColumnName = "idKnjiznica")
    @EqualsAndHashCode.Include
    private Knjiznica knjiznica;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKorisnik", referencedColumnName = "idKorisnik")
    @EqualsAndHashCode.Include
    private Korisnik korisnik;

    @Column(name = "datumUclanjivanja", nullable = false)
    private LocalDate datumUclanjenja;

    @Column(name = "krajUclanjivanja")
    private LocalDate krajUclanjenja;
    @Data
    @EqualsAndHashCode
    public static class ClanstvoId implements java.io.Serializable {
        private Integer knjiznica;
        private Integer korisnik;
    }
}
