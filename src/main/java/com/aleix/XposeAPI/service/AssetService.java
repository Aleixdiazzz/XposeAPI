package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.controller.FileUploadController;
import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.repository.AssetRepository;
import com.aleix.XposeAPI.specification.ArtistSpecifications;
import com.aleix.XposeAPI.specification.AssetSpecifications;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Asset entities.
 * Provides methods for CRUD operations and additional business logic related to Assets,
 * including file upload and management functionality.
 */
@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final FileUploadService fileUploadService;

    /**
     * Constructor for AssetService.
     * 
     * @param assetRepository Repository for Asset entity operations
     * @param fileUploadService Service for file upload operations
     */
    public AssetService(AssetRepository assetRepository, FileUploadService fileUploadService) {
        this.assetRepository = assetRepository;
        this.fileUploadService = fileUploadService;
    }

    /**
     * Retrieves all assets from the database.
     * 
     * @return List of all Asset entities
     */
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    /**
     * Retrieves a specific asset by its ID.
     * 
     * @param id The ID of the asset to retrieve
     * @return Optional containing the Asset if found, empty otherwise
     */
    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    /**
     * Creates a new asset in the database with an associated file.
     * 
     * @param asset The Asset entity to create
     * @param file The file to upload and associate with the asset
     * @return The saved Asset entity with generated ID and file URL
     * @throws Exception If there's an error during file upload
     */
    public Asset createAsset(Asset asset, MultipartFile file) throws Exception {
        asset.setUrl(fileUploadService.uploadFile(file));
        return assetRepository.save(asset);
    }

    /**
     * Updates an existing asset with new details.
     * 
     * @param id The ID of the asset to update
     * @param assetDetails The updated Asset entity data
     * @return Optional containing the updated Asset if found, empty otherwise
     */
    public Optional<Asset> updateAsset(Long id, Asset assetDetails) {
        return assetRepository.findById(id).map(asset -> {
            asset.setName(assetDetails.getName());
            asset.setDescription(assetDetails.getDescription());
            asset.setType(assetDetails.getType());
            asset.setActive(assetDetails.isActive() );
            asset.setAuthors(assetDetails.getAuthors());
            asset.setSeries(assetDetails.getSeries());
            asset.setUrl(assetDetails.getUrl());
            return assetRepository.save(asset);
        });
    }

    /**
     * Deletes an asset by its ID and removes the associated file.
     * 
     * @param id The ID of the asset to delete
     * @return true if the asset was deleted, false if it wasn't found
     * @throws RuntimeException If there's an error during file deletion
     */
    public boolean deleteAsset(Long id) {
        if (assetRepository.existsById(id) && assetRepository.findById(id).isPresent()) {

            Asset asset = assetRepository.findById(id).get();

            try {

                fileUploadService.deleteObject(asset.getUrl());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            assetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Filters assets based on provided criteria.
     * 
     * @param name Optional name filter
     * @param type Optional type filter
     * @param active Optional active status filter
     * @param artistId Optional artist ID filter
     * @param serieId Optional serie ID filter
     * @return List of Asset entities matching the filter criteria
     */
    public List<Asset> filterAssets (String name, String type, Boolean active, String artistId, String serieId){
        return assetRepository.findAll(AssetSpecifications.filterAssets(name, type, active, artistId,  serieId));
    }
}
