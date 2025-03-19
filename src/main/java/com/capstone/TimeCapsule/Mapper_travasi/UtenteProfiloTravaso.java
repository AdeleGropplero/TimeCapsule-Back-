package com.capstone.TimeCapsule.Mapper_travasi;

import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Payload.utente.UtenteProfiloDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UtenteProfiloTravaso {


    public UtenteProfiloDTO entity_dto(Utente utente) {

        System.out.println("Capsule dell'utente: " + utente.getCapsule());
        // Copia la collezione persistente in una nuova lista non gestita da Hibernate
        List<Capsula> capsuleList = new ArrayList<>(utente.getCapsule());

        // Calcola il numero di capsule personali, di gruppo ed evento
        int numCapsulePersonali = (int) capsuleList.stream()
                .filter(Capsula::isPersonale)
                .count();

        int numCapsuleGruppo = (int) capsuleList.stream()
                .filter(Capsula::isDiGruppo)
                .count();

        int numCapsuleEvento = (int) capsuleList.stream()
                .filter(Capsula::isEvento)
                .count();



        // Restituisce il DTO con i dati calcolati
        return new UtenteProfiloDTO(
                utente.getFullName(),
                utente.getEmail(),
                utente.getDataRegistrazione(),
                numCapsulePersonali,
                numCapsuleGruppo,
                numCapsuleEvento
        );
    }
}
