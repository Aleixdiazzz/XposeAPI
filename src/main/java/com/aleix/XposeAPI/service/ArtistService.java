package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.User;
import com.aleix.XposeAPI.repository.ArtistRepository;
import com.aleix.XposeAPI.specification.ArtistSpecifications;
import com.aleix.XposeAPI.specification.UserSpecifications;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Artist entities.
 * Provides methods for CRUD operations and additional business logic related to Artists.
 */
@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    /**
     * Constructor for ArtistService.
     * 
     * @param artistRepository Repository for Artist entity operations
     */
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    /**
     * Retrieves all artists from the database.
     * 
     * @return List of all Artist entities
     */
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    /**
     * Retrieves a specific artist by its ID.
     * 
     * @param id The ID of the artist to retrieve
     * @return Optional containing the Artist if found, empty otherwise
     */
    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    /**
     * Creates a new artist in the database.
     * 
     * @param artist The Artist entity to create
     * @return The saved Artist entity with generated ID
     */
    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    /**
     * Updates an existing artist with new details.
     * 
     * @param id The ID of the artist to update
     * @param artistDetails The updated Artist entity data
     * @return Optional containing the updated Artist if found, empty otherwise
     */
    public Optional<Artist> updateArtist(Long id, Artist artistDetails) {
        return artistRepository.findById(id).map(artist -> {
            artist.setName(artistDetails.getName());
            artist.setSurname(artistDetails.getSurname());
            artist.setArtisticName(artistDetails.getArtisticName());
            artist.setContactInformation(artistDetails.getContactInformation());
            artist.setAbout(artistDetails.getAbout());
            return artistRepository.save(artist);
        });
    }

    /**
     * Deletes an artist by its ID.
     * 
     * @param id The ID of the artist to delete
     * @return true if the artist was deleted, false if it wasn't found
     */
    public boolean deleteArtist(Long id) {
        if (artistRepository.existsById(id)) {
            artistRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Filters artists based on provided criteria.
     * 
     * @param name Optional name filter
     * @param surname Optional surname filter
     * @param artisticName Optional artistic name filter
     * @return List of Artist entities matching the filter criteria
     */
    public List<Artist> filterArtists (String name, String surname, String artisticName){
        return artistRepository.findAll(ArtistSpecifications.filterArtists(name, surname, artisticName));
    }
}
