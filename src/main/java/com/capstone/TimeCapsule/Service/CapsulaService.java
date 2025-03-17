package com.capstone.TimeCapsule.Service;

import com.capstone.TimeCapsule.Exception.ResourceNotFoundException;
import com.capstone.TimeCapsule.Mapper_travasi.CapsulaTravaso;
import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Model.MediaFile.TextFile;
import com.capstone.TimeCapsule.Model.MediaFile.VisualMedia;
import com.capstone.TimeCapsule.Payload.CapsulaDTO;
import com.capstone.TimeCapsule.Repository.CapsulaRepository;
import com.capstone.TimeCapsule.Repository.TextFilesRepository;
import com.capstone.TimeCapsule.Repository.VisualMediaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
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
    private CapsulaTravaso capsulaTravaso;

    public Capsula saveCap(Capsula caps) {
        return capsulaRepository.save(caps);
    }

    public Set<Capsula> getAllCapsule() {
        return new HashSet<>(capsulaRepository.findAll());
    }

    public List<CapsulaDTO> findAllById(String idUtente) {
        UUID id = UUID.fromString(idUtente);
        List<Capsula> capsule = capsulaRepository.findByUtente_Id(id);

        List<CapsulaDTO> capsuleDTO = new ArrayList<>();
        for (Capsula cap : capsule) {
            CapsulaDTO dto = capsulaTravaso.entity_dto(cap);
            capsuleDTO.add(dto);
        }
        return capsuleDTO;
    }

    public Capsula findById(String idCapsula){
        UUID id = UUID.fromString(idCapsula);
        Capsula cap = capsulaRepository.findById(id).orElseThrow(()-> new RuntimeException("Capsula non trovata con id: " + id));
       /* CapsulaDTO capDTO = capsulaTravaso.entity_dto(cap);*/
        return cap;
    }


    @Transactional
    public void deleteCapsula(String idCapsula) {
        UUID id = UUID.fromString(idCapsula);
        Capsula cap = capsulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Capsula non trovata"));

        // Rimozione dei media associati usando un Iterator
        Iterator<VisualMedia> mediaIterator = cap.getMedia().iterator();
        while (mediaIterator.hasNext()) {
            VisualMedia media = mediaIterator.next();
            visualMediaRepository.delete(media);
            mediaIterator.remove(); // ✅ Sicuro
        }

        // Rimozione dei file di testo associati usando un Iterator
        Iterator<TextFile> textFileIterator = cap.getTextFiles().iterator();
        while (textFileIterator.hasNext()) {
            TextFile textFile = textFileIterator.next();
            textFilesRepository.delete(textFile);
            textFileIterator.remove(); // ✅ Sicuro
        }

        // Elimina la capsula
        capsulaRepository.delete(cap);
    }




}
