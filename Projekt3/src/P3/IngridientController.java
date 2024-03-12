package P3;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class IngridientController {

    @FXML
    private TextField IngridientNameField;

    @FXML
    private ChoiceBox<String> IngridientTasteField;

    @FXML
    private Button CancelButton;

    @FXML
    private Button ConfirmButton;
    @FXML
    private Label textLabel;

    @FXML
    void CancelAction(MouseEvent event) {
        ((Stage) CancelButton.getScene().getWindow()).close();
    }

    @FXML
    void ConfirmAction(MouseEvent event) {
        if(!IngridientNameField.getText().equals("") && IngridientTasteField.getValue() != null) {
            boolean equality = true;
            Ingridient ingridient = new Ingridient(IngridientNameField.getText().toString(), Ingridient.TASTE.valueOf(IngridientTasteField.getValue()));
            for(Ingridient x : Recipe.getListOfAllIngridients()) {
                if(x.getName().equals(ingridient.getName())) {
                    equality = false;
                }
            }
            if(equality) {
                Recipe.addToListOfAllIngridients(ingridient);
                try {
                    DataController.saveIngridients(Recipe.getListOfAllIngridients());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((Stage) CancelButton.getScene().getWindow()).close();
            }
            else PopUpController.Pop("Składnik o takiej nazwie już istanieje!");

        }else {
            PopUpController.Pop("Podano nieprawidłowe wartości!");
        }
    }

    @FXML
    private void initialize() {
        for(Ingridient.TASTE taste : Ingridient.TASTE.values()) {
            IngridientTasteField.getItems().add(taste.name());
        }
    }

}
