package fxReseptihaku;

import java.util.ArrayList;
import java.util.List;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fxKanta.DynaaminenGridPane;
import fxKanta.NodeKasittely;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
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
 * @version 30 Sept 2023
 *
 * Reseptinäkymän controller, reseptin katselu
 */
public class ReseptinakymaGUIController implements ModalControllerInterface<Resepti> {

    @FXML private CheckBox checkBox1;
    @FXML private Label reseptinNimi;
    @FXML private Label reseptinKuvaus;
    @FXML private Label reseptinHinta;
    @FXML private Label reseptinValmistusaika;
    @FXML private Label reseptinTahdet;
    @FXML private Label reseptinVaativuus;
    @FXML private VBox ainesosatVBox;
    @FXML private VBox ohjeetVBox;
    
    @FXML private void handleSulje() { sulje(); }
    @FXML private void handleTulosta() { tulosta(); }
    @FXML private void handleMuokkaaResepti() { muokkaaResepti(); }
    @FXML private void handlePoistaResepti() { poistaResepti(); }
    
    // ====================================================================================================
    
    private Resepti valittuResepti;
    private final Insets PEHMUSTE_ISO = new Insets(10, 10, 10, 10);
    private final Font kirjasin14 = new Font("Arial Bold", 14);
    private RowConstraints rivirajoitteet = new RowConstraints();
    
    
    /**
     * Palautettava resepti näkymää suljettaessa.
     * Palautetaan null jos resepti on poistettu.
     */
    @Override
    public Resepti getResult() { 
        return this.valittuResepti; 
    }

    
    /**
     * Ei tehdä mitään kun resepti on laitettu näkyviin
     */
    @Override
    public void handleShown() {
        // 
    }

    
    /**
     * Asettaa näytetävän reseptin
     */
    @Override
    public void setDefault(Resepti oletus) {
        this.valittuResepti = oletus;
        this.rivirajoitteet.setMinHeight(25);
        this.rivirajoitteet.setVgrow(Priority.SOMETIMES);
        tyhjennaTiedot();
        naytaReseptinOminaisuudet();
        naytaReseptinOsiot();
    }
    
    
    /**
     * Tyhjentää FXMLLoaderin asettamat tiedot
     */
    private void tyhjennaTiedot() {
        this.ainesosatVBox.getChildren().clear();
        this.ohjeetVBox.getChildren().clear();
    }
    
    
    /**
     * Näyttää reseptin ominaisuudet
     */
    private void naytaReseptinOminaisuudet() {
        // asetetaan reseptin tiedot näkymään
        this.reseptinNimi.setText(this.valittuResepti.getNimi());
        this.reseptinKuvaus.setText(this.valittuResepti.getKuvaus());
        this.reseptinHinta.setText(this.valittuResepti.getHintaString());
        this.reseptinValmistusaika.setText(this.valittuResepti.getValmistusaikaString());
        this.reseptinTahdet.setText(this.valittuResepti.getTahdetString());
        this.reseptinVaativuus.setText(this.valittuResepti.getVaativuusString());
    }
    
    
    /**
     * Näyttää reseptin osioiden tiedot
     */
    private void naytaReseptinOsiot() {
        Osiot osiot = this.valittuResepti.getOsiot();
        
        for (int i = 0; i < osiot.getLkm(); i++) {
            Osio osio = osiot.annaIndeksista(i);
            naytaReseptinAinesosat(osio);
            naytaOsionOhjeet(osio);
        }
    }
    
    
    /**
     * Näyttää reseptin ainesosat.
     * Ei näytä mitään jos annetussa osiossa ei ole ainesosia.
     * 
     * @param osio minkä osion ainesosat näytetään
     */
    private void naytaReseptinAinesosat(Osio osio) {
        OsionAinesosat osionAinesosat = osio.annaOsionAinesosat();
        if (osionAinesosat.getLkm() < 1) return; // ei näytetä jos ei yhtään ainesosaa
        
        // lisätään osion nimi -Label
        Label osionNimi = luoOtsikko(osio.getNimi());
        ainesosatVBox.getChildren().add(osionNimi);
        ainesosatVBox.setPadding(PEHMUSTE_ISO);
        
        // luodaan GridPane ainesosille
        DynaaminenGridPane<OsionAinesosa> ainesosatGridPane = new DynaaminenGridPane<OsionAinesosa>(osionAinesosat, ainesosa -> luoAinesosaNodet(ainesosa), false);
        ainesosatGridPane.asetaRiviRajoitteet(rivirajoitteet);
        ainesosatGridPane.getColumnConstraints().add(NodeKasittely.luoSarakeRajoitteet(50, Priority.NEVER, HPos.RIGHT));
        ainesosatGridPane.getColumnConstraints().add(NodeKasittely.luoSarakeRajoitteet(125, Priority.SOMETIMES, HPos.LEFT));
        ainesosatGridPane.setHgap(5);
        ainesosatGridPane.setVgap(5);
        ainesosatGridPane.paivita();
        
        ainesosatVBox.getChildren().add(ainesosatGridPane);
    }
    
    
    /**
     * Luo ainesosalle vastaavat javafx-nodet
     * 
     * @param osionAinesosa mille ainesosalle luodaan
     * @return ainesosaa vastaavat nodet
     */
    private List<Node> luoAinesosaNodet(OsionAinesosa osionAinesosa) {
        List<Node> nodet = new ArrayList<Node>();
        
        // määrä
        Label maaraLabel = new Label(osionAinesosa.getMaara());
        nodet.add(maaraLabel);
        
        // ainesosa
        Label ainesosaLabel = new Label(osionAinesosa.getAinesosa());
        nodet.add(ainesosaLabel);
        
        return nodet;
    }
    
    
    /**
     * Luo otsikko Labelin
     * 
     * @param otsikko otsikon teksti
     * @return otsikko Label
     */
    private Label luoOtsikko(String otsikko) {
        Label otsikkoLabel = new Label(otsikko);
        otsikkoLabel.setFont(kirjasin14);
        otsikkoLabel.setPadding(PEHMUSTE_ISO);
        return otsikkoLabel;
    }
    
    
    /**
     * Näyttää osion ohjeet.
     * Jos osiossa ei ole yhtään ohjetta, ei näytä mitään.
     * 
     * @param osio minkä osion ohjeet näytetään
     */
    private void naytaOsionOhjeet(Osio osio) {
        Ohjeet osionOhjeet = osio.annaOsionOhjeet();
        if (osionOhjeet.getLkm() < 1) return; // ei näytetä jos ei yhtään ohjetta
        
        // lisätään osion nimi -Label
        Label OsionNimi = luoOtsikko(osio.getNimi());
        ohjeetVBox.getChildren().add(OsionNimi);
        ohjeetVBox.setPadding(PEHMUSTE_ISO);
        
        // luodaan GridPane ohjeille
        DynaaminenGridPane<Ohje> ohjeetGridPane = new DynaaminenGridPane<Ohje>(osionOhjeet, ohje -> luoOhjeNodet(ohje), false);
        ohjeetGridPane.asetaRiviRajoitteet(this.rivirajoitteet);
        ohjeetGridPane.getColumnConstraints().add(NodeKasittely.luoSarakeRajoitteet(30, Priority.NEVER, HPos.CENTER));
        ohjeetGridPane.getColumnConstraints().add(NodeKasittely.luoSarakeRajoitteet(30, Priority.NEVER, HPos.CENTER));
        ohjeetGridPane.getColumnConstraints().add(NodeKasittely.luoSarakeRajoitteet(125, Priority.SOMETIMES, HPos.LEFT));
        ohjeetGridPane.setHgap(5);
        ohjeetGridPane.setVgap(5);
        ohjeetGridPane.paivita();
        
        ohjeetVBox.getChildren().add(ohjeetGridPane);
    }
    
    
    /**
     * Luo ohjetta vastaavat javafx-nodet
     * 
     * @param ohje mille ohjeelle luodaan
     * @return ohjeen nodet
     */
    private List<Node> luoOhjeNodet(Ohje ohje) {
        List<Node> nodet = new ArrayList<Node>();
        
        // vaihe
        Label vaiheLabel = new Label("" + ohje.getVaihe());
        
        // ohjeistus
        Label ohjeistusLabel = new Label(ohje.getOhjeistus());
        
        // piilotuspainike
        CheckBox piilotusPainike = new CheckBox();
        piilotusPainike.setOnAction( e -> vaihdaNakyvyys(ohjeistusLabel) );
        
        nodet.add(piilotusPainike);
        nodet.add(vaiheLabel);
        nodet.add(ohjeistusLabel);
        
        return nodet;
    }
    
    
    /**
     * Vaihtaa tekstin näkyvyyttä (päällä -> pois | pois -> päällä)
     * 
     * @param teksti minkä näkyvyyttä vaihdetaan
     */
    private void vaihdaNakyvyys(Label teksti) {
        if (teksti.isVisible()) teksti.setVisible(false);
        else teksti.setVisible(true);
    }
    
    
    /**
     * Sulkee reseptinäkymän
     */
    private void sulje() {
        ModalController.closeStage(reseptinNimi);
    }
    
    
    /**
     * Avaa reseptin tulostusnäkymän
     * TODO tulostaminen toimimaan
     */
    private void tulosta() {
        Dialogs.showMessageDialog("Ei osata tulostaa vielä");
    }
    
    
    /**
     * Avaa katseltavan reseptin muokkausnäkymän.
     * Muokkausnäkymästä tultaessa varmistaa onko resepti poistettu tai onko sitä muokattu.
     * Poiston tapauksessa sulkee näkymän.
     * Muokkauksen tapauksessa päivittää näkymän.
     */
    private void muokkaaResepti() {
        Resepti muokattuResepti = ModalController.showModal( ReseptihakuGUIController.class.getResource("MuokkausGUIView.fxml"), "Muokkaa reseptiä", null, this.valittuResepti);
        
        // jos muokattu resepti on poistettu, suljetaan näkymä
        if (muokattuResepti == null) {
            this.valittuResepti = null;
            sulje();
            return;
        }
        
        // jos reseptiin on tehty muutoksia, päivitetään käyttöliittymän näkymä
        if (!valittuResepti.equals(muokattuResepti)) {
            this.valittuResepti = muokattuResepti;
            setDefault(valittuResepti); 
        }
    }
    
    
    /**
     * Poistaa katseltavan reseptin, näyttää varmistus dialogin ennen poistamista.
     * Sulkee näkymän poiston jälkeen.
     */
    private void poistaResepti() {
        boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (vastaus) {
            this.valittuResepti = null;
            sulje();
        }
    }
    
}
