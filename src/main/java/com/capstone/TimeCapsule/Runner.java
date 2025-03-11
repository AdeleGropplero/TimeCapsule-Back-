package com.capstone.TimeCapsule;

import com.capstone.TimeCapsule.Enum.ERuoli;
import com.capstone.TimeCapsule.Model.Ruolo;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Repository.UtenteRepository;
import com.capstone.TimeCapsule.Service.RuoloService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    RuoloService ruoloService;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${utente.admin.fullName}")
    private String fullName;

    @Value("${utente.admin.email}")
    private String email;

    @Value("${utente.admin.password}")
    private String password;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        System.out.println("♻️ Running...  ----------------------------------------------------------------");
        Arrays.stream(ERuoli.values()).forEach(ruoloEnum -> {
            if(!ruoloService.existsByNome(ruoloEnum)){
                Ruolo ruolo = new Ruolo(ruoloEnum);
                ruoloService.insertRuolo(ruolo);
                System.out.println("✅ Ruolo inserito: " + ruoloEnum);
            }
        });

        //Successivamente ai ruoli mi salvo in DB già un utente con ruolo admin che ha
        //accesso a diversi endpoint.
        // Le credenziali dell'admin sono salvate nel properties.
        if (!utenteRepository.existsByEmail(email)) {
            Utente utente = new Utente();
            utente.setEmail(email);
            utente.setFullName(fullName);
            String encodedPassword = passwordEncoder.encode(password);
            utente.setPassword(encodedPassword);
            Ruolo ruolo = ruoloService.getRuolo(2L); // L'admin ha id 2
            utente.setRuolo(ruolo);
            utente.setDataRegistrazione(LocalDate.now());
            utenteRepository.save(utente);
        }

    }
}
