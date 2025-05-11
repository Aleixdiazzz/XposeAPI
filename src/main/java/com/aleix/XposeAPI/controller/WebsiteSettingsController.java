package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.WebsiteSettings;
import com.aleix.XposeAPI.service.FileUploadService;
import com.aleix.XposeAPI.service.WebsiteSettingsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/website-settings")
public class WebsiteSettingsController {

    private final WebsiteSettingsService websiteSettingsService;
    private final FileUploadService fileUploadService;

    public WebsiteSettingsController(WebsiteSettingsService websiteSettingsService, FileUploadService fileUploadService) {
        this.websiteSettingsService = websiteSettingsService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public List<WebsiteSettings> getAllWebsiteSettings() {
        return websiteSettingsService.getAllWebsiteSettings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebsiteSettings> getWebsiteSettingsById(@PathVariable Long id) {
        Optional<WebsiteSettings> websiteSettings = websiteSettingsService.getWebsiteSettingsById(id);
        return websiteSettings.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WebsiteSettings> createWebsiteSettings(@RequestBody WebsiteSettings websiteSettings) {
        WebsiteSettings savedWebsiteSettings = websiteSettingsService.createWebsiteSettings(websiteSettings);
        return ResponseEntity.ok(savedWebsiteSettings);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WebsiteSettings> updateWebsiteSettings(
            @PathVariable Long id,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("favIconUrl") String favIconUrl,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("websiteName") String websiteName,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        WebsiteSettings websiteSettings = new WebsiteSettings();
        websiteSettings.setAddress(address);
        websiteSettings.setEmail(email);
        websiteSettings.setFavIconUrl(favIconUrl);
        websiteSettings.setName(name);
        websiteSettings.setPhone(phone);
        websiteSettings.setWebsiteName(websiteName);

        if (file != null && !file.isEmpty()) {
            websiteSettings.setFavIconUrl(fileUploadService.uploadLogo(file));
        }

        Optional<WebsiteSettings> updatedWebsiteSettings = websiteSettingsService.updateWebsiteSettings(id, websiteSettings);
        return updatedWebsiteSettings.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebsiteSettings(@PathVariable Long id) {
        if (websiteSettingsService.deleteWebsiteSettings(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
