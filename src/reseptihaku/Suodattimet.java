package reseptihaku;

import java.util.ArrayList;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Suodattimet {

    private ArrayList<Suodatin> suodattimet;
    private int lkm;
    
    /**
     * 
     */
    public Suodattimet() {
        this.suodattimet = new ArrayList<Suodatin>();
    }
    
    
    /**
     * @param nimi suodattimen nimi
     * @return luotu suodatin
     */
    public Suodatin lisaaSuodatin(String nimi) {
        Suodatin suodatin = new Suodatin(nimi);
        suodattimet.add(lkm, suodatin);
        this.lkm++;
        return suodatin;
    }
    
    
    /**
     * @param indeksi mistä indeksistä yritetään etsiä Suodatin
     * @return indeksistä löytynyt Suodatin tai null
     */
    public Suodatin annaIndeksista(int indeksi) {
        return suodattimet.get(indeksi);
    }
    
    
    @Override
    /**
     * Tiedot muodossa "lukumäärä"
     * 
     * @example
     * <pre name="test">
     * Suodattimet suodattimet = new Suodattimet();
     * suodattimet.toString() === "0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.lkm);
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Suodattimet suodattimet = new Suodattimet();
        System.out.println(suodattimet.toString());
        
        Suodatin hinta = suodattimet.lisaaSuodatin("Hinta");
        suodattimet.lisaaSuodatin("Valmistusaika");
        System.out.println(suodattimet.toString());
        
        Suodatin valmistusaika = suodattimet.annaIndeksista(1);
        
        System.out.println(hinta + "\n" + valmistusaika);
    }
}
