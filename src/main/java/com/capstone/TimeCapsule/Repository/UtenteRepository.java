package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.Capsula;
import com.capstone.TimeCapsule.Model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    //check login
    boolean existsByEmailAndPassword(String email, String password);

    //check duplicate key
    boolean existsByEmail(String email);

    Optional<Utente>findByEmail(String email);

    @Query("SELECT c FROM Capsula c JOIN c.utenti u WHERE u.id = :idUtente")
    List<Capsula> findByUtenteId(@Param("idUtente") UUID idUtente);


}
