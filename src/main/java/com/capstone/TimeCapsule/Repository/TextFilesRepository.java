package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.MediaFile.TextFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TextFilesRepository extends JpaRepository<TextFile, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM TextFile t WHERE t.capsula.id = :capsulaId")
    void deleteMediaByCapsulaId(@Param("capsulaId") UUID capsulaId);
}
