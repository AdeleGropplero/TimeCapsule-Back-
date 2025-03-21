package com.capstone.TimeCapsule.Payload;


import com.capstone.TimeCapsule.Enum.TipoCapsula;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapsulaDTO {
    private  UUID id;
    @NotBlank(message = "Il titolo è obbligatorio")
    private String title;

    private LocalDate creationDate;

    @NotNull(message = "La data di apertura è obbligatoria")
    private LocalDate openDate;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @Size(max = 10000, message = "Il messaggio non può superare i 10000 caratteri")
    private String message;

    @NotNull(message = "Il campo 'pubblica' è obbligatorio")
    private Boolean pubblica;

    @NotNull(message = "Il tipo di capsula è obbligatorio")
    private TipoCapsula capsula;

    private Set<VisualMediaDTO> media;
    private Set<TextFileDTO> textFiles;

    private Set<UUID> utentiIds;
}
