package org.library.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String lozinka;
}
