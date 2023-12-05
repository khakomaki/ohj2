package fxReseptihaku;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import kanta.SailoException;
import kanta.VaihtoehtoAttribuutti;
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
 * Muokkausnäkymä annetulle reseptille.
 * Muuttaa annetun reseptin tietoja, jos tallennetaan.
 * Sulkeminen ilman tallennusta ei tee muutoksia annettuun reseptiin.
 */
public class MuokkausGUIController implements ModalControllerInterface<Resepti> {
    
    @FXML private TextField osioTekstiKentta;
    @FXML private VBox ainesosaJaOhjeetVBox;
    @FXML private TextField reseptinNimiTextField;
    @FXML private TextArea kuvausTextArea;
    @FXML private GridPane attribuutitGridPane;

    @FXML private void handleSulje() { sulje(); }
    @FXML private void handleTallenna() { tallenna(); }
    @FXML private void handleLisaaOsio() { lisaaOsio(); }
    @FXML private void handlePoistaResepti() { poistaResepti(); }
    
    // ====================================================================================================
    
    // käyttöliittymän ulkonäkö
    private final Insets PEHMUSTE_ISO = new Insets(10, 10, 10, 10);
    private final Insets PEHMUSTE_PIENI = new Insets(5, 5, 5, 5);
    private final int VALI = 10;
    private final Font kirjasinB16 = new Font("System Bold", 16);
    private final Font kirjasin14 = new Font(14);
    
