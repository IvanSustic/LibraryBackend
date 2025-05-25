package org.library.service;

import lombok.RequiredArgsConstructor;
import org.library.model.Korisnik;
import org.library.model.Zaposlenik;
import org.library.repository.KorisnikRepository;
import org.library.repository.ZaposlenikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {


    private final ZaposlenikRepository zaposlenikRepository;

    private final KorisnikRepository korisnikRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Zaposlenik> zaposlenik = zaposlenikRepository.findByEmail(username);
        if (zaposlenik.isEmpty()){
            Korisnik korisnik =
                    korisnikRepository.findByEmail(username).orElseThrow(() ->
                            new UsernameNotFoundException("Username not found"));
            return korisnik;
        }

        return zaposlenik.get();
    }
}