package fxReseptihaku;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import reseptihaku.Resepti;
import reseptihaku.Reseptit;

/**
 * @author hakom
 * @version 13.9.2023
 *
 */
public class ReseptihakuGUIController implements Initializable {

      @FXML private TextField hakukentta;
      @FXML private Label tuloksetTeksti;
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
      
      @Override
      public void initialize(URL url, ResourceBundle rb) {
          alusta();
      }
      
      // ====================================================================================================
      
      private Reseptit reseptit;
      
      private void sulje() {
          Platform.exit();
      }
      
      
      /**
       * Hoitaa alustukset, joita FXMLLoader ei hoida
       */
      public void alusta() {
          hakutulokset.clear();
      }
      
      
      private void haeReseptit() {
          String hakusana = hakukentta.getText();
          hakutulokset.clear();
          
          StringBuilder sb = new StringBuilder(hakutulokset.getRivit());
          
          if (hakusana.isEmpty()) {
              hakusana = "*";
              // haetaan kaikki reseptit jos haetaan ilman hakusanaa
              for (int i = 0; i < reseptit.getLkm(); i++) {
                  Resepti resepti = reseptit.annaIndeksista(i);
                  if (resepti == null) { continue; }
                  sb.append(resepti.getTaulukkoMuodossa());
                  sb.append("\n");
              }
          } else {
              // käydään reseptit läpi ja lisätään hakusanaa vastaavat
              for (int i = 0; i < reseptit.getLkm(); i++) {
                  Resepti resepti = reseptit.annaIndeksista(i);
                  if (resepti == null) { continue; }
                  if (resepti.onkoNimessa(hakusana)) {
                      sb.append(resepti.getTaulukkoMuodossa());
                      sb.append("\n");
                  }
              }
          }
          
          hakutulokset.setRivit(sb.toString());
          asetaTuloksetTeksti(hakusana);
      }
      
      
      private void asetaTuloksetTeksti(String hakusana) {
          StringBuilder sb = new StringBuilder();
          sb.append("Tulokset hakusanalle \"");
          sb.append(hakusana);
          sb.append("\"");
          this.tuloksetTeksti.setText(sb.toString());
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
      
      
      /**
      * @param reseptit käytettävät Reseptit
      */
      public void setReseptit(Reseptit reseptit) {
          this.reseptit = reseptit;
          // TODO: ota nämä pois kun ei enää tarvitse
          reseptit.lisaaMustikkapiirakka();
          reseptit.lisaaMustikkapiirakka();
          reseptit.lisaa(new Resepti(15, "Juustokakku"));
      }
      
      
      
}