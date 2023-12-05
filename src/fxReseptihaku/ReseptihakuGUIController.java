package fxReseptihaku;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fxKanta.RajausSuodatin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kanta.SailoException;
import kanta.Satunnaisluku;
import kanta.VaihtoehtoAttribuutti;
import reseptihaku.Resepti;
import reseptihaku.Reseptit;

/**
 * @author hakom
 * @version 13.9.2023
 * 
 * Reseptihakunäkymän controller
 */
public class ReseptihakuGUIController implements Initializable {
    
    @FXML private TextField hakukentta;
    @FXML private Label tuloksetTeksti;
    @FXML private StringGrid<String> hakutulokset;
    @FXML private GridPane suodattimetGridPane;
    
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
    
    private final String hakutuloksetOtsikko = "reseptin nimi |hintaluokka |valmistusaika |tähdet |vaativuustaso\n";
    private Reseptit reseptit;
    private List<ComboBox<VaihtoehtoAttribuutti>> suodatinValinnat = new ArrayList<>();
    private List<Resepti> hakuReseptit = new ArrayList<Resepti>();
    
    
    /**
     * Tekee näkymän alustukset
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alusta();
        hakutulokset.setColumnWidth(-1, 120);
    }
    
    
    /**
     * Sulkee ohjelman
     */
    private void sulje() {
        Platform.exit();
    }
    
    
    /**
     * Hoitaa alustukset, joita FXMLLoader ei hoida
     */
    public void alusta() {
        alustaSuodattimet();
        this.hakukentta.clear();
        alustaHakutulokset();
    }
    
    
    /**
     * Alustaa hakutulokset tyhjentämällä FXMLLoaderin asettamat
     */
    private void alustaHakutulokset() {
        this.hakutulokset.clear();
    }
    
    
    /**
     * Luo suodattimet paneelin ja alustaa ne tyhjillä valinnoilla
     */
    private void alustaSuodattimet() {
        this.suodattimetGridPane.getChildren().clear();
        int rivi = 0;
        
        // lisätään reseptejen sisältämät oletus attribuutit
        for (VaihtoehtoAttribuutti suodatettava : Reseptit.getOletusAttribuutit()) {
            int sarake = 0;
            Label suodatinNimiLabel = new Label(suodatettava.getNimi());
            this.suodattimetGridPane.add(suodatinNimiLabel, sarake, rivi++);
            
            // luodaan suodatin ja sen nodet
            RajausSuodatin suodatin = new RajausSuodatin(suodatettava);
            ComboBox<VaihtoehtoAttribuutti> minimiComboBox = new ComboBox<VaihtoehtoAttribuutti>(suodatin.getMinimiVaihtoehdotList());
            Label valimerkkiLabel = new Label("-");
            ComboBox<VaihtoehtoAttribuutti> maksimiComboBox = new ComboBox<VaihtoehtoAttribuutti>(suodatin.getMaksimiVaihtoehdotList());
            
            // TODO vaihtoehdot reagoimaan valintoihin
            minimiComboBox.setOnAction(e -> {
                haeReseptit();
            });
            maksimiComboBox.setOnAction(e -> {
                haeReseptit();
            });
            
            // suodattimet GridPaneen
            this.suodattimetGridPane.add(minimiComboBox, sarake++, rivi);
            this.suodattimetGridPane.add(valimerkkiLabel, sarake++, rivi);
            this.suodattimetGridPane.add(maksimiComboBox, sarake, rivi++);
            
            // suodattimien ulkonäkö
            GridPane.setHalignment(minimiComboBox, HPos.RIGHT);
            
            // lisätään luokan listaan
            this.suodatinValinnat.add(minimiComboBox);
            this.suodatinValinnat.add(maksimiComboBox);
        }
        tyhjennaSuodattimet();
    }
    
    
    /**
     * Suorittaa haun resepteille
     */
    public void haeReseptit() {
        String hakusana = hakukentta.getText();
        this.hakutulokset.clear();
        
        StringBuilder sb = new StringBuilder(hakutuloksetOtsikko);
        
        if (this.reseptit != null) {
            // suodattimet
            List<VaihtoehtoAttribuutti> suodattimet = getSuodatinValinnat();
            List<VaihtoehtoAttribuutti> minimiSuodattimet = new ArrayList<VaihtoehtoAttribuutti>();
            List<VaihtoehtoAttribuutti> maksimiSuodattimet = new ArrayList<VaihtoehtoAttribuutti>();
            for (int i = 0; i < suodattimet.size(); i++) {
                if (i % 2 == 0) {
                    minimiSuodattimet.add(suodattimet.get(i));
                } else {
                    maksimiSuodattimet.add(suodattimet.get(i));
                }
            }
            // haetaan hakusanaa vastanneet reseptit
            this.hakuReseptit = this.reseptit.etsiNimella(hakusana, minimiSuodattimet, maksimiSuodattimet);
            
            // lisää reseptin taulukkomuotoisen tekstin StringBuilderiin
            for (int i = 0; i < this.hakuReseptit.size(); i++) {
                sb.append(this.hakuReseptit.get(i).getTaulukkoMuodossa());
                sb.append("\n");
            }
        }
        // asettaa tulokset StringGridiin ja sen oikein 
        this.hakutulokset.setRivit(sb.toString());
        
        // TODO StringGridin sijaan taulukko itse, jolloin ei tarvitse asettaa näitä uudestaan joka päivityksen jälkeen
        this.hakutulokset.setColumnWidth(-1, 120);
        hakutulokset.setSortable(-1, false);
        
        asetaTuloksetTeksti(hakusana);
    }
    
    
    /**
     * Asettaa "hakutulokset sanalle ()"-tekstin
     * 
     * @param hakusana mikä hakusana
     */
    private void asetaTuloksetTeksti(String hakusana) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tulokset hakusanalle \"");
        sb.append(hakusana);
        sb.append("\"");
        this.tuloksetTeksti.setText(sb.toString());
    }
    
    
    /**
     * Lisää uuden reseptin, avaa muokkausnäkymän null-reseptillä
     */
    private void lisaaResepti() {
        Resepti luotuResepti = ModalController.showModal(ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Lisää resepti", null, null);
        
        // poistutaan jos luotu resepti on null
        if (luotuResepti == null) return;
        
        reseptit.lisaa(luotuResepti);
        haeReseptit();
    }
    
    
    /**
     * Muokkaa valittua reseptiä, avaa muokkausnäkymän valitulla reseptillä
     */
    private void muokkaaResepti() {
        // haetaan mikä resepti on valittuna
        int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
        if (valittuResepti < 0) return;
        Resepti muokattavaResepti = hakuReseptit.get(valittuResepti);
        
        // avataan muokkausnäkymä, josta palatessa saadaan mahdollisesti muokattu resepti
        Resepti muokattuResepti = ModalController.showModal(ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaa reseptiä", null, muokattavaResepti);
        
        // poistaa reseptin resepteistä jos palautetaan null viite (resepti on poistettu muokkausnäkymässä)
        if (muokattuResepti == null) {
            reseptit.poista(muokattavaResepti);
            try {
                reseptit.tallenna();
            } catch (SailoException exception) {
                Dialogs.showMessageDialog("Poistossa ongelmia: " + exception.getMessage());
            }
            haeReseptit();
            return;
        }
        
        // muokkauksesta palatessa katsotaan tuliko muutoksia ja mahdollisesti päivitetään hakutulokset
        if (!muokattavaResepti.equals(muokattuResepti)) {
            this.reseptit.vaihdaResepti(muokattavaResepti, muokattuResepti);
            haeReseptit();
        }
    }
    
    
    /**
     * Avaa valitun reseptin, avaa reseptinäkymän valitulla reseptillä
     */
    private void avaaResepti() {
        int valittuResepti = hakutulokset.getSelectionModel().getSelectedIndex();
          
        // poistutaan jos indeksi ei ole mieluisa
        if (valittuResepti < 0 || this.hakuReseptit.size() < valittuResepti) return;
        Resepti avattavaResepti = hakuReseptit.get(valittuResepti);
        
        // avataan reseptinäkymä ja palatessa saadaan mahdollisesti muokattu resepti
        Resepti muokattuResepti = ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaGUIView.fxml"), "Reseptinäkymä", null, avattavaResepti);
        
        // poistaa reseptin resepteistä jos palautetaan null viite (resepti on poistettu avausnäkymässä)
        if (muokattuResepti == null) {
            reseptit.poista(avattavaResepti);
            try {
                reseptit.tallenna();
            } catch (SailoException exception) {
                Dialogs.showMessageDialog("Poistossa ongelmia: " + exception.getMessage());
            }
            haeReseptit();
            return;
        }
        
        // palatessa katsotaan tuliko muutoksia ja mahdollisesti päivitetään hakutulokset
        if (!avattavaResepti.equals(muokattuResepti)) {
            this.reseptit.vaihdaResepti(avattavaResepti, muokattuResepti);
            haeReseptit();
        }
    }
    
    
    /**
     * Avaa satunnaisen reseptin hakutuloksen resepteistä
     */
    private void avaaSatunnainenResepti() {
        // poistutaan jos ei ole reseptejä
        if (this.hakuReseptit.size() < 1) return;
        
        // arpoo hakutuloksista satunnaisen reseptin ja avaa sen
        Resepti satunnainenResepti = this.hakuReseptit.get(Satunnaisluku.rand(0, this.hakuReseptit.size() - 1));
        ModalController.showModal( ReseptihakuGUIController.class.getResource("ReseptinakymaGUIView.fxml"), "Reseptinäkymä", null, satunnainenResepti);
    }
    
    
    /**
     * Poistaa valitun reseptin, näyttää tapahtuman varmistavan dialogin ennen poistoa
     */
    private void poistaResepti() {
        // haetaan mikä resepti on valittuna
        int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
        if (valittuResepti < 0) return;
        
        // näytetään dialogi reseptin poistamisesta
        boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (vastaus) { 
            reseptit.poista(hakuReseptit.get(valittuResepti));
            try {
                reseptit.tallenna();
            } catch (SailoException exception) {
                Dialogs.showMessageDialog("Poistossa ongelmia: " + exception.getMessage());
            }
            haeReseptit();
        }
    }
    
    
    /**
     * Tyhjentää suodattimien valinnat
     */
    private void tyhjennaSuodattimet() {
        for (ComboBox<VaihtoehtoAttribuutti> suodatin : this.suodatinValinnat) {
            suodatin.setValue(suodatin.getItems().get(0));
        }
    }
    
    
    /**
     * Antaa suodatinvalinnat listana
     * 
     * @return suodatin valinnat
     */
    private List<VaihtoehtoAttribuutti> getSuodatinValinnat() {
        List<VaihtoehtoAttribuutti> suodattimet = new ArrayList<VaihtoehtoAttribuutti>();
        for (int i = 0; i < this.suodatinValinnat.size(); i++) {
            VaihtoehtoAttribuutti suodatin = this.suodatinValinnat.get(i).getValue();
            if (suodatin == null) break;
            suodattimet.add(suodatin);
        }
        return suodattimet;
    }
    
    
    /**
     * Avaa tulostusnäkymän valitulle reseptille
     * TODO tulostus toimimaan
     */
    private void tulosta() {
        // haetaan mikä resepti on valittuna
        int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
        if (valittuResepti < 0) return;
        
        Dialogs.showMessageDialog("Ei osata tulostaa vielä");
    }
    
    
    /**
     * Asettaa käytettävät reseptit
     * 
     * @param reseptit käytettävät Reseptit
     */
    public void setReseptit(Reseptit reseptit) {
        this.reseptit = reseptit;
    }
    
    
    /**
     * Lukee käsiteltävät reseptit tiedostosta
     */
    public void lueTiedostosta() {
        try {
            // this.reseptit.lueTiedostosta();
            // TODO testidata pois kuin ei enää tarvita
            File reseptidata = new File("reseptidata/");
            if (!reseptidata.exists()) {
                this.reseptit.setTiedostoPolku("testidata/");
                this.reseptit.lueTiedostosta();
                this.reseptit.setTiedostoPolku("reseptidata/");
                this.reseptit.tallenna();
            } else {
                this.reseptit.setTiedostoPolku("reseptidata/");
                this.reseptit.lueTiedostosta();
            }
        } catch (SailoException exception) {
            Dialogs.showMessageDialog("Tiedoston lukemisessa ongelmia: " + exception.getMessage());
        }
    }
}