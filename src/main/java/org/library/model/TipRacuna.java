package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "tip_racuna")
@Data
@EqualsAndHashCode
public class TipRacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipRacuna")
    @EqualsAndHashCode.Include
    private Integer idTipRacuna;

    @Column(name = "naziv", nullable = false)
    private String naziv;

}