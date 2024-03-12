package P3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Controller {

    @FXML
    private MenuItem AddIngridientMenuButton;

    @FXML
    private MenuItem AddRecipeMenuButton;

    @FXML
    private ListView<String> ContentIngridientList;

    @FXML
    private Label ContentNamePane;

    @FXML
    private Label ContentRating;

    @FXML
    private Label ContentRatingLabel;

    @FXML
    private Pane MainContentPane;

    @FXML
    private Pane MainPane;

    @FXML
    private ScrollPane MainScrollPane;
    ArrayList<Button> recipeButtons = new ArrayList<>();
    VBox root = new VBox();
    @FXML
    private void initialize() {
        for(Button b : recipeButtons) {
            root.getChildren().remove(b);
        }

        for(Recipe x : Main.getListOfRecipes()) {
            Button button = new Button();
            button.setText(x.getName());
            button.prefWidthProperty().bind(MainScrollPane.widthProperty());
            button.setPrefHeight(50);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    setContent(x);
                }
            });
            root.getChildren().add(button);
            recipeButtons.add(button);
        }

        MainScrollPane.setContent(root);

    }

    private void setContent(Recipe recipe) {
        ContentNamePane.setText(recipe.getName());
        ContentRating.setText(recipe.getRating());
        ContentIngridientList.getItems().clear();
        for(Ingridient x : recipe.getIngridients()) {
            ContentIngridientList.getItems().add(x.getName());
        }
    }

    @FXML
    void handleAddIngridient(ActionEvent event) {
        Parent root = null;
        try {
            Stage stage = new Stage();
            root = FXMLLoader.load(getClass().getResource("AddIngridient.fxml"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Dodawanie Składnika");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setAlwaysOnTop(true);
            stage.initOwner(MainPane.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddRecipe(ActionEvent event) {
        Parent root = null;
        try {
            Stage stage = new Stage();
            root = FXMLLoader.load(getClass().getResource("AddRecipe.fxml"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Dodawanie Receptury");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setAlwaysOnTop(true);
            stage.initOwner(MainPane.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
            stage.setOnHidden(e -> {
                initialize();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void Exit(ActionEvent event) {
        ((Stage) MainPane.getScene().getWindow()).close();
    }
}
