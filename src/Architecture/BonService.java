package Architecture;

import Utils.PDFUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class BonService {

    private static final String url = "jdbc:postgresql://localhost:5432/sysGB";
    private static final String user = "postgres";
    private static final String password = "0000";

    // SQL INSERT statement
    private static final String INSERT_SQL = "INSERT INTO bons (code_bon, code_employe, type_bon, date_bon, heure_bon, heure_v, motif, etat_bon) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static List<Bon> getBonsByEmployees(List<Employe> employees) {
        List<Bon> bonsByEmployees = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM bons WHERE code_employe = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Employe employe : employees) {
                    pstmt.setInt(1, employe.getCodeEmploye());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            Bon bon = createBonFromResultSet(rs, employe);
                            bonsByEmployees.add(bon);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

        return bonsByEmployees;
    }

    public static void saveBon(Bon bon, Employe emp) throws IOException, URISyntaxException {
        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            setBonParameters(pstmt, bon);
            pstmt.executeUpdate();
            PDFUtil.main(bon, emp);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
            JOptionPane.showMessageDialog(null, "Error occurred while saving the bon: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Bon createBonFromResultSet(ResultSet rs, Employe employe) throws SQLException {
        int codeBon = rs.getInt("code_bon");
        LocalDate dateBon = rs.getDate("date_bon").toLocalDate();
        LocalTime heureBon = rs.getTime("heure_bon") != null ? rs.getTime("heure_bon").toLocalTime() : null;
        LocalTime heureV = rs.getTime("heure_v") != null ? rs.getTime("heure_v").toLocalTime() : null;
        String typebon = rs.getString("type_bon");
        String motif = rs.getString("motif");
        char etatBon = rs.getString("etat_bon").charAt(0);

        return new Bon(codeBon, employe, typebon, dateBon, heureBon, heureV, motif, etatBon);
    }

    private static void setBonParameters(PreparedStatement pstmt, Bon bon) throws SQLException {
        pstmt.setInt(1, bon.getCodeBon());
        pstmt.setInt(2, bon.getEmploye().getCodeEmploye());
        pstmt.setString(3, bon.getType_bon());
        pstmt.setDate(4, Date.valueOf(bon.getDateBon()));

        // Convert LocalTime to Time objects, with null checks
        if (bon.getHeureBon() != null) {
            pstmt.setTime(5, Time.valueOf(bon.getHeureBon()));
        } else {
            pstmt.setNull(5, Types.TIME);
        }
        if (bon.getHeureV() != null) {
            pstmt.setTime(6, Time.valueOf(bon.getHeureV()));
        } else {
            pstmt.setNull(6, Types.TIME);
        }

        pstmt.setString(7, bon.getMotif());
        pstmt.setString(8, String.valueOf(bon.getEtatBon()));
    }

    public static int getNextCodeBon() {
        int nextCodeBon = 0;
        String sequenceName = "bons_code_bon_seq"; // Replace "your_sequence_name" with the actual sequence name

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement()) {
            String sql = "SELECT nextval('" + sequenceName + "')";
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    nextCodeBon = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

        return nextCodeBon;
    }
}
