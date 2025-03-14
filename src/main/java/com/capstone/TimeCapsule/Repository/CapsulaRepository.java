package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.Capsula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CapsulaRepository extends JpaRepository<Capsula, UUID> {
    List<Capsula> findByUtente_Id(UUID idUtente);
    List<Capsula> findByOpenDate(LocalDate openDate);
}