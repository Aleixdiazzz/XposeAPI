package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.service.AssetService;
import com.aleix.XposeAPI.service.FileUploadService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;
    private final FileUploadService fileUploadService;

    public AssetController(AssetService assetService, FileUploadService fileUploadService) {
        this.assetService = assetService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/serie/{id}")
    public List<Asset> getAllSerieAssets(@PathVariable Long id ){
        return assetService.getAllAssetsFromSerie(id);
    }

    @GetMapping("/artist/{id}")
    public List<Asset> getAllArtistAssets(@PathVariable Long id ){
        return assetService.getAllAssetsFromArtist(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        if (assetService.deleteAsset(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        Optional<Asset> asset = assetService.getAssetById(id);
        return asset.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public List<Asset> filterAssets(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) Boolean active,
                                    @RequestParam(required = false) String artistId,
                                    @RequestParam(required = false) String collectionId){
        return assetService.filterAssets(name, type, active, artistId, collectionId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Asset> createAsset(@RequestPart("asset") Asset asset, @RequestPart("file") MultipartFile file) throws Exception {

        Asset savedAsset = assetService.createAsset(asset, file);
        return ResponseEntity.ok(savedAsset);
    }

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
