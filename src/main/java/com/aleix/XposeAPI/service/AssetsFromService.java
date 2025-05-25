package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for retrieving assets related to specific series or artists.
 * Provides methods to filter assets based on their relationships with other entities.
 */
@Service
public class AssetsFromService {

    private final AssetRepository assetRepository;

    /**
     * Constructor for AssetsFromService.
     * 
     * @param assetRepository Repository for Asset entity operations
     */
    public AssetsFromService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    /**
     * Retrieves all assets associated with a specific serie.
     * 
     * @param id The ID of the serie to find assets for
     * @return List of Asset entities associated with the specified serie
     */
    public List<Asset> getAllAssetsFromSerie(Long id){
        return assetRepository.findAll().stream()
                .filter(asset -> asset.getSeries() != null &&
                        asset.getSeries().stream()
                                .anyMatch(serie -> serie.getId().equals(id)))
                .toList();
    }

    /**
     * Retrieves all assets associated with a specific artist.
     * 
     * @param id The ID of the artist to find assets for
     * @return List of Asset entities associated with the specified artist
     */
    public List<Asset> getAllAssetsFromArtist(Long id){
        return assetRepository.findAll().stream()
                .filter(asset -> asset.getAuthors() != null &&
                        asset.getAuthors().stream()
                                .anyMatch(author -> author.getId().equals(id)))
                .toList();
    }

}
