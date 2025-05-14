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

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final FileUploadService fileUploadService;

    public AssetService(AssetRepository assetRepository, FileUploadService fileUploadService) {
        this.assetRepository = assetRepository;
        this.fileUploadService = fileUploadService;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public Asset createAsset(Asset asset, MultipartFile file) throws Exception {
        asset.setUrl(fileUploadService.uploadFile(file));
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
            asset.setUrl(assetDetails.getUrl());
            return assetRepository.save(asset);
        });
    }

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
    public List<Asset> filterAssets (String name, String type, Boolean active, String artistId, String serieId){
        return assetRepository.findAll(AssetSpecifications.filterAssets(name, type, active, artistId,  serieId));
    }
}
