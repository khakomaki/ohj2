package fxReseptihaku;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * @author hakom
 * @version 1 Oct 2023
 *
 */
public class MuokkausGUIController implements ModalControllerInterface<String> {
    
    @FXML private TextField osioTekstiKentta;

    @FXML private void handleSulje() { sulje(); }
    @FXML private void handleTallenna() { tallenna();}
    @FXML private void handleLisaaOsio() { lisaaOsio(); }
    @FXML private void handlePoistaResepti() { poistaResepti();}
    @FXML private void handlePoistaOsio() { poistaOsio(); }
    @FXML private void handlePoistaAinesosa() { poistaAinesosa(); }
    @FXML private void handleLisaaAinesosa() { lisaaAinesosa(); }
    @FXML private void handlePoistaOhje() { poistaOhje(); }
    @FXML private void handleLisaaOhje() { lisaaOhje();}
    
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
    
    private boolean muutoksiaTehty = true;
    
    private void sulje() {
        boolean tallennetaanko = false;
        // näytetään dialogi tallentamisesta jos on tallentamattomia muutoksia
        if (muutoksiaTehty) { 
            tallennetaanko = Dialogs.showQuestionDialog("Reseptin tallennus", "Sinulla on tallentamattomia muutoksia. Haluatko tallentaa?", "Tallenna", "Sulje");
        }
        if (tallennetaanko) { tallenna(); }
        suljeTallentamatta();
    }
    
    
    private void suljeTallentamatta() {
        ModalController.closeStage(osioTekstiKentta);
    }
    
    
    private void tallenna() {
        Dialogs.showMessageDialog("Ei osata vielä tallentaa");
        muutoksiaTehty = false;
    }
    
    
    private void lisaaOsio() {
        String osioTeksti = osioTekstiKentta.getText();
        if (osioTeksti.isEmpty()) { return; }
        Dialogs.showMessageDialog("Ei osata vielä lisätä osiota \"" + osioTeksti + "\"");
    }
    
    
    private void poistaResepti() {
        boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (vastaus) { 
            Dialogs.showMessageDialog("Ei osata poistaa reseptiä vielä");
            suljeTallentamatta();
        }
    }
    
    private void poistaOsio() {
        Dialogs.showMessageDialog("Ei osata vielä poistaa osiota");
    }
    
    
    private void poistaAinesosa() {
        Dialogs.showMessageDialog("Ei osata vielä poistaa ainesosaa");
    }
    
    
    private void lisaaAinesosa() {
        Dialogs.showMessageDialog("Ei osata vielä lisätä ainesosaa");
    }
    
    
    private void poistaOhje() {
        Dialogs.showMessageDialog("Ei osata vielä poistaa ohjetta");
    }
    
    
    private void lisaaOhje() {
        Dialogs.showMessageDialog("Ei osata vielä lisätä ohjetta");
    }

}
