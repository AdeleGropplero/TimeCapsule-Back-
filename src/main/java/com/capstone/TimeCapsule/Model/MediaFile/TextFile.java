package com.capstone.TimeCapsule.Model.MediaFile;

import com.capstone.TimeCapsule.Model.Capsula;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "text_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextFile extends SuperFiles{
    @ManyToOne
    @JoinColumn(name = "capsula_id")
    private Capsula capsula;

}
