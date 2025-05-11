package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.service.AssetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
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



}
