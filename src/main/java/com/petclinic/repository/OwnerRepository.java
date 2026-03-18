package com.petclinic.repository;

import com.petclinic.config.DatabaseConfig;
import com.petclinic.model.Owner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerRepository {

    private static OwnerRepository instance;

    private OwnerRepository() {}

    public static synchronized OwnerRepository getInstance() {
        if (instance == null) {
            instance = new OwnerRepository();
        }
        return instance;
    }

    public List<Owner> findAll() throws SQLException {
        List<Owner> owners = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, address, city, telephone FROM owners ORDER BY id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                owners.add(map(rs));
            }
        }
        return owners;
    }

    public Optional<Owner> findById(long id) throws SQLException {
        String sql = "SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Owner save(Owner owner) throws SQLException {
        String sql = "INSERT INTO owners (first_name, last_name, address, city, telephone) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, owner.getFirstName());
            ps.setString(2, owner.getLastName());
            ps.setString(3, owner.getAddress());
            ps.setString(4, owner.getCity());
            ps.setString(5, owner.getTelephone());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                owner.setId(rs.getLong(1));
            }
        }
        return owner;
    }

    public boolean update(Owner owner) throws SQLException {
        String sql = "UPDATE owners SET first_name=?, last_name=?, address=?, city=?, telephone=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, owner.getFirstName());
            ps.setString(2, owner.getLastName());
            ps.setString(3, owner.getAddress());
            ps.setString(4, owner.getCity());
            ps.setString(5, owner.getTelephone());
            ps.setLong(6, owner.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM owners WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Owner map(ResultSet rs) throws SQLException {
        Owner o = new Owner();
        o.setId(rs.getLong("id"));
        o.setFirstName(rs.getString("first_name"));
        o.setLastName(rs.getString("last_name"));
        o.setAddress(rs.getString("address"));
        o.setCity(rs.getString("city"));
        o.setTelephone(rs.getString("telephone"));
        return o;
    }
}
