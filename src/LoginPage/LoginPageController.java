/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoginPage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import Architecture.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import stageapp.FXMLDocumentController;
import stageapp.StageApp;

/**
 * FXML Controller class
 *
 * @author Abidine
 */
public class LoginPageController implements Initializable {

    @FXML
    TextField userField;

    @FXML
    PasswordField passField;

    @FXML
    Button scanButton;

    @FXML
    Button conButton;

    @FXML
    Button quitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleScanButtonClick() {
        // Add your logic here for when the scanButton is clicked
    }

    @FXML
    private void handleConnectButtonClick() throws Exception {
        // Get the username and password entered by the user
        String username = userField.getText();
        String password = passField.getText();

        // Check if username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            // If either field is empty, display a message
            showAlert("Error", "Please fill in both username and password fields.", AlertType.ERROR);
            return;
        }

        // JDBC URL for PostgreSQL database
        String url = "jdbc:postgresql://localhost:5432/sysGB"; // Replace "your_database_name" with your actual database
                                                               // name

        try {
            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, "postgres", "0000");

            // Create a PreparedStatement to check if the user exists
            String query = "SELECT * FROM utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if a row was returned (i.e., if the user exists)
            if (resultSet.next()) {
                // User exists, open the StageApp or perform further actions here
                showAlert("Success", "User exists. Opening StageApp...", AlertType.INFORMATION);
                Stage loginStage = (Stage) userField.getScene().getWindow();
                loginStage.close();

                int c=resultSet.getInt("code_employe");
                System.out.println(username+password+c);

                // Show the StageApp stage
                Stage stageApp = new Stage();
                StageApp st=new StageApp();
                FXMLDocumentController.initData(new User(username, password, c));
                st.start(stageApp);

            } else {
                // User doesn't exist, display an error message
                showAlert("Error", "User does not exist. Please check your credentials.", AlertType.ERROR);
            }

            // Close the ResultSet, PreparedStatement, and database connection
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            // Handle any SQL exceptions
            showAlert("Error", "Failed to connect to PostgreSQL database:\n" + e.getMessage(), AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleQuitButtonClick() {
        // Add your logic here for when the quitButton is clicked
        Stage stage = (Stage) passField.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
}
