package fxReseptihaku;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * @author hakom
 * @version 13.9.2023
 *
 */
public class ReseptihakuGUIController implements Initializable {

      @FXML private TextField hakukentta;
      @FXML private StringGrid<String> hakutulokset;
    
      @FXML private void handleUusiResepti() { lisaaResepti(); }
      @FXML private void handleMuokkaaResepti() { muokkaaResepti(); }
      @FXML private void handleAvaaResepti() { avaaResepti(); }
      @FXML private void handleAvaaSatunnainenResepti() { avaaSatunnainenResepti(); }
      @FXML private void handleHaeReseptit() { haeReseptit(); }
      @FXML private void handlePoistaResepti() { poistaResepti(); }
      @FXML private void handleTyhjennaSuodattimet() { tyhjennaSuodattimet(); }
      @FXML private void handleSulje() { sulje(); }
      @FXML private void handleTulosta() { tulosta(); }
      
      // ====================================================================================================
      
      private void sulje() {
          Platform.exit();
      }
      
      
      private void haeReseptit() {
          String hakusana = hakukentta.getText();
          
          if (hakusana.isEmpty()) {
              Dialogs.showMessageDialog("Ei osata hakea kaikkia hakutuloksia vielä");
              return;
          }
          Dialogs.showMessageDialog("Ei osata hakea hakutuloksia hakusanalla \"" + hakusana + "\" vielä");
      }
      
      
      private void lisaaResepti() {
          Dialogs.showMessageDialog("Ei osata vielä luoda uutta reseptipohjaa");
          ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Lisää resepti", null, "");
      }
      
      
      private void muokkaaResepti() {
          // haetaan mikä resepti on valittuna
          int valittuResepti = hakutulokset.getSelectionModel().getSelectedIndex();
          if (valittuResepti < 0) { return; }
          ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaa reseptiä", null, "");
      }
      
      
      private void avaaResepti() {
          int valittuResepti = hakutulokset.getSelectionModel().getSelectedIndex();
          if (valittuResepti < 0) { return; }
          ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaGUIView.fxml"), "Reseptinäkymä", null, "");
      }
      
      
      private void avaaSatunnainenResepti() {
          ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaGUIView.fxml"), "Reseptinäkymä", null, "");
      }
      
      
      private void poistaResepti() {
          // haetaan mikä resepti on valittuna
          int valittuResepti = hakutulokset.getSelectionModel().getSelectedIndex();
          if (valittuResepti < 0) { return; }
          
          // näytetään dialogi reseptin poistamisesta
          boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
          if (vastaus) { Dialogs.showMessageDialog("Ei osata poistaa reseptiä vielä"); }
      }
      
      
      private void tyhjennaSuodattimet() {
          Dialogs.showMessageDialog("Ei osata tyhjentää suodattimia vielä");
      }
      
      
      private void tulosta() {
          // haetaan mikä resepti on valittuna
          int valittuResepti = hakutulokset.getSelectionModel().getSelectedIndex();
          if (valittuResepti < 0) { return; }
          
          Dialogs.showMessageDialog("Ei osata tulostaa vielä");
      }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //
    }
      
}