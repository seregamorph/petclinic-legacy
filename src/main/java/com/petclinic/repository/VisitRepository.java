package com.petclinic.repository;

import com.petclinic.config.DatabaseConfig;
import com.petclinic.model.Visit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitRepository {

    private static VisitRepository instance;

    private VisitRepository() {}

    public static synchronized VisitRepository getInstance() {
        if (instance == null) {
            instance = new VisitRepository();
        }
        return instance;
    }

    public List<Visit> findAll() throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT id, pet_id, visit_date, description FROM visits ORDER BY visit_date DESC";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) visits.add(map(rs));
        }
        return visits;
    }

    public List<Visit> findByPetId(long petId) throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT id, pet_id, visit_date, description FROM visits WHERE pet_id = ? ORDER BY visit_date DESC";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, petId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) visits.add(map(rs));
            }
        }
        return visits;
    }

    public Optional<Visit> findById(long id) throws SQLException {
        String sql = "SELECT id, pet_id, visit_date, description FROM visits WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Visit save(Visit visit) throws SQLException {
        String sql = "INSERT INTO visits (pet_id, visit_date, description) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, visit.getPetId());
            ps.setDate(2, Date.valueOf(visit.getVisitDate()));
            ps.setString(3, visit.getDescription());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                visit.setId(rs.getLong(1));
            }
        }
        return visit;
    }

    public boolean update(Visit visit) throws SQLException {
        String sql = "UPDATE visits SET pet_id=?, visit_date=?, description=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, visit.getPetId());
            ps.setDate(2, Date.valueOf(visit.getVisitDate()));
            ps.setString(3, visit.getDescription());
            ps.setLong(4, visit.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM visits WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Visit map(ResultSet rs) throws SQLException {
        Visit v = new Visit();
        v.setId(rs.getLong("id"));
        v.setPetId(rs.getLong("pet_id"));
        v.setVisitDate(rs.getDate("visit_date").toLocalDate());
        v.setDescription(rs.getString("description"));
        return v;
    }
}
