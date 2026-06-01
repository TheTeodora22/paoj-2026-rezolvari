package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Inscriere;
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

public class InscriereRepository implements Repository<Inscriere, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Inscriere mapRow(ResultSet rs) throws SQLException {
        return new Inscriere(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getInt("curs_id"),
                rs.getString("data_inscriere"),
                rs.getInt("activ") == 1);
    }

    @Override
    public void save(Inscriere inscriere) throws SQLException {
        String sql = "INSERT INTO inscriere (student_id, curs_id, data_inscriere, activ) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inscriere.getStudentId());
            ps.setInt(2, inscriere.getCursId());
            ps.setString(3, inscriere.getDataInscriere());
            ps.setInt(4, inscriere.isActiv() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    inscriere.setId(keys.getInt(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Inscriere> findById(Integer id) throws SQLException {
        String sql = "SELECT id, student_id, curs_id, data_inscriere, activ FROM inscriere WHERE id = ?";
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
    public List<Inscriere> findAll() throws SQLException {
        String sql = "SELECT id, student_id, curs_id, data_inscriere, activ FROM inscriere ORDER BY id";
        List<Inscriere> list = new ArrayList<>();
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
    public void update(Inscriere inscriere) throws SQLException {
        String sql = "UPDATE inscriere SET student_id = ?, curs_id = ?, data_inscriere = ?, activ = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, inscriere.getStudentId());
            ps.setInt(2, inscriere.getCursId());
            ps.setString(3, inscriere.getDataInscriere());
            ps.setInt(4, inscriere.isActiv() ? 1 : 0);
            ps.setInt(5, inscriere.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM inscriere WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
