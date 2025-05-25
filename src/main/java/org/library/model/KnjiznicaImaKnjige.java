package org.library.model;

import jakarta.persistence.*;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "knjiznica_ima_knjige")
@Data
@EqualsAndHashCode
public class KnjiznicaImaKnjige {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKnjiznica")
    @EqualsAndHashCode.Include
    private Knjiznica knjiznica;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idKnjiga")
    @EqualsAndHashCode.Include
    private Knjiga knjiga;

    @Column(name = "kolicina", nullable = false)
    private Integer kolicina;

}
