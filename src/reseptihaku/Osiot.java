package reseptihaku;

import kanta.TietueHallitsija;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Osiot extends TietueHallitsija {

    private String tiedostoNimi;
    private int juoksevaId;
    
    /**
     * 
     */
    public Osiot() {
        super();
        this.tiedostoNimi = "resepti_osiot.dat";
        this.juoksevaId = 0;
        // kasvatetaan osioiden tilaa yksi kerrallaan 2x sijasta
        setKerroin(1);
        setVakio(1);
    }
    
    
    /**
     * Asettaa tiedosto nimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
     * 
     * @param tiedostonimi tiedoston nimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|1|resepti_osiot.dat";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) { return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * @param nimi minkä niminen osio lisätään
     * @return lisätty osio
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|1|resepti_osiot.dat";
     * Osio pizzapohja = osiot.lisaaOsio("Pizzapohja");
     * osiot.toString() === "1|1|resepti_osiot.dat";
     * pizzapohja.toString() === "0|Pizzapohja";
     * 
     * Osio tomaattikastike = osiot.lisaaOsio("Tomaattikastike");
     * osiot.toString() === "2|2|resepti_osiot.dat";
     * tomaattikastike.toString() === "1|Tomaattikastike";
     * 
     * Osio taytteet = osiot.lisaaOsio("Täytteet");
     * osiot.toString() === "3|3|resepti_osiot.dat";
     * taytteet.toString() === "2|Täytteet";
     * 
     * osiot.lisaaOsio("Täytteet (vaihtoehto 2)");
     * osiot.toString() === "4|4|resepti_osiot.dat";
     * 
     * osiot.lisaaOsio("Täytteet (vaihtoehto 3)");
     * osiot.toString() === "5|5|resepti_osiot.dat";
     * </pre>
     */
    public Osio lisaaOsio(String nimi) {
        Osio osio = new Osio(this.juoksevaId, nimi);
        lisaa(osio);
        this.juoksevaId++;
        return osio;
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan Osio
     * @return indeksissä ollut Osio tai null
     */
    public Osio annaIndeksista(int indeksi) {
        // varmistetaan että olio on tyyppiä Ainesosa
        Object olio = getOlio(indeksi);
        if (olio.getClass() != Osio.class) { return null; }
        return (Osio)olio;
    }
    
    
    @Override
    /**
     * Palauttaa tiedot muodossa lukumäärä|maksimi lukumäärä|tiedostonimi
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|1|resepti_osiot.dat";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getLkm());
        sb.append('|');
        sb.append(getMaxLkm());
        sb.append('|');
        sb.append(this.tiedostoNimi);
        return sb.toString();
    }
    
    
    public static void main(String[] args) {
        Osiot pizzanOsiot = new Osiot();
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaaOsio("Pizzapohja");
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaaOsio("Tomaatti kastike");
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaaOsio("Täytteet");
        System.out.println(pizzanOsiot.toString());
        
        System.out.println("Lukumäärä: " + pizzanOsiot.getLkm() + ", maksimi lukumäärä: " + pizzanOsiot.getMaxLkm());
        Osio pizzapohja = pizzanOsiot.annaIndeksista(0);
        Osio tomaattikastike = pizzanOsiot.annaIndeksista(1);
        Osio taytteet = pizzanOsiot.annaIndeksista(2);
        System.out.println(pizzapohja + "\n" + tomaattikastike + "\n" + taytteet);
    }
}
