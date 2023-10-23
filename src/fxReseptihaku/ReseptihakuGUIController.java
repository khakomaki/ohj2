package fxReseptihaku;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kanta.Satunnaisluku;
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
          hakutulokset.setColumnWidth(-1, 120);
      }
      
      // ====================================================================================================
      
      private final String hakutuloksetOtsikko = "reseptin nimi |hintaluokka |valmistusaika |tähdet |vaativuustaso\n";
      private Reseptit reseptit;
      private ArrayList<Resepti> hakuReseptit = new ArrayList<Resepti>();
      
      private void sulje() {
          Platform.exit();
      }
      
      
      /**
       * Hoitaa alustukset, joita FXMLLoader ei hoida
       */
      public void alusta() {
          hakutulokset.clear();
          
          // asetetaan sama kuin hakiessa reseptejä
          hakutulokset.setColumnWidth(-1, 120);
      }
      
      
      private void haeReseptit() {
          String hakusana = hakukentta.getText();
          this.hakutulokset.clear();
          
          StringBuilder sb = new StringBuilder(hakutuloksetOtsikko);
          
          // haetaan hakusanaa vastanneet reseptit
          this.hakuReseptit = this.reseptit.etsiNimella(hakusana);
          
          for (int i = 0; i < this.hakuReseptit.size(); i++) {
              // lisää reseptin taulukkomuotoisen tekstin StringBuilderiin
              sb.append(this.hakuReseptit.get(i).getTaulukkoMuodossa());
              sb.append("\n");
          }
          
          this.hakutulokset.setRivit(sb.toString());
          
          // asettaa muuten väärän kokoiseksi
          this.hakutulokset.setColumnWidth(-1, 120);
          
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
          int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
          if (valittuResepti < 0) { return; }
          ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaa reseptiä", null, "");
      }
      
      
      private void avaaResepti() {
          int valittuResepti = hakutulokset.getSelectionModel().getSelectedIndex();
          
          // poistutaan jos indeksi ei ole mieluisa
          if (valittuResepti < 0 || this.hakuReseptit.size() < valittuResepti) { return; }
          ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaGUIView.fxml"), "Reseptinäkymä", null, reseptit.annaIndeksista(valittuResepti));
      }
      
      
      private void avaaSatunnainenResepti() {
          // poistutaan jos ei ole reseptejä
          if (this.hakuReseptit.size() < 1) { return; }
          
          // arpoo hakutuloksista satunnaisen reseptin ja avaa sen
          Resepti satunnainenResepti = this.hakuReseptit.get(Satunnaisluku.rand(0, this.hakuReseptit.size() - 1));
          ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaGUIView.fxml"), "Reseptinäkymä", null, satunnainenResepti);
      }
      
      
      private void poistaResepti() {
          // haetaan mikä resepti on valittuna
          int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
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
          int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
          if (valittuResepti < 0) { return; }
          
          Dialogs.showMessageDialog("Ei osata tulostaa vielä");
      }
      
      
      /**
      * @param reseptit käytettävät Reseptit
      */
      public void setReseptit(Reseptit reseptit) {
          this.reseptit = reseptit;
          // TODO: ota nämä pois kun ei enää tarvitse
          for (int i = 0; i < 10; i++) {
              Resepti uusiResepti = this.reseptit.lisaaMustikkapiirakka();
              uusiResepti.satunnaisetAttribuutit();
          }
          
          reseptit.lisaa(new Resepti(15, "Juustokakku"));
      }
      
      
      
}