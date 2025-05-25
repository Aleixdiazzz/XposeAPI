package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Address;
import com.aleix.XposeAPI.model.ContactInformation;
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

    /**
     * Retrieves a list of all website settings.
     *
     * @return a list of {@link WebsiteSettings} objects
     */
    @GetMapping
    public List<WebsiteSettings> getAllWebsiteSettings() {
        return websiteSettingsService.getAllWebsiteSettings();
    }

    /**
     * Retrieves a specific website settings entry by its ID.
     *
     * @param id the ID of the website settings to retrieve
     * @return a {@link ResponseEntity} containing the {@link WebsiteSettings} if found,
     *         or 404 Not Found if not present
     */
    @GetMapping("/{id}")
    public ResponseEntity<WebsiteSettings> getWebsiteSettingsById(@PathVariable Long id) {
        Optional<WebsiteSettings> websiteSettings = websiteSettingsService.getWebsiteSettingsById(id);
        return websiteSettings.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves the most recently added website settings entry.
     * Typically used to fetch the current or active settings, such as contact info for the site.
     *
     * @return a {@link ResponseEntity} containing the latest {@link WebsiteSettings},
     *         or 404 Not Found if no entries exist
     */
    @GetMapping("/contact")
    public ResponseEntity<WebsiteSettings> getLatestWebsiteSettings() {
        List<WebsiteSettings> websiteSettingsList = websiteSettingsService.getAllWebsiteSettings();
        if (websiteSettingsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        WebsiteSettings latestWebsiteSettings = websiteSettingsList.getLast();
        return ResponseEntity.ok(latestWebsiteSettings);
    }

    /**
     * Creates a new website settings entry.
     *
     * @param websiteSettings the {@link WebsiteSettings} object to create
     * @return a {@link ResponseEntity} containing the created {@link WebsiteSettings}
     */
    @PostMapping
    public ResponseEntity<WebsiteSettings> createWebsiteSettings(@RequestBody WebsiteSettings websiteSettings) {
        WebsiteSettings savedWebsiteSettings = websiteSettingsService.createWebsiteSettings(websiteSettings);
        return ResponseEntity.ok(savedWebsiteSettings);
    }

    /**
     * Updates an existing website settings entry by ID.
     * Accepts multipart form data for optional file upload (e.g., favicon/logo image).
     *
     * @param id the ID of the website settings to update
     * @param email the contact email
     * @param favIconUrl the favicon URL (overwritten if a file is provided)
     * @param name the internal name of the settings
     * @param phone the contact phone number
     * @param street the street part of the address
     * @param number the house/building number
     * @param postalCode the postal code
     * @param city the city
     * @param country the country
     * @param websiteName the public name of the website
     * @param file optional file upload for a new favicon
     * @return a {@link ResponseEntity} containing the updated {@link WebsiteSettings} if successful,
     *         or 404 Not Found if the entry does not exist
     * @throws Exception if the file upload fails
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WebsiteSettings> updateWebsiteSettings(
            @PathVariable Long id,
            @RequestParam("email") String email,
            @RequestParam("favIconUrl") String favIconUrl,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("street") String street,
            @RequestParam("number") String number,
            @RequestParam("postalCode") String postalCode,
            @RequestParam("city") String city,
            @RequestParam("country") String country,
            @RequestParam("websiteName") String websiteName,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        WebsiteSettings websiteSettings = new WebsiteSettings();
        ContactInformation contactInformation = new ContactInformation();
        Address address = new Address();

        address.setStreet(street);
        address.setNumber(number);
        address.setPostalCode(postalCode);
        address.setCity(city);
        address.setCountry(country);


        contactInformation.setEmail(email);
        contactInformation.setPhoneNumber(phone);
        contactInformation.setAddress(address);

        websiteSettings.setContactInformation(contactInformation);

        websiteSettings.setFavIconUrl(favIconUrl);
        websiteSettings.setName(name);
        websiteSettings.setWebsiteName(websiteName);


        if (file != null && !file.isEmpty()) {
            websiteSettings.setFavIconUrl(fileUploadService.uploadLogo(file));
        }

        Optional<WebsiteSettings> updatedWebsiteSettings = websiteSettingsService.updateWebsiteSettings(id, websiteSettings);
        return updatedWebsiteSettings.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a website settings entry by its ID.
     *
     * @param id the ID of the website settings to delete
     * @return a {@link ResponseEntity} with 204 No Content if the deletion was successful,
     *         or 404 Not Found if the entry does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebsiteSettings(@PathVariable Long id) {
        if (websiteSettingsService.deleteWebsiteSettings(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
