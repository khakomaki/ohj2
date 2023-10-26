package fxReseptihaku;

import java.io.PrintStream;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import reseptihaku.Osio;
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
    @FXML private VBox ainesosatVBox;
    @FXML private VBox ohjeetVBox;
    
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
        
        // tyhjennetään ainesosat ja ohjeet
        this.ainesosatVBox.getChildren().clear();
        this.ohjeetVBox.getChildren().clear();
        
        // lisää tekstiin osioiden nimet ja niiden ainesosat
        for (int i = 0; i < this.valittuResepti.getOsiot().getLkm(); i++) {
            Osio osio = this.valittuResepti.getOsiot().annaIndeksista(i);
            
            // lisätään osion nimi -Label
            Label OsionNimi = new Label(osio.getNimi());
            ainesosatVBox.getChildren().add(OsionNimi);
            
            // lisätään osion ainesosat -TextArea "tulostamalla" ne sen tietovirtaan
            TextArea ainesosatTextArea = new TextArea();
            try (PrintStream os = TextAreaOutputStream.getTextPrintStream(ainesosatTextArea)) {
                osio.annaOsionAinesosat().tulostaOsionAinesosat(os);
            }
            ainesosatTextArea.setEditable(false);
            ainesosatVBox.getChildren().add(ainesosatTextArea);
        }
    }
    
    private void sulje() {
        ModalController.closeStage(reseptinNimi);
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
