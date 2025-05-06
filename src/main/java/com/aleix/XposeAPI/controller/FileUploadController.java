package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.service.ArtistService;
import com.aleix.XposeAPI.service.AssetService;
import com.aleix.XposeAPI.service.SerieService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    private final MinioClient minioClient;
    private final SerieService serieService;
    private final ArtistService artistService;
    private final AssetService assetService;

    @Value("${minio.bucket}")
    private String bucketName;
    @Value("${minio.assetUrl}")
    private String urlPrefix;

    public FileUploadController(MinioClient minioClient, SerieService serieService, ArtistService artistService, AssetService assetService) {
        this.minioClient = minioClient;
        this.serieService = serieService;
        this.artistService = artistService;
        this.assetService = assetService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file,
                                              @RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("type") String type,
                                              @RequestParam("active") String active,
                                              @RequestParam("artistId") String artistId,
                                              @RequestParam("collectionId") String collectionId) {

        try {

            if (collectionId.isBlank()){
                collectionId = "0";
            }
            if (artistId.isBlank()){
                artistId = "0";
            }

            Asset asset = new Asset();
            asset.setName(name);
            asset.setDescription(description);
            asset.setType(type);
            asset.setActive(Boolean.parseBoolean(active));


            Optional<Serie> serie = serieService.getSerieById(Long.valueOf(collectionId));
            Optional<Artist> artist = artistService.getArtistById(Long.valueOf(artistId));

            List<Serie> series = new ArrayList<>();
            List<Artist> artists = new ArrayList<>();


            if (serie.isPresent() && artist.isPresent()) {
                series.add(serie.get());
                artists.add(artist.get());

                asset.setSeries(series);
                asset.setAuthors(artists);
            }

            // Ensure bucket exists
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            asset.setUrl(urlPrefix + fileName);
            assetService.createAsset(asset);

            return ResponseEntity.ok("Uploaded as: " + urlPrefix + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}
