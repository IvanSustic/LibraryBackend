package org.library.configs;

import org.library.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/api/knjiznica/all","/api/knjiznica/{id}","/api/knjige/all",
                                "/api/knjige/{id}", "/api/tipKnjige/all", "/api/mjesto/all",
                                "/api/autor/all", "/api/zanr/all", "/uploads/images/**").permitAll()
                        .requestMatchers("/api/korisnik/knjige","api/rezervacije/forUser", "api/posudba/forUser"
                                    , "api/clanstvo/forUser", "api/rezervacije/rezerviraj").hasAuthority("ROLE_USER")
                        .requestMatchers("/api/posudba/forZaposlenik","/api/rezervacije/forZaposlenik", "api/clanstvo/forZaposlenik", "api/clanstvo/dodaj",
                                "api/clanstvo/", "api/knjiznica/forZaposlenik" , "api/korisnik/emails",
                                "api/posudba/posudi", "api/posudba/ostecena/{id}", "api/mjesto/delete/{id}",
                                "api/rezervacije/posudiRezerviranu" , "api/zaposlenici/knjige").hasAnyAuthority("Voditelj knjižnice", "Admin", "Knjižničar")
                        .requestMatchers("/api/autor/dodaj", "/api/autor/delete/{id}", "api/knjige/allVoditelj", "api/knjige/dodaj"
                        ,"api/knjige/delete/{id}","api/knjiznica/updateRaspolaganje", "api/knjiznica/addRaspolaganje",
                                "api/knjiznica/deleteRaspolaganje","api/slika/**", "api/racun/all",
                                "api/tipKnjige/dodaj","api/tipKnjige/delete/{id}",
                                "api/zanr/dodaj","api/zanr/delete/{id}", "api/zaposlenici/zaposleniciForKnjiznicar",
                                "api/zaposlenici/register/zaposlenik","api/zaposlenici/disableZaposlenik").hasAnyAuthority("Voditelj knjižnice", "Admin")
                        .requestMatchers("api/knjiznica/delete/{id}", "api/knjiznica/dodaj", "api/korisnik/register/korisnik",
                                "api/korisnik/disableKorisnik", "api/mjesto/dodaj", "api/mjesto/delete/{id}" ,"api/korisnik/all"
                        ,"api/zaposlenici/all").hasAuthority( "Admin")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET","PUT", "POST","DELETE" , "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}