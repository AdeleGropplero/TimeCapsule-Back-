package com.capstone.TimeCapsule.Model;

import com.capstone.TimeCapsule.Enum.TipoCapsula;
import com.capstone.TimeCapsule.Model.MediaFile.TextFile;
import com.capstone.TimeCapsule.Model.MediaFile.VisualMedia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    private String message;

    private boolean pubblica;

    @Enumerated( EnumType.STRING)
    private TipoCapsula capsula;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "capsula_id")
    private Set<VisualMedia> media = new HashSet<>(); // Set di media, se mi servirà l'ordine di inserimento metti List

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "capsula_id")
    private Set<TextFile> textFiles  = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Utente utente;



}
