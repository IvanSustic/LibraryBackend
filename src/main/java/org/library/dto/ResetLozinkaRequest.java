package org.library.dto;

import lombok.Data;

@Data
public class ResetLozinkaRequest {

        private String token;
        private String newLozinka;

}
