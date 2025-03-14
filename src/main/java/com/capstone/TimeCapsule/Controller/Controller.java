package com.capstone.TimeCapsule.Controller;

import com.capstone.TimeCapsule.Enum.TipoCapsula;
import com.capstone.TimeCapsule.Mapper_travasi.CapsulaTravaso;
import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Payload.CapsulaDTO;
import com.capstone.TimeCapsule.Payload.TextFileDTO;
import com.capstone.TimeCapsule.Payload.VisualMediaDTO;
import com.capstone.TimeCapsule.Service.CapsulaService;
import com.capstone.TimeCapsule.Service.CloudinaryService;
import com.capstone.TimeCapsule.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("")
public class Controller {
    private final CapsulaService capsulaService;
    private final CloudinaryService cloudinaryService;
    private final CapsulaTravaso capsulaTravaso;
    private final EmailService emailService;

    public Controller(CapsulaService capsulaService, CloudinaryService cloudinaryService, CapsulaTravaso capsulaTravaso, EmailService emailService) {
        this.capsulaService = capsulaService;
        this.cloudinaryService = cloudinaryService;
        this.capsulaTravaso = capsulaTravaso;
        this.emailService = emailService;
    }

    @GetMapping("/home")
    public ResponseEntity<Map<String, String>> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Accesso consentito!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-personal")
    public ResponseEntity<?> createCapsula(
            @RequestParam("title") String title,
            @RequestParam("openDate") String openDate,
            @RequestParam("email") String email,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam("pubblica") boolean pubblica,
            @RequestParam(value = "media", required = false) Set<MultipartFile> mediaFiles,
            @RequestParam(value = "textFiles", required = false) Set<MultipartFile> textFiles,
            @RequestParam("idUtente") String idUtente
    ) throws IOException {
        // Verifica della dimensione massima consentita
        final long MAX_SIZE = 3 * 1024 * 1024; // 3MB

        CapsulaDTO dto = new CapsulaDTO();
        dto.setTitle(title);
        dto.setCreationDate(LocalDate.now());
        dto.setOpenDate(java.time.LocalDate.parse(openDate)); //openDate("") viene convertito in un oggetto LocalDate
        dto.setEmail(email);
        dto.setMessage(message);
        dto.setPubblica(pubblica);
        dto.setCapsula(TipoCapsula.PERSONALE);

        // lista per gli errori
        List<String> errori = new ArrayList<>();

        // Carico i media su Cloudinary
        Set<VisualMediaDTO> mediaDTOs = new HashSet<>();
        if (mediaFiles != null) {
            for (MultipartFile file : mediaFiles) {
                if (file.getSize() > MAX_SIZE) {
                    errori.add("File troppo grande: " + file.getOriginalFilename());
                    continue; // Salta il file senza interrompere tutto
                }
                String url = cloudinaryService.uploadFile(file);
                VisualMediaDTO mediaDTO = new VisualMediaDTO();
                mediaDTO.setName(file.getOriginalFilename());
                mediaDTO.setUrl(url);
                mediaDTO.setType(file.getContentType());

                mediaDTOs.add(mediaDTO);
            }
        }
        dto.setMedia(mediaDTOs);

        // Carica i file di testo su Cloudinary
        Set<TextFileDTO> textFileDTOs = new HashSet<>();
        if (textFiles != null) {
            for (MultipartFile file : textFiles) {
                if (file.getSize() > MAX_SIZE) {
                    errori.add("File troppo grande: " + file.getOriginalFilename());
                    continue; // Salta il file senza interrompere tutto
                }
                String url = cloudinaryService.uploadFile(file);
                TextFileDTO textFileDTO = new TextFileDTO();
                textFileDTO.setName(file.getOriginalFilename());
                textFileDTO.setType(file.getContentType());
                textFileDTO.setUrl(url);

                textFileDTOs.add(textFileDTO);
            }
        }
        dto.setTextFiles(textFileDTOs);

        // Se ci sono errori, restituisci un messaggio con i file scartati
        if (!errori.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errori", errori));
        }

        // Salvataggio della capsula
        Capsula capsulaEntity = capsulaTravaso.dto_entity(dto, idUtente);
        Capsula savedCapsula = capsulaService.saveCap(capsulaEntity);
        emailService.sendEmail(email, "Capsula creata con successo!",
                "La tua capsula '" + title + "' è stata creata. Riceverai un'email il giorno " + openDate + " quando sarà il momento di aprirla.");
        return ResponseEntity.ok(capsulaTravaso.entity_dto(savedCapsula));
    }

    @GetMapping("/le-mie-caps/{id}")
    public ResponseEntity<?> getCaps(@PathVariable String id) {
       List<CapsulaDTO> caps = capsulaService.findAllById(id);
        return ResponseEntity.ok(caps);
    }

}
