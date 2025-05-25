package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.User;
import com.aleix.XposeAPI.service.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Artist entities.
 * Provides endpoints for CRUD operations and additional functionalities related to Artists.
 */
@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    /**
     * Constructor for ArtistController.
     * 
     * @param artistService Service for Artist entity operations
     */
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * Retrieves all artists.
     * 
     * @return List of all Artist entities
     */
    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    /**
     * Retrieves a specific artist by its ID.
     * 
     * @param id The ID of the artist to retrieve
     * @return ResponseEntity containing the Artist if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        Optional<Artist> artist = artistService.getArtistById(id);
        return artist.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Filters artists based on provided criteria.
     * 
     * @param name Optional name filter
     * @param surname Optional surname filter
     * @param artisticName Optional artistic name filter
     * @return List of Artist entities matching the filter criteria
     */
    @GetMapping("/filter")
    public List<Artist> filterArtists(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String surname,
                                  @RequestParam(required = false) String artisticName) {
        return artistService.filterArtists(name, surname, artisticName);
    }

    /**
     * Creates a new artist.
     * 
     * @param artist The Artist entity to create
     * @return ResponseEntity containing the created Artist
     */
    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist savedArtist = artistService.createArtist(artist);
        return ResponseEntity.ok(savedArtist);
    }

    /**
     * Updates an existing artist.
     * 
     * @param id The ID of the artist to update
     * @param artist The updated Artist entity data
     * @return ResponseEntity containing the updated Artist if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        Optional<Artist> updatedArtist = artistService.updateArtist(id, artist);
        return updatedArtist.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an artist by its ID.
     * 
     * @param id The ID of the artist to delete
     * @return ResponseEntity with no content if deleted, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        if (artistService.deleteArtist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
