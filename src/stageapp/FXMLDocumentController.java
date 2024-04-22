/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stageapp;

import Architecture.Bon;
import Architecture.BonService;
import Architecture.Employe;
import Architecture.Service;
import Architecture.User;
import Utils.CalculatorCheck;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author Abidine
 */
public class FXMLDocumentController implements Initializable {

    private static User user_app;
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/sysGB";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "0000";
    private static Connection con;

    private static Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    @FXML
    private ComboBox<Employe> cBox;
    @FXML
    private ComboBox<String> cBoxB;
    @FXML
    private ComboBox<String> cBoxT;
    @FXML
    private TableView<Bon> bonTableView;
    @FXML
    private TextField empNumField;
    @FXML
    private TextArea motifText;

    @FXML
    private Label acclabel;

    @FXML
    private Label sorlabel;

    @FXML
    private Label enlabel;

    @FXML
    private Button confirmationButton;

    @FXML
    private AnchorPane sorPan;

    @FXML
    private AnchorPane accPan;

    @FXML
    private AnchorPane enPan;

    @FXML
    private void handleAccueilClick(MouseEvent event) {
        // Your code to handle click on Accueil label
        accPan.setVisible(true);
        accPan.setDisable(false);
        sorPan.setVisible(false);
        sorPan.setDisable(true);
        enPan.setVisible(false);
        enPan.setDisable(true);

        // Add the 'used-label' class to the acclabel element
        acclabel.getStyleClass().add("used-label");

        // Remove the 'used-label' class from the sorlabel and enlabel elements
        sorlabel.getStyleClass().remove("used-label");
        enlabel.getStyleClass().remove("used-label");

        System.out.println("Accueil label clicked!");
    }

