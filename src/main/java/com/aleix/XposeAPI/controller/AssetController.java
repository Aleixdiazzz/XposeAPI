package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.service.AssetService;
import com.aleix.XposeAPI.service.AssetsFromService;
import com.aleix.XposeAPI.service.FileUploadService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Asset entities.
 * Provides endpoints for CRUD operations and additional functionalities related to Assets,
 * including file uploads and retrieving assets by series or artists.
 */
@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;
    private final FileUploadService fileUploadService;
    private final AssetsFromService assetsFromService;

    /**
     * Constructor for AssetController.
     * 
     * @param assetService Service for Asset entity operations
     * @param fileUploadService Service for file upload operations
     * @param assetsFromService Service for retrieving assets related to series or artists
     */
    public AssetController(AssetService assetService, FileUploadService fileUploadService, AssetsFromService assetsFromService) {
        this.assetService = assetService;
        this.fileUploadService = fileUploadService;
        this.assetsFromService = assetsFromService;
    }

    /**
     * Retrieves all assets.
     * 
     * @return List of all Asset entities
     */
    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    /**
     * Retrieves all assets associated with a specific serie.
     * 
     * @param id The ID of the serie
     * @return List of Asset entities associated with the serie
     */
    @GetMapping("/serie/{id}")
    public List<Asset> getAllSerieAssets(@PathVariable Long id ){
        return assetsFromService.getAllAssetsFromSerie(id);
    }

    /**
     * Retrieves all assets associated with a specific artist.
     * 
     * @param id The ID of the artist
     * @return List of Asset entities associated with the artist
     */
    @GetMapping("/artist/{id}")
    public List<Asset> getAllArtistAssets(@PathVariable Long id ){
        return assetsFromService.getAllAssetsFromArtist(id);
    }

    /**
     * Deletes an asset by its ID.
     * 
     * @param id The ID of the asset to delete
     * @return ResponseEntity with no content if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        if (assetService.deleteAsset(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves a specific asset by its ID.
     * 
     * @param id The ID of the asset to retrieve
     * @return ResponseEntity containing the Asset if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        Optional<Asset> asset = assetService.getAssetById(id);
        return asset.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Filters assets based on provided criteria.
     * 
     * @param name Optional name filter
     * @param type Optional type filter
     * @param active Optional active status filter
     * @param artistId Optional artist ID filter
     * @param collectionId Optional collection ID filter
     * @return List of Asset entities matching the filter criteria
     */
    @GetMapping("/filter")
    public List<Asset> filterAssets(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) Boolean active,
                                    @RequestParam(required = false) String artistId,
                                    @RequestParam(required = false) String collectionId){
        return assetService.filterAssets(name, type, active, artistId, collectionId);
    }

    /**
     * Creates a new asset with an associated file.
     * 
     * @param asset The Asset entity to create
     * @param file The file to upload and associate with the asset
     * @return ResponseEntity containing the created Asset
     * @throws Exception If there's an error during file upload
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Asset> createAsset(@RequestPart("asset") Asset asset, @RequestPart("file") MultipartFile file) throws Exception {

        Asset savedAsset = assetService.createAsset(asset, file);
        return ResponseEntity.ok(savedAsset);
    }

    /**
     * Updates an existing asset with optional file replacement.
     * 
     * @param id The ID of the asset to update
     * @param asset The updated Asset entity data
     * @param file Optional new file to replace the existing one
     * @return ResponseEntity containing the updated Asset if found, or 404 Not Found
     * @throws Exception If there's an error during file upload
     */
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(
            @PathVariable Long id,
            @RequestPart("asset") Asset asset, @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        if (file != null && !file.isEmpty()) {
            asset.setUrl(fileUploadService.uploadFile(file));
        }
        Optional<Asset> updatedAsset = assetService.updateAsset(id, asset);
        return updatedAsset.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



}
