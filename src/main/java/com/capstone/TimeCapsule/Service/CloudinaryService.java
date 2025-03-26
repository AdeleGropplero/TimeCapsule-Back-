package com.capstone.TimeCapsule.Service;

import com.capstone.TimeCapsule.Model.ImgCapsula;
import com.capstone.TimeCapsule.Repository.ImgCapRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ImgCapRepository imgCapRepository;

    public String uploadFile(MultipartFile file) throws IOException {
        // Ottieni il nome originale del file senza l'estensione
        String originalFilename = file.getOriginalFilename();

        if (originalFilename != null) {
            originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        } else {
            originalFilename = "file_senza_nome";
        }

        // Determina il tipo di file (MIME type)
        String contentType = file.getContentType();

        // Imposta le opzioni di caricamento in base al tipo di file
        Map<String, Object> uploadOptions;

        if (contentType != null && contentType.startsWith("image/")) {
            // Opzioni per immagini (compressione e formato ottimizzato)
            uploadOptions = ObjectUtils.asMap(
                    "resource_type", "image",
                    "public_id", originalFilename,
                    "quality", 70,           // Imposta la qualità delle immagini al 60%
                    "format", "jpg",        // Converte tutto in JPEG per compatibilità
                    "width", 1080,           // Ridimensiona l'immagine
                    "height", 1080,
                    "crop", "limit"          // Mantiene le proporzioni senza deformare l'immagine
            );
        } else if (contentType != null && contentType.startsWith("video/")) {
            // Opzioni per video (compressione e riduzione della qualità)
            uploadOptions = ObjectUtils.asMap(
                    "resource_type", "video",
                    "public_id", originalFilename,
                    "quality", 70,        // Compressione automatica per video
                    "format", "mp4"           // Conversione in MP4 per compatibilità
            );
        } else {
            // Opzioni per altri file (PDF, documenti, ecc.)
            uploadOptions = ObjectUtils.asMap(
                    "resource_type", "auto",  // Cloudinary determina automaticamente il tipo di file
                    "public_id", originalFilename
            );
        }

        // Carica il file su Cloudinary con le opzioni scelte
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);

        // Restituisce l'URL del file caricato
        return uploadResult.get("url").toString();
    }

    //----------------------------------------------------------------------------------------
    // Caricamento immagini capsule
    //----------------------------------------------------------------------------------------

    // Metodo per caricare una singola immagine da file locale
    public String uploadImageFromFile(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("secure_url");  // Ottieni l'URL sicuro dell'immagine

        // Salva l'immagine nel database
        ImgCapsula image = new ImgCapsula();
        image.setUrl(imageUrl);
        imgCapRepository.save(image);  // Salva l'immagine nel database

        return imageUrl;  // Restituisce l'URL dell'immagine
    }

    // Metodo per caricare tutte le immagini da una cartella locale
    public List<String> uploadImagesFromFolder(String folderPath) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<String> imageUrls = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImage(file)) {
                    imageUrls.add(uploadImageFromFile(file));  // Carica l'immagine e salva nel database
                }
            }
        }
        return imageUrls;  // Restituisce gli URL delle immagini caricate
    }

    private boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }

}
