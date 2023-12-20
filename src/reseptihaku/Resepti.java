package reseptihaku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Hajautus;
import kanta.MerkkijonoKasittely;
import kanta.SailoException;
import kanta.Satunnaisluku;
import kanta.VaihtoehtoAttribuutti;
import kanta.Validoi;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 * Resepti koostuu osioista ja reseptin attribuuteista.
 */
public class Resepti {
    
    private final String oletusNimi = "Reseptin nimi";
    
    private int reseptiId                       = -1;
    private String nimi                         = "";
    private Osiot osiot                         = null;
    private String kuvaus                       = "";
    private VaihtoehtoAttribuutti hinta         = null;
    private VaihtoehtoAttribuutti valmistusaika = null;
    private VaihtoehtoAttribuutti tahdet        = null;
    private VaihtoehtoAttribuutti vaativuus     = null;
    private String tiedostonimi                 = "reseptit.dat";
    private String tiedostopolku                = "reseptidata/";
    private String rekisteroitynimi             = "";
    private boolean muutettu                    = true;
    
    private static int annettavaId  = 1;
    private static Set<String> rekisteroidytNimet = new HashSet<String>();
    
    private static final Map<Integer, String> hintaVaihtoehdot;
    private static final Map<Integer, String> valmistusaikaVaihtoehdot;
    private static final Map<Integer, String> tahdetVaihtoehdot;
    private static final Map<Integer, String> vaativuusVaihtoehdot;
    
