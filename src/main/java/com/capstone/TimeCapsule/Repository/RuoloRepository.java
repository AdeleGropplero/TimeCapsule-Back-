package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Enum.ERuoli;
import com.capstone.TimeCapsule.Model.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    Optional<Ruolo> findByNome(ERuoli nome);
    boolean existsByNome(ERuoli nome);

}
