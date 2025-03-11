package com.capstone.TimeCapsule.Model.MediaFile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class SuperFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String url; //Questo sarà l'url del file su Cloudinary.

    private String name; //nome originale file

    private String type; // Tipo MIME (es. image/png, video/mp4, application/pdf)

}
