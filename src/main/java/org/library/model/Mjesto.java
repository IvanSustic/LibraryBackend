package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import lombok.*;
import java.util.Set;

@Entity
@Table(name = "mjesto")
@Data
@EqualsAndHashCode
public class Mjesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMjesto")
    @EqualsAndHashCode.Include
    private Integer idMjesto;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "postanskiBroj")
    private String postanskiBroj;

}