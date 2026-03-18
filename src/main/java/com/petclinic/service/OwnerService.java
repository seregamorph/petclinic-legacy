package com.petclinic.service;

import com.petclinic.model.Owner;
import com.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public List<Owner> getAllOwners() {
        return ownerRepository.findAllByOrderById();
    }

    public Optional<Owner> getOwner(long id) {
        return ownerRepository.findById(id);
    }

    public Owner createOwner(Owner owner) {
        validate(owner);
        return ownerRepository.save(owner);
    }

    public Optional<Owner> updateOwner(long id, Owner incoming) {
        if (!ownerRepository.existsById(id)) return Optional.empty();
        validate(incoming);
        incoming.setId(id);
        return Optional.of(ownerRepository.save(incoming));
    }

    public boolean deleteOwner(long id) {
        if (!ownerRepository.existsById(id)) return false;
        ownerRepository.deleteById(id);
        return true;
    }

    private void validate(Owner o) {
        if (o.getFirstName() == null || o.getFirstName().isBlank())
            throw new IllegalArgumentException("firstName is required");
        if (o.getLastName() == null || o.getLastName().isBlank())
            throw new IllegalArgumentException("lastName is required");
        if (o.getTelephone() == null || o.getTelephone().isBlank())
            throw new IllegalArgumentException("telephone is required");
    }
}
