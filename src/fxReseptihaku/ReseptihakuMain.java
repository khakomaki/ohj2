package fxReseptihaku;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author hakom
 * @version 13.9.2023
 *
 */
public class ReseptihakuMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("ReseptihakuGUIView.fxml"));
            final Pane root = ldr.load();
            // final ReseptihakuGUIController reseptihakuCtrl = (ReseptihakuGUIController)ldr.getController();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("reseptihaku.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Reseptihaku");
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei kaytossa
     */
    public static void main(String[] args) {
        launch(args);
    }
}