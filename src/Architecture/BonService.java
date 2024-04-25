package Architecture;

import Utils.PDFUtil;
import res.R; // Import the R class
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class BonService {

    private static final String INSERT_SQL = "INSERT INTO bons (code_bon, code_employe, type_bon, date_bon, heure_bon, heure_v, motif, etat_bon) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static List<Bon> getBonsByEmployees(List<Employe> employees) throws ClassNotFoundException {
        List<Bon> bonsByEmployees = new ArrayList<>();

        try (Connection conn = R.getConnection()) {
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
            handleSQLException("Error occurred while fetching bons by employees", e);
        }

        return bonsByEmployees;
    }

    public static void saveBon(Bon bon, Employe emp) throws IOException, URISyntaxException, ClassNotFoundException {
        try (Connection conn = R.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
            setBonParameters(pstmt, bon);
            pstmt.executeUpdate();
            PDFUtil.main(bon, emp);
        } catch (SQLException e) {
            handleSQLException("Error occurred while saving the bon", e);
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

    public static int getNextCodeBon() throws ClassNotFoundException {
        int nextCodeBon = 0;
        String sequenceName = "bons_code_bon_seq"; // Replace with your actual sequence name

        try (Connection conn = R.getConnection();
                Statement stmt = conn.createStatement()) {
            String sql = "SELECT nextval('" + sequenceName + "')";
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    nextCodeBon = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error occurred while getting next code bon", e);
        }

        return nextCodeBon;
    }

    public static void pointageValidate(String num) throws ClassNotFoundException {
        Integer codeEmploye = Integer.valueOf(num.substring(0, num.length() - 1));
        Bon bon = getBonByCode(codeEmploye);

        String sql = "INSERT INTO pointages (date_pointage, code_employe, type_pointage, heure) VALUES (?, ?, ?, ?)";
        LocalDate datePointage = LocalDate.now();
        String typePointage = "S";
        LocalTime heure = LocalTime.now();

        try (Connection connection = R.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(datePointage));
            preparedStatement.setInt(2, bon.getEmploye().getCodeEmploye());
            preparedStatement.setString(3, typePointage);
            preparedStatement.setTime(4, Time.valueOf(heure));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Validation successful");
            }
        } catch (SQLException e) {
            handleSQLException("Error occurred while pointage validation", e);
        }
    }

    public static Bon getBonByCode(Integer codeBon) throws ClassNotFoundException {
        Bon bon = null;

        try (Connection conn = R.getConnection()) {
            String sql = "SELECT * FROM bons WHERE code_bon = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, codeBon);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Employe employe = Employe.getEmploye(rs.getInt("code_employe"));
                        bon = createBonFromResultSet(rs, employe);
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error occurred while getting bon by code", e);
        }

        return bon;
    }

    private static void handleSQLException(String message, SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, message + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
