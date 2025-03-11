package com.capstone.TimeCapsule.Service;

import com.capstone.TimeCapsule.Enum.ERuoli;
import com.capstone.TimeCapsule.Model.Ruolo;
import com.capstone.TimeCapsule.Repository.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuoloService {
    @Autowired
    RuoloRepository ruoloRepository;

    public void insertRuolo(Ruolo ruolo){
        ruoloRepository.save(ruolo);
    }

    public void insertRuoli(List<Ruolo> ruoli) {
        ruoloRepository.saveAll(ruoli);
    }

    public boolean existsByNome(ERuoli nome) {
        return ruoloRepository.existsByNome(nome);
    }

    public Ruolo getRuolo(Long id){
        return ruoloRepository.findById(id).orElseThrow();
    }
}
