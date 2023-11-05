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
    private String tiedostoNimi;
    private int osioId;
    
    
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
     * @param tiedostonimi tiedoston nimi johon tallennetaan
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu nimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) { return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * @param ainesosa lisättävä ainesosa
     * @param maara ainesosan määrä
     */
    public void lisaaOsionAinesosa(Ainesosa ainesosa, String maara) {
        // ei lisätä nulleja
        if (ainesosa == null) { return; }
        
        // koitetaan onko ainesosissa olemassa jo vastaava ainesosa, jos ei ole niin käsketään lisäämään
        Ainesosa lisattavaAinesosa = this.ainesosat.anna(ainesosa.getNimi());
        if (lisattavaAinesosa == null) { lisattavaAinesosa = this.ainesosat.lisaaAinesosa(ainesosa); }
        
        // luodaan osion ainesosa ainesosilta saadulla tiedolla ja määrällä
        OsionAinesosa osionAinesosa = new OsionAinesosa(lisattavaAinesosa.getId(), maara);
        lisaa(osionAinesosa);
    }
    
    
    /**
     * @param ainesosaNimi minkä niminen ainesosa lisätään
     * @param maara ainesosan määrä
     */
    public void lisaaOsionAinesosa(String ainesosaNimi, String maara) {
        Ainesosa ainesosa = new Ainesosa(ainesosaNimi);
        lisaaOsionAinesosa(ainesosa, maara);
    }
    
    
    /**
     * @param ainesosa lisättävä ainesosa
     */
    public void lisaaOsionAinesosa(Ainesosa ainesosa) {
        lisaaOsionAinesosa(ainesosa, "");
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan osion ainesosa
     * @return osion ainesosa halutusta indeksistä tai null
     */
    public OsionAinesosa annaIndeksista(int indeksi) {
        Object olio = getOlio(indeksi);
        if (olio.getClass() != OsionAinesosa.class) { return null; }
        return (OsionAinesosa)olio;
    }
    
    
    /**
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaOsionAinesosat(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < getLkm(); i++) {
            OsionAinesosa osionAinesosa = this.annaIndeksista(i);
            
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
            if (!verrattavaOA.annaIndeksista(i).equals(this.annaIndeksista(i))) { return false; }
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
            hash = Hajautus.hajautusInt(hash, this.annaIndeksista(i).hashCode());
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
        kaikkiAinesosat.lisaaAinesosa("sipulia");
        kaikkiAinesosat.lisaaAinesosa("suolaa");
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
