package com.capstone.TimeCapsule.Service;

import com.capstone.TimeCapsule.Exception.ResourceNotFoundException;
import com.capstone.TimeCapsule.Exception.UtenteNonTrovatoException;
import com.capstone.TimeCapsule.Mapper_travasi.CapsulaTravaso;
import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Model.ImgCapsula;
import com.capstone.TimeCapsule.Model.MediaFile.Invito;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Payload.CapsulaDTO;
import com.capstone.TimeCapsule.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class CapsulaService {
    private final CloudinaryService cloudinaryService;
    private final TextFilesRepository textFilesRepository;
    private final VisualMediaRepository visualMediaRepository;

    public CapsulaService(CloudinaryService cloudinaryService, TextFilesRepository textFilesRepository, VisualMediaRepository visualMediaRepository) {
        this.cloudinaryService = cloudinaryService;
        this.textFilesRepository = textFilesRepository;
        this.visualMediaRepository = visualMediaRepository;
    }

    @Autowired
    private CapsulaRepository capsulaRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private CapsulaTravaso capsulaTravaso;

    @Autowired
    private InvitoRepository invitoRepository;

    @Autowired
    private ImgCapRepository imgCapRepository;

    @Transactional
    public Capsula saveCap(Capsula caps) {
        return capsulaRepository.save(caps);
    }

    public List<Capsula> getAllCapsule() {
        return new ArrayList<>(capsulaRepository.findAll());
    }

    public List<CapsulaDTO> findAllById(String idUtente) {
        UUID id = UUID.fromString(idUtente);
        List<Capsula> capsule = capsulaRepository.findByUtenti_Id(id);

        List<CapsulaDTO> capsuleDTO = new ArrayList<>();
        for (Capsula cap : capsule) {
            CapsulaDTO dto = capsulaTravaso.entity_dto(cap);
            capsuleDTO.add(dto);
        }
        return capsuleDTO;
    }

    public Capsula findById(String idCapsula) {
        UUID id = UUID.fromString(idCapsula);
        Capsula cap = capsulaRepository.findById(id).orElseThrow(() -> new RuntimeException("Capsula non trovata con id: " + id));
        /* CapsulaDTO capDTO = capsulaTravaso.entity_dto(cap);*/
        return cap;
    }


    @Transactional
    public void deleteCapsula(String id) {
        UUID capsulaId = UUID.fromString(id);
        Capsula capsula = capsulaRepository.findById(capsulaId)
                .orElseThrow(() -> new EntityNotFoundException("Capsula non trovata con ID: " + id));
        visualMediaRepository.deleteMediaByCapsulaId(capsulaId);
        textFilesRepository.deleteMediaByCapsulaId(capsulaId);

        // Elimina prima gli inviti associati alla capsula
        invitoRepository.deleteByCapsulaId(capsulaId);

        capsulaRepository.deleteCapsulaById(capsulaId);
    }

    @Transactional
    public void invitaUtente(Capsula cap, String email) {
        Optional<Utente> utenteOpt = utenteRepository.findByEmail(email);
        if (utenteOpt.isPresent()) {
            // L'utente è già registrato, lo aggiungiamo direttamente alla capsula
            Utente utente = utenteOpt.get();
            cap.getUtenti().add(utente);
            capsulaRepository.save(cap);

            // 🔥 DEBUG: Stampa conferma aggiunta
            System.out.println("Aggiunto utente: " + utente.getEmail() + " alla capsula " + cap.getId());

        } else {
            // Creiamo un invito per l'utente non registrato
            Invito invito = new Invito();
            invito.setEmail(email);
            invito.setCapsula(cap);
            invitoRepository.save(invito);

            // 🔥 DEBUG: Stampa conferma invito
            System.out.println("Creato invito per: " + email + " alla capsula " + cap.getId());


            // Invio email con link di registrazione
            String link = "http://localhost:5173/register";
            emailService.sendEmail(email, "Hai ricevuto un invito per partecipare ad una capsula del tempo!", "Registrati qui per accedere alla capsula! --> " + link + " <--");
        }
    }
    /*
        UUID capsulaId = UUID.fromString(id);
        Capsula cap = capsulaRepository.findById(capsulaId).orElseThrow(() -> new ResourceNotFoundException("Capsula non trovata."));
*/



//Capsula img.
    public List<ImgCapsula> findAllImageCap(){
        return imgCapRepository.findAll();
    }

}
