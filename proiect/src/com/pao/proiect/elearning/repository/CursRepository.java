package com.pao.proiect.elearning.repository;

import com.pao.proiect.elearning.model.Categorie;
import com.pao.proiect.elearning.model.Curs;
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

public class CursRepository implements Repository<Curs, Integer> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Curs mapRow(ResultSet rs) throws SQLException {
        Categorie cat = new Categorie(rs.getInt("categorie_id"), "");
        Instructor inst = new Instructor();
        inst.setId(rs.getInt("instructor_id"));
        Curs c = new Curs(rs.getInt("id"), rs.getString("titlu"), cat, inst);
        return c;
    }

    @Override
    public void save(Curs curs) throws SQLException {
        String sql = "INSERT INTO curs (titlu, categorie_id, instructor_id, numar_inscrieri) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, curs.getTitlu());
            ps.setInt(2, curs.getCategorie().getId());
            ps.setInt(3, curs.getInstructor().getId());
            ps.setInt(4, 0);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    curs.setId(keys.getInt(1));
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Curs> findById(Integer id) throws SQLException {
        String sql = "SELECT id, titlu, categorie_id, instructor_id FROM curs WHERE id = ?";
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
    public List<Curs> findAll() throws SQLException {
        String sql = "SELECT id, titlu, categorie_id, instructor_id FROM curs ORDER BY id";
        List<Curs> list = new ArrayList<>();
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
    public void update(Curs curs) throws SQLException {
        String sql = "UPDATE curs SET titlu = ?, categorie_id = ?, instructor_id = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, curs.getTitlu());
            ps.setInt(2, curs.getCategorie().getId());
            ps.setInt(3, curs.getInstructor().getId());
            ps.setInt(4, curs.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM curs WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
