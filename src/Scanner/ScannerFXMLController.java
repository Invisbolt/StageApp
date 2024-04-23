package Scanner;

import Architecture.BonService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Abidine
 */
public class ScannerFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField numField;
    @FXML
    private Button scanButton;
    @FXML
    private Button quitButton;

    @FXML
    private void handleQuitButtonClick(ActionEvent e) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    private void handleScanButtonClick(ActionEvent e) throws ClassNotFoundException {
        scan();
    }

    @FXML
    private void handleEnterPressed(KeyEvent e) throws ClassNotFoundException {
        if (e.getCode() == KeyCode.ENTER) {
            scan();
        }
    }

    private void scan() throws ClassNotFoundException {
        String input = numField.getText();
        if (input.length() <= 13) {
            BonService.pointageValidate(input);
        } else {
            // Notify user about the maximum length restriction
            // You can show an alert or any other appropriate action
            JOptionPane.showMessageDialog(null, "le numero doi etre d'une longeur 13");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= 13) {
                return change;
            } else {
                return null;
            }
        }));
    }

}
