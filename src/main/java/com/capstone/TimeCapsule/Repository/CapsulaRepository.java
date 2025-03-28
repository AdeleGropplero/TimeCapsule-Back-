package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.Capsula;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    /*@EntityGraph(attributePaths = "inviti")
    Optional<Capsula> findWithInvitiById(UUID id);
    L'attributePaths nell'annotation @EntityGraph specifica quali relazioni (o "percorsi di attributi")
    dell'entità devono essere caricati eagermente (immediatamente) insieme all'entità principale.*/

    @Query("SELECT c FROM Capsula c LEFT JOIN FETCH c.inviti WHERE c.id = :id")
    Optional<Capsula> findByIdWithInviti(@Param("id") UUID id);

    List<Capsula> findByPubblicaTrueAndTitleContainingIgnoreCase(String input);

/*
SELECT * FROM capsula
WHERE pubblica = true
AND LOWER(titolo) LIKE LOWER('%input%');
*/
}