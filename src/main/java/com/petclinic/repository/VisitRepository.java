package com.petclinic.repository;

import com.petclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findAllByOrderByVisitDateDesc();

    List<Visit> findByPetIdOrderByVisitDateDesc(Long petId);
}
