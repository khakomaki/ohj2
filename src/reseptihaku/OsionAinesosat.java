package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;

import kanta.Hajautus;
import kanta.TietueHallitsija;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class OsionAinesosat extends TietueHallitsija {
    
    private Ainesosat ainesosat;
    private String tiedostoNimi     = "osion_ainesosat.dat";
    private int osioId              = 1;
    
    
    /**
     * @param osioTunnus osion tunnus
     * @param ainesosat viite ainesosiin
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * OsionAinesosat oa = new OsionAinesosat(1, ainesosat);
     * 
     * oa.toString() === "1|osion_ainesosat.dat|0|1";
     * </pre>
     */
    public OsionAinesosat(int osioTunnus, Ainesosat ainesosat) {
        super();
        this.ainesosat = ainesosat;
        this.tiedostoNimi = "osion_ainesosat.dat";
        this.osioId = osioTunnus;
    }
    
    
    /**
     * Parametriton muodostaja.
     * Luo itselleen oman Ainesosat-luokan.
     * Osio tunnus alustuu luvuksi 1.
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat oat = new OsionAinesosat();
     * oat.toString() === "1|osion_ainesosat.dat|0|1";
     * </pre>
     */
    public OsionAinesosat() {
        super();
        this.ainesosat = new Ainesosat();
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
    }
    
    
    /**
     * @param tiedostonimi tiedoston nimi johon tallennetaan
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu nimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) { return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * Lisää osion ainesosan (ainesosa + määrä).
     * Ei lisää nullia ainesosana.
     * 
     * @param ainesosa lisättävä ainesosa
     * @param maara ainesosan määrä
     */
    public void lisaa(Ainesosa ainesosa, String maara) {
        // ei lisätä nulleja
        if (ainesosa == null) { return; }
        
        // koitetaan onko ainesosissa olemassa jo vastaava ainesosa, jos ei ole niin käsketään lisäämään
        Ainesosa lisattavaAinesosa = this.ainesosat.anna(ainesosa.getNimi());
        if (lisattavaAinesosa == null) { lisattavaAinesosa = this.ainesosat.lisaa(ainesosa); }
        
        // luodaan osion ainesosa ainesosilta saadulla tiedolla ja määrällä
        OsionAinesosa osionAinesosa = new OsionAinesosa(lisattavaAinesosa.getId(), maara);
        lisaaOlio(osionAinesosa);
    }
    
    
    /**
     * @param ainesosaNimi minkä niminen ainesosa lisätään
     * @param maara ainesosan määrä
     */
    public void lisaaOsionAinesosa(String ainesosaNimi, String maara) {
        Ainesosa ainesosa = new Ainesosa(ainesosaNimi);
        lisaa(ainesosa, maara);
    }
    
    
    /**
     * @param osionAinesosa lisättävä osion ainesosa
     */
    public void lisaa(OsionAinesosa osionAinesosa) {
        Ainesosa ainesosa = this.ainesosat.anna(osionAinesosa.getId());
        String maara = osionAinesosa.getMaara();
        lisaa(ainesosa, maara);
    }
    
    
    /**
     * @param ainesosa lisättävä ainesosa
     */
    public void lisaaOsionAinesosa(Ainesosa ainesosa) {
        lisaa(ainesosa, "");
    }
    
    
    /**
     * Poistaa osion ainesosan, jos annettu ainesosa löytyy.
     * 
     * @param osionAinesosa poistettava osion ainesosa
     */
    public void poista(OsionAinesosa osionAinesosa) {
        poista(osionAinesosa);
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan osion ainesosa
     * @return osion ainesosa halutusta indeksistä tai null
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat oat = new OsionAinesosat();
     * oat.anna(0) == null === true;
     * 
     * oat.lisaaOsionAinesosa("Mansikka", "2dl");
     * oat.anna(0).equals(new OsionAinesosa(0, "2dl")) === true;
     * </pre>
     */
    public OsionAinesosa anna(int indeksi) {
        Object olio = getOlio(indeksi);
        if (olio == null) { return null; }
        if (olio.getClass() != OsionAinesosa.class) { return null; }
        return (OsionAinesosa)olio;
    }
    
    
    /**
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaOsionAinesosat(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < getLkm(); i++) {
            OsionAinesosa osionAinesosa = this.anna(i);
            
            // ei tulosteta mitään jos OsionAinesosa on null
            if (osionAinesosa == null) return;
            
            // tulostaa ainesosista tunnusta vastaavan ainesosan nimen
            out.print(ainesosat.anna(osionAinesosa.getId()).getNimi());
            out.print(" : ");
            
            // tulostaa ainesosan määrän ja rivin vaihdon
            out.print(osionAinesosa.getMaara());
            out.print("\n");
        }
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * OsionAinesosat oat = new OsionAinesosat(55, ainesosat);
     * oat.lisaaOsionAinesosa(new Ainesosa("riisi"));
     * oat.lisaaOsionAinesosa(new Ainesosa("soijakastike"));
     * 
     * OsionAinesosat oatKopio = oat.clone();
     * oat.equals(oatKopio) === true;
     * oat.toString() === "55|osion_ainesosat.dat|2|2";
     * 
     * oat.lisaaOsionAinesosa(new Ainesosa("sipuli"));
     * oat.equals(oatKopio) === false;
     * 
     * oatKopio.lisaaOsionAinesosa(new Ainesosa("sipuli"));
     * oat.equals(oatKopio) === true;
     * </pre>
     */
    public OsionAinesosat clone() {
        OsionAinesosat kopio = new OsionAinesosat();
        kopio.osioId = this.osioId;
        kopio.tiedostoNimi = this.tiedostoNimi;
        
        // annetaan viite samaan Ainesosat-luokkaan, koska tarvitaan vain yksi
        kopio.ainesosat = this.ainesosat;
        
        // kopioidaan kaikki alkiot kopioon
        for (int i = 0; i < this.getLkm(); i++) {
            kopio.lisaa(this.anna(i));
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ainesosat ainesosat1 = new Ainesosat();
     * Ainesosat ainesosat2 = new Ainesosat();
     * OsionAinesosat oa1 = new OsionAinesosat(1, ainesosat1);
     * OsionAinesosat oa2 = new OsionAinesosat(1, ainesosat2);
     * oa1.equals(oa2) === true;
     * 
     * oa1.setTiedostoNimi("oa.txt");
     * oa1.equals(oa2) === false;
     * 
     * oa2.setTiedostoNimi("oa.txt");
     * oa1.equals(oa2) === true;
     * 
     * oa1.lisaaOsionAinesosa(new Ainesosa("mustikka"));
     * oa1.equals(oa2) === false;
     * 
     * oa2.lisaaOsionAinesosa(new Ainesosa("mustikka"));
     * oa1.equals(oa2) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) { return false; }
        if (verrattava.getClass() != this.getClass()) { return false; }
        
        OsionAinesosat verrattavaOA = (OsionAinesosat)verrattava;
        if (verrattavaOA.osioId != this.osioId) { return false; }
        if (!verrattavaOA.tiedostoNimi.equals(this.tiedostoNimi)) { return false; }
        if (verrattavaOA.getLkm() != this.getLkm()) { return false; }
        
        // verrataan alkioita keskenään
        for (int i = 0; i < this.getLkm(); i++) {
            if (!verrattavaOA.anna(i).equals(this.anna(i))) { return false; }
        }
        if (!verrattavaOA.ainesosat.equals(this.ainesosat)) { return false; }
        
        return true;
    }
    
    
    @Override
    /**
     * @return hajautusluku
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat1 = new Ainesosat();
     * Ainesosat ainesosat2 = new Ainesosat();
     * OsionAinesosat oa1 = new OsionAinesosat(1, ainesosat1);
     * OsionAinesosat oa2 = new OsionAinesosat(1, ainesosat2);
     * oa1.hashCode() == oa2.hashCode() === true;
     * 
     * oa1.setTiedostoNimi("oa.txt");
     * oa1.hashCode() == oa2.hashCode() === false;
     * 
     * oa2.setTiedostoNimi("oa.txt");
     * oa1.hashCode() == oa2.hashCode() === true;
     * 
     * oa1.lisaaOsionAinesosa(new Ainesosa("mustikka"));
     * oa1.hashCode() == oa2.hashCode() === false;
     * 
     * oa2.lisaaOsionAinesosa(new Ainesosa("mustikka"));
     * oa1.hashCode() == oa2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.osioId);
        hash = Hajautus.hajautusString(hash, this.tiedostoNimi);
        
        for (int i = 0; i < this.getLkm(); i++) {
            hash = Hajautus.hajautusInt(hash, this.anna(i).hashCode());
        }
        
        return hash;
    }
    
    
    @Override
    /**
     * Palauttaa tiedot muodossa:
     * "osion id|tiedostonimi|lukumäärä|maksimi lukumäärä"
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * OsionAinesosat oa = new OsionAinesosat(1, ainesosat);
     * oa.toString() === "1|osion_ainesosat.dat|0|1";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.tiedostoNimi);
        sb.append('|');
        sb.append(getLkm());
        sb.append('|');
        sb.append(getMaxLkm());
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        int nykyisenOsionId = 1;
        Ainesosat kaikkiAinesosat = new Ainesosat();
        kaikkiAinesosat.lisaa("sipulia");
        kaikkiAinesosat.lisaa("suolaa");
        OsionAinesosat osionAinesosat = new OsionAinesosat(nykyisenOsionId, kaikkiAinesosat);
        
        System.out.println("Osion ainesosat -oliot:\n" + osionAinesosat.toString());
        
        osionAinesosat.setTiedostoNimi("soppa.txt");
        osionAinesosat.lisaaOsionAinesosa("perunoita", "500g");
        osionAinesosat.lisaaOsionAinesosa("porkkanoita", "2kpl");
        osionAinesosat.lisaaOsionAinesosa("sipulia", "1kpl");
        osionAinesosat.lisaaOsionAinesosa("suolaa", "3/4rkl");
        
        System.out.println(osionAinesosat.toString());
        
        System.out.println("\nAinesosa tunnukset ja määrät:");
        osionAinesosat.tulostaOsionAinesosat(System.out);
    }
}
