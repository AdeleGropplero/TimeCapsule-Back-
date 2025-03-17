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
    List<Capsula> findByUtente_Id(UUID idUtente);
    List<Capsula> findByOpenDate(LocalDate openDate);
    @Modifying
    @Query("DELETE FROM Capsula c WHERE c.id = :id")
    void deleteCapsulaById(@Param("id") UUID id);
}