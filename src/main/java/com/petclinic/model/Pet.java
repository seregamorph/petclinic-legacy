package com.petclinic.model;

import java.time.LocalDate;

public class Pet {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String type;
    private Long ownerId;

    public Pet() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
