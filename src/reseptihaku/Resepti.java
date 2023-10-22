package reseptihaku;

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
    private Suodatin hintaSuodatin =            new Suodatin("Hinta");;
    private Suodatin valmistusaikaSuodatin =    new Suodatin("Valmistusaika");
    private Suodatin tahdetSuodatin =           new Suodatin("Tähdet");
    private Suodatin vaativuusSuodatin =        new Suodatin("Vaativuus");
    private String kuvaus =                     "";
    private int hinta =                         -1;
    private int valmistusaika =                 -1;
    private int tahdet =                        -1;
    private int vaativuus =                     -1;
    
    
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
     * Asettaa hintoja käsittelevän suodattimen.
     * Ennen asettamista ei anna muuttaa Reseptin hintaa.
     * 
     * @param hintaSuodatin mihin hinta-Suodattimeen vaihdetaan
     * 
     * @example
     * <pre name="test">
     * Suodatin hintaSuodatin = new Suodatin("Hinta");
     * hintaSuodatin.lisaaVaihtoehto(2, "€€");
     * Resepti mokkapalat = new Resepti(2, "Mokkapalat");
     * 
     * mokkapalat.setHinta(2);
     * mokkapalat.getHinta() === -1;
     * 
     * mokkapalat.setHintaSuodatin(hintaSuodatin);
     * mokkapalat.setHinta(2);
     * mokkapalat.getHinta() === 2;
     * 
     * mokkapalat.setHinta(3);
     * mokkapalat.getHinta() === 2;
     * 
     * mokkapalat.setHinta(-1);
     * mokkapalat.getHinta() === -1;
     * </pre>
     */
    public void setHintaSuodatin(Suodatin hintaSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (hintaSuodatin == null) { return; }
        this.hintaSuodatin = hintaSuodatin;
    }
    
    
    /**
     * Asettaa valmistusaikaa käsittelevän suodattimen.
     * 
     * @param valmistusaikaSuodatin mihin valmistusaika-Suodattimeen vaihdetaan
     */
    public void setValmistusaikaSuodatin(Suodatin valmistusaikaSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (valmistusaikaSuodatin == null) { return; }
        this.valmistusaikaSuodatin = valmistusaikaSuodatin;
    }
    
    
    /**
     * Asettaa tähtiä käsittelevän suodattimen.
     * 
     * @param tahdetSuodatin mihin tähdet-Suodattimeen vaihdetaan
     */
    public void setTahdetSuodatin(Suodatin tahdetSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (tahdetSuodatin == null) { return; }
        this.tahdetSuodatin = tahdetSuodatin;
    }
    
    
    /**
     * Asettaa vaativuutta käsittelevän suodattimen.
     * 
     * @param vaativuusSuodatin mihin vaativuus-Suodattimeen vaihdetaan
     */
    public void setVaativuusSuodatin(Suodatin vaativuusSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (vaativuusSuodatin == null) { return; }
        this.vaativuusSuodatin = vaativuusSuodatin;
    }
    
    
    /**
     * Vaihtaa reseptin hinnan.
     * Reseptille on pitänyt asettaa suodatin ja annetun hinnan pitää löytyä asetetusta suodattimesta.
     * Ei tee muutoksia jos hintaa ei löydy.
     * 
     * Suodatin hintaSuodatin = new Suodatin("Hinta");
     * hintaSuodatin.lisaaVaihtoehto(2, "€€");
     * Resepti mokkapalat = new Resepti(2, "Mokkapalat");
     * 
     * mokkapalat.setHinta(2);
     * mokkapalat.getHinta() === -1;
     * 
     * mokkapalat.setHintaSuodatin(hintaSuodatin);
     * mokkapalat.setHinta(2);
     * mokkapalat.getHinta() === 2;
     * 
     * @param hinta mikä hinta-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setHinta(int hinta) {
        if (hinta == -1) { this.hinta = -1; }
        if (this.hintaSuodatin.onkoOlemassa(hinta)) { this.hinta = hinta; }
    }
    
    
    /**
     * Asettaa valmistusajan, mikäli löytyy suodattimesta.
     * 
     * @param valmistusaika mikä valmistusaika-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setValmistusaika(int valmistusaika) {
        if (valmistusaika == -1) { this.valmistusaika = -1; }
        if (this.valmistusaikaSuodatin.onkoOlemassa(valmistusaika)) { this.valmistusaika = valmistusaika; }
    }
    
    
    /**
     * Asettaa tähdet, mikäli löytyy suodattimesta.
     * 
     * @param tahdet mikä tahdet-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setTahdet(int tahdet) {
        if (tahdet == -1) { this.tahdet = -1; }
        if (this.tahdetSuodatin.onkoOlemassa(tahdet)) { this.tahdet = tahdet; }
    }
    
    
    /**
     * Asettaa vaativuuden, mikäli löytyy suodattimesta.
     * 
     * @param vaativuus mikä vaativuus-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setVaativuus(int vaativuus) {
        if (vaativuus == -1) { this.vaativuus = -1; }
        if (this.vaativuusSuodatin.onkoOlemassa(vaativuus)) { this.vaativuus = vaativuus; }
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
     * Antaa reseptin hinnan suodattimen vastaavana tekstinä.
     * 
     * @return reseptin hinta suodattimen muotoiltuna
     * 
     * @example
     * <pre name="test">
     * Resepti resepti = new Resepti();
     * Suodatin hintaSuodatin = new Suodatin("Hinta");
     * hintaSuodatin.lisaaVaihtoehto(1, "€");
     * hintaSuodatin.lisaaVaihtoehto(2, "€€");
     * hintaSuodatin.lisaaVaihtoehto(3, "€€€");
     * resepti.setHintaSuodatin(hintaSuodatin);
     * 
     * resepti.getHintaString() === null;
     * 
     * resepti.setHinta(2);
     * 
     * resepti.getHintaString() === "€€";
     * </pre>
     */
    public String getHintaString() {
        return this.hintaSuodatin.getVastaavaVaihtoehto(this.hinta);
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
     * Antaa reseptin valmistusajan suodattimen vastaavana tekstinä.
     * 
     * @return reseptin valmistusaika suodattimen muotoiltuna
     */
    public String getValmistusaikaString() {
        return this.valmistusaikaSuodatin.getVastaavaVaihtoehto(this.valmistusaika);
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
     * Antaa reseptin tähdet suodattimen vastaavana tekstinä.
     * 
     * @return reseptin tähdet suodattimen muotoiltuna
     */
    public String getTahdetString() {
        return this.tahdetSuodatin.getVastaavaVaihtoehto(this.tahdet);
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
     * Antaa reseptin vaativuuden suodattimen vastaavana tekstinä.
     * 
     * @return reseptin vaativuus suodattimen muotoiltuna
     */
    public String getVaativuusString() {
        return this.vaativuusSuodatin.getVastaavaVaihtoehto(this.vaativuus);
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
        sb.append(vaihdaNull(this.hintaSuodatin.getVastaavaVaihtoehto(this.hinta)));
        sb.append('|');
        sb.append(vaihdaNull(this.valmistusaikaSuodatin.getVastaavaVaihtoehto(this.valmistusaika)));
        sb.append('|');
        sb.append(vaihdaNull(this.tahdetSuodatin.getVastaavaVaihtoehto(this.tahdet)));
        sb.append('|');
        sb.append(vaihdaNull(this.vaativuusSuodatin.getVastaavaVaihtoehto(this.vaativuus)));
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
        Suodatin tahdetSuodatin = new Suodatin("Tähdet");
        tahdetSuodatin.luoVaihtoehdot(new String[]{ "☆", "☆☆", "☆☆☆", "☆☆☆☆", "☆☆☆☆☆" });
        lihapiirakka.setTahdetSuodatin(tahdetSuodatin);
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
