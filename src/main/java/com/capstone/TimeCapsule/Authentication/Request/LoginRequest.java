package com.capstone.TimeCapsule.Authentication.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Il campo email risulta vuoto")
    @Email(message = "Inserisci una mail valida")
    @Size(min = 3, max = 15)
    private String email;

    @NotBlank(message = "Il campo password risulta vuoto")
    @Size(min = 3, max = 20)
    private String password;
}
