package com.capstone.TimeCapsule.Payload.utente;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UtenteProfiloDTO {

    private String fullName;
    private String email;
    private LocalDate dataRegistrazione;
    private String avatar;
    private int numCapsulePersonali;
    private int numCapsuleGruppo;
    private int numCapsuleEvento;

    public UtenteProfiloDTO(String fullName, String email, LocalDate dataRegistrazione, String avatar, int numCapsulePersonali, int numCapsuleGruppo, int numCapsuleEvento) {
        this.fullName = fullName;
        this.email = email;
        this.dataRegistrazione = dataRegistrazione;
        this.avatar = avatar;
        this.numCapsulePersonali = numCapsulePersonali;
        this.numCapsuleGruppo = numCapsuleGruppo;
        this.numCapsuleEvento = numCapsuleEvento;
    }
}
