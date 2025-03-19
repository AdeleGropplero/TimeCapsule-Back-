package com.capstone.TimeCapsule.Mapper_travasi;

import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Payload.utente.UtenteDTO;
import org.springframework.stereotype.Component;

@Component
public class UtenteTravaso {

    public static UtenteDTO entity_dto(Utente utente) {
        UtenteDTO dto = new UtenteDTO();

        dto.setFullName(utente.getFullName());
        dto.setDataRegistrazione(utente.getDataRegistrazione());
        dto.setEmail(utente.getEmail());
        dto.setPassword(utente.getPassword());
        return dto;
    }

    public static Utente dto_entity(UtenteDTO dto) {
        Utente utente = new Utente();

        utente.setFullName(dto.getFullName());
        utente.setDataRegistrazione(dto.getDataRegistrazione());
        utente.setEmail(dto.getEmail());
        utente.setPassword(dto.getPassword());
        return utente;
    }
}
