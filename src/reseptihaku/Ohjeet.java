package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author hakom
 * @version 30 Oct 2023
 *
 */
public class Ohjeet {

    private String tiedostoNimi     = "ohjeet.dat";
    private int osioId              = 0;
    private int lkm                 = 0;
    private ArrayList<Ohje> ohjeet  = new ArrayList<Ohje>();
    
    
    /**
     * Hallitsee Ohje-oliota
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.toString() === "ohjeet.dat|0|0";
     * </pre>
     */
    public Ohjeet() {
        //
    }
    
    
    /**
     * Hallitsee Ohje-oliota
     * 
     * @param osioId osion tunnus, johon ohjeet kuuluu
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.toString() === "ohjeet.dat|1|0";
     * </pre>
     */
    public Ohjeet(int osioId) {
        this.osioId = osioId;
    }
    
    
    /**
     * @param id asetettava osion tunnus
     */
    public void setOsio(int id) {
        this.osioId = id;
    }
    
    
    /**
     * Lisää ohjeen ohjeisiin.
     * 
     * @param ohje lisättävä ohje
     * Ohjeet ohjeet = new Ohjeet(15);
     * ohjeet.toString() === "ohjeet.dat|15|0";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet.dat|15|1";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet.dat|15|4";
     * 
     * ohjeet.lisaa(null);
     * ohjeet.toString() === "ohjeet.dat|15|4";
     */
    public void lisaa(Ohje ohje) {
        // ei lisää null olioita
        if (ohje == null) { return; }
        
        this.ohjeet.add(ohje);
        this.lkm++;
    }
    
    
    /**
     * Poistaa indeksistä löytyvän ohjeen ohjeista.
     * 
     * @param indeksi mistä indeksistä poistetaan
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(2);
     * ohjeet.toString() === "ohjeet.dat|2|0";
     * 
     * ohjeet.poista(-2);
     * ohjeet.poista(0);
     * ohjeet.poista(100);
     * ohjeet.toString() === "ohjeet.dat|2|0";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet.dat|2|1";
     * 
     * ohjeet.poista(0);
     * ohjeet.toString() === "ohjeet.dat|2|0";
     * 
     * ohjeet.lisaa(new Ohje("ohje 1"));
     * ohjeet.lisaa(new Ohje("ohje 2"));
     * ohjeet.lisaa(new Ohje("ohje 3"));
     * ohjeet.anna(1).getOhjeistus() === "ohje 2";
     * 
     * ohjeet.poista(1);
     * ohjeet.anna(1).getOhjeistus() === "ohje 3";
     * </pre>
     */
    public void poista(int indeksi) {
        // ei tehdä mitään jos indeksi ei ole mieluisa
        if (indeksi < 0 || lkm <= indeksi) { return; }
        
        ohjeet.remove(indeksi);
        this.lkm--;
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan ohje
     * @return indeksistä löytyvä ohje tai null
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.anna(0) === null;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.anna(0) == null === false;
     * </pre>
     */
    public Ohje anna(int indeksi) {
        if (indeksi < 0 || this.lkm <= indeksi) { return null; }
        return ohjeet.get(indeksi);
    }
    
    
    /**
     * Asettaa tiedosto nimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
     * 
     * @param tiedostonimi tiedoston nimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.toString() === "ohjeet.dat|0|0";
     * 
     * ohjeet.setTiedostoNimi("ohjeistukset.txt");
     * ohjeet.toString() === "ohjeistukset.txt|0|0";
     * 
     * ohjeet.setTiedostoNimi("");
     * ohjeet.toString() === "ohjeistukset.txt|0|0";
     * 
     * ohjeet.setTiedostoNimi(null);
     * ohjeet.toString() === "ohjeistukset.txt|0|0";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) { return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * @return tiedoston nimi johon tallennetaan, null jos ei ole määritetty
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.getTiedostonimi() === "ainesosat.dat";
     * 
     * ainesosat.setTiedostoNimi("ostoslista.txt");
     * ainesosat.getTiedostonimi() === "ostoslista.txt";
     * </pre>
     */
    public String getTiedostonimi() {
        return this.tiedostoNimi;
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     */
    public void tulostaOhjeet(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < this.lkm; i++) {
            // vaihe numero
            out.print(i + 1);
            out.print(" ");
            out.print(this.ohjeet.get(i).getOhjeistus());
            out.print("\n");
        }
    }
    
    
    @Override
    /**
     * Ohjeet-olion tiedot muodossa: "tiedostonimi|osio id|lukumäärä"
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.toString() === "ohjeet.dat|1|0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.tiedostoNimi);
        sb.append('|');
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.lkm);
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ohjeet ohjeet = new Ohjeet(5);
        ohjeet.setTiedostoNimi("ohjeet.txt");
        
        Ohje ohje1 = new Ohje("Ripottele mustikat piirakkapohjan päälle");
        Ohje ohje2 = new Ohje("Sekoita sokeri, kermaviili ja kananmuna");
        Ohje ohje3 = new Ohje("Lisää vanilliinisokeri");
        
        ohjeet.lisaa(ohje1);
        ohjeet.lisaa(ohje2);
        ohjeet.lisaa(ohje3);
        
        System.out.println(ohjeet.toString());
        ohjeet.tulostaOhjeet(System.out);
        
        ohjeet.poista(0);
        System.out.println();
        
        System.out.println(ohjeet.toString());
        ohjeet.tulostaOhjeet(System.out);
    }
}
