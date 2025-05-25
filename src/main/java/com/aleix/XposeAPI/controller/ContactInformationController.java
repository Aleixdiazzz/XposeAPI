package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.ContactInformation;
import com.aleix.XposeAPI.service.ContactInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/contactInformations")
public class ContactInformationController {

    private final ContactInformationService contactInformationService;

    public ContactInformationController(ContactInformationService contactInformationService) {
        this.contactInformationService = contactInformationService;
    }
    /**
     * Retrieves a list of all contact information entries.
     *
     * @return a list of all {@link ContactInformation} objects
     */
    @GetMapping
    public List<ContactInformation> getAllContactInformations() {
        return contactInformationService.getAllContactInformations();
    }

    /**
     * Retrieves a contact information entry by its ID.
     *
     * @param id the ID of the contact information to retrieve
     * @return a {@link ResponseEntity} containing the {@link ContactInformation} if found,
     *         or a 404 Not Found status if the entry does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactInformation> getContactInformationById(@PathVariable Long id) {
        Optional<ContactInformation> contactInformation = contactInformationService.getContactInformationById(id);
        return contactInformation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new contact information entry.
     *
     * @param contactInformation the {@link ContactInformation} object to create
     * @return a {@link ResponseEntity} containing the created {@link ContactInformation}
     */
    @PostMapping
    public ResponseEntity<ContactInformation> createContactInformation(@RequestBody ContactInformation contactInformation) {
        ContactInformation savedContactInformation = contactInformationService.createContactInformation(contactInformation);
        return ResponseEntity.ok(savedContactInformation);
    }

    /**
     * Updates an existing contact information entry.
     *
     * @param id the ID of the contact information to update
     * @param contactInformation the updated {@link ContactInformation} data
     * @return a {@link ResponseEntity} containing the updated {@link ContactInformation} if the update was successful,
     *         or a 404 Not Found status if the entry does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactInformation> updateContactInformation(@PathVariable Long id, @RequestBody ContactInformation contactInformation) {
        Optional<ContactInformation> updatedContactInformation = contactInformationService.updateContactInformation(id, contactInformation);
        return updatedContactInformation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a contact information entry by its ID.
     *
     * @param id the ID of the contact information to delete
     * @return a {@link ResponseEntity} with 204 No Content if the deletion was successful,
     *         or 404 Not Found if the entry does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInformation(@PathVariable Long id) {
        if (contactInformationService.deleteContactInformation(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
