package com.capstone.TimeCapsule.Controller;

import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Repository.CapsulaRepository;
import com.capstone.TimeCapsule.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private CapsulaRepository capsulaRepository;

    @Autowired
    private EmailService emailService;

    // Esegui ogni giorno alle 8:00
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkAndSendCapsuleEmails() {
        LocalDate today = LocalDate.now();
        List<Capsula> capsulesToOpen = capsulaRepository.findByOpenDate(today);

        for (Capsula capsula : capsulesToOpen) {
            emailService.sendEmail(
                    capsula.getEmail(),
                    "È il momento di aprire la tua capsula!",
                    "Oggi puoi aprire la tua capsula '" + capsula.getTitle() + "'."
            );
        }
    }
}

/*Spiegazione del cron job ("0 0 17 * * ?"):

    0 → Secondi (inizia all'istante 0)
    0 → Minuti (inizia al minuto 0)
    17 → Ora (17:00)
    * * → Ogni giorno, mese e giorno della settimana
    ? → Ignora il giorno della settimana (è usato in alternative con * per evitare ambiguità)*/
