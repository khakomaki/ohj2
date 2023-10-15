package reseptihaku;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class OsionAinesosat {
    
    private OsionAinesosa[] osionAinesosat;
    private Ainesosat ainesosat;
    private String tiedostoNimi;
    private int osioId;
    private int maxLkm;
    private int lkm;
    
    
    /**
     * @param osioTunnus osion tunnus
     * @param ainesosat viite ainesosiin
     */
    public OsionAinesosat(int osioTunnus, Ainesosat ainesosat) {
        this.osionAinesosat = new OsionAinesosa[1];
        this.ainesosat = ainesosat;
        this.osioId = osioTunnus;
        this.maxLkm = 1;
        this.lkm = 0;
    }
    
    
    /**
     * @param tiedostonimi tiedoston nimi johon tallennetaan
     */
    public void setTiedostoNimi(String tiedostonimi) {
        if (tiedostonimi == null) { this.tiedostoNimi = tiedostonimi; return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * @return totuusarvo mahtuuko listaan lisää
     */
    public boolean onkoTilaa() {
        return (this.lkm < this.maxLkm);
    }
    
    
    /**
     * Kasvattaa taulukkoa 2x
     * @return uusi maximi lukumäärä, eli uusi tila
     */
    private int lisaaTilaa() {
        // kasvatetaan listaa 2x
        this.maxLkm = 2 * this.maxLkm;
        
        // luodaan uusi lista uudella koolla, lisätään olemassaolevat olioviitteet ja käännetään viite siihen
        OsionAinesosa[] uudetOsionAinesosat = new OsionAinesosa[this.maxLkm];
        for (int i = 0; i < this.lkm; i++) { uudetOsionAinesosat[i] = this.osionAinesosat[i]; }
        this.osionAinesosat = uudetOsionAinesosat;
        
        return this.maxLkm;
    }
    
    
    /**
     * @param ainesosaNimi minkä niminen ainesosa lisätään
     * @param maara ainesosan määrä
     */
    public void lisaaOsionAinesosa(String ainesosaNimi, String maara) {
        // varmistetaan että on tilaa lisätä
        if (!onkoTilaa()) { lisaaTilaa(); }
        
        // koitetaan onko ainesosissa olemassa jo vastaava ainesosa, jos ei ole niin käsketään lisäämään
        Ainesosa lisattavaAinesosa = this.ainesosat.anna(ainesosaNimi);
        if (lisattavaAinesosa == null) { lisattavaAinesosa = ainesosat.lisaaAinesosa(ainesosaNimi); }
        
        // luodaan osion ainesosa ainesosilta saadulla tiedolla ja määrällä
        OsionAinesosa osionAinesosa = new OsionAinesosa(lisattavaAinesosa.getId(), maara);
        
        // lisätään listaan ja kasvatetaan lukumäärää
        this.osionAinesosat[this.lkm] = osionAinesosa;
        this.lkm++;
    }
    
    
    /**
     * @return osion ainesosien lukumäärä
     */
    public int getLkm() {
        return this.lkm;
    }
    
    
    /**
     * @param indeksi mistä indeksistä yritetään antaa 
     * @return viite osion ainesosa -olioon
     */
    private OsionAinesosa anna(int indeksi) {
        if (this.lkm < indeksi) { return null; }
        return this.osionAinesosat[indeksi];
    }
    
    
    @Override
    public String toString() {
        return "" + this.osioId + "|" + this.tiedostoNimi + "|" + this.lkm + "|" + this.maxLkm;
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        int nykyisenOsionId = 1;
        Ainesosat kaikkiAinesosat = new Ainesosat();
        kaikkiAinesosat.lisaaAinesosa("sipulia");
        kaikkiAinesosat.lisaaAinesosa("suolaa");
        OsionAinesosat osionAinesosat = new OsionAinesosat(nykyisenOsionId, kaikkiAinesosat);
        
        System.out.println("Osion ainesosat -oliot:\n" + osionAinesosat.toString());
        
        osionAinesosat.setTiedostoNimi("osion_ainesosat.dat");
        osionAinesosat.lisaaOsionAinesosa("perunoita", "500g");
        osionAinesosat.lisaaOsionAinesosa("porkkanoita", "2kpl");
        osionAinesosat.lisaaOsionAinesosa("sipulia", "1kpl");
        osionAinesosat.lisaaOsionAinesosa("suolaa", "3/4rkl");
        
        System.out.println(osionAinesosat.toString());
        
        System.out.println("\nAinesosa tunnukset ja määrät:");
        for (int i = 0; i < osionAinesosat.getLkm(); i++) {
            System.out.println(osionAinesosat.anna(i));
        }
    }
}
