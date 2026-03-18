package com.petclinic.repository;

import com.petclinic.config.DatabaseConfig;
import com.petclinic.model.Vet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VetRepository {

    private static VetRepository instance;

    private VetRepository() {}

    public static synchronized VetRepository getInstance() {
        if (instance == null) {
            instance = new VetRepository();
        }
        return instance;
    }

    public List<Vet> findAll() throws SQLException {
        List<Vet> vets = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, specialty FROM vets ORDER BY id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) vets.add(map(rs));
        }
        return vets;
    }

    public Optional<Vet> findById(long id) throws SQLException {
        String sql = "SELECT id, first_name, last_name, specialty FROM vets WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Vet save(Vet vet) throws SQLException {
        String sql = "INSERT INTO vets (first_name, last_name, specialty) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vet.getFirstName());
            ps.setString(2, vet.getLastName());
            ps.setString(3, vet.getSpecialty());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                vet.setId(rs.getLong(1));
            }
        }
        return vet;
    }

    public boolean update(Vet vet) throws SQLException {
        String sql = "UPDATE vets SET first_name=?, last_name=?, specialty=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vet.getFirstName());
            ps.setString(2, vet.getLastName());
            ps.setString(3, vet.getSpecialty());
            ps.setLong(4, vet.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM vets WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Vet map(ResultSet rs) throws SQLException {
        Vet v = new Vet();
        v.setId(rs.getLong("id"));
        v.setFirstName(rs.getString("first_name"));
        v.setLastName(rs.getString("last_name"));
        v.setSpecialty(rs.getString("specialty"));
        return v;
    }
}
