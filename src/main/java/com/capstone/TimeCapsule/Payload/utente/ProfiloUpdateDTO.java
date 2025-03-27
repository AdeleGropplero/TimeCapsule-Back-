package com.capstone.TimeCapsule.Payload.utente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfiloUpdateDTO {
    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    private String avatar;
}
