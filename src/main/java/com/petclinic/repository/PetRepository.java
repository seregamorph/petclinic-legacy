package com.petclinic.repository;

import com.petclinic.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByOrderById();

    List<Pet> findByOwnerIdOrderById(Long ownerId);
}
