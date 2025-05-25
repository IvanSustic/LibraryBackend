package org.library.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "zaposlenik")
@Data
@EqualsAndHashCode
public class Zaposlenik implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idZaposlenik")
    @EqualsAndHashCode.Include
    private Integer idZaposlenik;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "lozinka", nullable = false)
    private String lozinka;

    @OneToMany(mappedBy = "zaposlenik")
    private Set<Racun> racuni;

    @ManyToMany
    @JoinTable(
            name = "zaposlenik_ima_knjiznicu",
            joinColumns = @JoinColumn(name = "idZaposlenik"),
            inverseJoinColumns = @JoinColumn(name = "idKnjiznica"))
    Set<Knjiznica> knjiznice;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "zaposlenik_ima_radno_mjesto",
            joinColumns = @JoinColumn(name = "idZaposlenik"),
            inverseJoinColumns = @JoinColumn(name = "idRadnoMjesto"))
    Set<RadnoMjesto> radnaMjesta;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return radnaMjesta.stream().map(radnoMjesto -> new SimpleGrantedAuthority(radnoMjesto.getNaziv())).toList();
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
