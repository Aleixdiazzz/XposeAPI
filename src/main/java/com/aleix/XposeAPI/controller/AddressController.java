package com.aleix.XposeAPI.controller;

import com.aleix.XposeAPI.model.Address;
import com.aleix.XposeAPI.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Retrieves all address entries.
     *
     * @return a list of {@link Address} objects
     */
    @GetMapping
    public List<Address> getAllAddresss() {
        return addressService.getAllAddresss();
    }

    /**
     * Retrieves a specific address by its ID.
     *
     * @param id the ID of the address to retrieve
     * @return a {@link ResponseEntity} containing the {@link Address} if found,
     *         or 404 Not Found if the address does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Optional<Address> address = addressService.getAddressById(id);
        return address.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new address entry.
     *
     * @param address the {@link Address} object to create
     * @return a {@link ResponseEntity} containing the created {@link Address}
     */
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address savedAddress = addressService.createAddress(address);
        return ResponseEntity.ok(savedAddress);
    }

    /**
     * Updates an existing address by ID.
     *
     * @param id the ID of the address to update
     * @param address the updated {@link Address} data
     * @return a {@link ResponseEntity} containing the updated {@link Address} if successful,
     *         or 404 Not Found if the address does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        Optional<Address> updatedAddress = addressService.updateAddress(id, address);
        return updatedAddress.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an address by its ID.
     *
     * @param id the ID of the address to delete
     * @return a {@link ResponseEntity} with 204 No Content if the deletion was successful,
     *         or 404 Not Found if the address does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        if (addressService.deleteAddress(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
