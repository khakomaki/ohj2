package reseptihaku;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Resepti {
    
    private final String oletusNimi = "Reseptin nimi";
    
    private Osiot osiot;
    private int reseptiId;
    private String nimi;
    private String kuvaus;
    private Suodatin hintaSuodatin;
    private Suodatin valmistusaikaSuodatin;
    private Suodatin tahdetSuodatin;
    private Suodatin vaativuusSuodatin;
    private int hinta;
    private int valmistusaika;
    private int tahdet;
    private int vaativuus;
    
    
    /**
     * Resepti.
     * Sisältää reseptin perustiedot sekä viitteet reseptin sisältämiin ainesosiin ja ohjeisiin.
     * 
     * Alustaa hinnan, valmistusajan, tähdet ja vaativuuden -1. Kuvaus alustuu tyhjänä merkkijonona.
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
        this.kuvaus = "";
        this.hinta = -1;
        this.valmistusaika = -1;
        this.tahdet = -1;
        this.vaativuus = -1;
        this.hintaSuodatin = new Suodatin("Hinta");
        this.valmistusaikaSuodatin = new Suodatin("Valmistusaika");
        this.tahdetSuodatin = new Suodatin("Tähdet");
        this.vaativuusSuodatin = new Suodatin("Vaativuus");
        setNimi(nimi);
        luoOsiot();
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
     * Luo reseptille Osiot
     */
    public void luoOsiot() {
        this.osiot = new Osiot();
    }
    
    
    /**
     * @param kuvaus reseptin kuvaus
     */
    public void setKuvaus(String kuvaus) {
        if (kuvaus == null) { return; }
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
     * @param hintaSuodatin mihin hinta-Suodattimeen vaihdetaan
     */
    public void setHintaSuodatin(Suodatin hintaSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (hintaSuodatin == null) { return; }
        this.hintaSuodatin = hintaSuodatin;
    }
    
    
    /**
     * @param valmistusaikaSuodatin mihin valmistusaika-Suodattimeen vaihdetaan
     */
    public void setValmistusaikaSuodatin(Suodatin valmistusaikaSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (valmistusaikaSuodatin == null) { return; }
        this.valmistusaikaSuodatin = valmistusaikaSuodatin;
    }
    
    
    /**
     * @param tahdetSuodatin mihin tähdet-Suodattimeen vaihdetaan
     */
    public void setTahdetSuodatin(Suodatin tahdetSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (tahdetSuodatin == null) { return; }
        this.tahdetSuodatin = tahdetSuodatin;
    }
    
    
    /**
     * @param vaativuusSuodatin mihin vaativuus-Suodattimeen vaihdetaan
     */
    public void setVaativuusSuodatin(Suodatin vaativuusSuodatin) {
        // ei tehdä mitään jos annettu suodatin on null
        if (vaativuusSuodatin == null) { return; }
        this.vaativuusSuodatin = vaativuusSuodatin;
    }
    
    
    /**
     * @param hinta mikä hinta-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setHinta(int hinta) {
        if (hinta == -1) { this.hinta = -1; }
        if (this.hintaSuodatin.onkoOlemassa(hinta)) { this.hinta = hinta; }
    }
    
    
    /**
     * @param valmistusaika mikä valmistusaika-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setValmistusaika(int valmistusaika) {
        if (valmistusaika == -1) { this.valmistusaika = -1; }
        if (this.valmistusaikaSuodatin.onkoOlemassa(valmistusaika)) { this.valmistusaika = valmistusaika; }
    }
    
    
    /**
     * @param tahdet mikä tahdet-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setTahdet(int tahdet) {
        if (tahdet == -1) { this.tahdet = -1; }
        if (this.tahdetSuodatin.onkoOlemassa(tahdet)) { this.tahdet = tahdet; }
    }
    
    
    /**
     * @param vaativuus mikä vaativuus-Suodattimen vaihtoehto halutaan asettaa
     */
    public void setVaativuus(int vaativuus) {
        if (vaativuus == -1) { this.vaativuus = -1; }
        if (this.vaativuusSuodatin.onkoOlemassa(vaativuus)) { this.vaativuus = vaativuus; }
    }
    
    
    /**
     * @return reseptin hinta
     */
    public int getHinta() {
        return this.hinta;
    }
    
    
    /**
     * @return reseptin hinta suodattimen muotoiltuna
     */
    public String getHintaString() {
        return this.hintaSuodatin.getVastaavaVaihtoehto(this.hinta);
    }
    
    
    /**
     * @return reseptin valmistusaika
     */
    public int getValmistusaika() {
        return this.valmistusaika;
    }
    
    
    /**
     * @return reseptin valmistusaika suodattimen muotoiltuna
     */
    public String getValmistusaikaString() {
        return this.valmistusaikaSuodatin.getVastaavaVaihtoehto(this.valmistusaika);
    }
    
    
    /**
     * @return reseptin tähdet
     */
    public int getTahdet() {
        return this.tahdet;
    }
    
    
    /**
     * @return reseptin tähdet suodattimen muotoiltuna
     */
    public String getTahdetString() {
        return this.tahdetSuodatin.getVastaavaVaihtoehto(this.tahdet);
    }
    
    
    /**
     * @return reseptin vaativuus
     */
    public int getVaativuus() {
        return vaativuus;
    }
    
    
    /**
     * @return reseptin vaativuus suodattimen muotoiltuna
     */
    public String getVaativuusString() {
        return this.vaativuusSuodatin.getVastaavaVaihtoehto(this.vaativuus);
    }
    
    
    /**
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
