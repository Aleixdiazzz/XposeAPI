package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.repository.AssetRepository;
import com.aleix.XposeAPI.repository.ArtistRepository;
import com.aleix.XposeAPI.repository.SerieRepository;
import com.aleix.XposeAPI.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final SerieRepository serieRepository;
    private final AssetRepository assetRepository;

    public DashboardService(UserRepository userRepository, 
                           ArtistRepository artistRepository,
                           SerieRepository serieRepository,
                           AssetRepository assetRepository) {
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.serieRepository = serieRepository;
        this.assetRepository = assetRepository;
    }

    public List<Integer> getDashboardStats() {
        int totalUsers = (int) userRepository.count();
        int totalArtists = (int) artistRepository.count();
        int totalSeries = (int) serieRepository.count();
        int totalAssets = (int) assetRepository.count();

        return Arrays.asList(totalUsers, totalArtists, totalSeries, totalAssets);
    }
}
