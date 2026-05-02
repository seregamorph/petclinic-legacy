package com.petclinic.service;

import com.petclinic.model.Visit;
import com.petclinic.repository.PetRepository;
import com.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    public VisitService(VisitRepository visitRepository, PetRepository petRepository) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
    }

    public List<Visit> getAllVisits() {
        return visitRepository.findAllByOrderByVisitDateDesc();
    }

    public List<Visit> getVisitsByPet(long petId) {
        return visitRepository.findByPetIdOrderByVisitDateDesc(petId);
    }

    public Optional<Visit> getVisit(long id) {
        return visitRepository.findById(id);
    }

    public Visit createVisit(Visit visit) {
        validate(visit);
        if (!petRepository.existsById(visit.getPetId())) {
            throw new IllegalArgumentException("Pet with id " + visit.getPetId() + " not found");
        }
        return visitRepository.save(visit);
    }

    public Optional<Visit> updateVisit(long id, Visit incoming) {
        if (!visitRepository.existsById(id)) return Optional.empty();
        validate(incoming);
        if (!petRepository.existsById(incoming.getPetId())) {
            throw new IllegalArgumentException("Pet with id " + incoming.getPetId() + " not found");
        }
        incoming.setId(id);
        return Optional.of(visitRepository.save(incoming));
    }

    public boolean deleteVisit(long id) {
        if (!visitRepository.existsById(id)) return false;
        visitRepository.deleteById(id);
        return true;
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
