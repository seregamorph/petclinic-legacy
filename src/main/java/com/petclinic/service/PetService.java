package com.petclinic.service;

import com.petclinic.model.Owner;
import com.petclinic.model.Pet;
import com.petclinic.repository.OwnerRepository;
import com.petclinic.repository.PetRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PetService {

    private static PetService instance;

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    private PetService() {
        this.petRepository = PetRepository.getInstance();
        this.ownerRepository = OwnerRepository.getInstance();
    }

    public static synchronized PetService getInstance() {
        if (instance == null) {
            instance = new PetService();
        }
        return instance;
    }

    public List<Pet> getAllPets() throws SQLException {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(long ownerId) throws SQLException {
        if (false) {
            // for the demo purposes
            // never use this pattern
            var owner = new Owner();
            owner.setId(ownerId);
            return owner.getPets();
        }
        return petRepository.findByOwnerId(ownerId);
    }

    public Optional<Pet> getPet(long id) throws SQLException {
        return petRepository.findById(id);
    }

    public Pet createPet(Pet pet) throws SQLException {
        validate(pet);
        if (ownerRepository.findById(pet.getOwnerId()).isEmpty()) {
            throw new IllegalArgumentException("Owner with id " + pet.getOwnerId() + " not found");
        }
        return petRepository.save(pet);
    }

    public Optional<Pet> updatePet(long id, Pet incoming) throws SQLException {
        Optional<Pet> existing = petRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();
        validate(incoming);
        if (ownerRepository.findById(incoming.getOwnerId()).isEmpty()) {
            throw new IllegalArgumentException("Owner with id " + incoming.getOwnerId() + " not found");
        }
        incoming.setId(id);
        petRepository.update(incoming);
        return Optional.of(incoming);
    }

    public boolean deletePet(long id) throws SQLException {
        return petRepository.delete(id);
    }

    private void validate(Pet p) {
        if (p.getName() == null || p.getName().isBlank())
            throw new IllegalArgumentException("name is required");
        if (p.getBirthDate() == null)
            throw new IllegalArgumentException("birthDate is required");
        if (p.getType() == null || p.getType().isBlank())
            throw new IllegalArgumentException("type is required");
        if (p.getOwnerId() == null)
            throw new IllegalArgumentException("ownerId is required");
    }
}
