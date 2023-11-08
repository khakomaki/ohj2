package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Reseptit {

    private ArrayList<Resepti> reseptit = new ArrayList<Resepti>();
    private String tiedostoNimi;
    private int juoksevaId;
    private int lkm;
    
    /**
     * Reseptit.
     * Hallitsee yksittäisiä reseptejä.
     * 
     * Alustaa tallennustiedoston nimellä "reseptit.dat".
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit.dat";
     * </pre>
     */
    public Reseptit() {
        this.tiedostoNimi = "reseptit.dat";
    }
    
    
    /**
     * Asettaa tiedostonimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
     * 
     * @param tiedostonimi tiedostonimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("");
     * reseptit.toString() === "0|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("salaiset_reseptit.txt");
     * reseptit.toString() === "0|salaiset_reseptit.txt";
     * 
     * reseptit.setTiedostoNimi(null);
     * reseptit.toString() === "0|salaiset_reseptit.txt";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) { return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * @param nimi reseptin nimi
     * @return luotu resepti
     */
    public Resepti lisaaResepti(String nimi) {
        Resepti resepti = new Resepti(this.juoksevaId, nimi);
        reseptit.add(resepti);
        this.juoksevaId++;
        this.lkm++;
        return resepti;
    }
    
    
    /**
     * @param resepti lisättävä resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit.dat";
     * 
     * reseptit.lisaa(new Resepti());
     * reseptit.toString() === "1|reseptit.dat";
     * 
     * reseptit.lisaa(new Resepti());
     * reseptit.lisaa(new Resepti());
     * reseptit.lisaa(new Resepti());
     * reseptit.toString() === "4|reseptit.dat";
     * </pre>
     */
    public void lisaa(Resepti resepti) {
        if (resepti == null) { return; }
        reseptit.add(resepti);
        this.juoksevaId++;
        this.lkm++;
    }
    
    
    /**
     * @param indeksi mistä indeksistä yritetään ottaa Resepti
     * @return indeksissä ollut Resepti tai null
     */
    public Resepti annaIndeksista(int indeksi) {
        // varmistetaan että olio on tyyppiä Ainesosa
        Object olio = reseptit.get(indeksi);
        if (olio.getClass() != Resepti.class) { return null; }
        return (Resepti)olio;
    }
    
    
    /**
     * @param os tietovirta johon halutaan tulostaa
     * @param maara kuinka monta reseptia tulostetaan
     */
    public void tulostaReseptit(OutputStream os, int maara) {
        int tulostettavaMaara = maara;
        
        // ei tulosta mitään jos annettu luku on negatiivinen
        if (maara < 0) { return; }
        
        // tulostaa lukumäärän verran jos annettu määrä olisi enemmän
        if (this.lkm < maara) { tulostettavaMaara = this.lkm; }
        
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < tulostettavaMaara; i++) {
            out.print(this.annaIndeksista(i));
            out.print("\n");
        }
    }
    
    
    /**
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaReseptit(OutputStream os) {
        tulostaReseptit(os, this.lkm);
    }
    
    
    /**
     * Palauttaa hakusanaa vastaavat reseptit.
     * Jos hakusana on tyhjä, palauttaa kaikki reseptit.
     * 
     * @param hakusana millä hakusanalla haetaan kaikki reseptit jotka sisältävät kyseisen sanan
     * @return kaikki reseptit jotka sisältävät hakusanan
     */
    public ArrayList<Resepti> etsiNimella(String hakusana) {
        ArrayList<Resepti> loydetytReseptit = new ArrayList<Resepti>();
        String kaytettavaHakusana = hakusana.strip();
        
        // tyhjällä hakusanalla palautetaan kaikki reseptit
        if (kaytettavaHakusana.isBlank()) { return this.reseptit; }
        
        for (int i = 0; i < this.lkm; i++) {
            Resepti resepti = annaIndeksista(i);
            if (resepti.onkoNimessa(kaytettavaHakusana)) { loydetytReseptit.add(resepti); } 
        }
        
        return loydetytReseptit;
    }
    
    
    /**
     * Luo lisää mustikkapiirakan resepteihin testaamista varten.
     * TODO: poista kun ei enää tarvita
     * @return mustikkapiirakka resepti
     */
    public Resepti lisaaMustikkapiirakka() {
        Resepti mustikkapiirakka = new Resepti(1, "");
        mustikkapiirakka.luoMustikkapiirakka(this.juoksevaId);
        lisaa(mustikkapiirakka);
        return mustikkapiirakka;
    }
    
    
    @Override
    /**
     * Tiedot muodossa "lukumäärä|tiedostonimi"
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit.dat";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.lkm);
        sb.append('|');
        sb.append(this.tiedostoNimi);
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Reseptit reseptit = new Reseptit();
        System.out.println(reseptit);
        
        Resepti mustikkapiirakka = reseptit.lisaaResepti("Mustikkapiirakka");
        reseptit.lisaaResepti("Pizza");
        reseptit.lisaaResepti("Lihapiirakka");
        
        System.out.println(reseptit + "\n");
        
        reseptit.tulostaReseptit(System.out);
        
        mustikkapiirakka.setHinta(2);
        mustikkapiirakka.setValmistusaika(2);
        mustikkapiirakka.setTahdet(3);
        mustikkapiirakka.setVaativuus(1);
        System.out.println(mustikkapiirakka);
    }
}
