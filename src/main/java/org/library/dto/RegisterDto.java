package org.library.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String email;

    private String lozinka;

    private String ime;

    private String prezime;

}