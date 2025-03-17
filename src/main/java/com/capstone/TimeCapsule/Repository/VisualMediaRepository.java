package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.MediaFile.VisualMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisualMediaRepository extends JpaRepository<VisualMedia, UUID> {
}
