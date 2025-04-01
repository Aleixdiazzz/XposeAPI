package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.User;
import com.aleix.XposeAPI.service.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        Optional<Artist> artist = artistService.getArtistById(id);
        return artist.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public List<Artist> filterArtists(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String surname,
                                  @RequestParam(required = false) String artisticName) {
        return artistService.filterArtists(name, surname, artisticName);
    }

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist savedArtist = artistService.createArtist(artist);
        return ResponseEntity.ok(savedArtist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        Optional<Artist> updatedArtist = artistService.updateArtist(id, artist);
        return updatedArtist.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        if (artistService.deleteArtist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
