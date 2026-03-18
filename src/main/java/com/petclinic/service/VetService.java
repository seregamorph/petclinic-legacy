package com.petclinic.service;

import com.petclinic.model.Vet;
import com.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VetService {

    private final VetRepository vetRepository;

    public VetService(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    public List<Vet> getAllVets() {
        return vetRepository.findAllByOrderById();
    }

    public Optional<Vet> getVet(long id) {
        return vetRepository.findById(id);
    }

    public Vet createVet(Vet vet) {
        validate(vet);
        return vetRepository.save(vet);
    }

    public Optional<Vet> updateVet(long id, Vet incoming) {
        if (!vetRepository.existsById(id)) return Optional.empty();
        validate(incoming);
        incoming.setId(id);
        return Optional.of(vetRepository.save(incoming));
    }

    public boolean deleteVet(long id) {
        if (!vetRepository.existsById(id)) return false;
        vetRepository.deleteById(id);
        return true;
    }

    private void validate(Vet v) {
        if (v.getFirstName() == null || v.getFirstName().isBlank())
            throw new IllegalArgumentException("firstName is required");
        if (v.getLastName() == null || v.getLastName().isBlank())
            throw new IllegalArgumentException("lastName is required");
    }
}
