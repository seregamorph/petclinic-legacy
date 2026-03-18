package com.petclinic.controller;

import com.petclinic.model.Owner;
import com.petclinic.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<Owner> getAll() {
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owner> getById(@PathVariable long id) {
        return ownerService.getOwner(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Owner> create(@RequestBody Owner owner) {
        return ResponseEntity.status(201).body(ownerService.createOwner(owner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Owner> update(@PathVariable long id, @RequestBody Owner owner) {
        return ownerService.updateOwner(id, owner)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return ownerService.deleteOwner(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
