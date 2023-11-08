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
     * ainesosat.toString() === "ainesosat.dat|0|1";
     * </pre>
     */
    public Ainesosat() {
        super();
        this.annettavaTunnusLuku = 0;
        this.tiedostoNimi = "ainesosat.dat";
    }
    
    
    /**
     * Asettaa tiedosto nimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
     * 
     * @param tiedostonimi tiedoston nimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "ainesosat.dat|0|1";
     * 
     * ainesosat.setTiedostoNimi("porkkanat.dat");
     * ainesosat.toString() === "porkkanat.dat|0|1";
     * 
     * ainesosat.setTiedostoNimi("");
     * ainesosat.toString() === "porkkanat.dat|0|1";
     * 
     * ainesosat.setTiedostoNimi("banaanit.txt");
     * ainesosat.toString() === "banaanit.txt|0|1";
     * 
     * ainesosat.setTiedostoNimi(null);
     * ainesosat.toString() === "banaanit.txt|0|1";
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
     * ainesosat.toString() === "ainesosat.dat|0|1";
     * ainesosat.anna("ketsuppia") === null;
     * 
     * ainesosat.lisaaAinesosa("ketsuppia");
     * ainesosat.toString() === "ainesosat.dat|1|1";
     * ainesosat.anna("ketsuppia").toString() === "0|ketsuppia";
     * 
     * ainesosat.lisaaAinesosa("sinappia");
     * ainesosat.toString() === "ainesosat.dat|2|2";
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
    
    
    /**
     * @param ainesosa lisättävä ainesosa
     * @return lisätty ainesosa
     */
    public Ainesosa lisaaAinesosa(Ainesosa ainesosa) {
        Ainesosa lisattavaAinesosa = lisaaAinesosa(ainesosa.getNimi());
        return lisattavaAinesosa;
    }
    
    
    /**
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaAinesosat(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < getLkm(); i++) {
            out.print(this.annaIndeksista(i));
            out.print("\n");
        }
    }
    
    
    @Override
    /**
     * Palauttaa olennaisia tietoja luokan oliosta muodossa "tiedostonimi|lukumäärä|maximilukumäärä".
     * 
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.toString() === "ainesosat.dat|0|1";
     * 
     * ainesosat.lisaaAinesosa("punasipuli");
     * ainesosat.toString() === "ainesosat.dat|1|1";
     * 
     * ainesosat.lisaaAinesosa("valkosipuli");
     * ainesosat.lisaaAinesosa("keltasipuli");
     * ainesosat.toString() === "ainesosat.dat|3|4";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.tiedostoNimi);
        sb.append('|');
        sb.append(getLkm());
        sb.append('|');
        sb.append(getMaxLkm());
        return sb.toString();
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ainesosat ainesosat = new Ainesosat();
     * ainesosat.lisaaAinesosa(new Ainesosa("porkkana"));
     * ainesosat.lisaaAinesosa(new Ainesosa("peruna"));
     * ainesosat.lisaaAinesosa(new Ainesosa("sipuli"));
     * 
     * Ainesosat kopioAinesosat = ainesosat.clone();
     * ainesosat.toString().equals(kopioAinesosat.toString()) === true;
     * 
     * kopioAinesosat.annaIndeksista(0).equals(new Ainesosa("porkkana")) === true;
     * kopioAinesosat.annaIndeksista(1).equals(new Ainesosa("peruna")) === true;
     * kopioAinesosat.annaIndeksista(2).equals(new Ainesosa("sipuli")) === true;
     * 
     * ainesosat.setTiedostoNimi("vihannekset.txt");
     * kopioAinesosat.lisaa(new Ainesosa("valkosipuli"));
     * ainesosat.toString() === "vihannekset.txt|3|4";
     * kopioAinesosat.toString() === "ainesosat.dat|4|4";
     * 
     * ainesosat = new Ainesosat();
     * kopioAinesosat = ainesosat.clone();
     * ainesosat.toString().equals(kopioAinesosat.toString()) === true;
     * </pre>
     */
    public Ainesosat clone() {
        Ainesosat kopio = new Ainesosat();
        
        for (int i = 0; i < this.getLkm(); i++) {
            Ainesosa kopioAinesosa = annaIndeksista(i).clone();
            kopio.lisaa(kopioAinesosa);
        }
        kopio.annettavaTunnusLuku = this.annettavaTunnusLuku;
        kopio.tiedostoNimi = this.tiedostoNimi;
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ainesosat ainesosat1 = new Ainesosat();
     * Ainesosat ainesosat2 = new Ainesosat();
     * ainesosat1.equals(ainesosat2) === true;
     * 
     * ainesosat1.equals(null) === false;
     * ainesosat1.equals(new Object()) === false;
     * 
     * ainesosat1.setTiedostoNimi("ainesosat.txt");
     * ainesosat1.equals(ainesosat2) === false;
     * 
     * ainesosat2.setTiedostoNimi("ainesosat.txt");
     * ainesosat1.equals(ainesosat2) === true;
     * 
     * ainesosat1.lisaaAinesosa(new Ainesosa("porkkana"));
     * ainesosat1.equals(ainesosat2) === false;
     * 
     * ainesosat2.lisaaAinesosa(new Ainesosa("porkkana"));
     * ainesosat1.equals(ainesosat2) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) { return false; }
        if (verrattava.getClass() != this.getClass()) { return false; }
        
        Ainesosat verrattavaAinesosat = (Ainesosat)verrattava;
        
        if (verrattavaAinesosat.annettavaTunnusLuku != this.annettavaTunnusLuku) { return false; }
        if (verrattavaAinesosat.tiedostoNimi != this.tiedostoNimi) { return false; }
        if (verrattavaAinesosat.getLkm() != this.getLkm()) { return false; }
        
        // verrataan kaikki alkiot
        for (int i = 0; i < this.getLkm(); i++) {
            if (!verrattavaAinesosat.annaIndeksista(i).equals(this.annaIndeksista(i))) { return false; }
        }
        
        return true;
    }
    
    

    @Override
    /**
     * @example
     * <pre name="test">
     * Ainesosat ainesosat1 = new Ainesosat();
     * Ainesosat ainesosat2 = new Ainesosat();
     * ainesosat1.hashCode() == ainesosat2.hashCode() === true;
     * 
     * ainesosat1.setTiedostoNimi("ainesosat.txt");
     * ainesosat1.hashCode() == ainesosat2.hashCode() === false;
     * 
     * ainesosat2.setTiedostoNimi("ainesosat.txt");
     * ainesosat1.hashCode() == ainesosat2.hashCode() === true;
     * 
     * ainesosat1.lisaaAinesosa(new Ainesosa("porkkana"));
     * ainesosat1.hashCode() == ainesosat2.hashCode() === false;
     * 
     * ainesosat2.lisaaAinesosa(new Ainesosa("porkkana"));
     * ainesosat1.hashCode() == ainesosat2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.annettavaTunnusLuku);
        hash = Hajautus.hajautusString(hash, this.tiedostoNimi);
        
        for (int i = 0; i < this.getLkm(); i++) {
            hash = Hajautus.hajautusInt(hash, this.annaIndeksista(i).hashCode());
        }
        
        return hash;
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
        
        ainesosat.tulostaAinesosat(System.out);
    }
}
