package reseptihaku;

import kanta.TietueHallitsija;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class Ainesosat extends TietueHallitsija {

    private String tiedostoNimi;
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
        super();
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
     * @param ainesosanNimi minkä niminen ainesosa halutaan
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
    public Ainesosa anna(String ainesosanNimi) {
        for (int i = 0; i < getLkm(); i++) {
            Ainesosa ainesosa = annaIndeksista(i);
            
            // varmistetaan ettei null ja verrataan nimiä
            if (ainesosa == null) { continue; }
            if (ainesosa.getNimi().equals(ainesosanNimi)) { return ainesosa; }
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
        for (int i = 0; i < getLkm(); i++) {
            Ainesosa ainesosa = annaIndeksista(i);
            
            // varmistetaan ettei null ja verrataan tunnuksia
            if (ainesosa == null) { continue; }
            if (ainesosa.getId() == tunnus) { return ainesosa; }
        }
        
        // jos ei löytynyt niin palautetaan null
        return null;
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan olio
     * @return indeksissä ollut olio tai null
     */
    public Ainesosa annaIndeksista(int indeksi) {
        // varmistetaan että olio on tyyppiä Ainesosa
        Object olio = this.getOlio(indeksi);
        if (olio.getClass() != Ainesosa.class) { return null; }
        return (Ainesosa)olio;
    }
    
    
    /**
     * @param ainesosaNimi minkä niminen ainesosa lisätään
     * @return luotu ainesosa
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
    public Ainesosa lisaaAinesosa(String ainesosaNimi) {
        // uusi ainesosa ja kasvatetaan tunnuslukua
        Ainesosa ainesosa = new Ainesosa(this.annettavaTunnusLuku, ainesosaNimi);
        this.annettavaTunnusLuku++;

        lisaa(ainesosa);
        
        return ainesosa;
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
        return "" + this.tiedostoNimi + "|" + getLkm() + "|" + getMaxLkm();
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
