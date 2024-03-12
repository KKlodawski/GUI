package P3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    /*public static ArrayList<Recipe> listOfRecipes = new ArrayList<>() {
        {
            try {
                add(new Recipe(new ArrayList<Ingridient>() {{
                    add(Recipe.getListOfAllIngridients().get(1));
                    add(Recipe.getListOfAllIngridients().get(6));
                    add(Recipe.getListOfAllIngridients().get(10));
                }},"Ciasto"));
                add(new Recipe(new ArrayList<Ingridient>() {{
                    add(Recipe.getListOfAllIngridients().get(2));
                    add(Recipe.getListOfAllIngridients().get(3));
                    add(Recipe.getListOfAllIngridients().get(4));
                }},"Kotlet z bekonem i brokułami"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };*/
    public static ArrayList<Recipe> listOfRecipes = new ArrayList<>();
    @Override
    public void init() throws Exception {
        super.init();
        DataController.loadIngridients();
        DataController.loadRecipes();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Ksiazka kucharska");
        stage.setOnHidden(e -> Platform.exit());
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(false);
        stage.show();
    }

    public static void addRecipe(Recipe recipe) {
        listOfRecipes.add(recipe);
    }

    public static ArrayList<Recipe> getListOfRecipes() {
        return listOfRecipes;
    }

    public static boolean recipesContainsName(String name) {
        for(Recipe s : listOfRecipes) {
            if(s.getName().equals(name)) return true;
        }
        return false;
    }
}
