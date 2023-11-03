package fxReseptihaku;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reseptihaku.Resepti;

/**
 * @author hakom
 * @version 1 Oct 2023
 *
 */
public class MuokkausGUIController implements ModalControllerInterface<Resepti> {
    
    @FXML private TextField osioTekstiKentta;
    @FXML private VBox ainesosaJaOhjeetVBox;
    @FXML private TextField reseptinNimiTextField;
    @FXML private TextArea kuvausTextArea;
    @FXML private ComboBoxChooser<String> hintaSuodatin;
    @FXML private ComboBoxChooser<String> valmistusaikaSuodatin;
    @FXML private ComboBoxChooser<String> tahdetSuodatin;
    @FXML private ComboBoxChooser<String> vaativuusSuodatin;

    @FXML private void handleSulje() { sulje(); }
    @FXML private void handleTallenna() { tallenna();}
    @FXML private void handleLisaaOsio() { lisaaOsio(); }
    @FXML private void handlePoistaResepti() { poistaResepti();}
    @FXML private void handlePoistaOsio() { poistaOsio(); }
    @FXML private void handlePoistaAinesosa() { poistaAinesosa(); }
    @FXML private void handleLisaaAinesosa() { lisaaAinesosa(); }
    @FXML private void handlePoistaOhje() { poistaOhje(); }
    @FXML private void handleLisaaOhje() { lisaaOhje();}
    
    // ====================================================================================================
    
    private Resepti valittuResepti;
    
    @Override
    public Resepti getResult() { 
        return this.valittuResepti; 
    }

    @Override
    public void handleShown() {
        //
    }

    @Override
    public void setDefault(Resepti oletus) {
        this.valittuResepti = oletus;
        // luodaan Reseptipohja jos annettu Resepti on null
        if (oletus == null) { this.valittuResepti = new Resepti(); }
        
        naytaReseptinOminaisuudet();
        
        // tyhjentää VBox sisällön
        this.ainesosaJaOhjeetVBox.getChildren().clear();
    }
    
    private void naytaReseptinOminaisuudet() {
        // TODO vähennä samanlaisen koodin toistoa
        this.reseptinNimiTextField.setText(this.valittuResepti.getNimi());
        this.kuvausTextArea.setText(this.valittuResepti.getKuvaus());
        
        this.hintaSuodatin.clear(); 
        this.hintaSuodatin.setRivit("\n" + Resepti.getHintaVaihtoehdot());
        this.hintaSuodatin.setSelectedIndex(this.valittuResepti.getHinta());
        
        this.valmistusaikaSuodatin.clear(); 
        this.valmistusaikaSuodatin.setRivit("\n" + Resepti.getValmistusaikaVaihtoehdot());
        this.valmistusaikaSuodatin.setSelectedIndex(this.valittuResepti.getValmistusaika());
        
        this.tahdetSuodatin.clear(); 
        this.tahdetSuodatin.setRivit("\n" + Resepti.getTahdetVaihtoehdot());
        this.tahdetSuodatin.setSelectedIndex(this.valittuResepti.getTahdet());
        
        this.vaativuusSuodatin.clear(); 
        this.vaativuusSuodatin.setRivit("\n" + Resepti.getVaativuusVaihtoehdot());
        this.vaativuusSuodatin.setSelectedIndex(this.valittuResepti.getVaativuus());
    }
    
    private void sulje() {
        boolean tallennetaanko = false;
        // näytetään dialogi tallentamisesta jos on tallentamattomia muutoksia
        if (tulikoMuutoksia()) { 
            tallennetaanko = Dialogs.showQuestionDialog("Reseptin tallennus", "Sinulla on tallentamattomia muutoksia. Haluatko tallentaa?", "Tallenna", "Sulje");
        }
        if (tallennetaanko) { tallenna(); }
        suljeTallentamatta();
    }
    
    
    private void suljeTallentamatta() {
        ModalController.closeStage(reseptinNimiTextField);
    }
    
    
    private void tallenna() {
        // ei tallenneta turhaan jos ei ole tullut muutoksia
        if (!tulikoMuutoksia()) { return; }
        Dialogs.showMessageDialog("Ei osata vielä tallentaa");
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
    
    
    private boolean tulikoMuutoksia() {
        // TODO vertaa tuliko reseptiin muutoksia
        return true;
    }

}
