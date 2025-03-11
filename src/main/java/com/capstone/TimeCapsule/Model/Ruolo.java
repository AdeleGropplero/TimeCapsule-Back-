package com.capstone.TimeCapsule.Model;

import com.capstone.TimeCapsule.Enum.ERuoli;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ruoli")
@NoArgsConstructor
@AllArgsConstructor
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERuoli nome;

    public Ruolo(ERuoli nome) {
        this.nome = nome;
    }
}
