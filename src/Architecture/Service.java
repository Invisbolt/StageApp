package Architecture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abidine
 */
public class Service {

    private int code_s;
    private String nom_s;
    private Employe chef_s;

    public Service(int code_s, String nom_s, Employe chef_s) {
        this.code_s = code_s;
        this.nom_s = nom_s;
        this.chef_s = chef_s;
    }

    public static ArrayList<Employe> getEmployes(Statement statement, Service service) throws SQLException {
        ResultSet set = statement.executeQuery("SELECT * FROM employes WHERE code_service=" + service.getCode_s());
        ArrayList<Employe> list = new ArrayList<Employe>();

        // No need to call set.next() here as it was already called to check if there are any rows
        while (set.next()) {
            list.add(new Employe(set.getInt("code_employe"), set.getString("nom"), set.getString("prenom"),LocalDate.of(2002, 12, 12), service.getCode_s()));
      }

        return list;
    }

    public int getCode_s() {
        return code_s;
    }

    public String getNom_s() {
        return nom_s;
    }

    public Employe getChef_s() {
        return chef_s;
    }

    public static void main(String[] args) throws ClassNotFoundException {

        try {
            // Establishing connection to the database
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sysGB", "postgres", "0000");

            // Creating a statement
            Statement statement = connection.createStatement();

            Service ser = new Service(1, "Fuck", new Employe());

            System.out.println(getEmployes(statement, ser));
        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    
    public static Service getService(int code_s,Employe emp) {
    Service service = null;
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    try {
        // Establishing connection to the database
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sysGB", "postgres", "0000");
        
        // Creating a statement
        statement = connection.createStatement();

        // Execute query to fetch service information
        String query = "SELECT * FROM services WHERE code_service = "+ code_s;
        resultSet = statement.executeQuery(query);

        // Check if result set has any rows
        if (resultSet.next()) {
            // Map the retrieved data to a Service object
            int serviceCode = resultSet.getInt("code_service");
            String serviceName = resultSet.getString("nom");
            // You can map other attributes as needed

            // Create a Service object
            service = new Service(serviceCode, serviceName, emp); // Replace null with the chef if available
        }
    } catch (SQLException ex) {
        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
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
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    return service;
}
}
