package com.petclinic.service;

import com.petclinic.model.Owner;
import com.petclinic.repository.OwnerRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OwnerService {

    private static OwnerService instance;

    private final OwnerRepository ownerRepository;

    private OwnerService() {
        this.ownerRepository = OwnerRepository.getInstance();
    }

    public static synchronized OwnerService getInstance() {
        if (instance == null) {
            instance = new OwnerService();
        }
        return instance;
    }

    public List<Owner> getAllOwners() throws SQLException {
        return ownerRepository.findAll();
    }

    public Optional<Owner> getOwner(long id) throws SQLException {
        return ownerRepository.findById(id);
    }

    public Owner createOwner(Owner owner) throws SQLException {
        validate(owner);
        return ownerRepository.save(owner);
    }

    public Optional<Owner> updateOwner(long id, Owner incoming) throws SQLException {
        Optional<Owner> existing = ownerRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();
        validate(incoming);
        incoming.setId(id);
        ownerRepository.update(incoming);
        return Optional.of(incoming);
    }

    public boolean deleteOwner(long id) throws SQLException {
        return ownerRepository.delete(id);
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
