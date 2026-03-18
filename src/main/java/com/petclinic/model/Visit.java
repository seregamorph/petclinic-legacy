package com.petclinic.model;

import java.time.LocalDate;

public class Visit {
    private Long id;
    private Long petId;
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
