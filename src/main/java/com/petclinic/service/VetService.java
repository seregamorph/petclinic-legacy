package com.petclinic.service;

import com.petclinic.model.Vet;
import com.petclinic.repository.VetRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class VetService {

    private static VetService instance;

    private final VetRepository vetRepository;

    private VetService() {
        this.vetRepository = VetRepository.getInstance();
    }

    public static synchronized VetService getInstance() {
        if (instance == null) {
            instance = new VetService();
        }
        return instance;
    }

    public List<Vet> getAllVets() throws SQLException {
        return vetRepository.findAll();
    }

    public Optional<Vet> getVet(long id) throws SQLException {
        return vetRepository.findById(id);
    }

    public Vet createVet(Vet vet) throws SQLException {
        validate(vet);
        return vetRepository.save(vet);
    }

    public Optional<Vet> updateVet(long id, Vet incoming) throws SQLException {
        Optional<Vet> existing = vetRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();
        validate(incoming);
        incoming.setId(id);
        vetRepository.update(incoming);
        return Optional.of(incoming);
    }

    public boolean deleteVet(long id) throws SQLException {
        return vetRepository.delete(id);
    }

    private void validate(Vet v) {
        if (v.getFirstName() == null || v.getFirstName().isBlank())
            throw new IllegalArgumentException("firstName is required");
        if (v.getLastName() == null || v.getLastName().isBlank())
            throw new IllegalArgumentException("lastName is required");
    }
}
