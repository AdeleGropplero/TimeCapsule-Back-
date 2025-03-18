package com.capstone.TimeCapsule.Payload;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UtenteProfiloDTO {

    private String fullName;
    private String email;
    private LocalDate dataRegistrazione;
    private int numCapsulePersonali;
    private int numCapsuleGruppo;
    private int numCapsuleEvento;

    public UtenteProfiloDTO( String fullName, String email, LocalDate dataRegistrazione, int numCapsulePersonali, int numCapsuleGruppo, int numCapsuleEvento) {
        this.fullName = fullName;
        this.email = email;
        this.numCapsulePersonali = numCapsulePersonali;
        this.numCapsuleGruppo = numCapsuleGruppo;
        this.numCapsuleEvento = numCapsuleEvento;
        this.dataRegistrazione = dataRegistrazione;
    }
}
