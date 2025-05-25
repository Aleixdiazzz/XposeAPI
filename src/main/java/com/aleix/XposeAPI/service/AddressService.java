package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Address;
import com.aleix.XposeAPI.repository.AddressRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


/**
 * Service class for managing Address entities.
 * Provides methods for CRUD operations and additional business logic related to Addresses.
 */
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    /**
     * Constructor for AddressService.
     * 
     * @param addressRepository Repository for Address entity operations
     */
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Retrieves all addresses from the database.
     * 
     * @return List of all Address entities
     */
    public List<Address> getAllAddresss() {
        return addressRepository.findAll();
    }

    /**
     * Retrieves a specific address by its ID.
     * 
     * @param id The ID of the address to retrieve
     * @return Optional containing the Address if found, empty otherwise
     */
    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    /**
     * Creates a new address in the database.
     * 
     * @param address The Address entity to create
     * @return The saved Address entity with generated ID
     */
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    /**
     * Updates an existing address with new details.
     * 
     * @param id The ID of the address to update
     * @param addressDetails The updated Address entity data
     * @return Optional containing the updated Address if found, empty otherwise
     */
    public Optional<Address> updateAddress(Long id, Address addressDetails) {
        return addressRepository.findById(id).map(address -> {
            address.setCity(addressDetails .getCity());
            address.setStreet(addressDetails.getStreet());
            address.setNumber(addressDetails.getNumber());
            address.setPostalCode(addressDetails.getPostalCode());
            address.setCountry(addressDetails.getCountry());
            return addressRepository.save(address);
        });
    }

    /**
     * Deletes an address by its ID.
     * 
     * @param id The ID of the address to delete
     * @return true if the address was deleted, false if it wasn't found
     */
    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
