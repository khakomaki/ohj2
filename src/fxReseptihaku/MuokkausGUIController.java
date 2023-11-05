package fxReseptihaku;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import reseptihaku.Ohje;
import reseptihaku.Ohjeet;
import reseptihaku.Osio;
import reseptihaku.OsionAinesosa;
import reseptihaku.OsionAinesosat;
import reseptihaku.Osiot;
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
    @FXML private void handlePoistaOsio() { poistaOsio(null); }
    @FXML private void handlePoistaAinesosa() { poistaAinesosa(null, 0); }
    @FXML private void handleLisaaAinesosa() { lisaaAinesosa(null); }
    @FXML private void handlePoistaOhje() { poistaOhje(null, 0); }
    @FXML private void handleLisaaOhje() { lisaaOhje(null);}
    
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
        naytaOsiot();
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
    
    
    private void naytaOsiot() {
        Osiot osiot = this.valittuResepti.getOsiot();
        for (int i = 0; i < osiot.getLkm(); i++) {
            // luo osion HBoxin ja lisää sen käyttöliittymään
            VBox osioVBox = naytaOsio(osiot.annaIndeksista(i));
            ainesosaJaOhjeetVBox.getChildren().add(osioVBox);
        }
    }
    
    
    private VBox naytaOsio(Osio osio) {
        // TODO ainesosat ja ohjeet
        
        // luodaan käytettävät fontit
        Font kirjasinB16 = new Font("System Bold", 16);
        Font kirjasin14 = new Font(14);
        
        VBox osioVBox = new VBox(); // koko osio
        
        HBox osioOstikkoHBox = new HBox();
        
        // luo osion nimen tekstikentän
        TextField osioNimiAinesosat = new TextField(osio.getNimi());
        osioNimiAinesosat.setFont(kirjasinB16);
        
        // luo osion poisto-painikkeen
        Button osioPoisto = new Button("x");
        osioPoisto.setOnAction(e -> poistaOsio(osioVBox)); // poistaa VBox-elementin
        osioPoisto.setFont(kirjasinB16);
        
        // lisätään teksti ja painike HBox-elementtiin
        osioOstikkoHBox.getChildren().add(osioNimiAinesosat);
        osioOstikkoHBox.getChildren().add(osioPoisto);
        
        HBox osionSisaltoHBox = new HBox();
        
        // ==================== näytetään ainesosat ====================
        
        VBox ainesosatVBox = new VBox();
        
        // luo ainesosien otsikon
        Label ainesosatLabel = new Label("Ainesosat");
        ainesosatLabel.setFont(kirjasin14);
        
        // luodaan GridPane ja otsikot
        GridPane ainesosatGridPane = new GridPane();
        ainesosatGridPane.add(new Label("määrä"), 0, 0);
        ainesosatGridPane.add(new Label("ainesosa"), 1, 0);
        
        OsionAinesosat osionAinesosat = osio.annaOsionAinesosat();
        for (int i = 0; i < osionAinesosat.getLkm(); i++) {
            int ainesosanRivi = i + 1;
            
            // luo ja lisää TextField-elementit GridPaneen
            OsionAinesosa oa = osionAinesosat.annaIndeksista(i);
            ainesosatGridPane.add(new TextField(oa.getMaara()), 0, ainesosanRivi);
            ainesosatGridPane.add(new TextField(osio.getAinesosanNimi(oa.getId())), 1, ainesosanRivi);
            
            Button ainesosaPoisto = new Button("x");
            ainesosaPoisto.setOnAction(e -> poistaAinesosa(ainesosatGridPane, ainesosanRivi));
            ainesosatGridPane.add(ainesosaPoisto, 2, ainesosanRivi);
        }
        
        ainesosatGridPane.add(new Label(), 0, ainesosatGridPane.getRowCount() + 1);
        lisaaAinesosa(ainesosatGridPane);
        
        // lisätään otsikko ja GridPane ainesosien VBox-elementtiin
        ainesosatVBox.getChildren().add(ainesosatLabel);
        ainesosatVBox.getChildren().add(ainesosatGridPane);
        
        // lisätään ainesosatVBox osion sisältöön
        osionSisaltoHBox.getChildren().add(ainesosatVBox);
        
        // ==================== näytetään ohjeet ====================
        
        VBox ohjeetVBox = new VBox();
        
        // luo ohjeiden otsikon
        Label ohjeetLabel = new Label("Ohjeet");
        ohjeetLabel.setFont(kirjasin14);
        
        
        // luodaan GridPane ja otsikot
        GridPane ohjeetGridPane = new GridPane();
        ohjeetGridPane.add(new Label("vaihe"), 0, 0);
        ohjeetGridPane.add(new Label("ohje"), 1, 0);
        
        Ohjeet osionOhjeet = osio.annaOsionOhjeet();
        for (int i = 0; i < osionOhjeet.getLkm(); i++) {
            int ohjeenRivi = i + 1;
            
            Ohje oo = osionOhjeet.anna(i);
            ohjeetGridPane.add(new Label("" + ohjeenRivi), 0, ohjeenRivi);
            ohjeetGridPane.add(new TextField(oo.getOhjeistus()), 1, ohjeenRivi);
            
            Button ohjePoisto = new Button("x");
            ohjePoisto.setOnAction(e -> poistaOhje(ohjeetGridPane, ohjeenRivi));
            ohjeetGridPane.add(ohjePoisto, 2, ohjeenRivi);
        }
        
        ohjeetGridPane.add(new Label(), 0, ohjeetGridPane.getRowCount() + 1);
        lisaaOhje(ohjeetGridPane);
        
        // lisätään otsikko ja GridPane ohjeiden VBox-elementtiin
        ohjeetVBox.getChildren().add(ohjeetLabel);
        ohjeetVBox.getChildren().add(ohjeetGridPane);
        
        // lisätään ohjeetVBox osion sisältöön
        osionSisaltoHBox.getChildren().add(ohjeetVBox);
        
        // lisätään HBox-elementit VBox elementtiin
        osioVBox.getChildren().add(osioOstikkoHBox);
        osioVBox.getChildren().add(osionSisaltoHBox);
        
        return osioVBox;
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
    
    
    private void poistaResepti() {
        boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (vastaus) { 
            Dialogs.showMessageDialog("Ei osata poistaa reseptiä vielä");
            suljeTallentamatta();
        }
    }
    
    
    private void lisaaOsio() {
        String osioTeksti = osioTekstiKentta.getText();
        if (osioTeksti.isEmpty()) { return; }
        
        // Luodaan uusi osio ja näytetään se käyttöliittymään
        Osio osio = new Osio(osioTeksti);
        VBox osioHBox = naytaOsio(osio);
        ainesosaJaOhjeetVBox.getChildren().add(osioHBox);
        
        // tyhjennetään tekstikenttä
        osioTekstiKentta.clear();
    }
    
    
    private void poistaOsio(VBox osioVBox) {
        if (osioVBox == null) { return; }
        if (osioVBox.getParent() instanceof VBox) {
            VBox ylempiVBox = (VBox)osioVBox.getParent();
            ylempiVBox.getChildren().remove(osioVBox);
        }
    }
    
    
    private void poistaAinesosa(GridPane paneeli, int rivi) {
        // ei yritetä poistaa null paneelista
        if (paneeli == null) { return; }
        
        // poistaa kaikki nodet kyseiseltä riviltä
        paneeli.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rivi);
    }
    
    
    private void lisaaAinesosa(GridPane paneeli) {
        if (paneeli == null) { return; }
        
        final int riviLkm = paneeli.getRowCount();
        
        // poistetaan kaikki viimeisteltä riviltä (lisäys painike)
        poistaAinesosa(paneeli, riviLkm - 1);
        
        // lisätään syöttökentät
        paneeli.add(new TextField(), 0, riviLkm);
        paneeli.add(new TextField(), 1, riviLkm);
        
        // lisätään rivin poisto painike
        Button ainesosaPoisto = new Button("x");
        ainesosaPoisto.setOnAction(e -> poistaAinesosa(paneeli, riviLkm));
        paneeli.add(ainesosaPoisto, 2, riviLkm);
        
        // lisätään rivien lisäys painike seuraavalle riville
        Button ainesosaLisays = new Button("+");
        ainesosaLisays.setOnAction(e -> lisaaAinesosa(paneeli));
        paneeli.add(ainesosaLisays, 2, riviLkm + 1);
        
    }
    
    
    private void poistaOhje(GridPane paneeli, int rivi) {
        // vältetään null viitteet
        if (paneeli == null) { return; }
        
        // poistaa kaikki nodet kyseiseltä riviltä
        paneeli.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rivi);
    }
    
    
    private void lisaaOhje(GridPane paneeli) {
        // vältetään null viitteet
        if (paneeli == null) { return; }
        
        final int riviLkm = paneeli.getRowCount();
        
        // poistetaan kaikki viimeisteltä riviltä (lisäys painike)
        poistaOhje(paneeli, riviLkm - 1);
        
        // lisätään teksti ja syöttökenttä
        paneeli.add(new Label(""), 0, riviLkm);
        paneeli.add(new TextField(), 1, riviLkm);
        
        // lisätään rivin poisto painike
        Button ohjePoisto = new Button("x");
        ohjePoisto.setOnAction(e -> poistaOhje(paneeli, riviLkm));
        paneeli.add(ohjePoisto, 2, riviLkm);
        
        // lisätään rivien lisäys painike seuraavalle riville
        Button ohjeLisaysButton = luoOhjeenLisaysPainike(paneeli);
        paneeli.add(ohjeLisaysButton, 2, paneeli.getRowCount() + 1);
    }
    
    
    private Button luoOhjeenLisaysPainike(GridPane paneeli) {
        Button ohjeLisays = new Button("+");
        ohjeLisays.setOnAction(e -> lisaaOhje(paneeli));
        return ohjeLisays;
    }
    
    
    private boolean tulikoMuutoksia() {
        // TODO vertaa tuliko reseptiin muutoksia
        return true;
    }

}
