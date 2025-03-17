package com.capstone.TimeCapsule.Controller;

import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Repository.CapsulaRepository;
import com.capstone.TimeCapsule.Service.EmailService;
import jakarta.transaction.Transactional;
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
    @Transactional
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkAndSendCapsuleEmails() {
        LocalDate today = LocalDate.now();
        System.out.println(today);
        List<Capsula> capsulesToOpen = capsulaRepository.findByOpenDate(today);
        System.out.println(capsulesToOpen);

        for (Capsula capsula : capsulesToOpen) {
            capsula.getMedia().size(); // Forza il caricamento
            capsula.getTextFiles().size();

            String linkToCapsule = "http://localhost:5173/capsula/" + capsula.getId();

            emailService.sendEmail(
                    capsula.getEmail(),
                    "È il momento di aprire la tua capsula!",
                    "Oggi puoi finalmente aprire la tua capsula: -" + capsula.getTitle()
                            + "- Clicca sul seguente link per aprirla: <a href=\"" + linkToCapsule + "\">Apri la tua capsula</a>"
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
