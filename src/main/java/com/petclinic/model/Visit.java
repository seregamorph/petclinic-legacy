package com.petclinic.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    private String description;

    public Visit() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
