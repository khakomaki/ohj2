package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import kanta.TietueHallitsija;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Reseptit extends TietueHallitsija {

    private Suodattimet suodattimet;
    private Suodatin hintaSuodatin;
    private Suodatin valmistusaikaSuodatin;
    private Suodatin tahdetSuodatin;
    private Suodatin vaativuusSuodatin;
    private String tiedostoNimi;
    private int juoksevaId;
    
    /**
     * Reseptit.
     * Hallitsee yksittäisiä reseptejä.
     * 
     * Alustaa tallennustiedoston nimellä "reseptit.dat".
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|1|reseptit.dat";
     * </pre>
     */
    public Reseptit() {
        super();
        this.tiedostoNimi = "reseptit.dat";
        luoSuodattimet();
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
     * reseptit.toString() === "0|1|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("");
     * reseptit.toString() === "0|1|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("salaiset_reseptit.txt");
     * reseptit.toString() === "0|1|salaiset_reseptit.txt";
     * 
     * reseptit.setTiedostoNimi(null);
     * reseptit.toString() === "0|1|salaiset_reseptit.txt";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) { return; }
        if (tiedostonimi.length() < 1) { return; }
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * Luo Resepteille Suodattimet
     */
    public void luoSuodattimet() {
        Suodattimet reseptienSuodattimet = new Suodattimet();
        this.suodattimet = reseptienSuodattimet;
        
        this.hintaSuodatin = reseptienSuodattimet.lisaaSuodatin("Hinta");
        this.hintaSuodatin.luoVaihtoehdot(new String[]{ "€", "€€", "€€€" });
        
        this.valmistusaikaSuodatin = reseptienSuodattimet.lisaaSuodatin("Valmistusaika");
        this.valmistusaikaSuodatin.luoVaihtoehdot(new String[]{ "välitön", "nopea", "keskimääräinen", "pitkä", "extra pitkä" });
        
        this.tahdetSuodatin = reseptienSuodattimet.lisaaSuodatin("Tähdet");
        this.tahdetSuodatin.luoVaihtoehdot(new String[]{ "☆", "☆☆", "☆☆☆", "☆☆☆☆", "☆☆☆☆☆" });
        
        this.vaativuusSuodatin = reseptienSuodattimet.lisaaSuodatin("Vaativuus");
        this.vaativuusSuodatin.luoVaihtoehdot(new String[]{ "helppo", "kohtalaisen helppo", "keskimääräinen", "kohtalaisen työläs", "työläs" });
    }
    
    
    /**
     * @param indeksi mistä indeksistä annetaan Suodatin
     * @return Suodatin halutusta indeksistä tai null
     */
    public Suodatin annaSuodatinIndeksista(int indeksi) {
        return this.suodattimet.annaIndeksista(indeksi);
    }
    
    
    /**
     * @param nimi reseptin nimi
     * @return luotu resepti
     */
    public Resepti lisaaResepti(String nimi) {
        Resepti resepti = new Resepti(this.juoksevaId, nimi);
        resepti.setHintaSuodatin(this.hintaSuodatin);
        resepti.setValmistusaikaSuodatin(this.valmistusaikaSuodatin);
        resepti.setTahdetSuodatin(this.tahdetSuodatin);
        resepti.setVaativuusSuodatin(this.vaativuusSuodatin);
        lisaa(resepti);
        this.juoksevaId++;
        return resepti;
    }
    
    
    /**
     * @param indeksi mistä indeksistä yritetään ottaa Resepti
     * @return indeksissä ollut Resepti tai null
     */
    public Resepti annaIndeksista(int indeksi) {
        // varmistetaan että olio on tyyppiä Ainesosa
        Object olio = getOlio(indeksi);
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
        if (getLkm() < maara) { tulostettavaMaara = getLkm(); }
        
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
        tulostaReseptit(os, getLkm());
    }
    
    
    /**
     * @param hakusana millä hakusanalla haetaan kaikki reseptit jotka sisältävät kyseisen sanan
     * @return kaikki reseptit jotka sisältävät hakusanan
     */
    public ArrayList<Resepti> etsiNimella(String hakusana) {
        ArrayList<Resepti> loydetyt = new ArrayList<Resepti>();
        
        StringBuilder regexLause = new StringBuilder();
        regexLause.append(".*");
        regexLause.append(hakusana);
        regexLause.append(".*");
        
        // käydään reseptien nimet läpi regex lauseella
        for (int i = 0; i < getLkm(); i++) {
            Resepti resepti = annaIndeksista(i);
            if (resepti.getNimi().matches(regexLause.toString())) {
                loydetyt.add(resepti);
            }
        }
        
        return loydetyt;
    }
    
    
    /**
     * Luo lisää mustikkapiirakan resepteihin testaamista varten.
     * TODO: poista kun ei enää tarvita
     */
    public void lisaaMustikkapiirakka() {
        Resepti mustikkapiirakka = new Resepti(1, "");
        mustikkapiirakka.setHintaSuodatin(this.hintaSuodatin);
        mustikkapiirakka.setValmistusaikaSuodatin(this.valmistusaikaSuodatin);
        mustikkapiirakka.setTahdetSuodatin(this.tahdetSuodatin);
        mustikkapiirakka.setVaativuusSuodatin(this.vaativuusSuodatin);
        mustikkapiirakka.luoMustikkapiirakka(this.juoksevaId);
        this.lisaa(mustikkapiirakka);
        this.juoksevaId++;
    }
    
    
    @Override
    /**
     * Tiedot muodossa "lukumäärä|maksimi lukumäärä|tiedostonimi"
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|1|reseptit.dat";
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
