package com.capstone.TimeCapsule.Service;

import com.capstone.TimeCapsule.Authentication.Request.LoginRequest;
import com.capstone.TimeCapsule.Authentication.Request.RegistrationRequest;
import com.capstone.TimeCapsule.Exception.EmailDuplicateException;
import com.capstone.TimeCapsule.Exception.UtenteNonTrovatoException;
import com.capstone.TimeCapsule.Mapper_travasi.UtenteProfiloTravaso;
import com.capstone.TimeCapsule.Model.Ruolo;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Payload.UtenteProfiloDTO;
import com.capstone.TimeCapsule.Repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service

public class UtenteService {

    @Autowired
    UtenteRepository utenteRepository;

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
        UtenteProfiloDTO profilo = profiloTravaso.entity_dto(utente);
        return profilo;
    }

//-----------------------------------------------------------------------------------------------------




}
