package com.petclinic.service;

import com.petclinic.model.Visit;
import com.petclinic.repository.PetRepository;
import com.petclinic.repository.VisitRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class VisitService {

    private static VisitService instance;

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    private VisitService() {
        this.visitRepository = VisitRepository.getInstance();
        this.petRepository = PetRepository.getInstance();
    }

    public static synchronized VisitService getInstance() {
        if (instance == null) {
            instance = new VisitService();
        }
        return instance;
    }

    public List<Visit> getAllVisits() throws SQLException {
        return visitRepository.findAll();
    }

    public List<Visit> getVisitsByPet(long petId) throws SQLException {
        return visitRepository.findByPetId(petId);
    }

    public Optional<Visit> getVisit(long id) throws SQLException {
        return visitRepository.findById(id);
    }

    public Visit createVisit(Visit visit) throws SQLException {
        validate(visit);
        if (petRepository.findById(visit.getPetId()).isEmpty()) {
            throw new IllegalArgumentException("Pet with id " + visit.getPetId() + " not found");
        }
        return visitRepository.save(visit);
    }

    public Optional<Visit> updateVisit(long id, Visit incoming) throws SQLException {
        Optional<Visit> existing = visitRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();
        validate(incoming);
        if (petRepository.findById(incoming.getPetId()).isEmpty()) {
            throw new IllegalArgumentException("Pet with id " + incoming.getPetId() + " not found");
        }
        incoming.setId(id);
        visitRepository.update(incoming);
        return Optional.of(incoming);
    }

    public boolean deleteVisit(long id) throws SQLException {
        return visitRepository.delete(id);
    }

    private void validate(Visit v) {
        if (v.getPetId() == null)
            throw new IllegalArgumentException("petId is required");
        if (v.getVisitDate() == null)
            throw new IllegalArgumentException("visitDate is required");
        if (v.getDescription() == null || v.getDescription().isBlank())
            throw new IllegalArgumentException("description is required");
    }
}
