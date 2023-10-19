package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;

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
     * @param ainesosaNimi minkä niminen ainesosa lisätään
     * @param maara ainesosan määrä
     */
    public void lisaaOsionAinesosa(String ainesosaNimi, String maara) {
        // koitetaan onko ainesosissa olemassa jo vastaava ainesosa, jos ei ole niin käsketään lisäämään
        Ainesosa lisattavaAinesosa = this.ainesosat.anna(ainesosaNimi);
        if (lisattavaAinesosa == null) { lisattavaAinesosa = ainesosat.lisaaAinesosa(ainesosaNimi); }
        
        // luodaan osion ainesosa ainesosilta saadulla tiedolla ja määrällä
        OsionAinesosa osionAinesosa = new OsionAinesosa(lisattavaAinesosa.getId(), maara);
        this.lisaa(osionAinesosa);
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
            out.print(this.annaIndeksista(i));
            out.print("\n");
        }
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
