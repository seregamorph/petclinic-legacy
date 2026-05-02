package com.petclinic.controller;

import com.petclinic.model.Pet;
import com.petclinic.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> getAll(@RequestParam(required = false) Long ownerId) {
        if (ownerId != null) return petService.getPetsByOwner(ownerId);
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getById(@PathVariable long id) {
        return petService.getPet(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pet> create(@RequestBody Pet pet) {
        return ResponseEntity.status(201).body(petService.createPet(pet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> update(@PathVariable long id, @RequestBody Pet pet) {
        return petService.updatePet(id, pet)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return petService.deletePet(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
