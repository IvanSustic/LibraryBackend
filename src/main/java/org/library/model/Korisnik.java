package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "korisnik")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

public class Korisnik implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idKorisnik")
    @EqualsAndHashCode.Include
    private Integer idKorisnik;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "lozinka", nullable = false)
    private String lozinka;


    @OneToMany(mappedBy = "korisnik")
    private Set<Posudba> posudbe;

    @OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Clanstvo> clanstva;

    @OneToMany(mappedBy = "korisnik")
    private Set<Racun> racuni;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "refreshTokenDatum")
    private LocalDate refreshTokenDatum;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }


    public String getPassword() {
        return lozinka;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
