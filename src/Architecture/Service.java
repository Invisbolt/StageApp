package Architecture;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import res.R; // Import the R class

public class Service {

    private int code_s;
    private String nom_s;
    private Employe chef_s;

    public Service(int code_s, String nom_s, Employe chef_s) {
        this.code_s = code_s;
        this.nom_s = nom_s;
        this.chef_s = chef_s;
    }

    public static ArrayList<Employe> getEmployes(Service service) throws ClassNotFoundException {
        ArrayList<Employe> list = new ArrayList<>();

        try (Connection connection = R.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM employes WHERE code_service=" + service.getCode_s() +"ORDER BY nom");

            while (set.next()) {
                list.add(new Employe(set.getInt("code_employe"), set.getString("nom"), set.getString("prenom"), LocalDate.of(2002, 12, 12), service.getCode_s()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
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

    public static Service getService(int code_s, Employe emp) throws ClassNotFoundException {
        Service service = null;

        try (Connection connection = R.getConnection();
                Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM services WHERE code_service = " + code_s);

            if (resultSet.next()) {
                String serviceName = resultSet.getString("nom");
                service = new Service(code_s, serviceName, emp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }

        return service;
    }
}
