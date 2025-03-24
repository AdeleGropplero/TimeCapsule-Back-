package com.capstone.TimeCapsule.Model.MediaFile;

import com.capstone.TimeCapsule.Model.Capsula;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisualMedia extends SuperFiles{

    @ManyToOne
    @JoinColumn(name = "capsula_id")
    private Capsula capsula;


}
