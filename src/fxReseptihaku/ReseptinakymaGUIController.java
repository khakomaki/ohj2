package fxReseptihaku;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

/**
 * @author hakom
 * @version 30 Sept 2023
 *
 */
public class ReseptinakymaGUIController implements ModalControllerInterface<String> {

    @FXML private CheckBox checkBox1;
    
    @FXML private void handleSulje() { sulje(); }
    @FXML private void handleTulosta() { tulosta(); }
    @FXML private void handleMuokkaaResepti() { muokkaaResepti(); }
    @FXML private void handlePoistaResepti() { poistaResepti(); }
    
    @Override
    public String getResult() { return null; }

    @Override
    public void handleShown() {
        // 
    }

    @Override
    public void setDefault(String oletus) {
        // 
    }
    
    // ====================================================================================================
    
    private void sulje() {
        ModalController.closeStage(checkBox1);
    }
    
    private void tulosta() {
        Dialogs.showMessageDialog("Ei osata tulostaa vielä");
    }
    
    private void muokkaaResepti() {
        ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaa reseptiä", null, null);
    }
    
    private void poistaResepti() {
        boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (vastaus) { Dialogs.showMessageDialog("Ei osata poistaa reseptiä vielä"); }
    }
    
}
