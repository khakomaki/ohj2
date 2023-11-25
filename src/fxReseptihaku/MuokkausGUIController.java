package fxReseptihaku;

import java.util.ArrayList;
import java.util.List;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fxKanta.DynaaminenGridPane;
import fxKanta.NodeKasittely;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    @FXML private void handleTallenna() { tallenna(); }
    @FXML private void handleLisaaOsio() { lisaaOsio(); }
    @FXML private void handlePoistaResepti() { poistaResepti(); }
    
    // ====================================================================================================
    
    private final Insets PEHMUSTE_ISO = new Insets(10, 10, 10, 10);
    private final Insets PEHMUSTE_PIENI = new Insets(5, 5, 5, 5);
    private final int VALI = 10;
    
    // luodaan käytettävät fontit
    private Font kirjasinB16 = new Font("System Bold", 16);
    private Font kirjasin14 = new Font(14);
    
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
        osioPoisto.setOnAction(e -> poistaOsio(osioVBox, osio)); // poistaa VBox-elementin
        osioPoisto.setFont(kirjasinB16);
        
        // lisätään teksti ja painike HBox-elementtiin
        osioOstikkoHBox.getChildren().add(osioNimiAinesosat);
        osioOstikkoHBox.getChildren().add(osioPoisto);
        
        HBox osionSisaltoHBox = new HBox();
        
        ColumnConstraints ainesosaConstraints = NodeKasittely.luoSarakeRajoitteet(140, Priority.SOMETIMES, HPos.CENTER);
        ColumnConstraints painikeConstraints = NodeKasittely.luoSarakeRajoitteet(30, Priority.NEVER, HPos.CENTER);
        ColumnConstraints vaiheConstraints = NodeKasittely.luoSarakeRajoitteet(50, Priority.NEVER, HPos.CENTER);
        ColumnConstraints ohjeConstraints = NodeKasittely.luoSarakeRajoitteet(250, Priority.ALWAYS, HPos.LEFT);
        
        // ==================== näytetään ainesosat ====================
        
        VBox ainesosatVBox = new VBox();
        ainesosatVBox.setPadding(PEHMUSTE_ISO);
        OsionAinesosat osionAinesosat = osio.annaOsionAinesosat();
        
        // luo ainesosien otsikon
        Label ainesosatLabel = new Label("Ainesosat");
        ainesosatLabel.setFont(kirjasin14);
        
        // luodaan GridPane ja otsikot
        DynaaminenGridPane<OsionAinesosa> ainesosatGridPane = new DynaaminenGridPane<OsionAinesosa>(osionAinesosat, ainesosa -> luoAinesosaNodet(ainesosa), true);
        ainesosatGridPane.getColumnConstraints().add(ainesosaConstraints);
        ainesosatGridPane.getColumnConstraints().add(ainesosaConstraints);
        ainesosatGridPane.getColumnConstraints().add(painikeConstraints);
        Label maara = new Label("määrä"); maara.setPadding(PEHMUSTE_PIENI);
        Label ainesosa = new Label("ainesosa"); ainesosa.setPadding(PEHMUSTE_PIENI);
        List<Label> ainesosaOtsikot = List.of(maara, ainesosa);
        ainesosatGridPane.lisaaOtsikot(ainesosaOtsikot);
        ainesosatGridPane.paivita();
        
        // lisätään otsikko ja GridPane ainesosien VBox-elementtiin
        ainesosatVBox.getChildren().add(ainesosatLabel);
        ainesosatVBox.getChildren().add(ainesosatGridPane);
        
        // lisätään ainesosatVBox osion sisältöön
        osionSisaltoHBox.getChildren().add(ainesosatVBox);
        
        // ==================== näytetään ohjeet ====================
        
        VBox ohjeetVBox = new VBox();
        ohjeetVBox.setPadding(PEHMUSTE_ISO);
        Ohjeet osionOhjeet = osio.annaOsionOhjeet();
        
        // luo ohjeiden otsikon
        Label ohjeetLabel = new Label("Ohjeet");
        ohjeetLabel.setFont(kirjasin14);
        ohjeetLabel.setPadding(PEHMUSTE_ISO);
        
        // luodaan GridPane ja otsikot
        DynaaminenGridPane<Ohje> ohjeetGridPane = new DynaaminenGridPane<Ohje>(osionOhjeet, ohje -> luoOhjeNodet(ohje), true);
        ohjeetGridPane.getColumnConstraints().add(vaiheConstraints);
        ohjeetGridPane.getColumnConstraints().add(ohjeConstraints);
        ohjeetGridPane.getColumnConstraints().add(painikeConstraints);
        Label vaihe = new Label("vaihe"); vaihe.setPadding(PEHMUSTE_PIENI);
        Label ohjeistus = new Label("ohjeistus"); ohjeistus.setPadding(PEHMUSTE_PIENI);
        List<Label> ohjeOtsikot = List.of(vaihe, ohjeistus);
        ohjeetGridPane.lisaaOtsikot(ohjeOtsikot);
        ohjeetGridPane.paivita();
        
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
    
    
    private List<Node> luoOhjeNodet(Ohje ohje) {
        List<Node> nodet = new ArrayList<Node>();
        
        // vaihe
        Label vaihe = new Label("" + ohje.getVaihe());
        nodet.add(vaihe);
        
        // ohjeistus
        TextArea ohjeistus = new TextArea(ohje.getOhjeistus());
        ohjeistus.minWidth(250);
        ohjeistus.setPrefColumnCount(0);
        ohjeistus.setPrefRowCount(2);
        ohjeistus.setWrapText(true);
        ohjeistus.setOnKeyTyped( e -> {ohje.setOhjeistus(ohjeistus.getText()); } );
        nodet.add(ohjeistus);
        
        return nodet;
    }
    
    
    private List<Node> luoAinesosaNodet(OsionAinesosa osionAinesosa) {
        List<Node> nodet = new ArrayList<Node>();
        
        // määrä
        TextField maaraTextField = new TextField(osionAinesosa.getMaara());
        maaraTextField.setAlignment(Pos.CENTER);
        maaraTextField.setOnKeyTyped( e -> {osionAinesosa.setMaara(maaraTextField.getText()); } );
        nodet.add(maaraTextField);
        
        // ainesosa
        TextField ainesosaTextField = new TextField(osionAinesosa.getAinesosa());
        ainesosaTextField.setAlignment(Pos.CENTER);
        ainesosaTextField.setOnKeyTyped( e -> {osionAinesosa.setAinesosa(ainesosaTextField.getText()); } );
        nodet.add(ainesosaTextField);
        
        return nodet;
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
    
    
    private void poistaOsio(VBox osioVBox, Osio osio) {
        if (osioVBox == null) { return; }
        if (osioVBox.getParent() instanceof VBox) {
            VBox ylempiVBox = (VBox)osioVBox.getParent();
            ylempiVBox.getChildren().remove(osioVBox);
        }
        
        this.valittuResepti.poistaOsio(osio);
    }
    
    
    private boolean tulikoMuutoksia() {
        // valittu resepti ei voi olla null, joten voidaan tehdä vertailu
        return !this.valittuResepti.equals(this.alkuperainenResepti);
    }

}
