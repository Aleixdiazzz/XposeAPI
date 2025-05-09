package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.service.AssetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
