package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.Capsula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CapsulaRepository extends JpaRepository<Capsula, UUID> {
    //restituisce tutte le capsule associate a un utente specifico
    List<Capsula> findByUtenti_Id(UUID idUtente);

    // Trova tutte le capsule con una data di apertura specifica
    List<Capsula> findByOpenDate(LocalDate openDate);

    // Trova tutte le capsule pubbliche
    List<Capsula> findByPubblicaTrue(); //da usare per il search

    // Elimina una capsula specifica
    @Modifying
    @Query("DELETE FROM Capsula c WHERE c.id = :id")
    void deleteCapsulaById(@Param("id") UUID id);
}