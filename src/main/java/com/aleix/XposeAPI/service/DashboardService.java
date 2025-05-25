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

    /**
     * Constructs a new {@code DashboardService} with the given repositories.
     *
     * @param userRepository the repository for user entities
     * @param artistRepository the repository for artist entities
     * @param serieRepository the repository for series entities
     * @param assetRepository the repository for asset entities
     */
    public DashboardService(UserRepository userRepository,
                            ArtistRepository artistRepository,
                            SerieRepository serieRepository,
                            AssetRepository assetRepository) {
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.serieRepository = serieRepository;
        this.assetRepository = assetRepository;
    }

    /**
     * Retrieves dashboard statistics including total counts of users, artists, series, and assets.
     *
     * @return a list of integers representing counts in the following order:
     *         total users, total artists, total series, total assets
     */
    public List<Integer> getDashboardStats() {
        int totalUsers = (int) userRepository.count();
        int totalArtists = (int) artistRepository.count();
        int totalSeries = (int) serieRepository.count();
        int totalAssets = (int) assetRepository.count();

        return Arrays.asList(totalUsers, totalArtists, totalSeries, totalAssets);
    }

}
