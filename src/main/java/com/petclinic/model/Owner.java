package com.petclinic.model;

import com.petclinic.repository.PetRepository;

import java.sql.SQLException;
import java.util.List;

public class Owner {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;

    public List<Pet> getPets() throws SQLException {
        return PetRepository.getInstance().findByOwnerId(getId());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}