     /* alustetaan HashMapit attribuuttien vaihtoehdoista ensimmäisellä kertaa.
     * samat kaikille, joten ei luoda erikseen joka oliolle.
     * luo Mapit muuttumattomiksi, esimerkiksi metodi
     * Resepti.hintaVaihtoehdot.put(7, "€€€€€€€"); heittää virheilmoituksen
     */
    static {
        Map<Integer, String> hinnat = new HashMap<>();
        hinnat.put(1, "€");
        hinnat.put(2, "€€");
        hinnat.put(3, "€€€");
        hintaVaihtoehdot = Collections.unmodifiableMap(hinnat);
        
        Map<Integer, String> valmistusajat = new HashMap<>();
        valmistusajat.put(1, "välitön");
        valmistusajat.put(2, "nopea");
        valmistusajat.put(3, "keskimääräinen");
        valmistusajat.put(4, "pitkä");
        valmistusajat.put(5, "extra pitkä");
        valmistusaikaVaihtoehdot = Collections.unmodifiableMap(valmistusajat);
        
        Map<Integer, String> tahtia = new HashMap<>();
        tahtia.put(1, "☆");
        tahtia.put(2, "☆☆");
        tahtia.put(3, "☆☆☆");
        tahtia.put(4, "☆☆☆☆");
        tahtia.put(5, "☆☆☆☆☆");
        tahdetVaihtoehdot = Collections.unmodifiableMap(tahtia);
        
        Map<Integer, String> vaativuudet = new HashMap<>();
        vaativuudet.put(1, "helppo");
        vaativuudet.put(2, "kohtalaisen helppo");
        vaativuudet.put(3, "keskimääräinen");
        vaativuudet.put(4, "kohtalaisen työläs");
        vaativuudet.put(5, "työläs");
        vaativuusVaihtoehdot = Collections.unmodifiableMap(vaativuudet);
    }
    
    
    /**
     * Luo Reseptin annetulla nimellä.
     * Reseptin tunnus alustuu -1.
     * 
     * @param nimi reseptin nimi
     * 
     * @example
     * <pre name="test">
     * Resepti makaronilaatikko = new Resepti("Makaronilaatikko");
     * makaronilaatikko.toString() === "-1|Makaronilaatikko|-1|-1|-1|-1";
     * </pre>
     */
    public Resepti(String nimi) {
        setNimi(nimi);
        luoOsiot();
        luoVaihtoehtoAttribuutit();
    }
    
    
    /**
     * Luo Reseptin.
     * Nimi alustuu oletusnimeksi ja reseptin tunnus -1.
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti();
     * resepti.toString() === "-1|Reseptin nimi|-1|-1|-1|-1";
     * </pre>
     */
    public Resepti() {
        setNimi(null);
        luoOsiot();
        luoVaihtoehtoAttribuutit();
    }
    
    
    /**
     * Luo reseptin osiot
     */
    private void luoOsiot() {
    	try {
    		this.osiot = new Osiot(this.reseptiId);
    	} catch (SailoException exception) {
    		System.err.println(exception.getMessage());
    	}
    }
    
    
    /**
     * Luo reseptille id:n
     * 
     * @return reseptin tunnus
     * 
     * @example
     * Resepti lihapullat = new Resepti("Lihapullat");
     * lihapullat.toString() === "-1|Lihapullat|-1|-1|-1|-1";
     * int n = lihapullat.rekisteroiTunnus();
     * 
     * Resepti perunamuusi = new Resepti("Perunamuusi");
     * perunamuusi.rekisteroiTunnus() === n + 1;
     */
    private int rekisteroiTunnus() {
        if (0 < this.reseptiId) return this.reseptiId; // jos on jo rekisteröity
        
        this.reseptiId = Resepti.annettavaId;
        this.osiot.setReseptiTunnus(this.reseptiId);
        Resepti.annettavaId++;
        return this.reseptiId;
    }
    
    
    /**
     * Yrittää asettaa reseptille annetun tunnuksen
     * 
     * @param id mikä tunnus yritetään laittaa
     * @return asetettu reseptin tunnus
     */
    private int rekisteroiTunnus(int id) {
        if (0 < this.reseptiId) return this.reseptiId; // jos on jo rekisteröity
        
        if (Resepti.annettavaId < id) Resepti.annettavaId = id;
        return rekisteroiTunnus();
    }
    
    
    /**
     * Koittaa rekisteröidä annetun nimen.
     * Jos onnistuu niin vaihtaa nykyisen nimen samaksi.
     * 
     * @param rekisteroitavaNimi mitä nimeä koitetaan rekisteröidä
     * @return rekisteröity nimi
     */
    private String rekisteroiNimi(String rekisteroitavaNimi) {
        // ei vaihda jos ei ole tallennettava tai sellainen on jo rekisteröity
        if (!Validoi.onkoNimiTallennettavissa(rekisteroitavaNimi)) return this.rekisteroitynimi;
        if (Resepti.rekisteroidytNimet.contains(rekisteroitavaNimi)) return this.rekisteroitynimi;
        
        this.rekisteroitynimi = rekisteroitavaNimi;
        Resepti.rekisteroidytNimet.add(this.rekisteroitynimi);
        setUusiNimi(this.rekisteroitynimi); // nimi vastaamaan rekisteröityä nimeä
        
        return this.rekisteroitynimi;
    }
    
    
    /**
     * Koittaa rekisteröidä nykyisen reseptinimen
     * 
     * @return rekisteröity nimi
     */
    private String rekisteroiNimi() {
        // ei tee mitään jos nimi on jo sama kuin rekisteröity
        if (getNimi().equals(this.rekisteroitynimi)) return this.rekisteroitynimi;
        
        // poistaa nykyisen rekisteröidyn nimen ja rekisteröi uuden
        poistaRekisteroidyistaNimista();
        return rekisteroiNimi(this.getNimi());
    }
    
    
    /**
     * Poistaa rekisteröidyn nimen luokan nimistä
     */
    private void poistaRekisteroidyistaNimista() {
        Resepti.rekisteroidytNimet.remove(this.rekisteroitynimi);
    }
    
    
    /**
     * Asettaa reseptin tunnuksen ja päivittää annettavaa tunnusta tarvittaessa
     * 
     * @param ID reseptin tunnus
     */
    private void setID(int ID) {
    	this.reseptiId = ID;
    	if (Resepti.annettavaId <= this.reseptiId) Resepti.annettavaId = this.reseptiId + 1;
    }
    
    
    /**
     * Tarkistaa tietokannan tiedoista onko tunnus muuttunut ja päivittää tarvittaessa omia tietoja
     * 
     * @param tulokset mistä tiedot katsotaan
     * @throws SQLException jos tulee ongelmia
     */
    public void tarkistaID(ResultSet tulokset) throws SQLException {
    	if (!tulokset.next()) return;
    	int ID = tulokset.getInt(1);
    	if (ID == this.reseptiId);
    	setID(ID);
    }
    
    
    private void luoVaihtoehtoAttribuutit() {
        this.hinta = new VaihtoehtoAttribuutti("Hinta", hintaVaihtoehdot);
        this.valmistusaika = new VaihtoehtoAttribuutti("Valmistusaika", valmistusaikaVaihtoehdot);
        this.tahdet = new VaihtoehtoAttribuutti("Tähdet", tahdetVaihtoehdot);
        this.vaativuus = new VaihtoehtoAttribuutti("Vaativuus", vaativuusVaihtoehdot);
    }
    
    
    /**
     * Reseptin attribuutit suodattamista varten listassa.
     * Järjestys:
     * - hinta
     * - valmistusaika
     * - tähdet
     * - vaativuus
     * 
     * @return reseptin attribuutit
     */
    public List<VaihtoehtoAttribuutti> getAttribuutit() {
        List<VaihtoehtoAttribuutti> attribuutit = new ArrayList<VaihtoehtoAttribuutti>();
        attribuutit.add(hinta);
        attribuutit.add(valmistusaika);
        attribuutit.add(tahdet);
        attribuutit.add(vaativuus);
        return attribuutit;
    }
    
    
    /**
     * Asettaa reseptin nimen ensimmäistä kertaa.
     * Jos syötetään tyhjä merkkijono tai null, antaa oletusnimen.
     * 
     * @param nimi reseptin nimi
     * 
     * @example
     * <pre name="test">
     * Resepti pizza = new Resepti("Pizza");
     * pizza.toString() === "-1|Pizza|-1|-1|-1|-1";
     * 
     * pizza = new Resepti("");
     * pizza.toString() === "-1|Reseptin nimi|-1|-1|-1|-1";
     * 
     * pizza = new Resepti("Itsetehty pizza");
     * pizza.toString() === "-1|Itsetehty pizza|-1|-1|-1|-1";
     * 
     * pizza = new Resepti(null);
     * pizza.toString() === "-1|Reseptin nimi|-1|-1|-1|-1";
     * </pre>
     */
    private void setNimi(String nimi) {
        // asettaa oletusnimen jos annettu nimi on null tai tyhjä merkkijono
        if (nimi == null || nimi.length() < 1) { 
            this.nimi = oletusNimi; 
            return;
        }
        
        this.nimi = nimi;
    }
    
    
    /**
     * Asettaa reseptin nimen uudelleen.
     * Ei anna muuttaa nimeä null-viitteeksi tai tyhjäksi merkkijonoksi.
     * 
     * @param nimi reseptin nimi
     * 
     * @example
     * <pre name="test">
     * Resepti pizza = new Resepti("Pizza");
     * pizza.toString() === "-1|Pizza|-1|-1|-1|-1";
     * 
     * pizza.setUusiNimi("");
     * pizza.toString() === "-1|Pizza|-1|-1|-1|-1";
     * 
     * pizza.setUusiNimi("Itsetehty pizza");
     * pizza.toString() === "-1|Itsetehty pizza|-1|-1|-1|-1";
     * 
     * pizza.setUusiNimi(null);
     * pizza.toString() === "-1|Itsetehty pizza|-1|-1|-1|-1";
     * </pre>
     */
    public void setUusiNimi(String nimi) {
        // ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
        if (nimi == null || nimi.length() < 1) return;
        this.nimi = nimi;
        this.muutettu = true;
    }
    
    
    /**
     * Antaa reseptin nimen
     * 
     * @return reseptin nimi
     * 
     * @example
     * <pre name="test">
     * Resepti pizza = new Resepti("Margarita pizza");
     * pizza.getNimi().equals("Margarita pizza") === true;
     * </pre>
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * Asettaa reseptin kuvauksen.
     * Jos annetaan null-viite, vaihtaa kuvauksen tyhjäksi merkkijonoksi.
     * 
     * @param kuvaus reseptin kuvaus
     * 
     * @example
     * <pre name="test">
     * Resepti sampyla = new Resepti();
     * sampyla.getKuvaus() === "";
     * 
     * sampyla.setKuvaus("Parhaimmillaan uunituoreena.");
     * sampyla.getKuvaus() === "Parhaimmillaan uunituoreena.";
     * 
     * sampyla.setKuvaus(null);
     * sampyla.getKuvaus() === "";
     * </pre>
     */
    public void setKuvaus(String kuvaus) {
        if (kuvaus == null) { 
            this.kuvaus = ""; 
            return; 
        }
        
        this.kuvaus = kuvaus;
        this.muutettu = true;
    }
    
    
    /**
     * Antaa reseptin kuvauksen.
     * 
     * @return reseptin kuvaus
     * 
     * @example
     * <pre name="test">
     * Resepti sampyla = new Resepti();
     * sampyla.getKuvaus() === "";
     * 
     * sampyla.setKuvaus("Parhaimmillaan uunituoreena.");
     * sampyla.getKuvaus() === "Parhaimmillaan uunituoreena.";
     * 
     * sampyla.setKuvaus(null);
     * sampyla.getKuvaus() === "";
     * </pre>
     */
    public String getKuvaus() {
        return this.kuvaus;
    }
    
    
    /**
     * Antaa reseptin tunnuksen/id:n
     * 
     * @return reseptin tunnus
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti();
     * resepti.getTunnus() === -1;
     * </pre>
     */
    public int getTunnus() {
        return this.reseptiId;
    }
    
    
    /**
     * Antaa tietokannan SQL-luontilausekkeen Reseptit-taululle
     * 
     * @return Reseptit-taulukon luontilauseke
     */
    public String getLuontilauseke() {
        StringBuilder lauseke = new StringBuilder();
        lauseke.append("CREATE TABLE Reseptit (");
        
        lauseke.append("resepti_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        lauseke.append("nimi VARCHAR(100) NOT NULL, ");
        lauseke.append("kuvaus VARCHAR(255), ");
        lauseke.append("hinta INTEGER, ");
        lauseke.append("valmistusaika INTEGER, ");
        lauseke.append("tahdet INTEGER, ");
        lauseke.append("vaativuus INTEGER");
        
        lauseke.append(")");
        return lauseke.toString();
    }
    
    
    /**
     * Antaa reseptin lisäyslausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return lisäyslauseke
     * @throws SQLException jos lausekkeen muodostamisessa ongelmia
     */
    public PreparedStatement getLisayslauseke(Connection yhteys) throws SQLException {
        PreparedStatement sql = yhteys.prepareStatement("INSERT INTO Reseptit (nimi, kuvaus, hinta, valmistusaika, tahdet, vaativuus) VALUES (?, ?, ?, ?, ?, ?)");
        
        sql.setString(1, getNimi());
        sql.setString(2, getKuvaus());
        sql.setInt(3, getHinta());
        sql.setInt(4, getValmistusaika());
        sql.setInt(5, getTahdet());
        sql.setInt(6, getVaativuus());
        
        return sql;
    }
    
    
    /**
     * Antaa reseptin poistolausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return poistolauseke
     * @throws SQLException jos lausekkeen muodostamisessa ongelmia
     */
    public PreparedStatement getPoistolauseke(Connection yhteys) throws SQLException {
        PreparedStatement sql = yhteys.prepareStatement("DELETE FROM Reseptit WHERE resepti_id = ?");
        
        // resepti yksilöity PK = resepti_id
        sql.setInt(1, this.reseptiId);
        
        return sql;
    }
    
    
    /**
     * Parsii annetuista tiedoista omat tietonsa
     * 
     * @param tulokset mistä tiedot saadaan
     * @throws SQLException jos tulee ongelmia
     */
    public void parse(ResultSet tulokset) throws SQLException {
    	setID(tulokset.getInt("resepti_id"));
        setUusiNimi(tulokset.getString("nimi"));
    }
    
    
    /**
     * Asettaa tiedostopolun ja tallentaa
     * 
     * @param tiedostopolku mihin polkuun tietoja tallennetaan ja luetaan
     * @throws SailoException jos polun vaihtamisessa tulee ongelmia
     */
    public void setTiedostopolku(String tiedostopolku) throws SailoException {
        // ei tee mitään jos null tai sama kuin oli
        if (tiedostopolku == null) return;
        
        this.tiedostopolku = tiedostopolku; // esim. reseptidata/
        
        // luo tiedostopolun siltä varalta että sitä ei ole
        File dir = new File(this.tiedostopolku);
        dir.mkdirs();
        
        this.osiot.setTiedostoPolku(getAlihakemistoPolku()); // esim. reseptidata/Mustikkapiirakka/
        this.muutettu = true;
    }
    
    
    /**
     * Antaa reseptin alihakemistopolun. (muotoa "reseptidata/Reseptin nimi/")
     * 
     * @return alihakemistopolku
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import kanta.SailoException;
     * 
     * Resepti resepti = new Resepti();
     * resepti.getAlihakemistoPolku() === "reseptidata/Reseptin nimi/";
     * 
     * resepti.setTiedostopolku("jälkiruokareseptit/");
     * resepti.setUusiNimi("Suklaakakku");
     * resepti.getAlihakemistoPolku() === "jälkiruokareseptit/Suklaakakku/";
     * </pre>
     */
    public String getAlihakemistoPolku() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.tiedostopolku);
        sb.append(getNimi());
        sb.append("/");
        return sb.toString();
    }
    
    
    /**
     * Antaa reseptin osiot.
     * 
     * @return reseptin osiot
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti();
     * resepti.getOsiot().equals(new Osiot()) === true;
     * 
     * resepti.lisaaOsio(new Osio("Pohja"));
     * resepti.lisaaOsio(new Osio("Täyte"));
     * 
     * resepti.getOsiot().annaIndeksista(0).equals(new Osio("Pohja")) === true;
     * resepti.getOsiot().annaIndeksista(1).equals(new Osio("Täyte")) === true;
     * </pre>
     */
    public Osiot getOsiot() {
         return this.osiot;
    }
    
    
    /**
     * Lisää reseptiin osion.
     * Ei lisää pelkkää null-viitettä.
     * 
     * @param osio lisättävä osio
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti();
     * resepti.getOsiot().equals(new Osiot()) === true;
     * 
     * resepti.lisaaOsio(new Osio("Pohja"));
     * resepti.lisaaOsio(new Osio("Täyte"));
     * 
     * resepti.getOsiot().annaIndeksista(0).equals(new Osio("Pohja")) === true;
     * resepti.getOsiot().annaIndeksista(1).equals(new Osio("Täyte")) === true;
     * </pre>
     */
    public void lisaaOsio(Osio osio) {
        if (osio == null) return;
        this.osiot.lisaa(osio);
    }
    
    
    /**
     * Poistaa annetun osion, jos sellainen löytyy
     * 
     * @param osio poistettava osio
     */
    public void poistaOsio(Osio osio) {
        this.osiot.poista(osio);
    }


    /**
     * Vaihtaa reseptin hinnan.
     * Ei tee muutoksia jos hintaa ei löydy.
     * 
     * @example
     * <pre name="test">
     * Resepti mokkapalat = new Resepti("Mokkapalat");
     * mokkapalat.setHinta(2);
     * mokkapalat.getHinta() === 2;
     * </pre>
     * 
     * @param hinta mikä hinta vaihtoehto halutaan asettaa
     */
    public void setHinta(int hinta) {
        this.hinta.setValinta(hinta);
        this.muutettu = true;
    }
    
    
    /**
     * Asettaa valmistusajan, mikäli löytyy vaihtoehdoista.
     * 
     * @param valmistusaika mikä valmistusaika vaihtoehto halutaan asettaa
     */
    public void setValmistusaika(int valmistusaika) {
        this.valmistusaika.setValinta(valmistusaika);
        this.muutettu = true;
    }
    
    
    /**
     * Asettaa tähdet, mikäli löytyy vaihtoehdoista.
     * 
     * @param tahdet mikä tahdet vaihtoehto halutaan asettaa
     */
    public void setTahdet(int tahdet) {
        this.tahdet.setValinta(tahdet);
        this.muutettu = true;
    }
    
    
    /**
     * Asettaa vaativuuden, mikäli löytyy vaihtoehdoista.
     * 
     * @param vaativuus mikä vaativuus vaihtoehto halutaan asettaa
     */
    public void setVaativuus(int vaativuus) {
        this.vaativuus.setValinta(vaativuus);
        this.muutettu = true;
    }
    
    
    /**
     * Antaa reseptin hinnan.
     * 
     * @return reseptin hinta
     */
    public int getHinta() {
        return this.hinta.getValinta();
    }
    
    
    /**
     * Antaa reseptin hinnan vaihtoehto vastaavana tekstinä.
     * 
     * @return reseptin hinta tekstimuodossa
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti();
     * resepti.getHintaString() === "";
     * 
     * resepti.setHinta(0);
     * resepti.getHintaString() === "";
     * 
     * resepti.setHinta(1);
     * resepti.getHintaString() === "€";
     * 
     * resepti.setHinta(2);
     * resepti.getHintaString() === "€€";
     * 
     * resepti.setHinta(3);
     * resepti.getHintaString() === "€€€";
     * 
     * resepti.setHinta(4);
     * resepti.getHintaString() === "€€€";
     * 
     * resepti.setHinta(-1);
     * resepti.getHintaString() === "";
     * </pre>
     */
    public String getHintaString() {
        return this.hinta.getValintaString();
    }
    
    
    /**
     * Antaa reseptin valmistusajan.
     * 
     * @return reseptin valmistusaika
     */
    public int getValmistusaika() {
        return this.valmistusaika.getValinta();
    }
    
    
    /**
     * Antaa reseptin valmistusajan vastaavana tekstinä.
     * 
     * @return reseptin valmistusaika tekstinä
     */
    public String getValmistusaikaString() {
        return this.valmistusaika.getValintaString();
    }
    
    
    /**
     * Antaa reseptin tähdet.
     * 
     * @return reseptin tähdet
     */
    public int getTahdet() {
        return this.tahdet.getValinta();
    }
    
    
    /**
     * Antaa reseptin tähdet vastaavana tekstinä.
     * 
     * @return reseptin tähdet tekstinä
     */
    public String getTahdetString() {
        return this.tahdet.getValintaString();
    }
    
    
    /**
     * Antaa reseptin vaativuuden.
     * 
     * @return reseptin vaativuus
     */
    public int getVaativuus() {
        return this.vaativuus.getValinta();
    }
    
    
    /**
     * Antaa reseptin vaativuuden vastaavana tekstinä.
     * 
     * @return reseptin vaativuus tekstinä
     */
    public String getVaativuusString() {
        return this.vaativuus.getValintaString();
    }
    
    
    /**
     * Palauttaa reseptin tiedot mielekkäämmässä taulukkomuodossa.
     * Ei näytä reseptin tunnusta ja määrittelemättömät attribuutit vaihdetaan null-viitteestä
     * tyhjään merkkijonoon.
     * 
     * @return reseptin tiedot muodossa nimi|hinta|valmistusaika|tähdet|vaativuus
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti("Kakku");
     * resepti.getTaulukkoMuodossa() === "Kakku||||";
     * </pre>
     */
    public String getTaulukkoMuodossa() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nimi);
        sb.append('|');
        sb.append(getHintaString());
        sb.append('|');
        sb.append(getValmistusaikaString());
        sb.append('|');
        sb.append(getTahdetString());
        sb.append('|');
        sb.append(getVaativuusString());
        return sb.toString();
    }
    
    
    /**
     * Tarkistaa onko reseptin nimessä annettua merkkijonoa.
     * Kirjainten koolla ei ole väliä.
     * Poistaa tyhjät haettavan merkkijonon alusta ja lopusta.
     * 
     * @param merkkijono merkkijono jota etsitään nimestä
     * @return totuusarvo löytyykö merkkijonoa nimestä
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti("Juustokakku");
     * resepti.onkoNimessa("kakku") === true;
     * resepti.onkoNimessa("KAKKU") === true;
     * resepti.onkoNimessa("Juustokakku") === true;
     * resepti.onkoNimessa("   kakku ") === true;
     * resepti.onkoNimessa("Voileipäkakku") === false;
     * resepti.onkoNimessa("juusto kakku") === false;
     * resepti.onkoNimessa("") === true;
     * resepti.onkoNimessa(null) === true;
     * </pre>
     */
    public boolean onkoNimessa(String merkkijono) {
        if (merkkijono == null) return true;
        // luo regex lauseen joka etsii merkkijonon sisältävää merkkijonoa
        StringBuilder regexLause = new StringBuilder();
        regexLause.append(".*");
        regexLause.append(merkkijono.strip().toLowerCase());
        regexLause.append(".*");
        
        // vertaa regex lausetta pienellä kirjoitettuun nimeen
        return (this.nimi.toLowerCase().matches(regexLause.toString()));
    }
    
    
    /**
     * Kertoo onko reseptillä tallentamattomia muutoksia
     * 
     * @return onko tallentamattomia muutoksia
     */
    public boolean onkoTallentamattomiaMuutoksia() {
        if (this.osiot.onkoTallentamattomiaMuutoksia()) return true;
        return this.muutettu;
    }
    
    
    /**
     * Validoi voidaanko resepti tallentaa
     * 
     * @return virheteksti tai null jos voidaan tallentaa
     */
    public String voidaankoTallentaa() {
        // kysytään osioilta
        String osioVirhe = this.osiot.voidaankoTallentaa();
        if (osioVirhe != null) return osioVirhe;
        
        // onko nimi ok
        if (!rekisteroiNimi().equals(getNimi())) return "Reseptin nimeä ei voida tallentaa tai saman niminen resepti on jo olemassa!";
        
        return null;
    }
    
    
    /**
     * Lukee reseptin tiedot tiedostosta
     * 
     * @throws SailoException jos tietoja ei saada luettua
     */
    public void lueTiedostosta() throws SailoException {
        this.osiot.setTiedostoPolku(getAlihakemistoPolku());
        this.osiot.lueTiedostosta();
        this.muutettu = true;
    }
    
    
    /**
     * Tallentaa reseptin tiedot tiedostoon
     * 
     * @throws SailoException jos tallentaminen epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!onkoTallentamattomiaMuutoksia()) return;
        
        // tarkistestaan voidaanko tallentaa
        String virhe = voidaankoTallentaa();
        if (virhe != null) throw new SailoException("Ei voida tallentaa: " + virhe);
        
        // antaa reseptille uniikin tunnuksen jos ollaan tallentamassa
        if (this.reseptiId < 0) rekisteroiTunnus();
        
        File tiedosto = new File(this.tiedostopolku + this.tiedostonimi);
        File varmuuskopio = new File(this.tiedostopolku + MerkkijonoKasittely.vaihdaTiedostopaate(this.tiedostonimi, "bak"));
        
        // koitetaan poistaa edellistä varmuuskopiota
        // heitetään virhe jos sellainen on olemassa eikä voida poistaa
        if (!varmuuskopio.delete() && varmuuskopio.exists()) {
            throw new SailoException("Ei voida poistaa varmuuskopio-tiedostoa");
        }
        
        // koitetaan luoda tiedosto jos sellaista ei vielä ole
        if (!tiedosto.exists()) {
            try {
                tiedosto.createNewFile();
            } catch (IOException exception) {
                throw new SailoException("Ei voida luoda tallennus-tiedostoa");
            }
        }
        
        // koitetaan nimetä olemassaoleva tiedosto varmuuskopioksi
        if (!tiedosto.renameTo(varmuuskopio)) {
            throw new SailoException("Ei voida nimetä uudelleen tallennus-tiedostoa");
        }
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(tiedosto.getCanonicalPath()))) {
            
            try (Scanner fi = new Scanner(new FileInputStream(varmuuskopio))) {
                
                boolean omatTiedotKirjoitettu = false;
                
                while (fi.hasNext()) {
                    String rivi = fi.nextLine();
                    
                    // skipataan tyhjät ja kommenttirivit
                    if (rivi.isBlank() || rivi.charAt(0) == ';') continue;
                    
                    // parsii rivin reseptiId:n, jos ei löydy niin asettaa arvoksi varmasti eri kuin nykyinen reseptiId
                    StringBuilder riviTiedot = new StringBuilder(rivi);
                    int rivinReseptiId = Mjonot.erotaInt(riviTiedot, this.reseptiId - 1);
                    
                    // syöttää alkuperäisen rivin, jos ei ole sama resepti mitä ollaan tallentamassa
                    if (rivinReseptiId != this.reseptiId) {
                        fo.println(rivi);
                    } else {
                        // ylikirjoittaa tiedot aiempien päälle
                        tulostaOmatTiedot(fo);
                        omatTiedotKirjoitettu = true;
                    }
                }
                
                // jos tietoja ei ylikirjoitettu
                if (!omatTiedotKirjoitettu) {
                    tulostaOmatTiedot(fo);
                    omatTiedotKirjoitettu = true;
                }
            }
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
        } catch (IOException exception) {
            throw new SailoException("Tiedostoon \"" + this.tiedostopolku + this.tiedostonimi + "\" kirjoittamisessa ongelma");
        }
        
        // käskee osioita tallentamaan
        this.osiot.setTiedostoPolku(getAlihakemistoPolku());
        this.osiot.tallenna();
        this.muutettu = false;
    }
    
    
    /**
     * Tulostaa reseptin tiedot annettuun virtaan
     * 
     * @param fo kirjoittaja
     */
    public void tulostaOmatTiedot(PrintWriter fo) {
        fo.print(this.reseptiId);
        fo.print('|');
        fo.print(this.getNimi());
        fo.print('|');
        fo.print(this.getKuvaus());
        fo.print('|');
        fo.print(this.getHinta());
        fo.print('|');
        fo.print(this.getValmistusaika());
        fo.print('|');
        fo.print(this.getTahdet());
        fo.print('|');
        fo.print(this.getVaativuus());
        fo.println();
    }
    
    
    /**
     * Koittaa parsia reseptin tiedot rivistä
     * 
     * @param rivi mistä reseptin tiedot yritetään parsia
     * 
     * @example
     * Resepti resepti = new Resepti();
     * resepti.parse("1|Mustikkapiirakka|halpa ja maukas.|2|2|3|1");
     * resepti.toString().endsWith("|Mustikkapiirakka|2|2|3|1") === true;
     * resepti.getKuvaus() === "halpa ja maukas.";
     * 
     * resepti.parse("2|Lihapiirakka");
     * resepti.toString().endsWith("|Lihapiirakka|2|2|3|1") === true;
     * resepti.getKuvaus() === "halpa ja maukas.";
     * 
     * resepti.parse("");
     * resepti.toString().endsWith("|Lihapiirakka|2|2|3|1") === true;
     * 
     * resepti.parse("5|Kasvispiirakka 1 3 2 2");
     * resepti.toString().endsWith("5|Kasvispiirakka|2|2|3|1");
     */
    public void parse(String rivi) {
        if (rivi == null || rivi.length() < 1) return;
        
        StringBuilder sb = new StringBuilder(rivi);
        
        rekisteroiTunnus(Mjonot.erota(sb, '|', this.reseptiId));
        rekisteroiNimi(Mjonot.erota(sb, '|', this.rekisteroitynimi));
        setKuvaus(Mjonot.erota(sb, '|', getKuvaus()));
        setHinta(Mjonot.erota(sb, '|', getHinta()));
        setValmistusaika(Mjonot.erota(sb, '|', getValmistusaika()));
        setTahdet(Mjonot.erota(sb, '|', getTahdet()));
        setVaativuus(Mjonot.erota(sb, '|', getVaativuus()));
    }
    
    
    /**
     * Palauttaa täydellisen kopion nykyisestä reseptistä
     * 
     * @return kopio nykyisestä reseptistä
     * 
     * @example
     * <pre name="test">
     * Resepti pizza = new Resepti("Pizza");
     * pizza.setKuvaus("Itsetehdyllä tomaattikastikkeella.");
     * pizza.setHinta(3);
     * pizza.setVaativuus(4);
     * pizza.lisaaOsio(new Osio("Pizzapohja"));
     * pizza.lisaaOsio(new Osio("Tomaattikastike"));
     * 
     * pizza.toString() === "-1|Pizza|3|-1|-1|4";
     * 
     * Resepti kopioPizza = pizza.clone();
     * kopioPizza.toString() === "-1|Pizza|3|-1|-1|4";
     * 
     * kopioPizza.getOsiot().annaIndeksista(0).getNimi() === "Pizzapohja";
     * kopioPizza.getOsiot().annaIndeksista(1).getNimi() === "Tomaattikastike";
     * 
     * pizza.setHinta(1);
     * pizza.toString() === "-1|Pizza|1|-1|-1|4";
     * kopioPizza.toString() === "-1|Pizza|3|-1|-1|4";
     * 
     * kopioPizza.setTahdet(1);
     * pizza.toString() === "-1|Pizza|1|-1|-1|4";
     * kopioPizza.toString() === "-1|Pizza|3|-1|1|4";
     * </pre>
     */
    @Override
    public Resepti clone() {
        Resepti kopio = new Resepti();
        kopio.reseptiId = this.reseptiId;
        kopio.rekisteroitynimi = this.rekisteroitynimi;
        kopio.setUusiNimi(this.getNimi());
        kopio.setHinta(this.getHinta());
        kopio.setValmistusaika(this.getValmistusaika());
        kopio.setTahdet(this.getTahdet());
        kopio.setVaativuus(this.getVaativuus());
        kopio.setKuvaus(this.getKuvaus());
        
        // luodaan täysi kopio reseptin osioista
        kopio.osiot = this.osiot.clone();
        
        return kopio;
    }
    
    
    /**
     * Palauttaa ovatko oliot samoja
     * 
     * @param verrattava mihin olioon verrataan
     * @return onko verrattava olio sama kuin tämä olio
     * 
     * @example
     * <pre name="test">
     * Resepti pizza1 = new Resepti("Pizza");
     * pizza1.setKuvaus("Pizzaa.");
     * pizza1.setHinta(1);
     * pizza1.setTahdet(2);
     * 
     * Resepti pizza2 = new Resepti("Pizza2");
     * pizza1.equals(pizza2) === false;
     * 
     * pizza2.setKuvaus("Pizzaa.");
     * pizza2.setHinta(1);
     * pizza2.setTahdet(2);
     * pizza1.equals(pizza2) === false;
     * pizza2.equals(pizza1) === false;
     * 
     * pizza2.setUusiNimi("Pizza");
     * pizza1.equals(pizza2) === true;
     * pizza2.equals(pizza1) === true;
     * 
     * pizza1.setVaativuus(2);
     * pizza1.equals(pizza2) === false;
     * 
     * pizza2.setVaativuus(2);
     * pizza1.equals(pizza2) === true;
     * 
     * pizza2.lisaaOsio(new Osio("Pizzapohja"));
     * pizza1.equals(pizza2) === false;
     * 
     * pizza1.lisaaOsio(new Osio("Pizzapohja"));
     * pizza1.equals(pizza2) === true;
     * 
     * Resepti resepti1 = new Resepti();
     * resepti1.equals(new Resepti()) === true;
     * </pre>
     */
    @Override
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Resepti verrattavaResepti = (Resepti)verrattava;
        
        if (!this.getNimi().equals(verrattavaResepti.getNimi())) return false;
        if (this.getHinta() != verrattavaResepti.getHinta()) return false;
        if (this.getValmistusaika() != verrattavaResepti.getValmistusaika()) return false;
        if (this.getTahdet() != verrattavaResepti.getTahdet()) return false;
        if (this.getVaativuus() != verrattavaResepti.getVaativuus()) return false;
        if (!this.getKuvaus().equals(verrattavaResepti.getKuvaus())) return false;
        if (!this.osiot.equals(verrattavaResepti.osiot)) return false;
        
        return true;
    }
    
    
    @Override
    /**
     * Muodostaa hash-koodin Reseptistä
     * 
     * @example
     * <pre name="test">
     * Resepti mustikkapiirakka = new Resepti("Mustikkapiirakka");
     * Resepti piirakka = new Resepti("Piirakka");
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === false;
     * 
     * piirakka.setUusiNimi("Mustikkapiirakka");
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === true;
     * 
     * mustikkapiirakka.setHinta(2);
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === false;
     * 
     * piirakka.setHinta(2);
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === true;
     * 
     * mustikkapiirakka.setValmistusaika(2);
     * mustikkapiirakka.setTahdet(3);
     * mustikkapiirakka.setVaativuus(1);
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === false;
     * 
     * piirakka.setValmistusaika(2);
     * piirakka.setTahdet(3);
     * piirakka.setVaativuus(1);
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === true;
     * 
     * mustikkapiirakka.lisaaOsio(new Osio("Muropohja"));
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === false;
     * 
     * piirakka.lisaaOsio(new Osio("Muropohja"));
     * mustikkapiirakka.hashCode() == piirakka.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusObject(hash, this.nimi, this.kuvaus);
        hash = Hajautus.hajautusInt(hash, this.reseptiId, this.getHinta(), this.getValmistusaika(), this.getTahdet(), this.getVaativuus(), this.osiot.hashCode());
        return hash;
    }
    
    
    /**
     * Testaamista varten luo mustikkapiirakan reseptin.
     * 
     * TODO: poista kun ei enää tarvita
     * 
     * @example
     * <pre name="test">
     * Resepti mustikkapiirakka = new Resepti("");
     * mustikkapiirakka.luoMustikkapiirakka();
     * mustikkapiirakka.toString() === "-1|Mustikkapiirakka|2|2|3|1";
     * </pre>
     */
    public void luoMustikkapiirakka() {
        this.nimi = "Mustikkapiirakka";
        setHinta(2);
        setValmistusaika(2);
        setTahdet(3);
        setVaativuus(1);
        this.kuvaus = "Halpa ja maukas.";
        this.osiot.luoMustikkapiirakanOsiot();
    }
    
    
    /**
     * Arpoo attribuutit id, hinta, valmistusaika, tähdet ja vaativuus reseptille testaamista varten.
     * TODO: poista kun ei enää tarvita
     */
    public void satunnaisetAttribuutit() {
        rekisteroiTunnus();
        
        setHinta(Satunnaisluku.rand(1, 3));
        setValmistusaika(Satunnaisluku.rand(1, 5));
        setTahdet(Satunnaisluku.rand(1, 5));
        setVaativuus(Satunnaisluku.rand(1, 5));
    }
    
    
    @Override
    /**
     * Reseptin tiedot muodossa "resepti id|nimi|hinta|valmistusaika|tähdet|vaativuus"
     * 
     * @example
     * <pre name="test">
     * Resepti lihapiirakka = new Resepti("Lihapiirakka");
     * lihapiirakka.toString() === "-1|Lihapiirakka|-1|-1|-1|-1";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.reseptiId);
        sb.append('|');
        sb.append(getNimi());
        sb.append('|');
        sb.append(getHinta());
        sb.append('|');
        sb.append(getValmistusaika());
        sb.append('|');
        sb.append(getTahdet());
        sb.append('|');
        sb.append(getVaativuus());
        return sb.toString();
    }
    
    
    /**
     * Testaamista varten tehty pääohjelma.
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Resepti lihapiirakka = new Resepti("Lihapiirakka");
        lihapiirakka.setTahdet(2);
        lihapiirakka.setKuvaus("Helppo ja hyvä");
        System.out.println(lihapiirakka);
        System.out.println(lihapiirakka.getKuvaus());
        
        Resepti mustikkapiirakka = new Resepti("");
        mustikkapiirakka.luoMustikkapiirakka();
        System.out.println(mustikkapiirakka);
        System.out.println(mustikkapiirakka.getKuvaus());
        
        System.out.println(mustikkapiirakka.rekisteroiTunnus());
        System.out.println(mustikkapiirakka);
        
        lihapiirakka.rekisteroiTunnus();
        System.out.println(lihapiirakka);
        
        try {
            lihapiirakka.tallenna();
        } catch (SailoException e) {
            System.out.println(e.getMessage());
        }
    }
}
