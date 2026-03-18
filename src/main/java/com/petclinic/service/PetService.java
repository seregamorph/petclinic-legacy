package com.petclinic.service;

import com.petclinic.model.Pet;
import com.petclinic.repository.OwnerRepository;
import com.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public PetService(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAllByOrderById();
    }

    public List<Pet> getPetsByOwner(long ownerId) {
        return petRepository.findByOwnerIdOrderById(ownerId);
    }

    public Optional<Pet> getPet(long id) {
        return petRepository.findById(id);
    }

    public Pet createPet(Pet pet) {
        validate(pet);
        if (!ownerRepository.existsById(pet.getOwnerId())) {
            throw new IllegalArgumentException("Owner with id " + pet.getOwnerId() + " not found");
        }
        return petRepository.save(pet);
    }

    public Optional<Pet> updatePet(long id, Pet incoming) {
        if (!petRepository.existsById(id)) return Optional.empty();
        validate(incoming);
        if (!ownerRepository.existsById(incoming.getOwnerId())) {
            throw new IllegalArgumentException("Owner with id " + incoming.getOwnerId() + " not found");
        }
        incoming.setId(id);
        return Optional.of(petRepository.save(incoming));
    }

    public boolean deletePet(long id) {
        if (!petRepository.existsById(id)) return false;
        petRepository.deleteById(id);
        return true;
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
