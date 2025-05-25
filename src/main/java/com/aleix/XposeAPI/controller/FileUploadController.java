package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/file")
public class FileUploadController {

    private final FileUploadService fileUploadService;


    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * Handles the upload of an image file along with its metadata.
     *
     * @param file the image file to upload
     * @param name the name of the image
     * @param description a description of the image
     * @param type the type/category of the image
     * @param active a flag indicating whether the image is active
     * @param artistId the ID of the artist associated with the image
     * @param collectionId the ID of the collection the image belongs to
     * @return a {@link ResponseEntity} containing the URL of the uploaded image if successful,
     *         or an error message if the upload fails
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("active") String active,
            @RequestParam("artistId") String artistId,
            @RequestParam("collectionId") String collectionId) {

        try {
            String url = fileUploadService.handleImageUpload(file, name, description, type, active, artistId, collectionId);
            return ResponseEntity.ok("Uploaded as: " + url);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    /**
     * Deletes a file from storage based on the provided URL.
     *
     * @param url the URL of the file to delete
     * @return a {@link ResponseEntity} confirming deletion if successful,
     *         or an error message if the deletion fails
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("url") String url) {
        try {
            fileUploadService.deleteObject(url);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed: " + e.getMessage());
        }
    }

}
