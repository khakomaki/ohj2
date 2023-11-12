package fxReseptihaku;

import java.net.URL;
import java.util.ArrayList;
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
      @FXML private ComboBoxChooser<String> lajitteluPeruste;
      @FXML private ComboBoxChooser<String> alinHinta;
      @FXML private ComboBoxChooser<String> ylinHinta;
      @FXML private ComboBoxChooser<String> alinValmistusaika;
      @FXML private ComboBoxChooser<String> ylinValmistusaika;
      @FXML private ComboBoxChooser<String> alinTahdet;
      @FXML private ComboBoxChooser<String> ylinTahdet;
      @FXML private ComboBoxChooser<String> alinVaativuus;
      @FXML private ComboBoxChooser<String> ylinVaativuus;
    
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
      private ArrayList<ComboBoxChooser<String>> suodatinValinnat = new ArrayList<>();
      private ArrayList<Resepti> hakuReseptit = new ArrayList<Resepti>();
      
      private void sulje() {
          Platform.exit();
      }
      
      
      /**
       * Hoitaa alustukset, joita FXMLLoader ei hoida
       */
      public void alusta() {
          alustaSuodattimet();
          alustaHakutulokset();
      }
      
      
      private void alustaHakutulokset() {
          this.hakutulokset.clear();
          
          // asetetaan sama kuin hakiessa reseptejä
          this.hakutulokset.setColumnWidth(-1, 120);
          
          hakutulokset.setSortable(-1, false);
      }
      
      
      private void alustaSuodattimet() {
          // TODO: varmistu ettei suodatin ole null
          // TODO: hankkiudu eroon toistuvasta koodista
          
          // lisätään yksittäiset suodattimet listaan ja tyhjennetään fxml tiedoston esimerkki tiedot
          this.suodatinValinnat.add(this.alinHinta);
          this.suodatinValinnat.add(this.ylinHinta);
          this.suodatinValinnat.add(this.alinValmistusaika);
          this.suodatinValinnat.add(this.ylinValmistusaika);
          this.suodatinValinnat.add(this.alinTahdet);
          this.suodatinValinnat.add(this.ylinTahdet);
          this.suodatinValinnat.add(this.alinVaativuus);
          this.suodatinValinnat.add(this.ylinVaativuus);
          for (ComboBoxChooser<String> suodatin : this.suodatinValinnat) {
              suodatin.clear();
              suodatin.setPrefWidth(145);
          }
          
          // lisää suodattimien vaihtoehdot ja jättää nykyisen valinnan tyhjäksi
          this.lajitteluPeruste.setRivit("Hinta\nValmistusaika\nTähdet\nVaativuus");
          this.alinHinta.setRivit("\n" + Resepti.getHintaVaihtoehdot());
          this.ylinHinta.setRivit("\n" + Resepti.getHintaVaihtoehdot());
          this.alinValmistusaika.setRivit("\n" + Resepti.getValmistusaikaVaihtoehdot());
          this.ylinValmistusaika.setRivit("\n" + Resepti.getValmistusaikaVaihtoehdot());
          this.alinTahdet.setRivit("\n" + Resepti.getTahdetVaihtoehdot());
          this.ylinTahdet.setRivit("\n" + Resepti.getTahdetVaihtoehdot());
          this.alinVaativuus.setRivit("\n" + Resepti.getVaativuusVaihtoehdot());
          this.ylinVaativuus.setRivit("\n" + Resepti.getVaativuusVaihtoehdot());
          
          // TODO: kuuntelijat suodattimien muuttumiselle
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
          hakutulokset.setSortable(-1, false);
          
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
          Resepti luotuResepti = ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Lisää resepti", null, null);
          
          // poistutaan jos luotu resepti on null
          if (luotuResepti == null) { return; }
          
          reseptit.lisaa(luotuResepti);
          haeReseptit();
      }
      
      
      private void muokkaaResepti() {
          // haetaan mikä resepti on valittuna
          int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
          if (valittuResepti < 0) { return; }
          Resepti muokattavaResepti = hakuReseptit.get(valittuResepti);
          
          // avataan muokkausnäkymä, josta palatessa saadaan mahdollisesti muokattu resepti
          Resepti muokattuResepti = ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaa reseptiä", null, muokattavaResepti);
          
          // poistaa reseptin resepteistä jos palautetaan null viite (resepti on poistettu muokkausnäkymässä)
          if (muokattuResepti == null) {
              reseptit.poista(muokattavaResepti);
              haeReseptit();
              return;
          }
          
          // muokkauksesta palatessa katsotaan tuliko muutoksia ja mahdollisesti päivitetään hakutulokset
          if (!muokattavaResepti.equals(muokattuResepti)) {
              this.reseptit.vaihdaResepti(muokattavaResepti, muokattuResepti);
              haeReseptit();
          }
      }
      
      
      private void avaaResepti() {
          int valittuResepti = hakutulokset.getSelectionModel().getSelectedIndex();
          
          // poistutaan jos indeksi ei ole mieluisa
          if (valittuResepti < 0 || this.hakuReseptit.size() < valittuResepti) { return; }
          Resepti avattavaResepti = hakuReseptit.get(valittuResepti);
          
          // avataan reseptinäkymä ja palatessa saadaan mahdollisesti muokattu resepti
          Resepti muokattuResepti = ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaGUIView.fxml"), "Reseptinäkymä", null, avattavaResepti);
          
          // poistaa reseptin resepteistä jos palautetaan null viite (resepti on poistettu avausnäkymässä)
          if (muokattuResepti == null) {
              reseptit.poista(avattavaResepti);
              haeReseptit();
              return;
          }
          
          // palatessa katsotaan tuliko muutoksia ja mahdollisesti päivitetään hakutulokset
          if (!avattavaResepti.equals(muokattuResepti)) {
              this.reseptit.vaihdaResepti(avattavaResepti, muokattuResepti);
              haeReseptit();
          }
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
          if (vastaus) { 
              reseptit.poista(hakuReseptit.get(valittuResepti));
              haeReseptit();
          }
      }
      
      
      private void tyhjennaSuodattimet() {
          for (ComboBoxChooser<String> suodatin : this.suodatinValinnat) { 
              suodatin.setSelectedIndex(-1); 
          }
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