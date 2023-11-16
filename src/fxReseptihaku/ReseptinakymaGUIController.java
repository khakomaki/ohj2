package fxReseptihaku;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
    private final Font kirjasin14 = new Font(14);
    
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
        // lisätään osion nimi -Label
        Label osionNimi = luoOtsikko(osio.getNimi());
        ainesosatVBox.getChildren().add(osionNimi);
        ainesosatVBox.setPadding(PEHMUSTE_ISO);
        
        // luodaan GridPane ainesosille
        GridPane ainesosatGridPane = luoAinesosaGridPane();
        
        OsionAinesosat ainesosat = osio.annaOsionAinesosat();
        for (int i = 0; i < ainesosat.getLkm(); i++) {
            OsionAinesosa ainesosa = ainesosat.anna(i);
            lisaaAinesosaGridPaneen(ainesosatGridPane, ainesosa, i);
        }
        
        ainesosatVBox.getChildren().add(ainesosatGridPane);
    }
    
    
    private Label luoOtsikko(String otsikko) {
        Label otsikkoLabel = new Label(otsikko);
        otsikkoLabel.setFont(kirjasin14);
        otsikkoLabel.setPadding(PEHMUSTE_ISO);
        return otsikkoLabel;
    }
    
    
    private GridPane luoAinesosaGridPane() {
        GridPane ainesosatGridPane = new GridPane();
        ainesosatGridPane.getColumnConstraints().add(luoRajoitteet(50, Priority.NEVER, HPos.RIGHT));
        ainesosatGridPane.getColumnConstraints().add(luoRajoitteet(125, Priority.SOMETIMES, HPos.LEFT));
        ainesosatGridPane.setHgap(5);
        ainesosatGridPane.setVgap(5);
        return ainesosatGridPane;
    }
    
    
    private void lisaaAinesosaGridPaneen(GridPane paneeli, OsionAinesosa ainesosa, int rivi) {
        Label maaraLabel = new Label(ainesosa.getMaara());
        Label ainesosaLabel = new Label(ainesosa.getAinesosa());
        
        paneeli.getRowConstraints().add(rivirajoitteet);
        paneeli.add(maaraLabel, 0, rivi);
        paneeli.add(ainesosaLabel, 1, rivi);
    }
    
    
    private void naytaOsionOhjeet(Osio osio) {
        // lisätään osion nimi -Label
        Label OsionNimi = luoOtsikko(osio.getNimi());
        ohjeetVBox.getChildren().add(OsionNimi);
        ohjeetVBox.setPadding(PEHMUSTE_ISO);
        
        GridPane ohjeetGridPane = luoOhjeetGridPane();
        
        Ohjeet ohjeet = osio.annaOsionOhjeet();
        for (int i = 0; i < ohjeet.getLkm(); i++) {
            Ohje ohje = ohjeet.anna(i);
            lisaaOhjeGridPaneen(ohjeetGridPane, ohje, i);
        }
        
        ohjeetVBox.getChildren().add(ohjeetGridPane);
    }
    
    
    private GridPane luoOhjeetGridPane() {
        GridPane ohjeetGridPane = new GridPane();
        ohjeetGridPane.getColumnConstraints().add(luoRajoitteet(30, Priority.NEVER, HPos.CENTER));
        ohjeetGridPane.getColumnConstraints().add(luoRajoitteet(30, Priority.NEVER, HPos.CENTER));
        ohjeetGridPane.getColumnConstraints().add(luoRajoitteet(125, Priority.SOMETIMES, HPos.LEFT));
        ohjeetGridPane.setHgap(5);
        ohjeetGridPane.setVgap(5);
        return ohjeetGridPane;
    }
    
    
    private void lisaaOhjeGridPaneen(GridPane paneeli, Ohje ohje, int rivi) {
        Label vaiheLabel = new Label("" + ohje.getVaihe());
        Label ohjeistusLabel = new Label(ohje.getOhjeistus());
        CheckBox piilotusPainike = new CheckBox();
        piilotusPainike.setOnAction( e -> vaihdaNakyvyys(ohjeistusLabel) );
        
        paneeli.getRowConstraints().add(rivirajoitteet);
        paneeli.add(piilotusPainike, 0, rivi);
        paneeli.add(vaiheLabel, 1, rivi);
        paneeli.add(ohjeistusLabel, 2, rivi);
    }
    
    
    private void vaihdaNakyvyys(Label teksti) {
        if (teksti.isVisible()) teksti.setVisible(false);
        else teksti.setVisible(true);
    }
        
    
    private ColumnConstraints luoRajoitteet(int minimiLeveys, Priority leveydenKasvu, HPos ryhmitys) {
        ColumnConstraints rajoite = new ColumnConstraints();
        rajoite.setMinWidth(minimiLeveys);
        rajoite.setHgrow(leveydenKasvu);
        rajoite.setHalignment(ryhmitys);
        return rajoite;
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
