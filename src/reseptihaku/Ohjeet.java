package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import kanta.Hajautus;

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
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(15);
     * ohjeet.toString() === "ohjeet.dat|15|0";
     * 
     * Ohje ohje1 = new Ohje("ensimmäinen", 100);
     * ohje1.getVaihe() === 100;
     * ohjeet.lisaa(ohje1);
     * ohje1.getVaihe() === 1;
     * ohjeet.toString() === "ohjeet.dat|15|1";
     * 
     * Ohje ohje2 = new Ohje("toinen", 75);
     * ohje2.getVaihe() === 75;
     * ohjeet.lisaa(ohje2);
     * ohje2.getVaihe() === 2;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet.dat|15|4";
     * 
     * ohjeet.lisaa(null);
     * ohjeet.toString() === "ohjeet.dat|15|5";
     * </pre>
     */
    public void lisaa(Ohje ohje) {
        Ohje lisattavaOhje = ohje;
        
        // luo uuden ohjeen jos annetaan null
        if (ohje == null) lisattavaOhje = new Ohje();
        this.ohjeet.add(lisattavaOhje);
        this.lkm++;
        paivitaVaiheet();
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
        if (indeksi < 0 || lkm <= indeksi) return;
        
        ohjeet.remove(indeksi);
        this.lkm--;
        paivitaVaiheet();
    }
    
    
    /**
     * @param poistettava mikä ohje poistetaan
     * @return ohjeiden lukumäärä mahdollisen poiston jälkeen
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * Ohje ohje1 = new Ohje("ohje 1"); ohjeet.lisaa(ohje1);
     * Ohje ohje2 = new Ohje("ohje 2"); ohjeet.lisaa(ohje2);
     * Ohje ohje3 = new Ohje("ohje 3"); ohjeet.lisaa(ohje3);
     * Ohje ohje4 = new Ohje("ohje 4"); ohjeet.lisaa(ohje4);
     * Ohje ohje5 = new Ohje("ohje 5"); ohjeet.lisaa(ohje5);
     * ohjeet.toString() === "ohjeet.dat|0|5";
     * 
     * ohjeet.anna(1).equals(ohje2) === true;
     * ohje2.getVaihe() === 2;
     * 
     * ohjeet.poista(ohje2);
     * ohjeet.toString() === "ohjeet.dat|0|4";
     * ohjeet.anna(1).equals(ohje2) === false;
     * 
     * ohjeet.poista(new Ohje("ohje 5"));
     * ohjeet.toString() === "ohjeet.dat|0|4";
     * 
     * ohjeet.poista(new Ohje("ohje 5", 4));
     * ohjeet.toString() === "ohjeet.dat|0|3";
     * 
     * ohjeet.poista(ohje2);
     * ohjeet.toString() === "ohjeet.dat|0|3";
     * </pre>
     */
    public int poista(Ohje poistettava) {
        for (int i = 0; i < this.lkm; i++) {
            if (this.ohjeet.get(i).equals(poistettava)) {
                ohjeet.remove(i);
                this.lkm--;
                paivitaVaiheet();
                break;
            }
        }
        return this.lkm;
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
        if (indeksi < 0 || this.lkm <= indeksi) return null;
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
        if (tiedostonimi == null) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * @return tiedoston nimi johon tallennetaan, null jos ei ole määritetty
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.getTiedostonimi() === "ohjeet.dat";
     * 
     * ohjeet.setTiedostoNimi("uudet_ohjeet.txt");
     * ohjeet.getTiedostonimi() === "uudet_ohjeet.txt";
     * </pre>
     */
    public String getTiedostonimi() {
        return this.tiedostoNimi;
    }
    
    
    /**
     * @return ohjeiden lukumäärä
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * 
     * ohjeet.getLkm() === 0;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.getLkm() === 1;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.getLkm() === 4;
     * </pre>
     */
    public int getLkm() {
        return this.lkm;
    }
    
    
    /**
     * Päivittää ohjeiden sisältämien ohjeiden vaihe numerot vastaamaan
     * taulukon järjestystä. Alkavat numerosta 1.
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * 
     * ohjeet.anna(0).getVaihe() === 1;
     * ohjeet.anna(1).getVaihe() === 2;
     * ohjeet.anna(2).getVaihe() === 3;
     * 
     * ohjeet.anna(1).setVaihe(555);
     * ohjeet.anna(1).getVaihe() === 555;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.anna(1).getVaihe() === 2;
     * 
     * </pre>
     */
    private void paivitaVaiheet() {
        for (int i = 0; i < this.lkm; i++) {
            Ohje ohje = this.ohjeet.get(i);
            ohje.setVaihe(i + 1);
        }
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
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(2);
     * ohjeet.setTiedostoNimi("mustikkapiirakka.txt");
     * ohjeet.lisaa(new Ohje("Sekoita sokeri ja vehnäjauhot"));
     * ohjeet.lisaa(new Ohje("Lisää kananmuna"));
     * ohjeet.toString() === "mustikkapiirakka.txt|2|2";
     * 
     * Ohjeet kopioOhjeet = ohjeet.clone();
     * kopioOhjeet.toString() === "mustikkapiirakka.txt|2|2";
     * kopioOhjeet.anna(0).getOhjeistus() === "Sekoita sokeri ja vehnäjauhot";
     * kopioOhjeet.anna(1).getOhjeistus() === "Lisää kananmuna";
     * 
     * ohjeet.anna(1).setOhjeistus("Lisää vanilliinisokeri");
     * ohjeet.anna(1).getOhjeistus() === "Lisää vanilliinisokeri";
     * kopioOhjeet.anna(1).getOhjeistus() === "Lisää kananmuna";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "mustikkapiirakka.txt|2|4";
     * kopioOhjeet.toString() === "mustikkapiirakka.txt|2|2";
     * </pre>
     */
    public Ohjeet clone() {
        Ohjeet kopio = new Ohjeet();
        kopio.lkm = this.lkm;
        kopio.osioId = this.osioId;
        kopio.tiedostoNimi = this.tiedostoNimi;
        
        for (int i = 0; i < this.lkm; i++) {
            kopio.ohjeet.add(this.ohjeet.get(i).clone());
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ohjeet ohjeet1 = new Ohjeet();
     * Ohjeet ohjeet2 = new Ohjeet();
     * ohjeet1.equals(ohjeet2) === true;
     * 
     * ohjeet1.lisaa(new Ohje());
     * ohjeet1.equals(ohjeet2) === false;
     * 
     * ohjeet2.lisaa(new Ohje());
     * ohjeet1.equals(ohjeet2) === true;
     * 
     * Ohje sokeri = new Ohje("Lisää sokeri");
     * Ohje kananmuna = new Ohje("Lisää kananmuna");
     * 
     * ohjeet2.lisaa(sokeri);
     * ohjeet1.equals(ohjeet2) === false;
     * 
     * ohjeet1.lisaa(kananmuna);
     * ohjeet1.equals(ohjeet2) === false;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Ohjeet verrattavaOhjeet = (Ohjeet)verrattava;
        if (verrattavaOhjeet.lkm != this.lkm) return false;
        if (verrattavaOhjeet.osioId != this.osioId) return false;
        if (!verrattavaOhjeet.tiedostoNimi.equals(this.tiedostoNimi)) return false;
        
        // verrataan yksittäiset ohjeet
        for (int i = 0; i < this.lkm; i++) {
            if (!verrattavaOhjeet.ohjeet.get(i).equals(this.ohjeet.get(i))) return false;
        }
        
        return true;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ohjeet ohjeet1 = new Ohjeet();
     * Ohjeet ohjeet2 = new Ohjeet();
     * ohjeet1.hashCode() == ohjeet2.hashCode() === true;
     * 
     * ohjeet1.lisaa(new Ohje());
     * ohjeet1.hashCode() == ohjeet2.hashCode() === false;
     * 
     * ohjeet2.lisaa(new Ohje());
     * ohjeet1.hashCode() == ohjeet2.hashCode() === true;
     * 
     * Ohje sokeri = new Ohje("Lisää sokeri");
     * Ohje kananmuna = new Ohje("Lisää kananmuna");
     * 
     * ohjeet2.lisaa(sokeri);
     * ohjeet1.hashCode() == ohjeet2.hashCode() === false;
     * 
     * ohjeet1.lisaa(kananmuna);
     * ohjeet1.hashCode() == ohjeet2.hashCode() === false;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.osioId);
        hash = Hajautus.hajautusString(hash, this.tiedostoNimi);
        
        for (int i = 0; i < this.lkm; i++) {
            hash = Hajautus.hajautusInt(hash, this.ohjeet.get(i).hashCode());
        }
        
        return hash;
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
