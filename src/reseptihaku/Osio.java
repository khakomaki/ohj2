package reseptihaku;

import kanta.Hajautus;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Osio {
    
    private int osioId;
    private String nimi;
    private OsionAinesosat osionAinesosat;
    private Ohjeet ohjeet;
    private Ainesosat ainesosat;            // TODO kaikille viite yhteen ja samaan Ainesosat-olioon
    private final String oletusNimi = "Osion nimi";
    
    
    /**
     * @param id osion tunnus
     * @param nimi osion nimi
     * 
     * @example
     * <pre name="test">
     * Osio pizzapohja = new Osio(2, "Pizzapohja");
     * pizzapohja.toString() === "2|Pizzapohja";
     * </pre>
     */
    public Osio(int id, String nimi) {
        this.osioId = id;
        this.ohjeet = new Ohjeet(this.osioId);
        setNimi(nimi);
        luoOsionAinesosat();
    }
    
    
    /**
     * Ohje valitulla nimellä
     * 
     * @param nimi osion nimi
     * @example
     * <pre name="test">
     * Osio pizzapohja = new Osio("Pizzapohja");
     * pizzapohja.toString() === "0|Pizzapohja";
     * </pre>
     */
    public Osio(String nimi) {
        this.ohjeet = new Ohjeet(this.osioId);
        setNimi(nimi);
        luoOsionAinesosat();
    }
    
    
    /**
     * Oletus ohje
     * 
     * @example
     * <pre name="test">
     * Osio osio = new Osio();
     * osio.toString() === "0|Osion nimi";
     * </pre>
     */
    public Osio() {
        setNimi(null);
        luoOsionAinesosat();
        this.ohjeet = new Ohjeet(this.osioId);
    }
    
    
    /**
     * Luo osion ainesosat
     */
    private void luoOsionAinesosat() {
        this.ainesosat = new Ainesosat();
        this.osionAinesosat = new OsionAinesosat(this.osioId, ainesosat);
    }
    
    
    /**
     * @param nimi osion nimi
     * 
     * @example
     * <pre name="test">
     * Osio kakkupohja = new Osio(8, "Kakkupohja");
     * kakkupohja.toString() === "8|Kakkupohja";
     * 
     * kakkupohja = new Osio(8, "");
     * kakkupohja.toString() === "8|Osion nimi";
     * 
     * kakkupohja = new Osio(8, null);
     * kakkupohja.toString() === "8|Osion nimi";
     * 
     * kakkupohja = new Osio(8, "K");
     * kakkupohja.toString() === "8|K";
     * </pre>
     */
    private void setNimi(String nimi) {
        // asettaa oletusnimen jos annettu nimi on null tai tyhjä merkkijono
        if (nimi == null) { this.nimi = oletusNimi; return; }
        if (nimi.length() < 1) { this.nimi = oletusNimi; return; }
        this.nimi = nimi;
    }
    
    
    /**
     * @param nimi osion nimi
     * 
     * @example
     * <pre name="test">
     * Osio kakkupohja = new Osio(8, "Kakkupohja");
     * kakkupohja.toString() === "8|Kakkupohja";
     * 
     * kakkupohja.setUusiNimi("");
     * kakkupohja.toString() === "8|Kakkupohja";
     * 
     * kakkupohja.setUusiNimi("Piirakkapohja");
     * kakkupohja.toString() === "8|Piirakkapohja";
     * 
     * kakkupohja.setUusiNimi(null);
     * kakkupohja.toString() === "8|Piirakkapohja";
     * </pre>
     */
    public void setUusiNimi(String nimi) {
        // ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
        if (nimi == null) { return; }
        if (nimi.length() < 1) { return; }
        this.nimi = nimi;
    }
    
    
    /**
     * @return osion nimi
     * 
     * @example
     * <pre name="test">
     * Osio kakkupohja = new Osio(8, "Kakkupohja");
     * kakkupohja.getNimi() === "Kakkupohja";
     * </pre>
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * @return osion ainesosat -olio
     */
    public OsionAinesosat annaOsionAinesosat() {
        return this.osionAinesosat;
    }
    
    
    /**
     * @return osion ohjeet
     */
    public Ohjeet annaOsionOhjeet() {
        return this.ohjeet;
    }
    
    
    /**
     * @param ainesosa lisättävän ainesosan nimi
     * @param maara lisättävän ainesosan määrä
     */
    public void lisaaAinesosa(String ainesosa, String maara) {
        this.osionAinesosat.lisaaOsionAinesosa(ainesosa, maara);
    }
    
    
    /**
     * @param ohje lisättävä ohje
     */
    public void lisaaOhje(Ohje ohje) {
        this.ohjeet.lisaa(ohje);
    }
    
    
    /**
     * @param ohjeistus lisättävän ohjeen ohjeistusteksti
     */
    public void lisaaOhje(String ohjeistus) {
        Ohje ohje = new Ohje(ohjeistus);
        this.ohjeet.lisaa(ohje);
    }
    
    
    /**
     * Luo muropohja osion testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoMuropohja() {
        this.osioId = 1;
        this.nimi = "Muropohja";
        lisaaAinesosa("voita", "100g");
        lisaaAinesosa("sokeria", "1dl");
        lisaaAinesosa("kananmunia", "1kpl");
        lisaaAinesosa("vehnäjauhoja", "2,5dl");
        lisaaAinesosa("leivinjauhetta", "1tl");
        
        lisaaOhje("Sekoita sokeri ja pehmeä voi");
        lisaaOhje("Lisää kananmuna ja vaahdota");
        lisaaOhje("Lisää leivinjauho vehnäjauhoihin");
        lisaaOhje("Sekoita jauhot vaahtoon");
        lisaaOhje("Painele taikina piirakkavuokaan");
    }
    
    
    /**
     * Luo muropohja osion testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoTayte() {
        this.osioId = 1;
        this.nimi = "Täyte";
        lisaaAinesosa("sokeria", "0,75dl");
        lisaaAinesosa("kananmunia", "1kpl");
        lisaaAinesosa("mustikoita", "300g");
        lisaaAinesosa("kermaviiliä", "200g");
        lisaaAinesosa("vanilliinisokeria", "1tl");
        
        lisaaOhje("Ripottele mustikat piirakkapohjan päälle");
        lisaaOhje("Sekoita sokeri, kermaviili ja kananmuna");
        lisaaOhje("Lisää vanilliinisokeri");
        lisaaOhje("Kaada kermaviiliseos piirakkapohjan päälle");
    }
    
    
    @Override
    /**
     * Osio sampylat = new Osio("Sämpylät");
     * Osio sampylaKopio = sampylat.clone();
     * 
     * sampylaKopio.toString().equals(sampylat.toString()) === true;
     * 
     * sampylat.setUusiNimi("Sämpylä");
     * sampylaKopio.toString().equals(sampylat.toString()) === false;
     */
    public Osio clone() {
        Osio kopio = new Osio();
        kopio.nimi = this.nimi;
        kopio.osioId = this.osioId;
        
        return kopio;
    }
    
    
    @Override
    /**
     * Osio pohja1 = new Osio(1, "Kakkupohja");
     * Osio pohja2 = new Osio(1, "Kakkupohj");
     * Osio pohja3 = new Osio(2, "Kakkupohja");
     * 
     * pohja1.equals(pohja2) === false;
     * pohja1.equals(pohja3) === false;
     * pohja2.equals(pohja3) === false;
     * 
     * pohja2.setUusiNimi("Kakkupohja");
     * pohja1.equals(pohja2) === true;
     * pohja2.equals(pohja3) === false;
     * pohja2.equals(pohja1) === true;
     */
    public boolean equals(Object verrattava) {
        if (verrattava.getClass() != this.getClass()) { return false; }
        Osio verrattavaOsio = (Osio)verrattava;
        if (!verrattavaOsio.nimi.equals(this.nimi)) { return false; }
        if (verrattavaOsio.osioId != this.osioId) { return false; }
        
        return true;
    }
    
    
    @Override
    /**
     * Osio pohja1 = new Osio(1, "Kakkupohja");
     * Osio pohja2 = new Osio(1, "Kakkupohj");
     * Osio pohja3 = new Osio(2, "Kakkupohja");
     * 
     * pohja1.hashCode() == pohja2.hashCode() === false;
     * pohja1.hashCode() == pohja3.hashCode() === false;
     * pohja2.hashCode() == pohja3.hashCode() === false;
     * 
     * pohja2.setUusiNimi("Kakkupohja");
     * pohja1.hashCode() == pohja2.hashCode() === true;
     * pohja1.hashCode() == pohja3.hashCode() === false;
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.osioId);
        hash = Hajautus.hajautusString(hash, this.nimi);
        
        return hash;
    }
    
    
    @Override
    /**
     * Osio pizzapohja = new Osio(13, "Hampurilaissämpylät");
     * pizzapohja.toString() === "13|Hampurilaissämpylät";
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.nimi);
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osio lattytaikina = new Osio(1, "Lättytaikina");
        System.out.println(lattytaikina.toString());
        
        OsionAinesosat osionAinesosat = lattytaikina.annaOsionAinesosat();
        osionAinesosat.lisaaOsionAinesosa("maitoa", "5dl");
        osionAinesosat.lisaaOsionAinesosa("kananmunia", "3kpl");
        osionAinesosat.lisaaOsionAinesosa("sokeria", "2rkl");
        osionAinesosat.lisaaOsionAinesosa("vehnäjauhoja", "3dl");
        osionAinesosat.lisaaOsionAinesosa("suolaa", "1tl");
        osionAinesosat.lisaaOsionAinesosa("voi", "");
        System.out.println(osionAinesosat.toString() + "\n");
        
        osionAinesosat.tulostaOsionAinesosat(System.out);
        
        lattytaikina.setUusiNimi("Lettutaikina");
        System.out.println("\n" + lattytaikina.toString());
    }
}
