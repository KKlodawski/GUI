package P3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopUpController {

    public static void Pop(String text)
    {
        Stage popUpWindow = new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.initStyle(StageStyle.UTILITY);
        popUpWindow.setTitle("PopUp");
        popUpWindow.setAlwaysOnTop(true);

        Label label1= new Label(text);
        Button button1= new Button("Zamknij");

        button1.setOnAction(e -> popUpWindow.close());

        VBox layout= new VBox(10);

        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);

        Scene scene= new Scene(layout, 250, 150);



        popUpWindow.setScene(scene);
        popUpWindow.showAndWait();

    }
}
