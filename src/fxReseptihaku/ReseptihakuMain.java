package fxReseptihaku;

import javafx.application.Application;
import javafx.stage.Stage;
import kanta.SailoException;
import reseptihaku.Reseptit;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author hakom
 * @version 13.9.2023
 *
 * Reseptihaku-ohjelman pääohjelma
 */
public class ReseptihakuMain extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("ReseptihakuGUIView.fxml"));
            final Pane root = ldr.load();
            final ReseptihakuGUIController reseptihakuCtrl = (ReseptihakuGUIController)ldr.getController();
            
            // luo ja näyttää reseptihaun stagen
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Reseptihaku");
            primaryStage.show();
            
            // luo uuden reseptit-olion, antaa reseptihakucontrollerille ja käskee lukemaan tiedostosta
            Reseptit reseptit = new Reseptit();
            reseptihakuCtrl.setReseptit(reseptit);
            reseptihakuCtrl.lueTiedostosta();
            reseptihakuCtrl.haeReseptit();
            
        } catch (SailoException sailoException) {
        	System.err.println(sailoException.getMessage());
        	
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param args Ei kaytossa
     */
    public static void main(String[] args) {
        launch(args);
    }
}