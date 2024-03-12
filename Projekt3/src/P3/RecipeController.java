package P3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class RecipeController {

    @FXML
    private Button CancelButton;

    @FXML
    private Pane MainAddRecipePanel;

    @FXML
    private ListView<String> tableView;
    @FXML
    private TextField nameField;
    @FXML
    private Button confirmButton;
    ArrayList<String> listOfRecipeIngridients = new ArrayList<>();
    @FXML
    private void initialize(){
        Recipe.getListOfAllIngridients().stream().forEach(e->tableView.getItems().add(e.getName()));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        /*tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    System.out.println(tableView.getItems().get());
                    *//*if(listOfRecipeIngridients.contains(mouseEvent.getTarget().toString())) {
                        listOfRecipeIngridients.remove(mouseEvent.getTarget().toString());
                    } else {
                        listOfRecipeIngridients.add(mouseEvent.getTarget().toString());
                    }*//*
                }
                //for(String s : listOfRecipeIngridients) System.out.println(s + " ");
            }
        });*/
        tableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                //System.out.println(change.getList());
                listOfRecipeIngridients.clear();
                for(String s : change.getList().stream().toList()) listOfRecipeIngridients.add(s);
            }
        });
    }

    @FXML
    void CancelAction(MouseEvent event) {
        ((Stage) CancelButton.getScene().getWindow()).close();
    }

    @FXML
    void confirmAction(MouseEvent event) {
        if(Main.recipesContainsName(nameField.getText())) {
            PopUpController.Pop("Taki przepis już istnieje!");
        } else if(nameField.getText().equals("")){
            PopUpController.Pop("Musisz nazwać przepis!");
        }
        else {
            {
                ArrayList<Ingridient> tmp = new ArrayList<>();
                for (String s : listOfRecipeIngridients) {
                    try {
                        tmp.add(Recipe.searchIngidientByName(s));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(tmp.size() < 2) {
                    PopUpController.Pop("Musisz wybrać conajmniej 2 skłądniki!");
                } else {
                    try {
                        Recipe recTmp = new Recipe(tmp,nameField.getText().toString());
                        Main.addRecipe(recTmp);
                        DataController.saveRecipes(Main.getListOfRecipes());
                        ((Stage)MainAddRecipePanel.getScene().getWindow()).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
