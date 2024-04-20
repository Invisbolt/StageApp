/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Architecture;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Abidine
 */
public class BonService {
    
    public static List<Bon> getBonsByEmployees(List<Employe> employees) {
    List<Bon> bonsByEmployees = new ArrayList<>();

    // Database connection parameters
    String url = "jdbc:postgresql://localhost:5432/sysGB";
    String user = "postgres";
    String password = "0000";

    // SQL query to fetch bons by employee ID
    String sql = "SELECT * FROM bons WHERE code_employe = ";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         Statement stmt = conn.createStatement()) {

        for (Employe employe : employees) {
            // Concatenate the employee's ID into the SQL query
            String query = sql + employe.getCodeEmploye();

            // Execute the query
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    // Retrieve bon data from the result set
                    int codeBon = rs.getInt("code_bon");
                    LocalDate dateBon = rs.getDate("date_bon").toLocalDate();
                    LocalTime heureBon = rs.getTime("heure_bon").toLocalTime();
                    LocalTime heureV = rs.getTime("heure_v").toLocalTime();
                    String typebon = rs.getString("type_bon");
                    String motif = rs.getString("motif");
                    char etatBon = rs.getString("etat_bon").charAt(0);

                    // Create Bon object and add to the list
                    Bon bon = new Bon(codeBon, employe, typebon, dateBon, heureBon, heureV, motif, etatBon);
                    bonsByEmployees.add(bon);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exceptions appropriately
    }

    return bonsByEmployees;
}
    public void saveBons(Bon bon) {
    // Database connection parameters
    String url = "jdbc:postgresql://localhost:5432/sysGB";
    String user = "postgres";
    String password = "0000";

    // SQL INSERT statement
    String sql = "INSERT INTO bons (code_bon, code_employe, type_bon, date_bon, heure_bon, heure_v, motif, etat_bon) "
                 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Set values for parameters in the SQL statement
        pstmt.setInt(1, bon.getCodeBon());
        pstmt.setInt(2, bon.getEmploye().getCodeEmploye());
        pstmt.setString(3, bon.getType_bon());
        pstmt.setDate(4, Date.valueOf(bon.getDateBon()));
        pstmt.setTime(5, Time.valueOf(bon.getHeureBon()));
        pstmt.setTime(6, Time.valueOf(bon.getHeureV()));
        pstmt.setString(7, bon.getMotif());
        pstmt.setString(8, String.valueOf(bon.getEtatBon()));

        // Execute the INSERT statement
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exceptions appropriately
    }
}
    
}
