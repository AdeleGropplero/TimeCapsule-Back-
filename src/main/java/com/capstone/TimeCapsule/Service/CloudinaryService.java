package com.capstone.TimeCapsule.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        // Carica il file su Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));

        // Restituisce l'URL del file caricato
        return  uploadResult.get("url").toString();
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
