package com.capstone.TimeCapsule.Mapper_travasi;

import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Model.MediaFile.TextFile;
import com.capstone.TimeCapsule.Model.MediaFile.VisualMedia;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Payload.CapsulaDTO;
import com.capstone.TimeCapsule.Payload.TextFileDTO;
import com.capstone.TimeCapsule.Payload.VisualMediaDTO;
import com.capstone.TimeCapsule.Service.UtenteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CapsulaTravaso {

    @Autowired
    private UtenteService utenteService;

    public CapsulaTravaso() {
    }

    @Transactional
    public Capsula dto_entity(CapsulaDTO dto, String idUtente) {
        Capsula capsula = new Capsula();

        // Mappatura dei campi base
        capsula.setTitle(dto.getTitle());
        capsula.setCreationDate(dto.getCreationDate());
        capsula.setOpenDate(dto.getOpenDate());
        capsula.setEmail(dto.getEmail());
        capsula.setMessage(dto.getMessage());
        capsula.setPubblica(dto.getPubblica());
        capsula.setCapsula(dto.getCapsula());

        // Recupero utente e gestione errore
        UUID id = UUID.fromString(idUtente);
        Utente utente = utenteService.findById(id);
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato con id: " + idUtente);
        }
        capsula.getUtenti().add(utente);

        // Mappatura dei file media
        List<VisualMedia> mediaList = dto.getMedia().stream()
                .map(fileDTO -> {
                    VisualMedia media = new VisualMedia();
                    media.setUrl(fileDTO.getUrl());
                    media.setName(fileDTO.getName());
                    media.setType(fileDTO.getType());
                    return media;
                }).collect(Collectors.toList());
        capsula.setMedia(mediaList);

        // Mappatura dei file di testo
        List<TextFile> textFileList = dto.getTextFiles().stream()
                .map(fileDTO -> {
                    TextFile textFile = new TextFile();
                    textFile.setUrl(fileDTO.getUrl());
                    textFile.setName(fileDTO.getName());
                    textFile.setType(fileDTO.getType());
                    return textFile;
                }).collect(Collectors.toList());
        capsula.setTextFiles(textFileList);

        utente.getCapsule().add(capsula);

        return capsula;
    }

    public CapsulaDTO entity_dto(Capsula capsula) {
        CapsulaDTO dto = new CapsulaDTO();

        // Mappatura dei campi base
        dto.setId(capsula.getId());
        dto.setTitle(capsula.getTitle());
        dto.setOpenDate(capsula.getOpenDate());
        dto.setEmail(capsula.getEmail());
        dto.setMessage(capsula.getMessage());
        dto.setPubblica(capsula.isPubblica());
        dto.setCapsula(capsula.getCapsula());

        // Conversione dei media in DTO
        Set<VisualMediaDTO> mediaDTOs = capsula.getMedia().stream()
                .map(media -> new VisualMediaDTO(media.getUrl(), media.getName(), media.getType()))
                .collect(Collectors.toSet());
        dto.setMedia(mediaDTOs);

        // Conversione dei text files in DTO
        Set<TextFileDTO> textFileDTOs = capsula.getTextFiles().stream()
                .map(textFile -> new TextFileDTO(textFile.getUrl(), textFile.getName(), textFile.getType()))
                .collect(Collectors.toSet());
        dto.setTextFiles(textFileDTOs);

        // Mappatura dell'utente (se presente)
        if (capsula.getUtenti() != null) {
            dto.setUtentiIds(capsula.getUtenti().stream().map(Utente::getId).collect(Collectors.toSet()));
        }

        return dto;
    }
}