    @FXML
    private void handleExitClick(MouseEvent e) throws IOException, URISyntaxException {
        Stage stage = (Stage) acclabel.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    @FXML
    private void handleBonSortieClick(MouseEvent event) {
        // Your code to handle click on Bon Sortie label
        accPan.setVisible(false);
        accPan.setDisable(true);
        sorPan.setVisible(true);
        sorPan.setDisable(false);
        enPan.setVisible(false);
        enPan.setDisable(true);
        // Add the 'used-label' class to the acclabel element
        sorlabel.getStyleClass().add("used-label");

        // Remove the 'used-label' class from the sorlabel and enlabel elements
        acclabel.getStyleClass().remove("used-label");
        enlabel.getStyleClass().remove("used-label");
        System.out.println("Bon Sortie label clicked!");
    }

    @FXML
    private void handleBonEntreeClick(MouseEvent event) {
        // Your code to handle click on Bon Entrée label
        accPan.setVisible(false);
        accPan.setDisable(true);
        sorPan.setVisible(false);
        sorPan.setDisable(true);
        enPan.setVisible(true);
        enPan.setDisable(false);

        // Add the 'used-label' class to the acclabel element
        enlabel.getStyleClass().add("used-label");

        // Remove the 'used-label' class from the sorlabel and enlabel elements
        sorlabel.getStyleClass().remove("used-label");
        acclabel.getStyleClass().remove("used-label");

        System.out.println("Bon Entrée label clicked!");
    }

    @FXML
    private void handleConfirmationClick(ActionEvent event) throws IOException, URISyntaxException {
        System.out.println("TEST");
        Employe emp = cBox.getValue();
        String cBoxTValue = cBoxT.getValue(); // Store the value to avoid multiple calls
        System.out.println(emp + "  " + cBoxTValue);
        // Check if cBox and cBoxT are not null
        if (emp != null && cBoxTValue != null) {
            if (cBoxTValue.equals("Bon Entrée")) {
                String type = "E";
                char etatbon = 'V';
                Bon bon = new Bon(BonService.getNextCodeBon(), emp, type, LocalDate.now(), LocalTime.now(), LocalTime.now(), motifText.getText(), etatbon);
                // Add code to use bon as needed
                BonService.saveBon(bon, emp);
                BonService.pointageValidate(CalculatorCheck.fullDigit(String.valueOf(bon.getCodeBon())));

            } else if (cBoxTValue.equals("Bon Sortie")) {
                String type = "S";
                char etatbon = 'N';
                Bon bon = new Bon(BonService.getNextCodeBon(), emp, type, LocalDate.now(), LocalTime.now(), null, motifText.getText(), etatbon);

                // Add code to use bon as needed
                BonService.saveBon(bon, emp);
            } else {
                JOptionPane.showMessageDialog(null, "Choix De bon n'est pas valide");
            }
        } else {
            // Handle the case where emp, cBox, or cBoxT is null
            JOptionPane.showMessageDialog(null, "Les champs ne doivent pas être nuls.");
        }
        empNumField.setText("");
        motifText.setText("");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        confirmationButton.setOnAction((event) -> {
            try {
                this.handleConfirmationClick(event);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Connectdb();
        cBox.setOnAction(event -> {
            // Code to execute when the ComboBox selection changes
            Employe selectedItem = cBox.getSelectionModel().getSelectedItem();
            empNumField.setText(String.valueOf(selectedItem.getCodeEmploye()));
            empNumField.setEditable(false);
        });
        cBoxB.setOnAction(e -> {
            filterBonsBySelectedOption();
        });

        TableColumn<Bon, Integer> codeBonColumn = new TableColumn<>("Code Bon");
        codeBonColumn.setCellValueFactory(new PropertyValueFactory<>("codeBon"));

        TableColumn<Bon, Employe> employeColumn = new TableColumn<>("Employe");
        employeColumn.setCellValueFactory(new PropertyValueFactory<>("employe"));

        TableColumn<Bon, String> typeBonColumn = new TableColumn<>("Type Bon");
        typeBonColumn.setCellValueFactory(new PropertyValueFactory<>("type_bon"));

        TableColumn<Bon, LocalDate> dateBonColumn = new TableColumn<>("Date Bon");
        dateBonColumn.setCellValueFactory(new PropertyValueFactory<>("dateBon"));

        TableColumn<Bon, LocalTime> heureBonColumn = new TableColumn<>("Heure Bon");
        heureBonColumn.setCellValueFactory(new PropertyValueFactory<>("heureBon"));

        TableColumn<Bon, LocalTime> heureVColumn = new TableColumn<>("Heure V");
        heureVColumn.setCellValueFactory(new PropertyValueFactory<>("heureV"));

        TableColumn<Bon, String> motifColumn = new TableColumn<>("Motif");
        motifColumn.setCellValueFactory(new PropertyValueFactory<>("motif"));

        TableColumn<Bon, Character> etatBonColumn = new TableColumn<>("Etat Bon");
        etatBonColumn.setCellValueFactory(new PropertyValueFactory<>("etatBon"));
        bonTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bonTableView.getColumns().addAll(codeBonColumn, employeColumn, typeBonColumn,
                dateBonColumn, heureBonColumn, heureVColumn, motifColumn, etatBonColumn);

        // TODO
        accPan.setVisible(true);
        accPan.setDisable(false);
        sorPan.setVisible(false);
        sorPan.setDisable(true);
        enPan.setVisible(false);
        enPan.setDisable(true);
        ArrayList<String> tlist = new ArrayList<String>(2);
        tlist.add("Bon Sortie");
        tlist.add("Bon Entrée");
        cBoxT.getItems().setAll(tlist);
        ArrayList<String> blist = new ArrayList<String>(3);
        blist.add("Annuel");
        blist.add("Mensuel");
        blist.add("Semaine");
        cBoxB.getItems().setAll(blist);

        try {
            con = getConnection();
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Connectdb() {
        try {
            con = getConnection();
            Statement statement = con.createStatement();
            Employe chef = Employe.getEmploye(user_app.getCodeEmploye());
            Service sr = Service.getService(chef.getService_e(), chef);
            System.out.println(chef.getService_e());
            cBox.getItems().setAll(Service.getEmployes(statement, sr));
            bonTableView.getItems().clear();
            bonTableView.getItems().addAll(BonService.getBonsByEmployees(cBox.getItems()));
            originalBons = bonTableView.getItems();
            System.out.println(cBox.getItems());
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR); // Change to ERROR type for exceptions
            alert.setTitle("Error Dialog"); // Change title to indicate it's an error
            alert.setHeaderText("Exception Encountered"); // Provide a header text
            alert.setContentText(ex.getMessage()); // Set the exception message as content
            alert.showAndWait(); // Show the alert and wait for user response
            ex.printStackTrace();
            System.out.println(user_app);
            Platform.exit(); // Exit the application if user acknowledges the error
        }
    }

    public static void initData(User user) {
        user_app = user;

    }

    private List<Bon> originalBons = new ArrayList<>();

    private void filterBonsBySelectedOption() {
        String selectedOption = cBoxB.getValue();
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = null;
        LocalDate endDate = null;

        switch (selectedOption) {
            case "Annuel":
                startDate = currentDate.minusYears(1);
                endDate = currentDate;
                break;
            case "Mensuel":
                startDate = currentDate.minusMonths(1);
                endDate = currentDate;
                break;
            case "Semaine":
                startDate = currentDate.minusWeeks(1);
                endDate = currentDate;
                break;
            default:
                break;
        }

        // Filter the original list of bons based on the selected date range
        ArrayList<Bon> filteredBons = new ArrayList<>();
        for (Bon bon : originalBons) {
            LocalDate bonDate = bon.getDateBon();
            if (bonDate.isAfter(startDate) || bonDate.isEqual(startDate)) {
                filteredBons.add(bon);
            }
        }

        // Clear the existing items in the TableView and add the filtered items
        bonTableView.getItems().clear();
        bonTableView.getItems().addAll(filteredBons);
    }

}
