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
    private RowConstraints rivirajoitteet = new RowConstraints();
    private final Font kirjasin14 = new Font("Arial Bold", 14);
    
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
        this.rivirajoitteet.setMinHeight(25);
        this.rivirajoitteet.setVgrow(Priority.SOMETIMES);
        tyhjennaTiedot();
        naytaReseptinOminaisuudet();
        naytaReseptinOsiot();
    }
    
    
    private void tyhjennaTiedot() {
        this.ainesosatVBox.getChildren().clear();
        this.ohjeetVBox.getChildren().clear();
    }
    
    
    private void naytaReseptinOminaisuudet() {
        // asetetaan reseptin tiedot näkymään
        this.reseptinNimi.setText(this.valittuResepti.getNimi());
        this.reseptinKuvaus.setText(this.valittuResepti.getKuvaus());
        this.reseptinHinta.setText(this.valittuResepti.getHintaString());
        this.reseptinValmistusaika.setText(this.valittuResepti.getValmistusaikaString());
        this.reseptinTahdet.setText(this.valittuResepti.getTahdetString());
        this.reseptinVaativuus.setText(this.valittuResepti.getVaativuusString());
    }
    
    
    private void naytaReseptinOsiot() {
        Osiot osiot = this.valittuResepti.getOsiot();
        
        for (int i = 0; i < osiot.getLkm(); i++) {
            Osio osio = osiot.annaIndeksista(i);
            naytaReseptinAinesosat(osio);
            naytaOsionOhjeet(osio);
        }
    }
    
    
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
    
    
    private Label luoOtsikko(String otsikko) {
        Label otsikkoLabel = new Label(otsikko);
        otsikkoLabel.setFont(kirjasin14);
        otsikkoLabel.setPadding(PEHMUSTE_ISO);
        return otsikkoLabel;
    }
    
    
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
    
    
    private void vaihdaNakyvyys(Label teksti) {
        if (teksti.isVisible()) teksti.setVisible(false);
        else teksti.setVisible(true);
    }
    
    
    private void sulje() {
        ModalController.closeStage(reseptinNimi);
    }
    
    
    private void tulosta() {
        Dialogs.showMessageDialog("Ei osata tulostaa vielä");
    }
    
    
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
    
    
    private void poistaResepti() {
        boolean vastaus = Dialogs.showQuestionDialog("Reseptin poisto", "Haluatko varmasti poistaa reseptin pysyvästi?", "Poista", "Peruuta");
        if (vastaus) {
            this.valittuResepti = null;
            sulje();
        }
    }
    
}
