package fxReseptihaku;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import reseptihaku.Ainesosa;
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
    @FXML private void handleTallenna() { tallenna(); }
    @FXML private void handleLisaaOsio() { lisaaOsio(); }
    @FXML private void handlePoistaResepti() { poistaResepti(); }
    @FXML private void handlePoistaOsio() { poistaOsio(null); }
    @FXML private void handlePoistaAinesosa() { poistaAinesosa(null, 0); }
    @FXML private void handleLisaaAinesosa() { lisaaAinesosa(null, null, null, ""); }
    @FXML private void handlePoistaOhje() { poistaOhje(null, 0); }
    @FXML private void handleLisaaOhje() { lisaaOhje(null); }
    
    // ====================================================================================================
    
    private final Insets PEHMUSTE_ISO = new Insets(10, 10, 10, 10);
    private final Insets PEHMUSTE_PIENI = new Insets(5, 5, 5, 5);
    private final int VALI = 10;
    
    // luodaan käytettävät fontit
    Font kirjasinB16 = new Font("System Bold", 16);
    Font kirjasin14 = new Font(14);
    
    private Resepti valittuResepti;
    private Resepti alkuperainenResepti;
    
    
    @Override
    public Resepti getResult() { 
        return this.alkuperainenResepti;
    }

    @Override
    public void handleShown() {
        //
    }

    @Override
    public void setDefault(Resepti oletus) {
        this.alkuperainenResepti = oletus;
        // luodaan Reseptipohja jos annettu Resepti on null, muuten luodaan kopio annetusta
        if (oletus == null) { this.valittuResepti = new Resepti(); }
        else { this.valittuResepti = oletus.clone(); }
        
        naytaReseptinOminaisuudet();
        
        // tyhjentää VBox sisällön
        this.ainesosaJaOhjeetVBox.getChildren().clear();
        naytaOsiot();
    }
    
    
    private void naytaReseptinOminaisuudet() {
        // TODO vähennä samanlaisen koodin toistoa
        this.reseptinNimiTextField.setText(this.valittuResepti.getNimi());
        reseptinNimiTextField.setOnKeyTyped(e -> { valittuResepti.setUusiNimi(reseptinNimiTextField.getText()); });
        
        this.kuvausTextArea.setText(this.valittuResepti.getKuvaus());
        kuvausTextArea.setOnKeyTyped(e -> { valittuResepti.setKuvaus(kuvausTextArea.getText()); });
        
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
        VBox osioVBox = new VBox(); // koko osio
        
        HBox osioOstikkoHBox = new HBox();
        osioOstikkoHBox.setSpacing(VALI);
        osioOstikkoHBox.setPadding(PEHMUSTE_ISO);
        
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
        
        ColumnConstraints ainesosaConstraints = luoOtsikkoRajoitteet();
        ColumnConstraints painikeConstraints = luoPainikeRajoitteet();
        ColumnConstraints vaiheConstraints = luoVaiheRajoitteet();
        ColumnConstraints ohjeConstraints = luoOhjeRajoitteet();
        
        // ==================== näytetään ainesosat ====================
        
        VBox ainesosatVBox = new VBox();
        ainesosatVBox.setPadding(PEHMUSTE_ISO);
        
        // luo ainesosien otsikon
        Label ainesosatLabel = new Label("Ainesosat");
        ainesosatLabel.setFont(kirjasin14);
        
        // luodaan GridPane ja otsikot
        GridPane ainesosatGridPane = new GridPane();
        Label maaraLabel = new Label("määrä"); maaraLabel.setPadding(PEHMUSTE_PIENI);
        Label ainesosaLabel = new Label("ainesosa"); ainesosaLabel.setPadding(PEHMUSTE_PIENI);
        ainesosatGridPane.add(maaraLabel, 0, 0);
        ainesosatGridPane.add(ainesosaLabel, 1, 0);
        
        // määritellään rajoitteet GridPanen sarakkeille
        ainesosatGridPane.getColumnConstraints().add(ainesosaConstraints);
        ainesosatGridPane.getColumnConstraints().add(ainesosaConstraints);
        ainesosatGridPane.getColumnConstraints().add(painikeConstraints);
        
        // lisätään osion ainesosat yksi kerralla käyttöliittymään
        OsionAinesosat osionAinesosat = osio.annaOsionAinesosat();
        for (int i = 0; i < osionAinesosat.getLkm(); i++) {
            int ainesosanRivi = i + 1;
            
            // luo ja lisää TextField-elementit GridPaneen
            OsionAinesosa oa = osionAinesosat.anna(i);
            TextField maaraTextField = new TextField(oa.getMaara());
            maaraTextField.setAlignment(Pos.CENTER);
            TextField ainesosaTextField = new TextField(osio.getAinesosanNimi(oa.getId()));
            ainesosaTextField.setAlignment(Pos.CENTER);
            
            ainesosatGridPane.add(maaraTextField, 0, ainesosanRivi);
            ainesosatGridPane.add(ainesosaTextField, 1, ainesosanRivi);
            
            Button ainesosaPoisto = new Button("x");
            ainesosaPoisto.setOnAction(e -> poistaAinesosa(ainesosatGridPane, ainesosanRivi));
            ainesosatGridPane.add(ainesosaPoisto, 2, ainesosanRivi);
        }
        
        // lisätään tyhjä ainesosa
        ainesosatGridPane.add(new Label(), 0, ainesosatGridPane.getRowCount() + 1);
        lisaaAinesosa(ainesosatGridPane, osio, new Ainesosa(""), "");
        
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
        ohjeetLabel.setPadding(PEHMUSTE_ISO);
        
        // luodaan GridPane ja otsikot
        GridPane ohjeetGridPane = new GridPane();
        Label vaiheLabel = new Label("vaihe"); vaiheLabel.setPadding(PEHMUSTE_PIENI);
        Label ohjeLabel = new Label("ohje"); ohjeLabel.setPadding(PEHMUSTE_PIENI);
        ohjeetGridPane.add(vaiheLabel, 0, 0);
        ohjeetGridPane.add(ohjeLabel, 1, 0);
        
        // määritellään rajoitteet GridPanen sarakkeille
        ohjeetGridPane.getColumnConstraints().add(vaiheConstraints);
        ohjeetGridPane.getColumnConstraints().add(ohjeConstraints);
        ohjeetGridPane.getColumnConstraints().add(painikeConstraints);
        
        // lisätään osion ohjeet yksi kerralla käyttöliittymään
        Ohjeet osionOhjeet = osio.annaOsionOhjeet();
        for (int i = 0; i < osionOhjeet.getLkm(); i++) {
            int ohjeenRivi = i + 1;
            
            Ohje oo = osionOhjeet.anna(i);
            
            Label riviNumeroLabel = new Label("" + ohjeenRivi);
            TextArea ohjeTextArea = luoOhjeTextArea(oo.getOhjeistus());
            
            ohjeetGridPane.add(riviNumeroLabel, 0, ohjeenRivi);
            ohjeetGridPane.add(ohjeTextArea, 1, ohjeenRivi);
            
            Button ohjePoisto = new Button("x");
            ohjePoisto.setOnAction(e -> poistaOhje(ohjeetGridPane, ohjeenRivi));
            ohjeetGridPane.add(ohjePoisto, 2, ohjeenRivi);
        }
        
        // lisätään tyhjä ohje
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
    
    
    private ColumnConstraints luoOtsikkoRajoitteet() {
        ColumnConstraints otsikkoConstraints = new ColumnConstraints();
        otsikkoConstraints.setHalignment(HPos.CENTER);
        otsikkoConstraints.setHgrow(Priority.SOMETIMES);
        otsikkoConstraints.setMinWidth(140);
        return otsikkoConstraints;
    }
    
    
    private ColumnConstraints luoPainikeRajoitteet() {
        ColumnConstraints otsikkoConstraints = new ColumnConstraints();
        otsikkoConstraints.setHalignment(HPos.CENTER);
        otsikkoConstraints.setHgrow(Priority.NEVER);
        otsikkoConstraints.setMinWidth(30);
        otsikkoConstraints.setPrefWidth(30);
        return otsikkoConstraints;
    }
    
    
    private ColumnConstraints luoOhjeRajoitteet() {
        ColumnConstraints otsikkoConstraints = new ColumnConstraints();
        otsikkoConstraints.setHalignment(HPos.LEFT);
        otsikkoConstraints.setMinWidth(250);
        otsikkoConstraints.setHgrow(Priority.ALWAYS);
        return otsikkoConstraints;
    }
    
    
    private ColumnConstraints luoVaiheRajoitteet() {
        ColumnConstraints otsikkoConstraints = new ColumnConstraints();
        otsikkoConstraints.setHalignment(HPos.CENTER);
        otsikkoConstraints.setHgrow(Priority.NEVER);
        otsikkoConstraints.setMinWidth(50);
        return otsikkoConstraints;
    }
    
    
    private TextArea luoOhjeTextArea(String ohjeteksti) {
        TextArea textArea = new TextArea(ohjeteksti);
        textArea.minWidth(250);
        textArea.setPrefColumnCount(0);
        textArea.setPrefRowCount(2);
        textArea.setWrapText(true);
        return textArea;
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
        
        this.alkuperainenResepti = this.valittuResepti;
        // TODO: tallenna tiedostoon
    }
    
    
    private void poistaResepti() {
        boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (vastaus) { 
            this.alkuperainenResepti = null;
            suljeTallentamatta();
        }
    }
    
    
    private void lisaaOsio() {
        String osioTeksti = osioTekstiKentta.getText();
        if (osioTeksti.isEmpty()) { osioTeksti = ""; }
        
        // Luodaan uusi osio ja näytetään se käyttöliittymään
        Osio osio = new Osio(osioTeksti);
        VBox osioHBox = naytaOsio(osio);
        ainesosaJaOhjeetVBox.getChildren().add(osioHBox);
        
        // lisätään osio reseptiin
        this.valittuResepti.lisaaOsio(osio);
        
        // tyhjennetään tekstikenttä
        osioTekstiKentta.clear();
    }
    
    
    private void poistaOsio(VBox osioVBox) {
        if (osioVBox == null) { return; }
        if (osioVBox.getParent() instanceof VBox) {
            VBox ylempiVBox = (VBox)osioVBox.getParent();
            ylempiVBox.getChildren().remove(osioVBox);
            
            // TODO: this.valittuResepti.poistaOsio(osio)
        }
    }
    
    
    private void poistaAinesosa(GridPane paneeli, int rivi) {
        // ei yritetä poistaa null paneelista
        if (paneeli == null) { return; }
        
        // poistaa kaikki nodet kyseiseltä riviltä
        paneeli.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rivi);
        
        // TODO: osio.poistaAinesosa(ainesosa)
    }
    
    
    private void lisaaAinesosa(GridPane paneeli, Osio osio, Ainesosa ainesosa, String maara) {
        if (paneeli == null || osio == null || ainesosa == null) { return; }
        
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
        
        // luo samaan paneeliin, samaan osioon tyhjän ainesosan ja määrän
        ainesosaLisays.setOnAction(e -> lisaaAinesosa(paneeli, osio, new Ainesosa(""), "")); 
        paneeli.add(ainesosaLisays, 2, riviLkm + 1);
        
        // lisätään ainesosa osioon
        osio.lisaaAinesosa(ainesosa, maara);
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
        paneeli.add(luoOhjeTextArea(""), 1, riviLkm);
        
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
        // valittu resepti ei voi olla null, joten voidaan tehdä vertailu
        return !this.valittuResepti.equals(this.alkuperainenResepti);
    }

}
