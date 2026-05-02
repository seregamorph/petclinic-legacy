package com.petclinic.controller;

import com.petclinic.model.Visit;
import com.petclinic.service.VisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public List<Visit> getAll(@RequestParam(required = false) Long petId) {
        if (petId != null) return visitService.getVisitsByPet(petId);
        return visitService.getAllVisits();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> getById(@PathVariable long id) {
        return visitService.getVisit(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Visit> create(@RequestBody Visit visit) {
        return ResponseEntity.status(201).body(visitService.createVisit(visit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> update(@PathVariable long id, @RequestBody Visit visit) {
        return visitService.updateVisit(id, visit)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return visitService.deleteVisit(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
