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

    @GetMapping
    public List<ContactInformation> getAllContactInformations() {
        return contactInformationService.getAllContactInformations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactInformation> getContactInformationById(@PathVariable Long id) {
        Optional<ContactInformation> contactInformation = contactInformationService.getContactInformationById(id);
        return contactInformation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ContactInformation> createContactInformation(@RequestBody ContactInformation contactInformation) {
        ContactInformation savedContactInformation = contactInformationService.createContactInformation(contactInformation);
        return ResponseEntity.ok(savedContactInformation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInformation> updateContactInformation(@PathVariable Long id, @RequestBody ContactInformation contactInformation) {
        Optional<ContactInformation> updatedContactInformation = contactInformationService.updateContactInformation(id, contactInformation);
        return updatedContactInformation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInformation(@PathVariable Long id) {
        if (contactInformationService.deleteContactInformation(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
