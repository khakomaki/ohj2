package fxReseptihaku;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.CheckBox;
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
    private List<VaihtoehtoAttribuutti> minimiValinnat = new ArrayList<VaihtoehtoAttribuutti>();
    private List<VaihtoehtoAttribuutti> maksimiValinnat = new ArrayList<VaihtoehtoAttribuutti>();
    private List<ComboBox<String>> suodattimet = new ArrayList<ComboBox<String>>();
    private List<Resepti> hakuReseptit = new ArrayList<Resepti>();
    private String lajitteluPeruste = null;
    private boolean kaanteinenJarjestys = false;
    
    
    /**
     * Tekee näkymän alustukset
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alusta();
        this.hakutulokset.setColumnWidth(-1, 120);
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
        
        // lajittelu teksti
        Label lajitteluLabel = new Label("Lajittele tulokset");
        
        // lajittelu vaihtoehdot
        ComboBox<String> lajitteluComboBox = new ComboBox<String>();
        lajitteluComboBox.getItems().add("Nimi");
        lajitteluComboBox.getSelectionModel().select(0); // valitaan nimi lajittelu perusteeksi
        lajitteluComboBox.setOnAction(e -> {
            this.lajitteluPeruste = lajitteluComboBox.getValue();
            haeReseptit();
        });
        
        // lajittelun käänteinen järjestys
        CheckBox kaanteinenJarjestysCheckBox = new CheckBox("käänteinen järjestys");
        kaanteinenJarjestysCheckBox.setOnAction(e -> {
            this.kaanteinenJarjestys = kaanteinenJarjestysCheckBox.isSelected();
            haeReseptit();
        });
        
        // lajittelu GridPaneen
        this.suodattimetGridPane.add(lajitteluLabel, 0, rivi++);
        this.suodattimetGridPane.add(lajitteluComboBox, 0, rivi++);
        this.suodattimetGridPane.add(kaanteinenJarjestysCheckBox, 0, rivi++);
        
        // kysytään oletusattribuutit
        List<VaihtoehtoAttribuutti> oletusAttribuutit = null;
		try {
			oletusAttribuutit = Reseptit.getOletusAttribuutit();
		} catch (SailoException exception) {
			Dialogs.showMessageDialog(exception.getMessage());
		}
		
		if (oletusAttribuutit == null) return;
		
		// lisätään reseptejen sisältämät oletus attribuutit
        for (VaihtoehtoAttribuutti suodatettava : oletusAttribuutit) {
            int sarake = 0;
            Label suodatinNimiLabel = new Label(suodatettava.getNimi());
            this.suodattimetGridPane.add(suodatinNimiLabel, sarake, rivi++);
            
            // lajitteluvaihtoehdoksi
            lajitteluComboBox.getItems().add(suodatettava.getNimi());
                        
            // minimi
            VaihtoehtoAttribuutti minimiVA = new VaihtoehtoAttribuutti(suodatettava.getNimi(), suodatettava.getVaihtoehdot(), suodatettava.getOletus(), suodatettava.getOletusString());
            ComboBox<String> minimiComboBox = new ComboBox<String>();
            asetaVaihtoehdot(minimiComboBox, suodatettava);
            
            // välimerkki
            Label valimerkkiLabel = new Label("-");
            
            // maksimi
            VaihtoehtoAttribuutti maksimiVA = new VaihtoehtoAttribuutti(suodatettava.getNimi(), suodatettava.getVaihtoehdot(), suodatettava.getOletus(), suodatettava.getOletusString());
            ComboBox<String> maksimiComboBox = new ComboBox<String>();
            asetaVaihtoehdot(maksimiComboBox, suodatettava);
            
            // TODO vaihtoehdot reagoimaan valintoihin
            minimiComboBox.setOnAction(e -> {
                minimiVA.setValinta(minimiVA.getValinta(minimiComboBox.getValue()));
                haeReseptit();
            });
            maksimiComboBox.setOnAction(e -> {
                maksimiVA.setValinta(maksimiVA.getValinta(maksimiComboBox.getValue()));
                haeReseptit();
            });
            
            // suodattimet GridPaneen
            this.suodattimetGridPane.add(minimiComboBox, sarake++, rivi);
            this.suodattimetGridPane.add(valimerkkiLabel, sarake++, rivi);
            this.suodattimetGridPane.add(maksimiComboBox, sarake, rivi++);
            
            // suodattimien ulkonäkö
            GridPane.setHalignment(minimiComboBox, HPos.RIGHT);
            
            // lisätään tiedot listaan
            this.minimiValinnat.add(minimiVA);
            this.maksimiValinnat.add(maksimiVA);

            this.suodattimet.add(minimiComboBox);
            this.suodattimet.add(maksimiComboBox);
        }
        
        // asetetaan tyhjään valintaan
        tyhjennaSuodattimet();
    }
    
    
    /**
     * Asettaa annetulle combobox-nodelle attribuutin mukaiset vaihtoehdot
     * 
     * @param combobox mihin vaihtoehdot laitetaan
     * @param va vaihtoehtoattribuutti jonka vaihtoehdot laitetaan
     */
    private void asetaVaihtoehdot(ComboBox<String> combobox, VaihtoehtoAttribuutti va) {
        for (Entry<Integer, String> entry : va.getVaihtoehdot().entrySet()) {
            String arvo = entry.getValue();
            combobox.getItems().add(arvo);
        }
    }
    
    
    /**
     * Suorittaa haun resepteille
     */
    public void haeReseptit() {
        String hakusana = this.hakukentta.getText();
        this.hakutulokset.clear();
        
        StringBuilder sb = new StringBuilder(hakutuloksetOtsikko);
        
        if (this.reseptit != null) {
        	// haetaan hakusanaa vastanneet reseptit
        	try {
                this.hakuReseptit = this.reseptit.etsiNimella(hakusana, this.minimiValinnat, this.maksimiValinnat, this.lajitteluPeruste, this.kaanteinenJarjestys);
        	} catch (SailoException exception) {
        		Dialogs.showMessageDialog(exception.getMessage());
        	}

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
        this.hakutulokset.setSortable(-1, false);
        
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
        
        this.reseptit.lisaa(luotuResepti);
        haeReseptit();
    }
    
    
    /**
     * Muokkaa valittua reseptiä, avaa muokkausnäkymän valitulla reseptillä
     */
    private void muokkaaResepti() {
        // haetaan mikä resepti on valittuna
        int valittuResepti = this.hakutulokset.getSelectionModel().getSelectedIndex();
        if (valittuResepti < 0) return;
        Resepti muokattavaResepti = this.hakuReseptit.get(valittuResepti);
        
        // avataan muokkausnäkymä, josta palatessa saadaan mahdollisesti muokattu resepti
        Resepti muokattuResepti = ModalController.showModal(ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaa reseptiä", null, muokattavaResepti);
        
        // poistaa reseptin resepteistä jos palautetaan null viite (resepti on poistettu muokkausnäkymässä)
        if (muokattuResepti == null) {
            this.reseptit.poista(muokattavaResepti);
            try {
                this.reseptit.tallenna();
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
        Resepti avattavaResepti = this.hakuReseptit.get(valittuResepti);
        
        // avataan reseptinäkymä ja palatessa saadaan mahdollisesti muokattu resepti
        Resepti muokattuResepti = ReseptinakymaGUIController.avaaResepti(avattavaResepti);
        
        // poistaa reseptin resepteistä jos palautetaan null viite (resepti on poistettu avausnäkymässä)
        if (muokattuResepti == null) {
            this.reseptit.poista(avattavaResepti);
            try {
                this.reseptit.tallenna();
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
            this.reseptit.poista(this.hakuReseptit.get(valittuResepti));
            try {
                this.reseptit.tallenna();
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
        for (ComboBox<String> suodatin : this.suodattimet) {
            suodatin.setValue(suodatin.getItems().get(0));
        }
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
            //this.reseptit.setTiedostoPolku("testidata/");
            this.reseptit.lueTiedostosta();
            //this.reseptit.setTiedostoPolku("reseptidata/");
            //this.reseptit.tallenna();
            
            
        } catch (SailoException exception) {
            Dialogs.showMessageDialog("Tiedoston lukemisessa ongelmia: " + exception.getMessage());
        }
    }
}