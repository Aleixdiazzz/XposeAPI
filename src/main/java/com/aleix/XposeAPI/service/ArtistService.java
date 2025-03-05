package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.repository.ArtistRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

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

    public boolean deleteArtist(Long id) {
        if (artistRepository.existsById(id)) {
            artistRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

