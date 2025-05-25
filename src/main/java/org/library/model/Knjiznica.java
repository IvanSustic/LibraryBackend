package org.library.model;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

import lombok.*;
import java.util.Set;

@Entity
@Table(name = "knjiznica")
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Knjiznica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idKnjiznica")
    @EqualsAndHashCode.Include
    private Integer idKnjiznica;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "adresa")
    private String adresa;

    @Column(name = "slika")
    private String slika;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMjesto")
    private Mjesto mjesto;

    @OneToMany(mappedBy = "knjiznica", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KnjiznicaImaKnjige> knjige;

    @OneToMany(mappedBy = "knjiznica", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Clanstvo> clanstva;

    @OneToMany(mappedBy = "knjiznica")
    private Set<Posudba> posudbe;

}
