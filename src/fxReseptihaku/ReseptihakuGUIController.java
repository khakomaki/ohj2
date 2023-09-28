package fxReseptihaku;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import javafx.application.Platform;
import javafx.fxml.FXML;

/**
 * @author hakom
 * @version 13.9.2023
 *
 */
public class ReseptihakuGUIController {

      @FXML private void handleUusiResepti() { lisaaResepti(); }
      
      @FXML private void handleMuokkaaResepti() { muokkaaResepti(); }
      
      @FXML private void handleAvaaResepti() { avaaResepti(); }
      
      @FXML private void handleAvaaSatunnainenResepti() { avaaSatunnainenResepti(); }
      
      @FXML private void handleHaeReseptit() { haeReseptit(); }
      
      @FXML private void handlePoistaResepti() { poistaResepti(); }
      
      @FXML private void handleTyhjennaSuodattimet() { tyhjennaSuodattimet(); }
      
      @FXML private void handleSulje() { sulje(); }
      
      @FXML private void handleTulosta() { tulosta();}
      
      // ====================================================================================================
      
      private void sulje() {
          Platform.exit();
      }
      
      
      private void haeReseptit() {
          Dialogs.showMessageDialog("Ei osata hakea hakutuloksia vielä");
      }
      
      
      private void lisaaResepti() {
          ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausView.fxml"), "Lisää resepti", null, null);
      }
      
      
      private void muokkaaResepti() {
          ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausView.fxml"), "Muokkaa reseptiä", null, null);
      }
      
      
      private void avaaResepti() {
          ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaView.fxml"), "Reseptinäkymä", null, null);
      }
      
      
      private void avaaSatunnainenResepti() {
          ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaView.fxml"), "Reseptinäkymä", null, null);
      }
      
      
      private void poistaResepti() {
          boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
          if (vastaus) { Dialogs.showMessageDialog("Ei osata poistaa reseptiä vielä"); }
      }
      
      
      private void tyhjennaSuodattimet() {
          Dialogs.showMessageDialog("Ei osata tyhjentää suodattimia vielä");
      }
      
      
      private void tulosta() {
          Dialogs.showMessageDialog("Ei osata tulostaa vielä");
      }
      
}