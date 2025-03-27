package com.capstone.TimeCapsule.Service;

import com.capstone.TimeCapsule.Authentication.Request.RegistrationRequest;
import com.capstone.TimeCapsule.Exception.EmailDuplicateException;
import com.capstone.TimeCapsule.Exception.UtenteNonTrovatoException;
import com.capstone.TimeCapsule.Mapper_travasi.UtenteProfiloTravaso;
import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Model.MediaFile.Invito;
import com.capstone.TimeCapsule.Model.Ruolo;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Payload.utente.ProfiloUpdateDTO;
import com.capstone.TimeCapsule.Payload.utente.UtenteProfiloDTO;
import com.capstone.TimeCapsule.Repository.CapsulaRepository;
import com.capstone.TimeCapsule.Repository.InvitoRepository;
import com.capstone.TimeCapsule.Repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service

public class UtenteService {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    CapsulaRepository capsulaRepository;

    @Autowired
    InvitoRepository invitoRepository;

    @Autowired
    UtenteProfiloTravaso profiloTravaso;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UtenteService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    RuoloService ruoloService;

    @Transactional
    public String salvaUtente(RegistrationRequest newUtente){
        checkDuplicatedKey(newUtente.getEmail());
        String encodedPassword = passwordEncoder.encode(newUtente.getPassword());

        Utente utente = new Utente();
        utente.setFullName(newUtente.getFullName());
        utente.setDataRegistrazione(LocalDate.now());
        utente.setEmail(newUtente.getEmail());
        utente.setPassword(encodedPassword);

        // ❗ Gli utenti sono registrati come BASIC
        Ruolo ruoloBasic = ruoloService.getRuolo(1L);
        utente.setRuolo(ruoloBasic);
        utenteRepository.save(utente);

        // Controllo se ha ricevuto inviti a capsule
        List<Invito> inviti = invitoRepository.findByEmail(newUtente.getEmail());
        for (Invito invito : inviti) {
            Capsula capsula = invito.getCapsula();
            capsula.getUtenti().add(utente);
            invito.setAccettato(true);
            capsulaRepository.save(capsula);
            invitoRepository.save(invito);
        }
        return "L'utente " + utente.getFullName() + " è stato salvato correttamente";
    }

    public void checkDuplicatedKey( String email) {
        if (utenteRepository.existsByEmail(email)) {
            throw new EmailDuplicateException("Questa email esiste già!");
        }
    }

    public Utente findById(UUID id){
        return utenteRepository.findById(id).orElseThrow(()->
                new UtenteNonTrovatoException("Nessun Utente trovato con id: " + id));
    }


    @Transactional
    public Utente findByEmail(String email){
        return utenteRepository.findByEmail(email).orElseThrow(()->
                new UtenteNonTrovatoException("Nessun Utente trovato con email: " + email));
    }

    public UtenteProfiloDTO getProfilo(String id){
        UUID idUtente = UUID.fromString(id);
        Utente utente = utenteRepository.findById(idUtente).orElseThrow(()->
                new UtenteNonTrovatoException("Nessun Utente trovato con id: " + id));
        Hibernate.initialize(utente.getCapsule()); // Forza il caricamento delle capsule
        UtenteProfiloDTO profilo = profiloTravaso.entity_dto(utente);
        return profilo;
    }

    @Transactional
    public UtenteProfiloDTO patchProfilo(String id, ProfiloUpdateDTO dto){
        UUID idUtente = UUID.fromString(id);
        Utente utente = utenteRepository.findById(idUtente).orElseThrow(()->
                new UtenteNonTrovatoException("Nessun Utente trovato con id: " + id));

        utente.setFullName(dto.getFullName());
        utente.setEmail(dto.getEmail());
        if (dto.getAvatar() != null) {
            utente.setAvatar(dto.getAvatar());  // Salva il nuovo avatar
        }
        utenteRepository.save(utente);
        return profiloTravaso.entity_dto(utente);
    }

//-----------------------------------------------------------------------------------------------------




}