    // alkuperäinen ja muokattava resepti
    private Resepti valittuResepti;
    private Resepti alkuperainenResepti;
    
    
    /**
     * Palautettava resepti muokkauksen jälkeen
     */
    @Override
    public Resepti getResult() { 
        return this.alkuperainenResepti;
    }

    
    /**
     * Ei tehdä mitään kun resepti on laitettu näkyville
     */
    @Override
    public void handleShown() {
        //
    }
    
    
    /**
     * Asettaa reseptin näkymälle
     */
    @Override
    public void setDefault(Resepti oletus) {
        this.alkuperainenResepti = oletus;
        
        // luodaan kopio annetusta reseptistä muokattavaksi tai uusi jos annettu on null
        if (oletus == null) { 
            this.valittuResepti = new Resepti();
            
            // lisätään uuteen reseptiin osio tyhjällä ainesosalla ja ohjeella
            Osio osio = new Osio();
            osio.lisaaAinesosa(new OsionAinesosa());
            osio.lisaaOhje(new Ohje());
            this.valittuResepti.lisaaOsio(osio);
        } else { 
            this.valittuResepti = oletus.clone(); 
        }
        
        // näytetään resepti
        naytaResepti();
    }
    
    
    /**
     * Näyttää reseptin käyttöliittymään
     */
    private void naytaResepti() {
        naytaReseptinOminaisuudet();
        naytaOsiot();
    }
    
    
    /**
     * Näyttää reseptin ominaisuudet kuten nimi, kuvaus sekä sille määritellyt attribuutit
     */
    private void naytaReseptinOminaisuudet() {
        // reseptin nimi
        this.reseptinNimiTextField.setText(this.valittuResepti.getNimi());
        reseptinNimiTextField.setOnKeyTyped(e -> { valittuResepti.setUusiNimi(reseptinNimiTextField.getText()); });
        
        // reseptin kuvaus
        this.kuvausTextArea.setText(this.valittuResepti.getKuvaus());
        kuvausTextArea.setOnKeyTyped(e -> { valittuResepti.setKuvaus(kuvausTextArea.getText()); });
        
        // tyhjennetään attribuutit gridpane
        this.attribuutitGridPane.getChildren().clear();
        
        // attribuutit omille riveilleen
        int rivi = 0;
        for (VaihtoehtoAttribuutti attribuutti : this.valittuResepti.getAttribuutit()) {
            Label attribuutinNimi = new Label(attribuutti.getNimi());
            ComboBox<VaihtoehtoAttribuutti> attribuutinVaihtoehdot = new ComboBox<VaihtoehtoAttribuutti>();

            // lisätään vaihtoehdot (tyhjä + valittavat) ComboBox-nodeen
            attribuutinVaihtoehdot.getItems().add(new VaihtoehtoAttribuutti(attribuutti.getNimi(), attribuutti.getVaihtoehdot()));
            for (Entry<Integer, String> entry : attribuutti.getVaihtoehdot().entrySet()) {
                VaihtoehtoAttribuutti va = new VaihtoehtoAttribuutti(attribuutti.getNimi(), attribuutti.getVaihtoehdot());
                va.setValinta(entry.getKey());
                attribuutinVaihtoehdot.getItems().add(va);
            }
            
            // lisätään kuuntelija muutoksille
            attribuutinVaihtoehdot.setOnAction(e -> {
                attribuutti.setValinta(attribuutinVaihtoehdot.getValue().getValinta());
            });
            
            // lisätään nimi Label ja vaihtoehto ComboBox GridPaneen
            this.attribuutitGridPane.add(attribuutinNimi, 0, rivi);
            this.attribuutitGridPane.add(attribuutinVaihtoehdot, 1, rivi);
            
            rivi++;
        }
    }
    
    
    /**
     * Näyttää reseptin osiot
     */
    private void naytaOsiot() {
        // tyhjentää VBox sisällön
        this.ainesosaJaOhjeetVBox.getChildren().clear();
        
        Osiot osiot = this.valittuResepti.getOsiot();
        for (int i = 0; i < osiot.getLkm(); i++) {
            // luo osion HBoxin ja lisää sen käyttöliittymään
            VBox osioVBox = naytaOsio(osiot.annaIndeksista(i));
            ainesosaJaOhjeetVBox.getChildren().add(osioVBox);
        }
    }
    
    
    /**
     * Luo annetulle osiolle VBox-noden joka sisältää osion tiedot
     * 
     * @param osio mikä osio näytetään
     * @return luotu VBox osion tiedoilla
     */
    private VBox naytaOsio(Osio osio) {
        VBox osioVBox = new VBox(); // koko osio
        
        // otsikko joka koostuu osion nimestä ja osion poistopainikkeesta
        HBox osioOstikkoHBox = new HBox();
        osioOstikkoHBox.setSpacing(VALI);
        osioOstikkoHBox.setPadding(PEHMUSTE_ISO);
        
        // luo osion nimen tekstikentän
        TextField osioNimiAinesosat = new TextField(osio.getNimi());
        osioNimiAinesosat.setOnKeyTyped(e -> { osio.setUusiNimi(osioNimiAinesosat.getText()); });
        osioNimiAinesosat.setFont(kirjasinB16);
        
        // luo osion poisto-painikkeen
        Button osioPoisto = new Button("x");
        osioPoisto.setOnAction(e -> poistaOsio(osioVBox, osio)); // poistaa VBox-elementin
        osioPoisto.setFont(kirjasinB16);
        
        // lisätään teksti ja painike HBox-elementtiin
        osioOstikkoHBox.getChildren().add(osioNimiAinesosat);
        osioOstikkoHBox.getChildren().add(osioPoisto);
        
        // osion sisältö kuten ohjeet ja ainesosat
        HBox osionSisaltoHBox = new HBox();
        
        // sarakkeinden muotoilut
        ColumnConstraints ainesosaConstraints = NodeKasittely.luoSarakeRajoitteet(140, Priority.SOMETIMES, HPos.CENTER);
        ColumnConstraints painikeConstraints = NodeKasittely.luoSarakeRajoitteet(30, Priority.NEVER, HPos.CENTER);
        ColumnConstraints vaiheConstraints = NodeKasittely.luoSarakeRajoitteet(50, Priority.NEVER, HPos.CENTER);
        ColumnConstraints ohjeConstraints = NodeKasittely.luoSarakeRajoitteet(250, Priority.ALWAYS, HPos.LEFT);
        
        // ==================== näytetään ainesosat ====================
        
        // ainesosat
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
        
        // ohjeet
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
    
    
    /**
     * Luo annetulle ohjeelle javafx nodet tarvittua muokkausta varten
     * 
     * @param ohje mille ohjeelle luodaan nodet
     * @return lista luoduista nodeista
     */
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
        ohjeistus.setOnKeyTyped( e -> { ohje.setOhjeistus(ohjeistus.getText()); } );
        nodet.add(ohjeistus);
        
        return nodet;
    }
    
    
    /**
     * Luo annetulle ainesosalle javafx nodet muokkausta varten
     * 
     * @param osionAinesosa mille ainesosalle luodaan
     * @return lista luoduista nodeista
     */
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
    
    
    /**
     * yrittää sulkea muokkausnäkymän.
     * jos muokkausnäkymässä on tallentamattomia muutoksia, näyttää dialogin tallentamisesta.
     */
    private void sulje() {
        boolean tallennetaanko = false;
        // näytetään dialogi tallentamisesta jos on tallentamattomia muutoksia
        if (tulikoMuutoksia()) { 
            tallennetaanko = Dialogs.showQuestionDialog("Reseptin tallennus", "Sinulla on tallentamattomia muutoksia. Haluatko tallentaa?", "Tallenna", "Sulje");
        }
        if (tallennetaanko) {
            if (!tallenna()) return; // koittaa tallentaa, jos ei onnistu niin ei sulje
        };
        suljeTallentamatta();
    }
    
    
    /**
     * sulkee näkymän ilman dialogeja tai tallentamista
     */
    private void suljeTallentamatta() {
        ModalController.closeStage(reseptinNimiTextField);
    }
    
    
    /**
     * koittaa tallentaa reseptin, jos on tullut muutoksia.
     * ilmoittaa ongelmista tallentamisesta dialogilla.
     * 
     * @return saatiinko tallennettua
     */
    private boolean tallenna() {
        // ei tallenneta turhaan jos ei ole tullut muutoksia
        if (!tulikoMuutoksia()) return true;
        
        // yrittää tallentaa, näyttää dialogin jos ei onnistunut
        try {
            this.valittuResepti.tallenna();
            this.alkuperainenResepti = this.valittuResepti;
            return true;
        } catch (SailoException exception) {
            Dialogs.showMessageDialog("Tallentamisessa ongelmia: " + exception.getMessage());
            return false;
        }
    }
    
    
    /**
     * poistaa reseptin ja sulkee näkymän.
     * näyttää dialogin poistamisen varmistamiseksi.
     */
    private void poistaResepti() {
        boolean poistetaanko = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (poistetaanko) {
            this.alkuperainenResepti = null;
            suljeTallentamatta();
        }
    }
    
    
    /**
     * lisää reseptiin uuden osion sille tehdyn TextField-nodet tekstillä.
     * jos TextField on tyhjä niin luo osion oletusnimellä.
     */
    private void lisaaOsio() {
        // teksti osion lisäys-kentästä
        String osioTeksti = osioTekstiKentta.getText();
        if (osioTeksti.isEmpty()) osioTeksti = "";
        
        // Luodaan uusi osio ja näytetään se käyttöliittymään
        Osio osio = new Osio(osioTeksti);
        VBox osioHBox = naytaOsio(osio);
        ainesosaJaOhjeetVBox.getChildren().add(osioHBox);
        
        // lisätään osio reseptiin
        this.valittuResepti.lisaaOsio(osio);
        
        // tyhjennetään tekstikenttä
        osioTekstiKentta.clear();
    }
    
    
    /**
     * poistaa osion käyttöliittymästä ja reseptistä
     * 
     * @param osioVBox poistettavan osion käyttöliittymä-komponentti
     * @param osio poistettava osio
     */
    private void poistaOsio(VBox osioVBox, Osio osio) {
        if (osioVBox == null) return;
        
        // varmistetaan poistoaikomus dialogilla
        StringBuilder osioPoistoKysymys = new StringBuilder();
        osioPoistoKysymys.append("Haluatko varmasti poistaa osion \"");
        osioPoistoKysymys.append(osio.getNimi());
        osioPoistoKysymys.append("\"?");
        boolean poistetaanko = Dialogs.showQuestionDialog("Osion poisto", osioPoistoKysymys.toString(), "Poista", "Peruuta");
        if (!poistetaanko) return;
        
        // kutsuu osion parent-nodea ja käskee sen poistamaan
        if (osioVBox.getParent() instanceof VBox) {
            VBox ylempiVBox = (VBox)osioVBox.getParent();
            ylempiVBox.getChildren().remove(osioVBox);
        }
        
        // poistaa osion reseptistä
        this.valittuResepti.poistaOsio(osio);
    }
    
    
    /**
     * Kertoo onko muokkauksen aikana tullut muutoksia alkuperäiseen reseptiin
     * 
     * @return totuusarvo tuliko muutoksia
     */
    private boolean tulikoMuutoksia() {
        if (valittuResepti == null) return true;
        return !this.valittuResepti.equals(this.alkuperainenResepti);
    }

}
