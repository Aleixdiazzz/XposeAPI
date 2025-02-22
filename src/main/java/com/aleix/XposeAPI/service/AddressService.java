package com.aleix.XposeAPI.service;

import com.aleix.XposeAPI.model.Address;
import com.aleix.XposeAPI.repository.AddressRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresss() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    public Optional<Address> updateAddress(Long id, Address addressDetails) {
        return addressRepository.findById(id).map(address -> {
            address.setCity(addressDetails.getCity());
            address.setStreet(addressDetails.getStreet());
            address.setNumber(addressDetails.getNumber());
            address.setPostalCode(addressDetails.getPostalCode());
            address.setCountry(addressDetails.getCountry());
            return addressRepository.save(address);
        });
    }

    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

