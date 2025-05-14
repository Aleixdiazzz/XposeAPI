package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetsFromService {

    private final AssetRepository assetRepository;

    public AssetsFromService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAssetsFromSerie(Long id){
        return assetRepository.findAll().stream()
                .filter(asset -> asset.getSeries() != null &&
                        asset.getSeries().stream()
                                .anyMatch(serie -> serie.getId().equals(id)))
                .toList();
    }

    public List<Asset> getAllAssetsFromArtist(Long id){
        return assetRepository.findAll().stream()
                .filter(asset -> asset.getAuthors() != null &&
                        asset.getAuthors().stream()
                                .anyMatch(author -> author.getId().equals(id)))
                .toList();
    }

}
