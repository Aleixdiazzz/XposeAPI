package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Asset;
import com.aleix.XposeAPI.repository.AssetRepository;
import com.aleix.XposeAPI.specification.ArtistSpecifications;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public Optional<Asset> updateAsset(Long id, Asset assetDetails) {
        return assetRepository.findById(id).map(asset -> {
            asset.setName(assetDetails.getName());
            asset.setDescription(assetDetails.getDescription());
            asset.setType(assetDetails.getType());
            asset.setActive(assetDetails.isActive() );
            asset.setAuthors(assetDetails.getAuthors());
            asset.setSeries(assetDetails.getSeries());
            return assetRepository.save(asset);
        });
    }

    public boolean deleteAsset(Long id) {
        if (assetRepository.existsById(id)) {
            assetRepository.deleteById(id);
            return true;
        }
        return false;
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

    //TODO ADD SPECIFICATION
    /*public List<Asset> filterArtists (String name, String surname, String artisticName){
        return assetRepository.findAll(ArtistSpecifications.filterArtists(name, surname, artisticName));
    }*/
}
