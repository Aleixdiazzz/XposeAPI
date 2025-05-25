package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.CollectionsPublicRS;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.service.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Serie entities.
 * Provides endpoints for CRUD operations and additional functionalities related to Series.
 */
@RestController
@RequestMapping("/series")
public class SerieController {

    private final SerieService serieService;

    /**
     * Constructor for SerieController.
     * 
     * @param serieService Service for Serie entity operations
     */
    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    /**
     * Retrieves all series.
     * 
     * @return List of all Serie entities
     */
    @GetMapping
    public List<Serie> getAllSeries() {
        return serieService.getAllSeries();
    }

    /**
     * Retrieves all public collections with their associated assets.
     * 
     * @return List of public collections with assets
     */
    @GetMapping("/public-collections")
    public List<CollectionsPublicRS> getPublicCollections() {
        return serieService.getAllPublicSeries();
    }

    /**
     * Retrieves a specific serie by its ID.
     * 
     * @param id The ID of the serie to retrieve
     * @return ResponseEntity containing the Serie if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Serie> getSerieById(@PathVariable Long id) {
        Optional<Serie> serie = serieService.getSerieById(id);
        return serie.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Filters series based on provided criteria.
     * 
     * @param name Optional name filter
     * @param artistId Optional artist ID filter
     * @param active Optional active status filter
     * @return List of Serie entities matching the filter criteria
     */
    @GetMapping("/filter")
    public List<Serie> filterSeries(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String artistId,
                                      @RequestParam(required = false) Boolean active) {
        return serieService.filterSeries(name, artistId, active);
    }

    /**
     * Creates a new serie.
     * 
     * @param serie The Serie entity to create
     * @return ResponseEntity containing the created Serie
     */
    @PostMapping
    public ResponseEntity<Serie> createSerie(@RequestBody Serie serie) {
        Serie savedSerie = serieService.createSerie(serie);
        return ResponseEntity.ok(savedSerie);
    }

    /**
     * Updates an existing serie.
     * 
     * @param id The ID of the serie to update
     * @param serie The updated Serie entity data
     * @return ResponseEntity containing the updated Serie if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Serie> updateSerie(@PathVariable Long id, @RequestBody Serie serie) {
        Optional<Serie> updatedSerie = serieService.updateSerie(id, serie);
        return updatedSerie.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a serie by its ID.
     * 
     * @param id The ID of the serie to delete
     * @return ResponseEntity with no content if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerie(@PathVariable Long id) {
        if (serieService.deleteSerie(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
