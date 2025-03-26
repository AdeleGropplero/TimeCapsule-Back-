package com.capstone.TimeCapsule.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Immagini Capsule")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImgCapsula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

}
