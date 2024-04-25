package Architecture;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import res.R; // Import the R class

public class Employe {

    private int codeEmploye;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private int service_c;

    public Employe() {
    }

    public Employe(int codeEmploye, String nom, String prenom, LocalDate dateNaissance, int service_c) {
        this.codeEmploye = codeEmploye;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.service_c = service_c;
    }

    public void setCodeEmploye(int codeEmploye) {
        this.codeEmploye = codeEmploye;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getCodeEmploye() {
        return codeEmploye;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public int getService_e() {
        return service_c;
    }

    public static Employe getEmploye(int codeEmploye) {
        Employe employe = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Establishing connection to the database using resources from R class
            connection = R.getConnection();

            // Creating a statement
            statement = connection.createStatement();

            // Execute query to fetch employee information
            String query = "SELECT * FROM employes WHERE code_employe = " + codeEmploye;
            resultSet = statement.executeQuery(query);

            // Check if result set has any rows
            if (resultSet.next()) {
                // Map the retrieved data to an Employe object
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                LocalDate dateNaissance = resultSet.getDate("date_naissance").toLocalDate();
                int serviceCode = resultSet.getInt("code_service");

                // Create an Employe object
                employe = new Employe(codeEmploye, nom, prenom, dateNaissance, serviceCode);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Employe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Employe.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Close ResultSet, Statement, and Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Employe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return employe;
    }

    public String toString() {
        return (this.nom + " " + this.prenom);
    }

}
