package fxReseptihaku;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import reseptihaku.Resepti;

/**
 * @author hakom
 * @version 30 Sept 2023
 *
 */
public class ReseptinakymaGUIController implements ModalControllerInterface<Resepti> {

    @FXML private CheckBox checkBox1;
    @FXML private Label reseptinNimi;
    @FXML private Label reseptinKuvaus;
    @FXML private Label reseptinHinta;
    @FXML private Label reseptinValmistusaika;
    @FXML private Label reseptinTahdet;
    @FXML private Label reseptinVaativuus;
    
    @FXML private void handleSulje() { sulje(); }
    @FXML private void handleTulosta() { tulosta(); }
    @FXML private void handleMuokkaaResepti() { muokkaaResepti(); }
    @FXML private void handlePoistaResepti() { poistaResepti(); }
    
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
        
        // asetetaan reseptin tiedot näkymään
        this.reseptinNimi.setText(this.valittuResepti.getNimi());
        this.reseptinKuvaus.setText(this.valittuResepti.getKuvaus());
        this.reseptinHinta.setText(this.valittuResepti.getHintaString());
        this.reseptinValmistusaika.setText(this.valittuResepti.getValmistusaikaString());
        this.reseptinTahdet.setText(this.valittuResepti.getTahdetString());
        this.reseptinVaativuus.setText(this.valittuResepti.getVaativuusString());
    }
    
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
