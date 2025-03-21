package com.capstone.TimeCapsule.Model.MediaFile;

import com.capstone.TimeCapsule.Model.Capsula;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Invito {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private boolean accettato;

    @ManyToOne
    @JoinColumn(name = "capsula_id")
    private Capsula capsula;



}
