package reseptihaku;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kanta.Satunnaisluku;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Resepti {
    
    private final String oletusNimi = "Reseptin nimi";
    
    private int reseptiId;
    private String nimi;
    private Osiot osiot =                       new Osiot();
    private String kuvaus =                     "";
    private int hinta =                         -1;
    private int valmistusaika =                 -1;
    private int tahdet =                        -1;
    private int vaativuus =                     -1;
    
    private static final Map<Integer, String> hintaVaihtoehdot;
    private static final Map<Integer, String> valmistusaikaVaihtoehdot;
    private static final Map<Integer, String> tahdetVaihtoehdot;
    private static final Map<Integer, String> vaativuusVaihtoehdot;
    
     /* alustetaan HashMapit attribuuttien vaihtoehdoista ensimmäisellä kertaa
     * ei luoda erikseen joka oliolle
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
    }
    
    
    /**
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
     * @return reseptin nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
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
     * @return reseptin kuvaus
     */
    public String getKuvaus() {
        return this.kuvaus;
    }
    
    
    /**
     * @return reseptin osiot
     */
    public Osiot getOsiot() {
         return this.osiot;
    }
    
    
    /**
     * Vaihtaa reseptin hinnan.
     * Ei tee muutoksia jos hintaa ei löydy.
     * 
     * Resepti mokkapalat = new Resepti(2, "Mokkapalat");
     * 
     * @example
     * <pre name="test">
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
