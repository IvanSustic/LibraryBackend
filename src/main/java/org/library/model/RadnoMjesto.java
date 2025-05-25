package org.library.model;

import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "radno_mjesto")
@Data
@EqualsAndHashCode
public class RadnoMjesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRadnoMjesto")
    @EqualsAndHashCode.Include
    private Integer idRadnoMjesto;

    @Column(name = "naziv", nullable = false)
    private String naziv;

}