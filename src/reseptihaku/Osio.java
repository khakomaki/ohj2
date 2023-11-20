package reseptihaku;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Hajautus;
import kanta.SailoException;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Osio {
    
    private int osioId                      = -1;
    private String nimi                     = "Osion nimi";
    private OsionAinesosat osionAinesosat;
    private Ohjeet ohjeet;
    
    private static int annettavaId          = 1;
    
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
        setUusiNimi(nimi);
        
        luoOsionAinesosat();
    }
    
    
    /**
     * Ohje valitulla nimellä
     * 
     * @param nimi osion nimi
     * @example
     * <pre name="test">
     * Osio pizzapohja = new Osio("Pizzapohja");
     * pizzapohja.toString() === "-1|Pizzapohja";
     * </pre>
     */
    public Osio(String nimi) {
        this.ohjeet = new Ohjeet(this.osioId);
        setUusiNimi(nimi);
        
        luoOsionAinesosat();
    }
    
    
    /**
     * Oletus ohje
     * 
     * @example
     * <pre name="test">
     * Osio osio = new Osio();
     * osio.toString() === "-1|Osion nimi";
     * </pre>
     */
    public Osio() {
        this.ohjeet = new Ohjeet(this.osioId);
        
        luoOsionAinesosat();
    }
    
    
    /**
     * Antaa osiolle tunnuksen
     * 
     * @return annettu osion tunnus
     */
    public int rekisteroi() {
        this.osioId = Osio.annettavaId;
        Osio.annettavaId++;
        return this.osioId;
    }
    
    
    /**
     * Luo osion ainesosat
     */
    private void luoOsionAinesosat() {
        this.osionAinesosat = new OsionAinesosat(this.osioId);
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
        if (nimi == null) return;
        if (nimi.length() < 1) return;
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
     * Luo ja lisää annetun mukaisen osion ainesosan
     * 
     * @param ainesosa ainesosan nimi
     * @param maara ainesosan määrä
     * @return luotu osion ainesosa
     */
    public OsionAinesosa lisaaAinesosa(String ainesosa, String maara) {
        OsionAinesosa osionAinesosa = new OsionAinesosa(ainesosa, maara);
        this.osionAinesosat.lisaa(osionAinesosa);
        return osionAinesosa;
    }
    
    
    /**
     * @param ainesosa lisättävä osion ainesosa
     */
    public void lisaaAinesosa(OsionAinesosa ainesosa) {
        this.osionAinesosat.lisaa(ainesosa);
    }
    
    
    /**
     * Poistaa osion ainesosan, jos sellainen löytyy
     * 
     * @param osionAinesosa poistettava osion ainesosa
     */
    public void poistaAinesosa(OsionAinesosa osionAinesosa) {
        this.osionAinesosat.poista(osionAinesosa);
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
     * Poistaa annetun ohjeen, jos sellainen löydetään
     * 
     * @param ohje poistettava ohje
     */
    public void poistaOhje(Ohje ohje) {
        this.ohjeet.poista(ohje);
    }
    
    
    /**
     * Koittaa parsia osion tiedot annetusta rivistä
     * 
     * @param rivi mistä koitetaan lukea osion tiedot
     * 
     * @example
     * <pre name="test">
     * Osio osio = new Osio();
     * osio.parse("1|1|Muropohja");
     * osio.toString() === "1|Muropohja";
     * 
     * osio.parse("  5   |  2    |       Pizzapohja   ");
     * osio.toString() === "2|Pizzapohja";
     * 
     * osio.parse("1|Muropohja");
     * osio.toString() === "2|Pizzapohja";
     * 
     * osio.parse("1 1 Muropohja");
     * osio.toString() === "2|Pizzapohja";
     * </pre>
     */
    public void parse(String rivi) {
        if (rivi == null || rivi.length() < 1) return;
        
        StringBuilder sb = new StringBuilder(rivi);
        
        // ei huomioida ensimmäisen kentän tietoja (resepti_id)
        Mjonot.erota(sb, '|');
        
        this.osioId = Mjonot.erota(sb, '|', this.osioId);
        this.nimi = Mjonot.erota(sb, '|', this.nimi);
    }
    
    
    /**
     * tallentaa osion tiedot tiedostoihin
     * 
     * @throws SailoException jos tallentaminen epäonnistuu
     */
    public void tallenna() throws SailoException {
        // rekisteröi osion jos sille ei ole annettu vielä omaa tunnusta
        if (this.osioId == -1) rekisteroi();
        
        this.osionAinesosat.tallenna();
        this.ohjeet.tallenna();
    }
    
    
    /**
     * Luo muropohja osion testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoMuropohja() {
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
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Osio verrattavaOsio = (Osio)verrattava;
        if (!verrattavaOsio.nimi.equals(this.nimi)) return false;
        if (verrattavaOsio.osioId != this.osioId) return false;
        if (!verrattavaOsio.osionAinesosat.equals(this.osionAinesosat)) return false;
        if (!verrattavaOsio.ohjeet.equals(this.ohjeet)) return false;
        
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
        osionAinesosat.lisaa(new OsionAinesosa("maitoa", "5dl"));
        osionAinesosat.lisaa(new OsionAinesosa("kananmunia", "3kpl"));
        osionAinesosat.lisaa(new OsionAinesosa("sokeria", "2rkl"));
        osionAinesosat.lisaa(new OsionAinesosa("vehnäjauhoja", "3dl"));
        osionAinesosat.lisaa(new OsionAinesosa("suolaa", "1tl"));
        osionAinesosat.lisaa(new OsionAinesosa("voi", ""));
        System.out.println(osionAinesosat.toString() + "\n");
        
        osionAinesosat.tulostaOsionAinesosat(System.out);
        
        lattytaikina.setUusiNimi("Lettutaikina");
        System.out.println("\n" + lattytaikina.toString());
    }
}
