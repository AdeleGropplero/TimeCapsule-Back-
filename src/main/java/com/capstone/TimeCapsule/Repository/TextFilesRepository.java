package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.MediaFile.TextFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TextFilesRepository extends JpaRepository<TextFile, UUID> {
}
