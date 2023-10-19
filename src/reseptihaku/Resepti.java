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
        Resepti mustikkapiirakka = new Resepti(1, "Mustikkapiirakka");
        System.out.println(mustikkapiirakka);
        
        Osiot mustikkapiirakkaOsiot = mustikkapiirakka.getOsiot();
        Osio muropohja = mustikkapiirakkaOsiot.lisaaOsio("Muropohja");
        OsionAinesosat muropohjanAinesosat = muropohja.annaOsionAinesosat();
        muropohjanAinesosat.lisaaOsionAinesosa("Voita", "100g");
        muropohjanAinesosat.lisaaOsionAinesosa("Sokeria", "1dl");
        muropohjanAinesosat.lisaaOsionAinesosa("Kananmunia", "1kpl");
        
        Osio tayte = mustikkapiirakkaOsiot.lisaaOsio("Täyte");
        OsionAinesosat taytteenAinesosat = tayte.annaOsionAinesosat();
        taytteenAinesosat.lisaaOsionAinesosa("Sokeria", "0,75dl");
        taytteenAinesosat.lisaaOsionAinesosa("Kananmunia", "1kpl");
        taytteenAinesosat.lisaaOsionAinesosa("Mustikoita", "300g");
        
        mustikkapiirakka.setKuvaus("Halpa ja maukas.");
        
        System.out.println(mustikkapiirakka.getKuvaus());
        System.out.println(mustikkapiirakkaOsiot);
        System.out.println();
        muropohjanAinesosat.tulostaOsionAinesosat(System.out);
        System.out.println();
        taytteenAinesosat.tulostaOsionAinesosat(System.out);
        
    }
}
