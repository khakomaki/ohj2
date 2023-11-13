package reseptihaku;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kanta.Hajautus;
import kanta.Satunnaisluku;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Resepti {
    
    private final String oletusNimi = "Reseptin nimi";
    
    private int reseptiId =                     -1;
    private String nimi =                       "";
    private Osiot osiot =                       null;
    private String kuvaus =                     "";
    private int hinta =                         -1;
    private int valmistusaika =                 -1;
    private int tahdet =                        -1;
    private int vaativuus =                     -1;
    private Ainesosat ainesosat =               null;
    
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
     * Luo reseptin.
     * Sisältää reseptin perustiedot sekä viitteet reseptin sisältämiin ainesosiin ja ohjeisiin.
     * 
     * @param id reseptin tunnus
     * @param nimi reseptin nimi
     * 
     * @example
     * <pre name="test">
     * Resepti makaronilaatikko = new Resepti(15, "Makaronilaatikko");
     * makaronilaatikko.toString() === "15|Makaronilaatikko|-1|-1|-1|-1";
     * </pre>
     */
    public Resepti(int id, String nimi) {
        this.reseptiId = id;
        setNimi(nimi);
        this.ainesosat = new Ainesosat();
        this.osiot = new Osiot(this.ainesosat);
    }
    
    
    /**
     * Luo Reseptin.
     * Reseptin tunnus alustuu -1 ja nimi oletukseksi.
     * 
     * @param ainesosat asetettava ainesosien hallitsija
     */
    public Resepti(Ainesosat ainesosat) {
        this.reseptiId = -1;
        setNimi(null);
        setAinesosat(ainesosat);
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
        this.reseptiId = -1;
        setNimi(nimi);
        this.ainesosat = new Ainesosat();
        this.osiot = new Osiot();
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
        this.reseptiId = -1;
        setNimi(null);
        this.ainesosat = new Ainesosat();
        this.osiot = new Osiot(this.ainesosat);
    }
    
    
    /**
     * Asettaa reseptin nimen ensimmäistä kertaa.
     * Jos syötetään tyhjä merkkijono tai null, antaa oletusnimen.
     * 
     * @param nimi reseptin nimi
     * 
     * @example
     * <pre name="test">
     * Resepti pizza = new Resepti(3, "Pizza");
     * pizza.toString() === "3|Pizza|-1|-1|-1|-1";
     * 
     * pizza = new Resepti(3, "");
     * pizza.toString() === "3|Reseptin nimi|-1|-1|-1|-1";
     * 
     * pizza = new Resepti(3, "Itsetehty pizza");
     * pizza.toString() === "3|Itsetehty pizza|-1|-1|-1|-1";
     * 
     * pizza = new Resepti(3, null);
     * pizza.toString() === "3|Reseptin nimi|-1|-1|-1|-1";
     * </pre>
     */
    private void setNimi(String nimi) {
        // asettaa oletusnimen jos annettu nimi on null tai tyhjä merkkijono
        if (nimi == null) { this.nimi = oletusNimi; return; }
        if (nimi.length() < 1) { this.nimi = oletusNimi; return; }
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
     * Resepti pizza = new Resepti(3, "Pizza");
     * pizza.toString() === "3|Pizza|-1|-1|-1|-1";
     * 
     * pizza.setUusiNimi("");
     * pizza.toString() === "3|Pizza|-1|-1|-1|-1";
     * 
     * pizza.setUusiNimi("Itsetehty pizza");
     * pizza.toString() === "3|Itsetehty pizza|-1|-1|-1|-1";
     * 
     * pizza.setUusiNimi(null);
     * pizza.toString() === "3|Itsetehty pizza|-1|-1|-1|-1";
     * </pre>
     */
    public void setUusiNimi(String nimi) {
        // ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
        if (nimi == null) { return; }
        if (nimi.length() < 1) { return; }
        this.nimi = nimi;
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
        if (kuvaus == null) { this.kuvaus = ""; return; }
        this.kuvaus = kuvaus;
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
     * Asettaa reseptin tunnuksen
     * 
     * @param tunnus asetettava tunnus
     * 
     * @example
     * <pre name="test">
     * Resepti maksalaatikko = new Resepti("Maksalaatikko");
     * maksalaatikko.getTunnus() === -1;
     * 
     * maksalaatikko.setTunnus(1);
     * maksalaatikko.getTunnus() === 1;
     * 
     * maksalaatikko.setTunnus(-500);
     * maksalaatikko.getTunnus() === -500;
     * 
     * maksalaatikko.setTunnus(250);
     * maksalaatikko.getTunnus() === 250;
     * </pre>
     */
    public void setTunnus(int tunnus) {
        this.reseptiId = tunnus;
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
     * 
     * resepti = new Resepti(1, "Mustikkapiirakka");
     * resepti.getTunnus() === 1;
     * </pre>
     */
    public int getTunnus() {
        return this.reseptiId;
    }
    
    
    /**
     * Asettaa viitteen luokkaan joka hallitsee olemassa olevia ainesosia.
     * Ei hyväksy null-viitettä.
     * 
     * @param ainesosat asetettavat ainesosat
     */
    public void setAinesosat(Ainesosat ainesosat) {
        if (ainesosat == null) { return; }
        this.ainesosat = ainesosat;
        this.osiot.setAinesosat(ainesosat);
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
        if (osio == null) { return; }
        this.osiot.lisaaOsio(osio);
    }
    
    
    /**
     * Poistaa annetun osion, jos sellainen löytyy
     * 
     * @param osio poistettava osio
     */
    public void poistaOsio(Osio osio) {
        this.osiot.poistaOsio(osio);
    }


    /**
     * Vaihtaa reseptin hinnan.
     * Ei tee muutoksia jos hintaa ei löydy.
     * 
     * @example
     * <pre name="test">
     * Resepti mokkapalat = new Resepti(2, "Mokkapalat");
     * mokkapalat.setHinta(2);
     * mokkapalat.getHinta() === 2;
     * </pre>
     * 
     * @param hinta mikä hinta vaihtoehto halutaan asettaa
     */
    public void setHinta(int hinta) {
        if (hinta == -1) { this.hinta = -1; }
        if (Resepti.hintaVaihtoehdot.containsKey(hinta)) { this.hinta = hinta; }
    }
    
    
    /**
     * Asettaa valmistusajan, mikäli löytyy vaihtoehdoista.
     * 
     * @param valmistusaika mikä valmistusaika vaihtoehto halutaan asettaa
     */
    public void setValmistusaika(int valmistusaika) {
        if (valmistusaika == -1) { this.valmistusaika = -1; }
        if (Resepti.valmistusaikaVaihtoehdot.containsKey(valmistusaika)) { this.valmistusaika = valmistusaika; }
    }
    
    
    /**
     * Asettaa tähdet, mikäli löytyy vaihtoehdoista.
     * 
     * @param tahdet mikä tahdet vaihtoehto halutaan asettaa
     */
    public void setTahdet(int tahdet) {
        if (tahdet == -1) { this.tahdet = -1; }
        if (Resepti.tahdetVaihtoehdot.containsKey(tahdet)) { this.tahdet = tahdet; }
    }
    
    
    /**
     * Asettaa vaativuuden, mikäli löytyy vaihtoehdoista.
     * 
     * @param vaativuus mikä vaativuus vaihtoehto halutaan asettaa
     */
    public void setVaativuus(int vaativuus) {
        if (vaativuus == -1) { this.vaativuus = -1; }
        if (Resepti.vaativuusVaihtoehdot.containsKey(vaativuus)) { this.vaativuus = vaativuus; }
    }
    
    
    /**
     * Antaa reseptin hinnan.
     * 
     * @return reseptin hinta
     */
    public int getHinta() {
        return this.hinta;
    }
    
    
    /**
     * Antaa reseptin hinnan vaihtoehto vastaavana tekstinä.
     * 
     * @return reseptin hinta tekstimuodossa
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti();
     * resepti.getHintaString() === null;
     * 
     * resepti.setHinta(0);
     * resepti.getHintaString() === null;
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
     * resepti.getHintaString() === null;
     * </pre>
     */
    public String getHintaString() {
        return Resepti.hintaVaihtoehdot.get(this.hinta);
    }
    
    
    /**
     * Antaa reseptin valmistusajan.
     * 
     * @return reseptin valmistusaika
     */
    public int getValmistusaika() {
        return this.valmistusaika;
    }
    
    
    /**
     * Antaa reseptin valmistusajan vastaavana tekstinä.
     * 
     * @return reseptin valmistusaika tekstinä
     */
    public String getValmistusaikaString() {
        return Resepti.valmistusaikaVaihtoehdot.get(this.valmistusaika);
    }
    
    
    /**
     * Antaa reseptin tähdet.
     * 
     * @return reseptin tähdet
     */
    public int getTahdet() {
        return this.tahdet;
    }
    
    
    /**
     * Antaa reseptin tähdet vastaavana tekstinä.
     * 
     * @return reseptin tähdet tekstinä
     */
    public String getTahdetString() {
        return Resepti.tahdetVaihtoehdot.get(this.tahdet);
    }
    
    
    /**
     * Antaa reseptin vaativuuden.
     * 
     * @return reseptin vaativuus
     */
    public int getVaativuus() {
        return vaativuus;
    }
    
    
    /**
     * Antaa reseptin vaativuuden vastaavana tekstinä.
     * 
     * @return reseptin vaativuus tekstinä
     */
    public String getVaativuusString() {
        return Resepti.vaativuusVaihtoehdot.get(this.vaativuus);
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
     * Resepti resepti = new Resepti(15, "Kakku");
     * resepti.getTaulukkoMuodossa() === "Kakku||||";
     * </pre>
     */
    public String getTaulukkoMuodossa() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nimi);
        sb.append('|');
        sb.append(vaihdaNull(Resepti.hintaVaihtoehdot.get(this.hinta)));
        sb.append('|');
        sb.append(vaihdaNull(Resepti.valmistusaikaVaihtoehdot.get(this.valmistusaika)));
        sb.append('|');
        sb.append(vaihdaNull(Resepti.tahdetVaihtoehdot.get(this.tahdet)));
        sb.append('|');
        sb.append(vaihdaNull(Resepti.vaativuusVaihtoehdot.get(this.vaativuus)));
        return sb.toString();
    }
    
    
    private String vaihdaNull(String s) {
        if (s == null ) { return ""; }
        return s;
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
     * Resepti resepti = new Resepti(1, "Juustokakku");
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
        if (merkkijono == null) { return true; }
        // luo regex lauseen joka etsii merkkijonon sisältävää merkkijonoa
        StringBuilder regexLause = new StringBuilder();
        regexLause.append(".*");
        regexLause.append(merkkijono.strip().toLowerCase());
        regexLause.append(".*");
        
        // vertaa regex lausetta pienellä kirjoitettuun nimeen
        return (this.nimi.toLowerCase().matches(regexLause.toString()));
    }
    
    
    /**
     * Palauttaa täydellisen kopion nykyisestä reseptistä
     * 
     * @return kopio nykyisestä reseptistä
     * 
     * @example
     * <pre name="test">
     * Resepti pizza = new Resepti(20, "Pizza");
     * pizza.setKuvaus("Itsetehdyllä tomaattikastikkeella.");
     * pizza.setHinta(3);
     * pizza.setVaativuus(4);
     * pizza.lisaaOsio(new Osio("Pizzapohja"));
     * pizza.lisaaOsio(new Osio("Tomaattikastike"));
     * 
     * pizza.toString() === "20|Pizza|3|-1|-1|4";
     * 
     * Resepti kopioPizza = pizza.clone();
     * kopioPizza.toString() === "20|Pizza|3|-1|-1|4";
     * 
     * kopioPizza.getOsiot().annaIndeksista(0).getNimi() === "Pizzapohja";
     * kopioPizza.getOsiot().annaIndeksista(1).getNimi() === "Tomaattikastike";
     * 
     * pizza.setHinta(1);
     * pizza.toString() === "20|Pizza|1|-1|-1|4";
     * kopioPizza.toString() === "20|Pizza|3|-1|-1|4";
     * 
     * kopioPizza.setTahdet(1);
     * pizza.toString() === "20|Pizza|1|-1|-1|4";
     * kopioPizza.toString() === "20|Pizza|3|-1|1|4";
     * </pre>
     */
    @Override
    public Resepti clone() {
        Resepti kopio = new Resepti();
        kopio.reseptiId = this.reseptiId;
        kopio.nimi = this.nimi;
        kopio.hinta = this.hinta;
        kopio.valmistusaika = this.valmistusaika;
        kopio.tahdet = this.tahdet;
        kopio.kuvaus = this.kuvaus;
        kopio.vaativuus = this.vaativuus;
        
        // annetaan viite samaan ainesosien hallitsijaan kopioinnin sijasta
        kopio.ainesosat = this.ainesosat;
        
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
     * Resepti pizza1 = new Resepti(12, "Pizza");
     * pizza1.setKuvaus("Pizzaa.");
     * pizza1.setHinta(1);
     * pizza1.setTahdet(2);
     * 
     * Resepti pizza2 = new Resepti(12, "Pizza2");
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
        if (verrattava == null) { return false; }
        if (verrattava.getClass() != this.getClass()) { return false; }
        Resepti verrattavaResepti = (Resepti)verrattava;
        
        if (!this.nimi.equals(verrattavaResepti.getNimi())) { return false; }
        if (this.hinta != verrattavaResepti.hinta) { return false; }
        if (this.valmistusaika != verrattavaResepti.valmistusaika) { return false; }
        if (this.tahdet != verrattavaResepti.tahdet) { return false; }
        if (this.vaativuus != verrattavaResepti.vaativuus) { return false; }
        if (!this.kuvaus.equals(verrattavaResepti.getKuvaus())) { return false; }
        if (!this.osiot.equals(verrattavaResepti.osiot)) { return false; }
        if (!this.ainesosat.equals(verrattavaResepti.ainesosat)) { return false; }
        
        return true;
    }
    
    
    @Override
    /**
     * Muodostaa hash-koodin Reseptistä
     * 
     * @example
     * <pre name="test">
     * Resepti mustikkapiirakka = new Resepti(2, "Mustikkapiirakka");
     * Resepti piirakka = new Resepti(2, "Piirakka");
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
        hash = Hajautus.hajautusString(hash, this.nimi);
        hash = Hajautus.hajautusString(hash, this.kuvaus);
        hash = Hajautus.hajautusInt(hash, this.reseptiId);
        hash = Hajautus.hajautusInt(hash, this.hinta);
        hash = Hajautus.hajautusInt(hash, this.valmistusaika);
        hash = Hajautus.hajautusInt(hash, this.tahdet);;
        hash = Hajautus.hajautusInt(hash, this.vaativuus);
        hash = Hajautus.hajautusInt(hash, this.osiot.hashCode());
        hash = Hajautus.hajautusInt(hash, this.ainesosat.hashCode());
        return hash;
    }
    
    
    /**
     * Testaamista varten luo mustikkapiirakan reseptin.
     * 
     * TODO: poista kun ei enää tarvita
     * @param id mustikkapiirakan id
     * 
     * @example
     * <pre name="test">
     * Resepti mustikkapiirakka = new Resepti(1, "");
     * mustikkapiirakka.luoMustikkapiirakka(5);
     * mustikkapiirakka.toString() === "5|Mustikkapiirakka|2|2|3|1";
     * </pre>
     */
    public void luoMustikkapiirakka(int id) {
        this.reseptiId = id;
        this.nimi = "Mustikkapiirakka";
        this.hinta = 2;
        this.valmistusaika = 2;
        this.tahdet = 3;
        this.vaativuus = 1;
        this.kuvaus = "Halpa ja maukas.";
        this.osiot.luoMustikkapiirakanOsiot();
    }
    
    
    /**
     * Arpoo attribuutit id, hinta, valmistusaika, tähdet ja vaativuus reseptille testaamista varten.
     * TODO: poista kun ei enää tarvita
     */
    public void satunnaisetAttribuutit() {
        this.reseptiId = Satunnaisluku.rand(1, 999);
        
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
     * Resepti lihapiirakka = new Resepti(2, "Lihapiirakka");
     * lihapiirakka.toString() === "2|Lihapiirakka|-1|-1|-1|-1";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.reseptiId);
        sb.append('|');
        sb.append(this.nimi);
        sb.append('|');
        sb.append(this.hinta);
        sb.append('|');
        sb.append(this.valmistusaika);
        sb.append('|');
        sb.append(this.tahdet);
        sb.append('|');
        sb.append(this.vaativuus);
        return sb.toString();
    }


    /**
     * @return kaikki mahdolliset hintavaihtoehdot
     */
    public static String getHintaVaihtoehdot() {
        return getVaihtoehdot(hintaVaihtoehdot);
    }
    
    
    /**
     * @return kaikki mahdolliset valmistusaikavaihtoehdot
     */
    public static String getValmistusaikaVaihtoehdot() {
        return getVaihtoehdot(valmistusaikaVaihtoehdot);
    }
    
    
    /**
     * @return kaikki mahdolliset tahdetvaihtoehdot
     */
    public static String getTahdetVaihtoehdot() {
        return getVaihtoehdot(tahdetVaihtoehdot);
    }
    
    
    /**
     * @return kaikki mahdolliset vaativuusvaihtoehdot
     */
    public static String getVaativuusVaihtoehdot() {
        return getVaihtoehdot(vaativuusVaihtoehdot);
    }
    
    
    private static String getVaihtoehdot(Map<Integer, String> vaihtoehdotMap) {
        StringBuilder sb = new StringBuilder();
        
        // lisää vaihtoehdot riviväleillä erotettuna StringBuilderiin
        for (String vaihtoehto : vaihtoehdotMap.values()) {
            sb.append(vaihtoehto);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    
    /**
     * Testaamista varten tehty pääohjelma.
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Resepti lihapiirakka = new Resepti(1, "Lihapiirakka");
        lihapiirakka.setTahdet(2);
        lihapiirakka.setKuvaus("Helppo ja hyvä");
        System.out.println(lihapiirakka);
        System.out.println(lihapiirakka.getKuvaus());
        
        Resepti mustikkapiirakka = new Resepti(1, "");
        mustikkapiirakka.luoMustikkapiirakka(1);
        System.out.println(mustikkapiirakka);
        System.out.println(mustikkapiirakka.getKuvaus());
    }
}
