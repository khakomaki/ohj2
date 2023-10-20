package reseptihaku;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Osio {
    
    private int osioId;
    private String nimi;
    private OsionAinesosat osionAinesosat;
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
        setNimi(nimi);
        luoOsionAinesosat();
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
     * @return osion ainesosat -olio
     */
    public OsionAinesosat annaOsionAinesosat() {
        return this.osionAinesosat;
    }
    
    
    /**
     * Luo muropohja osion testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoMuropohja() {
        this.osioId = 1;
        this.nimi = "Muropohja";
        this.osionAinesosat.lisaaOsionAinesosa("voita", "100g");
        this.osionAinesosat.lisaaOsionAinesosa("sokeria", "1dl");
        this.osionAinesosat.lisaaOsionAinesosa("kananmunia", "1kpl");
        this.osionAinesosat.lisaaOsionAinesosa("vehnäjauhoja", "2,5dl");
        this.osionAinesosat.lisaaOsionAinesosa("leivinjauhetta", "1tl");
    }
    
    
    /**
     * Luo muropohja osion testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoTayte() {
        this.osioId = 1;
        this.nimi = "Täyte";
        this.osionAinesosat.lisaaOsionAinesosa("sokeria", "0,75dl");
        this.osionAinesosat.lisaaOsionAinesosa("kananmunia", "1kpl");
        this.osionAinesosat.lisaaOsionAinesosa("mustikoita", "300g");
        this.osionAinesosat.lisaaOsionAinesosa("kermaviiliä", "200g");
        this.osionAinesosat.lisaaOsionAinesosa("vanilliinisokeria", "1tl");
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
