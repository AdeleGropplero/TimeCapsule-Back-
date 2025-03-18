package com.capstone.TimeCapsule.Model;

import com.capstone.TimeCapsule.Enum.TipoCapsula;
import com.capstone.TimeCapsule.Model.MediaFile.TextFile;
import com.capstone.TimeCapsule.Model.MediaFile.VisualMedia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "capsule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Capsula {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private LocalDate creationDate;

    @Column(nullable = false)
    private LocalDate openDate;

    @Column(nullable = false)
    private String email;

 /*   @Lob  // Permette di salvare grandi quantità di testo
    @Column(columnDefinition = "TEXT")*/
 @Column(length = 10000)
 private String message;

    private boolean pubblica;

    @Enumerated( EnumType.STRING)
    private TipoCapsula capsula;

    @OneToMany(mappedBy = "capsula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisualMedia> media = new ArrayList<>();
     // Set di media, se mi servirà l'ordine di inserimento metti List

    @OneToMany(mappedBy = "capsula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextFile> textFiles  = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public void removeMedia(VisualMedia media) {
        this.media.remove(media);  // Rimuove il media dalla capsula
    }

    public void removeTextFile(TextFile textFile) {
        this.textFiles.remove(textFile);  // Rimuove il file di testo dalla capsula
    }

    public boolean isPersonale() {
        return this.capsula == TipoCapsula.PERSONALE;
    }

    public boolean isDiGruppo() {
        return this.capsula == TipoCapsula.GRUPPO;
    }

    public boolean isEvento() {
        return this.capsula == TipoCapsula.EVENTO;
    }



}
