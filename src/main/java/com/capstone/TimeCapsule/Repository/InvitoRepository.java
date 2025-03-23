package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.MediaFile.Invito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvitoRepository extends JpaRepository<Invito, UUID> {
    List<Invito> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM Invito i WHERE i.capsula.id = :capsulaId")
    void deleteByCapsulaId(@Param("capsulaId") UUID capsulaId);
}
