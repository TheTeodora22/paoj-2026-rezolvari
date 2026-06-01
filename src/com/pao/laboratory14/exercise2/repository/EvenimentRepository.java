package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {

    private Connection connection() throws SQLException {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public void initSchema() throws SQLException {
        Connection conn = connection();
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS evenimente");
            st.execute("""
                CREATE TABLE evenimente (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    nume       TEXT    NOT NULL,
                    data       TEXT    NOT NULL,
                    capacitate INTEGER,
                    tip        TEXT
                )
                """);
        }
    }

    @Override
    public void save(Eveniment entity) throws SQLException {
        String sql = "INSERT INTO evenimente (nume, data, capacitate, tip) VALUES (?, ?, ?, ?)";
        Connection conn = connection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM evenimente WHERE id = ?";
        Connection conn = connection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() throws SQLException {
        String sql = "SELECT * FROM evenimente ORDER BY id";
        List<Eveniment> list = new ArrayList<>();
        Connection conn = connection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(Eveniment entity) throws SQLException {
        String sql = """
            UPDATE evenimente
            SET nume = ?, data = ?, capacitate = ?, tip = ?
            WHERE id = ?
            """;
        Connection conn = connection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip().name());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        deleteImpl(id);
    }

    public int deleteImpl(int id) throws SQLException {
        String sql = "DELETE FROM evenimente WHERE id = ?";
        Connection conn = connection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenimente";
        Connection conn = connection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private Eveniment mapRow(ResultSet rs) throws SQLException {
        return new Eveniment(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("data"),
                rs.getInt("capacitate"),
                TipBilet.valueOf(rs.getString("tip")));
    }
}
