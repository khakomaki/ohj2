package reseptihaku;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class Ainesosat {
    
    private Ainesosa[] ainesosat;
    private String tiedostoNimi;
    private int maxLkm;
    private int lkm;
    private int annettavaTunnusLuku;
    
    /**
     * Ainesosat.
     * Tiedostonimeä ei ole vielä määritetty, maximi ainesosa määrä alkaa yhdestä ja
     * kasvaa ainesosia lisättäessä automaattisesti.
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "null|0|1";
     * </pre>
     */
    public Ainesosat() {
        this.ainesosat = new Ainesosa[1];
        this.maxLkm = 1;
        this.lkm = 0;
        this.annettavaTunnusLuku = 0;
    }
    
    
    /**
     * Asettaa tiedosto nimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä, mutta sallii
     * asettamisen null-viitteeseen.
     * 
     * @param tiedostonimi tiedoston nimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "null|0|1";
     * 
     * ainesosat.setTiedostoNimi("ainesosat.dat");
     * ainesosat.toString() === "ainesosat.dat|0|1";
     * 
     * ainesosat.setTiedostoNimi("");
     * ainesosat.toString() === "ainesosat.dat|0|1";
     * 
     * ainesosat.setTiedostoNimi("banaanit.txt");
     * ainesosat.toString() === "banaanit.txt|0|1";
     * 
     * ainesosat.setTiedostoNimi(null);
     * ainesosat.toString() === "null|0|1";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        if (tiedostonimi == null) { this.tiedostoNimi = null; return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * @return tiedoston nimi johon tallennetaan, null jos ei ole määritetty
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.getTiedostonimi() === null;
     * 
     * ainesosat.setTiedostoNimi("ainesosat.dat");
     * ainesosat.getTiedostonimi() === "ainesosat.dat";
     * </pre>
     */
    public String getTiedostonimi() {
        return this.tiedostoNimi;
    }
    
    
    /**
     * @return totuusarvo mahtuuko ainesosat listaan lisää ainesosia
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "null|0|1";
     * ainesosat.onkoTilaa() === true;
     * 
     * ainesosat.lisaaAinesosa("appelsiinin kuorta");
     * ainesosat.toString() === "null|1|1";
     * ainesosat.onkoTilaa() === false;
     * 
     * ainesosat.lisaaAinesosa("sokeria");
     * ainesosat.toString() === "null|2|2";
     * ainesosat.onkoTilaa() === false;
     * 
     * ainesosat.lisaaAinesosa("valkosipulia");
     * ainesosat.toString() === "null|3|4";
     * ainesosat.onkoTilaa() === true;
     * </pre>
     */
    public boolean onkoTilaa() {
        return (this.lkm < this.maxLkm);
    }
    
    
    /**
     * Kasvattaa ainesosat taulukkoa 2x
     * @return uusi maximi lukumäärä, eli uusi tila
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "null|0|1";
     * 
     * ainesosat.lisaaAinesosa("appelsiinin kuorta");
     * ainesosat.toString() === "null|1|1";
     * 
     * ainesosat.lisaaAinesosa("sokeria");
     * ainesosat.toString() === "null|2|2";
     * 
     * ainesosat.lisaaAinesosa("valkosipulia");
     * ainesosat.toString() === "null|3|4";
     * </pre>
     */
    private int lisaaTilaa() {
        // kasvatetaan listaa 2x
        this.maxLkm = 2 * this.maxLkm;
        
        // luodaan uusi lista uudella koolla, lisätään olemassaolevat olioviitteet ja käännetään viite siihen
        Ainesosa[] uudetAinesosat = new Ainesosa[this.maxLkm];
        for (int i = 0; i < this.lkm; i++) { uudetAinesosat[i] = this.ainesosat[i]; }
        this.ainesosat = uudetAinesosat;
        
        return this.maxLkm;
    }
    
    
    /**
     * @param ainesosa minkä niminen ainesosa halutaan
     * @return haluttu ainesosa, tai jos ei löytynyt niin null-viite
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.anna("banaani") === null;
     * 
     * ainesosat.lisaaAinesosa("BANAANI");
     * ainesosat.anna("banaani").toString() === "0|banaani";
     * 
     * </pre>
     */
    public Ainesosa anna(String ainesosa) {
        for (int i = 0; i < this.lkm; i++) {
            if (ainesosat[i].oletko(ainesosa)) { return ainesosat[i]; }
        }
        
        // jos ei löytynyt palautetaan null
        return null;
    }
    
    
    /**
     * @param tunnus minkä tunnuksinen ainesosa halutaan
     * @return haluttu ainesosa, tai jos ei löytynyt niin null-viite
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.anna(0) === null;
     * ainesosat.anna(1) === null;
     * 
     * ainesosat.lisaaAinesosa("BANAANI");
     * ainesosat.anna(0).toString() === "0|banaani";
     * </pre>
     */
    public Ainesosa anna(int tunnus) {
        for (int i = 0; i < this.lkm; i++) {
            if (ainesosat[i].oletko(tunnus)) { return ainesosat[i]; }
        }
        
        // jos ei löytynyt palautetaan null
        return null;
    }
    
    
    /**
     * @param ainesosaNimi minkä niminen ainesosa lisätään
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "null|0|1";
     * ainesosat.anna("ketsuppia") === null;
     * 
     * ainesosat.lisaaAinesosa("ketsuppia");
     * ainesosat.toString() === "null|1|1";
     * ainesosat.anna("ketsuppia").toString() === "0|ketsuppia";
     * 
     * ainesosat.lisaaAinesosa("sinappia");
     * ainesosat.toString() === "null|2|2";
     * ainesosat.anna("sinappia").toString() === "1|sinappia";
     * </pre>
     */
    public void lisaaAinesosa(String ainesosaNimi) {
        // varmistetaan että on tilaa lisätä
        if (!onkoTilaa()) { lisaaTilaa(); }
        
        // uusi ainesosa ja kasvatetaan tunnuslukua
        Ainesosa ainesosa = new Ainesosa(this.annettavaTunnusLuku, ainesosaNimi);
        this.annettavaTunnusLuku++;
        
        // lisätään listaan ja kasvatetaan lukumäärää
        this.ainesosat[this.lkm] = ainesosa;
        this.lkm++;
    }
    
    
    @Override
    /**
     * Palauttaa olennaisia tietoja luokan oliosta muodossa "tiedostonimi|lukumäärä|maximilukumäärä".
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "null|0|1";
     * 
     * ainesosat.lisaaAinesosa("punasipuli");
     * ainesosat.toString() === "null|1|1";
     * 
     * ainesosat.setTiedostoNimi("ainesosat.dat");
     * ainesosat.lisaaAinesosa("valkosipuli");
     * ainesosat.lisaaAinesosa("keltasipuli");
     * ainesosat.toString() === "ainesosat.dat|3|4";
     * </pre>
     */
    public String toString() {
        return "" + this.tiedostoNimi + "|" + this.lkm + "|" + this.maxLkm;
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ainesosat ainesosat = new Ainesosat();
        ainesosat.setTiedostoNimi("ainesosat.dat");
        System.out.println("Tallennus tiedosto: " + ainesosat.getTiedostonimi());
        
        System.out.println(ainesosat.toString() );
        
        ainesosat.lisaaAinesosa("maitoa");
        System.out.println(ainesosat.toString() );
        
        ainesosat.lisaaAinesosa("perunoita");
        System.out.println(ainesosat.toString() );
        
        ainesosat.lisaaAinesosa("porkkanoita");
        System.out.println(ainesosat.toString() );
        
        Ainesosa maito = ainesosat.anna("PERUNOITA");
        System.out.println(maito);
        
        int tunnus = 2;
        System.out.println("Ainesosa jonka tunnus on  " + tunnus + ": " + ainesosat.anna(tunnus).getNimi());
    }
}
