package com.capstone.TimeCapsule.Model.MediaFile;

import com.capstone.TimeCapsule.Model.Capsula;
import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class VisualMedia extends SuperFiles{

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "capsula_id")
    private Capsula capsula;
}
