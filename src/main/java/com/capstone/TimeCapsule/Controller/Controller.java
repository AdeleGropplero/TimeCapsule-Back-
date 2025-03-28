package com.capstone.TimeCapsule.Controller;

import com.capstone.TimeCapsule.Enum.TipoCapsula;
import com.capstone.TimeCapsule.Mapper_travasi.CapsulaTravaso;
import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Model.ImgCapsula;
import com.capstone.TimeCapsule.Model.MediaFile.TextFile;
import com.capstone.TimeCapsule.Model.MediaFile.VisualMedia;
import com.capstone.TimeCapsule.Model.Utente;
import com.capstone.TimeCapsule.Payload.CapsulaDTO;
import com.capstone.TimeCapsule.Payload.TextFileDTO;
import com.capstone.TimeCapsule.Payload.utente.ProfiloUpdateDTO;
import com.capstone.TimeCapsule.Payload.utente.UtenteProfiloDTO;
import com.capstone.TimeCapsule.Payload.VisualMediaDTO;
import com.capstone.TimeCapsule.Service.CapsulaService;
import com.capstone.TimeCapsule.Service.CloudinaryService;
import com.capstone.TimeCapsule.Service.EmailService;
import com.capstone.TimeCapsule.Service.UtenteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final UtenteService utenteService;

    public Controller(CapsulaService capsulaService, CloudinaryService cloudinaryService, CapsulaTravaso capsulaTravaso, EmailService emailService, UtenteService utenteService) {
        this.capsulaService = capsulaService;
        this.cloudinaryService = cloudinaryService;
        this.capsulaTravaso = capsulaTravaso;
        this.emailService = emailService;
        this.utenteService = utenteService;
    }

    @GetMapping("/home")
    public ResponseEntity<Map<String, String>> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Accesso consentito!");
        return ResponseEntity.ok(response);
    }

    //---------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------
    //CAPSULE
    //---------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------

    @PostMapping("/create-capsula")
    public ResponseEntity<?> createCapsula(
            @RequestParam("title") String title,
            @RequestParam("openDate") String openDate,
            @RequestParam("email") String email,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam("pubblica") boolean pubblica,
            @RequestParam("tipoCapsula") TipoCapsula tipoCapsula, // Nuovo parametro per distinguere il tipo
            @RequestParam(value = "media", required = false) Set<MultipartFile> mediaFiles,
            @RequestParam(value = "textFiles", required = false) Set<MultipartFile> textFiles,
            @RequestParam("idUtente") String idUtente,
            @RequestParam(value = "invitiMail", required = false) List<String> emails
    ) throws IOException {
        System.out.println(emails);

        // Verifica della dimensione massima consentita
        final long MAX_SIZE = 3 * 1024 * 1024; // 3MB

        UUID id = UUID.fromString(idUtente);
        Utente utente = utenteService.findById(id);

        CapsulaDTO dto = new CapsulaDTO();
        dto.setTitle(title);
        dto.setCreationDate(LocalDate.now());
        dto.setOpenDate(java.time.LocalDate.parse(openDate)); //openDate("") viene convertito in un oggetto LocalDate
        dto.setEmail(utente.getEmail());
        dto.setMessage(message);
        dto.setPubblica(pubblica);
        dto.setCapsula(tipoCapsula);

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
        Capsula capsulaEntity = capsulaTravaso.dto_entity(dto, utente);
        Capsula savedCapsula = capsulaService.saveCap(capsulaEntity);

        if (savedCapsula == null) {
            // Gestisci l'errore: la capsula non è stata salvata correttamente
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel salvataggio della capsula.");
        }

        if (emails != null) {
            for (String mailInvitata : emails) {
                capsulaService.invitaUtente(savedCapsula, mailInvitata);
            }
        }
        // 🔥 DEBUG: Stampa gli utenti della capsula
        System.out.println("Utenti nella capsula: " + savedCapsula.getUtenti());

        emailService.sendEmail(utente.getEmail(), "Capsula creata con successo!",
                "La tua capsula '" + title + "' è stata creata. Riceverai un'email il giorno " + openDate + " quando sarà il momento di aprirla.");
        return ResponseEntity.ok(capsulaTravaso.entity_dto(savedCapsula));
    }

    @GetMapping("/le-mie-caps/{id}")
    public ResponseEntity<?> getCaps(@PathVariable String id) {
        List<CapsulaDTO> caps = capsulaService.findAllById(id);
        return ResponseEntity.ok(caps);
    }

    /*
        @GetMapping("/capsula/{idCap}")
        public ResponseEntity<CapsulaDTO> getSelectedCap(@PathVariable String idCap){
            CapsulaDTO capsulaDTO = capsulaTravaso.entity_dto(capsulaService.findById(idCap));
            return ResponseEntity.ok(capsulaDTO);
        }
    */
    @GetMapping("/capsula/{idCap}")
    public ResponseEntity<CapsulaDTO> getSelectedCap(@PathVariable String idCap) {
        CapsulaDTO capsulaDTO = capsulaService.getInvitati(idCap);
        return ResponseEntity.ok(capsulaDTO);
    }


    //---------------------------------------------------------------------------------------
    //immagini capsule
    //---------------------------------------------------------------------------------------

    @GetMapping("/immagini_caps")
    public ResponseEntity<List<ImgCapsula>> getImg() {
        List<ImgCapsula> imgs = capsulaService.findAllImageCap();
        return ResponseEntity.ok(imgs);
    }

    //---------------------------------------------------------------------------------------
    //UPDATE - PUT
    //---------------------------------------------------------------------------------------

    @PutMapping("capsula/{id}/update")
    public ResponseEntity<String> updateCapsula(
            @PathVariable String id,
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam("pubblica") Boolean pubblica,
            @RequestParam(value = "media", required = false) List<MultipartFile> media,
            @RequestParam(value = "textFiles", required = false) List<MultipartFile> textFiles,
            @RequestParam(value = "removeMedia", required = false) List<String> removeMediaUrls,
            @RequestParam(value = "removeTextFiles", required = false) List<String> removeTextFileUrls,
            @RequestParam(value = "addMail", required = false) List<String> newInviti
    ) {
        try {
            // Trova la capsula esistente
            Capsula existingCapsula = capsulaService.findById(id);
            if (existingCapsula == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Capsula non trovata.");
            }

            // Aggiorna i campi di testo
            existingCapsula.setTitle(title);
            existingCapsula.setMessage(message);
            existingCapsula.setPubblica(pubblica);

            // Rimuove i file multimediali specificati dall'utente
            if (removeMediaUrls != null) {
                existingCapsula.getMedia().removeIf(newMedia -> removeMediaUrls.contains(newMedia.getUrl()));
            }

            // Rimuove i file di testo specificati dall'utente
            if (removeTextFileUrls != null) {
                existingCapsula.getTextFiles().removeIf(textFile -> removeTextFileUrls.contains(textFile.getUrl()));
            }

            // Aggiunge i nuovi file multimediali
            if (media != null) {
                for (MultipartFile file : media) {
                    if (file.getSize() > 3 * 1024 * 1024) {
                        return ResponseEntity.badRequest().body("File troppo grande: " + file.getOriginalFilename());
                    }
                    String url = cloudinaryService.uploadFile(file);
                    VisualMedia newMedia = new VisualMedia();
                    newMedia.setName(file.getOriginalFilename());
                    newMedia.setType(file.getContentType());
                    newMedia.setUrl(url);
                    newMedia.setCapsula(existingCapsula);
                    existingCapsula.getMedia().add(newMedia);
                }
            }

            // Aggiunge i nuovi file di testo
            if (textFiles != null) {
                for (MultipartFile file : textFiles) {
                    if (file.getSize() > 3 * 1024 * 1024) {
                        return ResponseEntity.badRequest().body("File troppo grande: " + file.getOriginalFilename());
                    }
                    String url = cloudinaryService.uploadFile(file);
                    TextFile newTextFile = new TextFile();
                    newTextFile.setName(file.getOriginalFilename());
                    newTextFile.setType(file.getContentType());
                    newTextFile.setUrl(url);
                    newTextFile.setCapsula(existingCapsula);
                    existingCapsula.getTextFiles().add(newTextFile);
                }
            }
            // Salva la capsula aggiornata
            Capsula savedCapsula = capsulaService.saveCap(existingCapsula);

            if (savedCapsula == null) {
                // Gestisci l'errore: la capsula non è stata salvata correttamente
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel salvataggio della capsula.");
            }

            if (newInviti != null) {
                for (String mailInvitata : newInviti) {
                    capsulaService.invitaUtente(savedCapsula, mailInvitata);
                }
            }

            return ResponseEntity.ok("Capsula aggiornata con successo!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiornamento.");
        }
    }


    @DeleteMapping("/capsula/{id}")
    public ResponseEntity<String> deleteCapsula(@PathVariable String id) {
        try {
            System.out.println("Tentativo di eliminazione capsula con ID: " + id);
            capsulaService.deleteCapsula(id);
            return ResponseEntity.ok("Capsula eliminata con successo.");
        } catch (EntityNotFoundException e) {
            System.err.println("Errore: Capsula non trovata - ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Capsula non trovata.");
        } catch (Exception e) {
            System.err.println("Errore durante l'eliminazione della capsula: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno.");
        }
    }

    //---------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------
    //PROFILO
    //---------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------

    @GetMapping("/profilo/{id}")
    public ResponseEntity<UtenteProfiloDTO> getUtente(@PathVariable String id) {
        UtenteProfiloDTO profilo = utenteService.getProfilo(id);
        return ResponseEntity.ok(profilo);
    }

    @PatchMapping("/profilo/{id}")
    public ResponseEntity<UtenteProfiloDTO> updateProfilo(
            @PathVariable String id,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam(value = "avatar", required = false) MultipartFile imgProfilo) throws IOException {

        ProfiloUpdateDTO dto = new ProfiloUpdateDTO();
        dto.setFullName(fullName);
        dto.setEmail(email);

        if (imgProfilo != null && !imgProfilo.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(imgProfilo);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                dto.setAvatar(imageUrl);
            } else {
                throw new RuntimeException("Errore nel caricamento dell'avatar.");
            }
        }

        return ResponseEntity.ok(utenteService.patchProfilo(id, dto));
    }

    //---------------------------------------------------------------------------------------
    // SEARCH BAR
    //---------------------------------------------------------------------------------------

    @GetMapping("/search-bar")
    public ResponseEntity<List<CapsulaDTO>> findPubbliche(@RequestParam String input){
        List<CapsulaDTO> caps = capsulaService.findPubbliche(input);
        return ResponseEntity.ok(caps);
    }

}
