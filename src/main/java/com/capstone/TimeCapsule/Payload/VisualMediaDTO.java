package com.capstone.TimeCapsule.Payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisualMediaDTO {
    @NotBlank(message = "L'URL è obbligatorio")
    private String url;

    private String name;

    @NotBlank(message = "Il tipo MIME è obbligatorio")
    private String type;
}
