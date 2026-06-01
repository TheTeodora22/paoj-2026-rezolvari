package com.pao.proiect.elearning.service;

import com.pao.proiect.elearning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlatformService {

    private static PlatformService instance;

    private PlatformService() {
    }

    public static PlatformService getInstance() {
        if (instance == null) {
            instance = new PlatformService();
        }
        return instance;
    }

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public int inscrieStudentLaCurs(int studentId, int cursId) throws SQLException, IOException {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            String checkCurs = "SELECT id FROM curs WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkCurs)) {
                ps.setInt(1, cursId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Cursul nu exista.");
                    }
                }
            }

            String checkStudent = "SELECT id FROM student WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkStudent)) {
                ps.setInt(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Studentul nu exista.");
                    }
                }
            }

            String checkInscriere = """
                    SELECT id FROM inscriere
                    WHERE student_id = ? AND curs_id = ? AND activ = 1
                    """;
            try (PreparedStatement ps = conn.prepareStatement(checkInscriere)) {
                ps.setInt(1, studentId);
                ps.setInt(2, cursId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new SQLException("Studentul e deja inscris.");
                    }
                }
            }

            int inscriereId;
            String insert = "INSERT INTO inscriere (student_id, curs_id, data_inscriere, activ) VALUES (?, ?, ?, 1)";
            try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, studentId);
                ps.setInt(2, cursId);
                ps.setString(3, LocalDate.now().toString());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    inscriereId = keys.getInt(1);
                }
            }

            String updateCurs = "UPDATE curs SET numar_inscrieri = numar_inscrieri + 1 WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateCurs)) {
                ps.setInt(1, cursId);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("[TX] Inscriere reusita, id=" + inscriereId);
            return inscriereId;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("[TX] Rollback: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<String> getInscrieriActiveCuDetalii() throws SQLException, IOException {
        String sql = """
                SELECT c.titlu AS curs_titlu, s.username AS student_nume, i.data_inscriere
                FROM inscriere i
                JOIN curs c ON i.curs_id = c.id
                JOIN student s ON i.student_id = s.id
                WHERE i.activ = 1
                ORDER BY i.data_inscriere DESC
                """;
        List<String> results = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(rs.getString("curs_titlu") + " - " + rs.getString("student_nume")
                        + ", din " + rs.getString("data_inscriere"));
            }
        }
        return results;
    }

    public List<String> getTopCursuriCuCategorie() throws SQLException, IOException {
        String sql = """
                SELECT c.titlu AS curs_titlu, cat.nume AS categorie_nume, COUNT(i.id) AS total
                FROM curs c
                JOIN categorie cat ON c.categorie_id = cat.id
                LEFT JOIN inscriere i ON i.curs_id = c.id AND i.activ = 1
                GROUP BY c.id, c.titlu, cat.nume
                ORDER BY total DESC
                """;
        List<String> results = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(rs.getString("curs_titlu") + " (" + rs.getString("categorie_nume")
                        + "): " + rs.getLong("total") + " inscrieri");
            }
        }
        return results;
    }

    public List<String> getNumarInscrieriPerStudent() throws SQLException, IOException {
        String sql = """
                SELECT s.username AS student_nume, COUNT(i.id) AS total
                FROM student s
                LEFT JOIN inscriere i ON i.student_id = s.id AND i.activ = 1
                GROUP BY s.id, s.username
                ORDER BY total DESC
                """;
        List<String> results = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(rs.getString("student_nume") + ": " + rs.getLong("total") + " inscrieri");
            }
        }
        return results;
    }
}
