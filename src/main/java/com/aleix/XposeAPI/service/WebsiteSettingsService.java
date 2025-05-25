package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.WebsiteSettings;
import com.aleix.XposeAPI.repository.WebsiteSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebsiteSettingsService {

    private final WebsiteSettingsRepository websiteSettingsRepository;

    public WebsiteSettingsService(WebsiteSettingsRepository websiteSettingsRepository) {
        this.websiteSettingsRepository = websiteSettingsRepository;
    }

    /**
     * Retrieves all website settings entries from the repository.
     *
     * @return a list of {@link WebsiteSettings} objects
     */
    public List<WebsiteSettings> getAllWebsiteSettings() {
        return websiteSettingsRepository.findAll();
    }

    /**
     * Retrieves a website settings entry by its ID.
     *
     * @param id the ID of the website settings to retrieve
     * @return an {@link Optional} containing the {@link WebsiteSettings} if found,
     *         or an empty Optional if not found
     */
    public Optional<WebsiteSettings> getWebsiteSettingsById(Long id) {
        return websiteSettingsRepository.findById(id);
    }

    /**
     * Creates and saves a new website settings entry.
     *
     * @param websiteSettings the {@link WebsiteSettings} object to save
     * @return the saved {@link WebsiteSettings} object
     */
    public WebsiteSettings createWebsiteSettings(WebsiteSettings websiteSettings) {
        return websiteSettingsRepository.save(websiteSettings);
    }

    /**
     * Updates an existing website settings entry with new values.
     *
     * @param id the ID of the website settings to update
     * @param websiteSettingsDetails the updated {@link WebsiteSettings} details
     * @return an {@link Optional} containing the updated {@link WebsiteSettings} if successful,
     *         or an empty Optional if no matching entry was found
     */
    public Optional<WebsiteSettings> updateWebsiteSettings(Long id, WebsiteSettings websiteSettingsDetails) {
        return websiteSettingsRepository.findById(id).map(websiteSettings -> {
            websiteSettings.setName(websiteSettingsDetails.getName());
            websiteSettings.setWebsiteName(websiteSettingsDetails.getWebsiteName());
            websiteSettings.setContactInformation(websiteSettingsDetails.getContactInformation());
            websiteSettings.setFavIconUrl(websiteSettingsDetails.getFavIconUrl());
            return websiteSettingsRepository.save(websiteSettings);
        });
    }

    /**
     * Deletes a website settings entry by its ID.
     *
     * @param id the ID of the website settings to delete
     * @return {@code true} if the entry was found and deleted, {@code false} otherwise
     */
    public boolean deleteWebsiteSettings(Long id) {
        if (websiteSettingsRepository.existsById(id)) {
            websiteSettingsRepository.deleteById(id);
            return true;
        }
        return false;
    }

}