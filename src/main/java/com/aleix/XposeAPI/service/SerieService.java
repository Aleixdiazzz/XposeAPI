package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.CollectionsPublicRS;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.repository.SerieRepository;
import com.aleix.XposeAPI.specification.ArtistSpecifications;
import com.aleix.XposeAPI.specification.SerieSpecifications;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Serie entities.
 * Provides methods for CRUD operations and additional business logic related to Series.
 */
@Service
public class SerieService {

    private final SerieRepository serieRepository;
    private final AssetsFromService assetsFromService;

    /**
     * Constructor for SerieService.
     * 
     * @param serieRepository Repository for Serie entity operations
     * @param assetsFromService Service for retrieving assets related to series
     */
    public SerieService(SerieRepository serieRepository, AssetsFromService assetsFromService) {
        this.serieRepository = serieRepository;
        this.assetsFromService = assetsFromService;
    }

    /**
     * Retrieves all series from the database.
     * 
     * @return List of all Serie entities
     */
    public List<Serie> getAllSeries() {
        return serieRepository.findAll();
    }

    /**
     * Retrieves a specific serie by its ID.
     * 
     * @param id The ID of the serie to retrieve
     * @return Optional containing the Serie if found, empty otherwise
     */
    public Optional<Serie> getSerieById(Long id) {
        return serieRepository.findById(id);
    }

    /**
     * Creates a new serie in the database.
     * 
     * @param serie The Serie entity to create
     * @return The saved Serie entity with generated ID
     */
    public Serie createSerie(Serie serie) {
        return serieRepository.save(serie);
    }

    /**
     * Updates an existing serie with new details.
     * 
     * @param id The ID of the serie to update
     * @param serieDetails The updated Serie entity data
     * @return Optional containing the updated Serie if found, empty otherwise
     */
    public Optional<Serie> updateSerie(Long id, Serie serieDetails) {
        return serieRepository.findById(id).map(serie -> {
            serie.setName(serieDetails.getName());
            serie.setDescription(serieDetails.getDescription());
            serie.setActive(serieDetails.isActive());
            serie.setArtists(serieDetails.getArtists());
            return serieRepository.save(serie);
        });
    }

    /**
     * Deletes a serie by its ID.
     * 
     * @param id The ID of the serie to delete
     * @return true if the serie was deleted, false if it wasn't found
     */
    public boolean deleteSerie(Long id) {
        if (serieRepository.existsById(id)) {
            serieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Filters series based on provided criteria.
     * 
     * @param name Optional name filter
     * @param artistId Optional artist ID filter
     * @param active Optional active status filter
     * @return List of Serie entities matching the filter criteria
     */
    public List<Serie> filterSeries (String name, String artistId, Boolean active){
        return serieRepository.findAll(SerieSpecifications.filterSeries(name, artistId, active));
    }

    /**
     * Retrieves all active series with their associated assets for public display.
     * 
     * @return List of CollectionsPublicRS containing series and their assets
     */
    public List<CollectionsPublicRS> getAllPublicSeries() {
        List<CollectionsPublicRS> collectionsPublicRS = new ArrayList<>();
        List<Serie> series = serieRepository.findByActiveTrue();

        for (Serie serie : series) {
            CollectionsPublicRS collectionsPublicRs = new CollectionsPublicRS();
            collectionsPublicRs.setSerie(serie);
            collectionsPublicRs.setAssets(assetsFromService.getAllAssetsFromSerie(serie.getId()));
            collectionsPublicRs.setImageUrl(collectionsPublicRs.getAssets().getFirst().getUrl());
            collectionsPublicRS.add(collectionsPublicRs);
        }

        return collectionsPublicRS;
    }
}
