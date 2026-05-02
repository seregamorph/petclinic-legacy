package com.petclinic.controller;

import com.petclinic.model.Vet;
import com.petclinic.service.VetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vets")
public class VetController {

    private final VetService vetService;

    public VetController(VetService vetService) {
        this.vetService = vetService;
    }

    @GetMapping
    public List<Vet> getAll() {
        return vetService.getAllVets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vet> getById(@PathVariable long id) {
        return vetService.getVet(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vet> create(@RequestBody Vet vet) {
        return ResponseEntity.status(201).body(vetService.createVet(vet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vet> update(@PathVariable long id, @RequestBody Vet vet) {
        return vetService.updateVet(id, vet)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return vetService.deleteVet(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
