package com.capstone.TimeCapsule.Payload.utente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UtenteDTO {

    @NotBlank(message = "Il nome completo è obbligatorio")
    @Size(max = 100, message = "Il nome completo non può superare i 100 caratteri")
    private String fullName;

    @NotNull(message = "La data di registrazione è obbligatoria")
    private LocalDate dataRegistrazione;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
    private String password;
}
