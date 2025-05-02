package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.service.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/series")
public class SerieController {

    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping
    public List<Serie> getAllSeries() {
        return serieService.getAllSeries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Serie> getSerieById(@PathVariable Long id) {
        Optional<Serie> serie = serieService.getSerieById(id);
        return serie.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public List<Serie> filterSeries(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String artistId,
                                      @RequestParam(required = false) Boolean active) {
        return serieService.filterSeries(name, artistId, active);
    }

    @PostMapping
    public ResponseEntity<Serie> createSerie(@RequestBody Serie serie) {
        Serie savedSerie = serieService.createSerie(serie);
        return ResponseEntity.ok(savedSerie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Serie> updateSerie(@PathVariable Long id, @RequestBody Serie serie) {
        Optional<Serie> updatedSerie = serieService.updateSerie(id, serie);
        return updatedSerie.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerie(@PathVariable Long id) {
        if (serieService.deleteSerie(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
