package com.petclinic.repository;

import com.petclinic.config.DatabaseConfig;
import com.petclinic.model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetRepository {

    private static PetRepository instance;

    private PetRepository() {}

    public static synchronized PetRepository getInstance() {
        if (instance == null) {
            instance = new PetRepository();
        }
        return instance;
    }

    public List<Pet> findAll() throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT id, name, birth_date, type, owner_id FROM pets ORDER BY id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) pets.add(map(rs));
        }
        return pets;
    }

    public List<Pet> findByOwnerId(long ownerId) throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT id, name, birth_date, type, owner_id FROM pets WHERE owner_id = ? ORDER BY id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) pets.add(map(rs));
            }
        }
        return pets;
    }

    public Optional<Pet> findById(long id) throws SQLException {
        String sql = "SELECT id, name, birth_date, type, owner_id FROM pets WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Pet save(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (name, birth_date, type, owner_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pet.getName());
            ps.setDate(2, Date.valueOf(pet.getBirthDate()));
            ps.setString(3, pet.getType());
            ps.setLong(4, pet.getOwnerId());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                pet.setId(rs.getLong(1));
            }
        }
        return pet;
    }

    public boolean update(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET name=?, birth_date=?, type=?, owner_id=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pet.getName());
            ps.setDate(2, Date.valueOf(pet.getBirthDate()));
            ps.setString(3, pet.getType());
            ps.setLong(4, pet.getOwnerId());
            ps.setLong(5, pet.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM pets WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Pet map(ResultSet rs) throws SQLException {
        Pet p = new Pet();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setBirthDate(rs.getDate("birth_date").toLocalDate());
        p.setType(rs.getString("type"));
        p.setOwnerId(rs.getLong("owner_id"));
        return p;
    }
}
