package com.capstone.TimeCapsule.Authentication.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class RegistrationRequest {

    @NotBlank(message = "Il nome completo è obbligatorio")
    @Size(max = 100, message = "Il nome completo non può superare i 100 caratteri")
    private String fullName;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
    private String password;

}
