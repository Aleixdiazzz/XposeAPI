package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Serie;
import com.aleix.XposeAPI.repository.SerieRepository;
import com.aleix.XposeAPI.specification.ArtistSpecifications;
import com.aleix.XposeAPI.specification.SerieSpecifications;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SerieService {

    private final SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public List<Serie> getAllSeries() {
        return serieRepository.findAll();
    }

    public Optional<Serie> getSerieById(Long id) {
        return serieRepository.findById(id);
    }

    public Serie createSerie(Serie serie) {
        return serieRepository.save(serie);
    }

    public Optional<Serie> updateSerie(Long id, Serie serieDetails) {
        return serieRepository.findById(id).map(serie -> {
            serie.setName(serieDetails.getName());
            serie.setDescription(serieDetails.getDescription());
            serie.setActive(serieDetails.isActive());
            serie.setArtists(serieDetails.getArtists());
            return serieRepository.save(serie);
        });
    }

    public boolean deleteSerie(Long id) {
        if (serieRepository.existsById(id)) {
            serieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Serie> filterSeries (String name, String artistId, Boolean active){
        return serieRepository.findAll(SerieSpecifications.filterSeries(name, artistId, active));
    }
}
