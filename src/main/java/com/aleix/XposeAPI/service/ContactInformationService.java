package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.ContactInformation;
import com.aleix.XposeAPI.repository.AddressRepository;
import com.aleix.XposeAPI.repository.ContactInformationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class ContactInformationService {

    private final ContactInformationRepository contactInformationRepository;

    public ContactInformationService(ContactInformationRepository contactInformationRepository, AddressRepository addressRepository) {
        this.contactInformationRepository = contactInformationRepository;
    }


    public List<ContactInformation> getAllContactInformations() {
        return contactInformationRepository.findAll();
    }

    public Optional<ContactInformation> getContactInformationById(Long id) {
        return contactInformationRepository.findById(id);
    }

    public ContactInformation createContactInformation(ContactInformation contactInformation) {
        return contactInformationRepository.save(contactInformation);
    }

    public Optional<ContactInformation> updateContactInformation(Long id, ContactInformation contactInformationDetails) {
        return contactInformationRepository.findById(id).map(contactInformation -> {
            contactInformation.setEmail(contactInformationDetails.getEmail());
            contactInformation.setPhoneNumber(contactInformationDetails.getPhoneNumber());
            return contactInformationRepository.save(contactInformation);
        });
    }

    public boolean deleteContactInformation(Long id) {
        if (contactInformationRepository.existsById(id)) {
            contactInformationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

