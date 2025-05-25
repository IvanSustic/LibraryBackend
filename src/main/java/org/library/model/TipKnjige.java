package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "tip_knjige")
@Data
@EqualsAndHashCode
public class TipKnjige {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTipKnjige")
    @EqualsAndHashCode.Include
    private Integer idTipKnjige;

    @Column(name = "naziv")
    private String naziv;

}

