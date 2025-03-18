package com.capstone.TimeCapsule.Model.MediaFile;

import com.capstone.TimeCapsule.Model.Capsula;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "text_files")
public class TextFile extends SuperFiles{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "capsula_id")
    private Capsula capsula;
}
