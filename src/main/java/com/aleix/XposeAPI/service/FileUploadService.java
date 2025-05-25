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

/**
 * Service class for handling file uploads to MinIO object storage.
 * Provides methods for uploading, retrieving, and deleting files from MinIO buckets,
 * as well as creating associated Asset entities with metadata.
 */
@Service
public class FileUploadService {

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.logoBucket}")
    private String logoBucketName;

    @Value("${minio.assetUrl}")
    private String urlPrefix;

    @Value("${minio.logoUrl}")
    private String logoUrlPrefix;

    private final MinioClient minioClient;
    private final SerieService serieService;
    private final ArtistService artistService;
    private final AssetRepository assetRepository;

    /**
     * Constructor for FileUploadService.
     * 
     * @param minioClient Client for MinIO operations
     * @param serieService Service for Serie entity operations
     * @param artistService Service for Artist entity operations
     * @param assetRepository Repository for Asset entity operations
     */
    public FileUploadService(MinioClient minioClient,
                        SerieService serieService,
                        ArtistService artistService,
                        AssetRepository assetRepository) {
        this.minioClient = minioClient;
        this.serieService = serieService;
        this.artistService = artistService;
        this.assetRepository = assetRepository;
    }

    /**
     * Handles image upload with associated metadata and creates an Asset entity.
     * 
     * @param file The file to upload
     * @param name The name for the asset
     * @param description The description for the asset
     * @param type The type of the asset
     * @param active Whether the asset is active
     * @param artistId The ID of the associated artist (optional)
     * @param collectionId The ID of the associated collection/serie (optional)
     * @return The URL of the uploaded file
     * @throws Exception If an error occurs during upload or asset creation
     */
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

    /**
     * Ensures that the MinIO bucket exists, creating it if necessary.
     * 
     * @throws Exception If an error occurs during bucket existence check or creation
     */
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

    /**
     * Uploads a file to the MinIO bucket.
     * 
     * @param file The file to upload
     * @return The URL of the uploaded file
     * @throws Exception If an error occurs during upload
     */
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

    /**
     * Uploads a logo file to the dedicated logo bucket in MinIO.
     * 
     * @param file The logo file to upload
     * @return The URL of the uploaded logo
     * @throws Exception If an error occurs during upload
     */
    public String uploadLogo(MultipartFile file) throws Exception {

        ensureBucketExists();

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(logoBucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return logoUrlPrefix + fileName;
    }

}
