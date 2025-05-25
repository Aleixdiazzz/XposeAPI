package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.ContactInformation;
import com.aleix.XposeAPI.repository.ContactInformationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class ContactInformationService {

    private final ContactInformationRepository contactInformationRepository;

    public ContactInformationService(ContactInformationRepository contactInformationRepository) {
        this.contactInformationRepository = contactInformationRepository;
    }


    /**
     * Retrieves all contact information entries from the repository.
     *
     * @return a list of {@link ContactInformation} objects
     */
    public List<ContactInformation> getAllContactInformations() {
        return contactInformationRepository.findAll();
    }

    /**
     * Retrieves a contact information entry by its ID.
     *
     * @param id the ID of the contact information to retrieve
     * @return an {@link Optional} containing the {@link ContactInformation} if found,
     *         or an empty Optional if not found
     */
    public Optional<ContactInformation> getContactInformationById(Long id) {
        return contactInformationRepository.findById(id);
    }

    /**
     * Creates and saves a new contact information entry.
     *
     * @param contactInformation the {@link ContactInformation} object to save
     * @return the saved {@link ContactInformation} object
     */
    public ContactInformation createContactInformation(ContactInformation contactInformation) {
        return contactInformationRepository.save(contactInformation);
    }

    /**
     * Updates an existing contact information entry with new values.
     *
     * @param id the ID of the contact information to update
     * @param contactInformationDetails the updated {@link ContactInformation} details
     * @return an {@link Optional} containing the updated {@link ContactInformation} if successful,
     *         or an empty Optional if no matching entry was found
     */
    public Optional<ContactInformation> updateContactInformation(Long id, ContactInformation contactInformationDetails) {
        return contactInformationRepository.findById(id).map(contactInformation -> {
            contactInformation.setEmail(contactInformationDetails.getEmail());
            contactInformation.setPhoneNumber(contactInformationDetails.getPhoneNumber());
            contactInformation.setAddress(contactInformationDetails.getAddress());
            return contactInformationRepository.save(contactInformation);
        });
    }

    /**
     * Deletes a contact information entry by its ID.
     *
     * @param id the ID of the contact information to delete
     * @return {@code true} if the entry was found and deleted, {@code false} otherwise
     */
    public boolean deleteContactInformation(Long id) {
        if (contactInformationRepository.existsById(id)) {
            contactInformationRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

