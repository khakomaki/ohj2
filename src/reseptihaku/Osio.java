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
    private Ainesosat ainesosat;
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
        
        this.ainesosat = new Ainesosat();
        luoOsionAinesosat();
        this.osionAinesosat.setAinesosat(ainesosat);
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
        
        this.ainesosat = new Ainesosat();
        luoOsionAinesosat();
        this.osionAinesosat.setAinesosat(ainesosat);
    }
    
    
    /**
     * @param ainesosat viite ainesosien hallitsijaan
     */
    public Osio(Ainesosat ainesosat) {
        setNimi(null);
        this.ohjeet = new Ohjeet(this.osioId);
        
        Ainesosat asetettavaAinesosat = ainesosat;
        if (asetettavaAinesosat == null) { asetettavaAinesosat = new Ainesosat(); }
        luoOsionAinesosat();
        this.osionAinesosat.setAinesosat(ainesosat);
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
        this.ohjeet = new Ohjeet(this.osioId);
        
        this.ainesosat = new Ainesosat();
        luoOsionAinesosat();
        this.osionAinesosat.setAinesosat(ainesosat);
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
        this.osionAinesosat.setAinesosat(ainesosat);
    }
    
    
    /**
     * Luo osion ainesosat
     */
    private void luoOsionAinesosat() {
        this.osionAinesosat = new OsionAinesosat(this.osioId, this.ainesosat);
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
     * @param ainesosaId minkä aineosan nimi halutaan
     * @return ainesosan nimi
     */
    public String getAinesosanNimi(int ainesosaId) {
        Ainesosa ainesosa = this.ainesosat.anna(ainesosaId);
        if (ainesosa == null) { return ""; }
        return ainesosa.getNimi();
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
     * @param ainesosa lisättävä ainesosa
     * @param maara lisättävän ainesosa määrä
     */
    public void lisaaAinesosa(Ainesosa ainesosa, String maara) {
        this.osionAinesosat.lisaaOsionAinesosa(ainesosa, maara);
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
     * @example
     * <pre name="test">
     * Osio sampylat = new Osio("Sämpylät");
     * sampylat.lisaaAinesosa("vehnäjauhoja", "8dl");
     * sampylat.lisaaAinesosa("maito", "4dl");
     * sampylat.lisaaAinesosa("kuivahiiva", "1ps");
     * sampylat.lisaaOhje(new Ohje("Lämmitä maito 42-asteiseksi"));
     * sampylat.lisaaOhje(new Ohje("Sekoita kuivahiiva maitoon"));
     * sampylat.lisaaOhje(new Ohje("Sekoita jauhot maitoon"));
     * Osio sampylaKopio = sampylat.clone();
     * 
     * sampylaKopio.equals(sampylat) === true;
     * sampylaKopio.toString().equals(sampylat.toString()) === true;
     * sampylat.annaOsionAinesosat().equals(sampylaKopio.annaOsionAinesosat()) === true;
     * sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet()) === true;
     * 
     * sampylat.lisaaOhje("Anna kohota liinan alla");
     * sampylaKopio.equals(sampylat) === false;
     * sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet()) === false;
     * 
     * sampylaKopio.lisaaOhje("Anna kohota liinan alla");
     * sampylaKopio.equals(sampylat) === true;
     * sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet()) === true;
     * 
     * sampylat.setUusiNimi("Sämpylä");
     * sampylaKopio.toString().equals(sampylat.toString()) === false;
     * 
     * sampylaKopio.setUusiNimi("Sämpylä");
     * sampylaKopio.toString().equals(sampylat.toString()) === true;
     * </pre>
     */
    public Osio clone() {
        Osio kopio = new Osio();
        kopio.nimi = this.nimi;
        kopio.osioId = this.osioId;
        
        // annetaan viite ainesosiin, ei kopioida
        kopio.ainesosat = this.ainesosat;
        
        // kopioidaan osion omat ainesosat ja ohjeet
        kopio.osionAinesosat = this.osionAinesosat.clone();
        kopio.ohjeet = this.ohjeet.clone();
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
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
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) { return false; }
        if (verrattava.getClass() != this.getClass()) { return false; }
        Osio verrattavaOsio = (Osio)verrattava;
        if (!verrattavaOsio.nimi.equals(this.nimi)) { return false; }
        if (verrattavaOsio.osioId != this.osioId) { return false; }
        if (!verrattavaOsio.osionAinesosat.equals(this.osionAinesosat)) { return false; }
        if (!verrattavaOsio.ohjeet.equals(this.ohjeet)) { return false; }
        if (!verrattavaOsio.ainesosat.equals(this.ainesosat)) { return false; }
        
        return true;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
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
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.osioId);
        hash = Hajautus.hajautusString(hash, this.nimi);
        hash = Hajautus.hajautusInt(hash, this.osionAinesosat.hashCode());
        hash = Hajautus.hajautusInt(hash, this.ohjeet.hashCode());
        hash = Hajautus.hajautusInt(hash, this.ainesosat.hashCode());
        
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
