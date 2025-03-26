package com.capstone.TimeCapsule.Repository;

import com.capstone.TimeCapsule.Model.ImgCapsula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgCapRepository extends JpaRepository<ImgCapsula, Long> {
}
