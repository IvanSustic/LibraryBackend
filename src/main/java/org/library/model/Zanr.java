package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "zanr")
@Data
@EqualsAndHashCode
public class Zanr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idZanr")
    @EqualsAndHashCode.Include
    private Integer idZanr;

    @Column(name = "naziv")
    private String naziv;

}