package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.repository.AssetRepository;
import io.minio.*;
import io.minio.RemoveObjectArgs;
import org.springframework.stereotype.Service;
import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.model.Serie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.assetUrl}")
    private String urlPrefix;

    private final MinioClient minioClient;
    private final SerieService serieService;
    private final ArtistService artistService;
    private final AssetRepository assetRepository;

    public FileUploadService(MinioClient minioClient,
                        SerieService serieService,
                        ArtistService artistService,
                        AssetRepository assetRepository) {
        this.minioClient = minioClient;
        this.serieService = serieService;
        this.artistService = artistService;
        this.assetRepository = assetRepository;
    }

    public String handleImageUpload(MultipartFile file, String name, String description,
                                    String type, String active, String artistId, String collectionId) throws Exception {

        if (collectionId.isBlank()) collectionId = "0";
        if (artistId.isBlank()) artistId = "0";

        Asset asset = new Asset();
        asset.setName(name);
        asset.setDescription(description);
        asset.setType(type);
        asset.setActive(Boolean.parseBoolean(active));

        Optional<Serie> serie = serieService.getSerieById(Long.parseLong(collectionId));
        Optional<Artist> artist = artistService.getArtistById(Long.parseLong(artistId));

        serie.ifPresent(s -> asset.setSeries(List.of(s)));
        artist.ifPresent(a -> asset.setAuthors(List.of(a)));

        ensureBucketExists();

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        String fullUrl = urlPrefix + fileName;
        asset.setUrl(fullUrl);

        assetRepository.save(asset);

        return fullUrl;
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * Deletes an object from the Minio bucket
     * @param objectUrl The URL or filename of the object to delete
     * @throws Exception If an error occurs during deletion
     */
    public void deleteObject(String objectUrl) throws Exception {
        // Extract the filename from the URL if needed
        String objectName = objectUrl;
        if (objectUrl.startsWith(urlPrefix)) {
            objectName = objectUrl.substring(urlPrefix.length());
        }

        // Delete the object from the bucket
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public String uploadFile(MultipartFile file) throws Exception {

        ensureBucketExists();

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return urlPrefix + fileName;
    }
}
