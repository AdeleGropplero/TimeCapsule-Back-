package com.capstone.TimeCapsule.Service;

import com.capstone.TimeCapsule.Mapper_travasi.CapsulaTravaso;
import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Payload.CapsulaDTO;
import com.capstone.TimeCapsule.Repository.CapsulaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class CapsulaService {

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
}
