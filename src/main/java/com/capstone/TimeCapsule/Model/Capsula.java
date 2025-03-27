package com.capstone.TimeCapsule.Model;

import com.capstone.TimeCapsule.Enum.TipoCapsula;
import com.capstone.TimeCapsule.Model.MediaFile.Invito;
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

    @Column(length = 10000)
    private String message;

    private boolean pubblica;

    @Enumerated(EnumType.STRING)
    private TipoCapsula capsula;

    @OneToMany(mappedBy = "capsula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisualMedia> media = new ArrayList<>();
    // Set di media, se mi servirà l'ordine di inserimento metti List

    @OneToMany(mappedBy = "capsula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextFile> textFiles = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "capsula_utente",
            joinColumns = @JoinColumn(name = "capsula_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private Set<Utente> utenti = new HashSet<>();

    @OneToMany(mappedBy = "capsula", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invito> inviti = new HashSet<>();


    //-------------------------------------------------


    public boolean isPersonale() {
        return this.capsula == TipoCapsula.PERSONALE;
    }

    public boolean isDiGruppo() {
        return this.capsula == TipoCapsula.GRUPPO;
    }

    public boolean isEvento() {
        return this.capsula == TipoCapsula.EVENTO;
    }

    @Override
    public String toString() {
        return "Capsula{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", creationDate=" + creationDate +
                ", openDate=" + openDate +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", pubblica=" + pubblica +
                ", capsula=" + capsula +
                ", mediaCount=" + (media != null ? media.size() : 0) +
                ", textFilesCount=" + (textFiles != null ? textFiles.size() : 0) +
                ", utentiCount=" + (utenti != null ? utenti.size() : 0) +
                ", invitiCount=" + (inviti != null ? "inviti.size()" : 0) +
                '}';
    }

}
