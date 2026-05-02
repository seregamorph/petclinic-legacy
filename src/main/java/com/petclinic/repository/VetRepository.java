package com.petclinic.repository;

import com.petclinic.model.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VetRepository extends JpaRepository<Vet, Long> {

    List<Vet> findAllByOrderById();
}
