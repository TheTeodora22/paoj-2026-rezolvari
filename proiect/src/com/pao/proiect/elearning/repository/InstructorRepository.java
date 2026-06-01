package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Instructor;
import com.pao.proiect.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstructorRepository implements Repository<Instructor, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Instructor mapRow(ResultSet rs) throws SQLException {
        return new Instructor(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("parola"),
                rs.getString("departament"));
    }

    @Override
    public void save(Instructor instructor) throws SQLException {
        String sql = "INSERT INTO instructor (username, email, parola, departament) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, instructor.getUsername());
            ps.setString(2, instructor.getEmail());
            ps.setString(3, instructor.getParola());
            ps.setString(4, instructor.getDepartament());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    instructor.setId(keys.getInt(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Instructor> findById(Integer id) throws SQLException {
        String sql = "SELECT id, username, email, parola, departament FROM instructor WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Instructor> findAll() throws SQLException {
        String sql = "SELECT id, username, email, parola, departament FROM instructor ORDER BY id";
        List<Instructor> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Instructor instructor) throws SQLException {
        String sql = "UPDATE instructor SET username = ?, email = ?, parola = ?, departament = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, instructor.getUsername());
            ps.setString(2, instructor.getEmail());
            ps.setString(3, instructor.getParola());
            ps.setString(4, instructor.getDepartament());
            ps.setInt(5, instructor.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM instructor WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
