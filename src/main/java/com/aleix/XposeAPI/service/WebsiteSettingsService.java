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

    public List<WebsiteSettings> getAllWebsiteSettings() {
        return websiteSettingsRepository.findAll();
    }

    public Optional<WebsiteSettings> getWebsiteSettingsById(Long id) {
        return websiteSettingsRepository.findById(id);
    }

    public WebsiteSettings createWebsiteSettings(WebsiteSettings websiteSettings) {
        return websiteSettingsRepository.save(websiteSettings);
    }

    public Optional<WebsiteSettings> updateWebsiteSettings(Long id, WebsiteSettings websiteSettingsDetails) {
        return websiteSettingsRepository.findById(id).map(websiteSettings -> {
            websiteSettings.setName(websiteSettingsDetails.getName());
            websiteSettings.setWebsiteName(websiteSettingsDetails.getWebsiteName());
            websiteSettings.setAddress(websiteSettingsDetails.getAddress());
            websiteSettings.setEmail(websiteSettingsDetails.getEmail());
            websiteSettings.setPhone(websiteSettingsDetails.getPhone());
            websiteSettings.setFavIconUrl(websiteSettingsDetails.getFavIconUrl());
            return websiteSettingsRepository.save(websiteSettings);
        });
    }

    public boolean deleteWebsiteSettings(Long id) {
        if (websiteSettingsRepository.existsById(id)) {
            websiteSettingsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}