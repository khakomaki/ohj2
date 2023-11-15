package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import kanta.Hajautus;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Reseptit {

    private ArrayList<Resepti> reseptit = new ArrayList<Resepti>();
    private String tiedostoNimi         = "reseptit.dat";
    private int juoksevaId              = 1;
    private int lkm                     = 0;
    
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
        //
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
        if (tiedostonimi == null) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostoNimi = tiedostonimi;
    }
    
    
    /**
     * Luo uuden reseptin nimellä ja palauttaa sen.
     * Antaa tunnuksen juoksevalla id:llä.
     * 
     * @param nimi reseptin nimi
     * @return luotu resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.toString() === "3|reseptit.dat";
     * 
     * resepti1.getTunnus() === 1;
     * resepti2.getTunnus() === 2;
     * resepti3.getTunnus() === 3;
     * </pre>
     */
    public Resepti lisaa(String nimi) {
        Resepti resepti = new Resepti(this.juoksevaId, nimi);
        reseptit.add(resepti);
        this.juoksevaId++;
        this.lkm++;
        return resepti;
    }
    
    
    /**
     * Lisää uuden reseptin.
     * Varmistaa että tunnukset pysyvät kasvavassa järjestyksessä.
     * 
     * @param resepti lisättävä resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * 
     * Resepti mustikkapiirakka = new Resepti("Mustikkapiirakka");
     * mustikkapiirakka.getTunnus() === -1;
     * reseptit.lisaa(mustikkapiirakka);
     * mustikkapiirakka.getTunnus() === 1;
     * reseptit.toString() === "1|reseptit.dat";
     * 
     * Resepti lihapiirakka = new Resepti(12, "Lihapiirakka");
     * lihapiirakka.getTunnus() === 12;
     * reseptit.lisaa(lihapiirakka);
     * lihapiirakka.getTunnus() === 12;
     * 
     * Resepti kasvispiirakka = new Resepti(5, "Kasvispiirakka");
     * kasvispiirakka.getTunnus() === 5;
     * reseptit.lisaa(kasvispiirakka);
     * kasvispiirakka.getTunnus() === 13;
     * 
     * Resepti makaronilaatikko = new Resepti(13, "Makaronilaatikko");
     * makaronilaatikko.getTunnus() === 13;
     * reseptit.lisaa(makaronilaatikko);
     * makaronilaatikko.getTunnus() === 14;
     * reseptit.toString() === "4|reseptit.dat";
     * 
     * Resepti maksalaatikko = new Resepti(15, "Maksalaatikko");
     * maksalaatikko.getTunnus() === 15;
     * reseptit.lisaa(maksalaatikko);
     * maksalaatikko.getTunnus() === 15;
     * </pre>
     */
    public void lisaa(Resepti resepti) {
        if (resepti == null) { return; }
        Resepti lisattavaResepti = resepti;
        
        // asettaa tunnukseksi juoksevan id:n jos tunnus ei ole kasvavassa järjestyksessä
        int reseptinTunnus = lisattavaResepti.getTunnus();        
        if (reseptinTunnus < this.juoksevaId) lisattavaResepti.setTunnus(this.juoksevaId);
        
        // vaihtaa juoksevan id:n reseptin tunnukseen, jos se on enemmän kuin juokseva id
        if (this.juoksevaId < reseptinTunnus) this.juoksevaId = reseptinTunnus;
        
        reseptit.add(lisattavaResepti);
        this.juoksevaId++;
        this.lkm++;
    }
    
    
    /**
     * Antaa annetussa indeksissä olevan reseptin.
     * Palauttaa null jos indeksi ei ollut mieluisa.
     * 
     * @param indeksi mistä indeksistä yritetään ottaa Resepti
     * @return indeksissä ollut Resepti tai null
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.toString() === "3|reseptit.dat";
     * 
     * reseptit.anna(-1) == null === true;
     * reseptit.anna(0) == resepti1 === true;
     * reseptit.anna(1) == resepti2 === true;
     * reseptit.anna(2) == resepti3 === true;
     * </pre>
     */
    public Resepti anna(int indeksi) {
        if (indeksi < 0 || this.lkm < indeksi) return null;
        return reseptit.get(indeksi);
    }
    
    
    /**
     * Vaihtaa vanhan resepti-viitteen tilalle uuden.
     * Ei tee mitään jos jompi kumpi resepteistä on null-viite tai vaihdettavaa reseptiä ei löydy.
     * 
     * @param vanhaResepti minkä reseptin tilalle vaihdetaan
     * @param uusiResepti tilalle vaihdettava resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.anna(1) == resepti2 === true;
     * 
     * Resepti resepti4 = new Resepti(1, "Kasvispiirakka");
     * reseptit.vaihdaResepti(resepti4, resepti2);
     * 
     * reseptit.vaihdaResepti(resepti2, resepti4);
     * reseptit.anna(1) == resepti4 === true;
     * 
     * reseptit.vaihdaResepti(resepti2, resepti1);
     * reseptit.anna(1) == resepti4 === true;
     * 
     * reseptit.vaihdaResepti(resepti1, resepti1);
     * reseptit.anna(0) == resepti1 === true;
     * 
     * reseptit.vaihdaResepti(resepti3, resepti1);
     * reseptit.anna(2) == resepti1 === true;
     * 
     * reseptit.vaihdaResepti(resepti3, null);
     * reseptit.anna(2) == resepti1 === true;
     * 
     * reseptit.vaihdaResepti(null, resepti1);
     * reseptit.anna(2) == resepti1 === true;
     * </pre>
     */
    public void vaihdaResepti(Resepti vanhaResepti, Resepti uusiResepti) {
        if (vanhaResepti == null || uusiResepti == null) return;
        
        // varmistaa että uusi resepti käyttää samaa Ainesosat-viitettä kuin luokka
        Resepti vaihdettavaResepti = uusiResepti;
        
        // etsii vaihdettavan indeksin, poistuu jos ei löydy
        int vanhaReseptiIndeksi = this.reseptit.indexOf(vanhaResepti);
        if (vanhaReseptiIndeksi < 0) return;
        
        this.reseptit.set(vanhaReseptiIndeksi, vaihdettavaResepti);
    }
    
    
    /**
     * Tulostaa annetun määrän verran reseptejä
     * 
     * @param os tietovirta johon halutaan tulostaa
     * @param maara kuinka monta reseptia tulostetaan
     */
    public void tulostaReseptit(OutputStream os, int maara) {
        int tulostettavaMaara = maara;
        
        // ei tulosta mitään jos annettu luku on negatiivinen
        if (maara < 0) return;
        
        // tulostaa lukumäärän verran jos annettu määrä olisi enemmän
        if (this.lkm < maara) tulostettavaMaara = this.lkm;
        
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < tulostettavaMaara; i++) {
            out.print(this.anna(i));
            out.print("\n");
        }
    }
    
    
    /**
     * Tulostaa kaikki reseptit
     * 
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaReseptit(OutputStream os) {
        tulostaReseptit(os, this.lkm);
    }
    
    
    /**
     * Poistaa annetun reseptin resepteistä
     * 
     * @param resepti poistettava resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.toString() === "3|reseptit.dat";
     * 
     * reseptit.poista(resepti2);
     * reseptit.toString() === "2|reseptit.dat";
     * 
     * reseptit.poista(null);
     * reseptit.toString() === "2|reseptit.dat";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "1|reseptit.dat";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "1|reseptit.dat";
     * 
     * reseptit.poista(resepti3);
     * reseptit.toString() === "0|reseptit.dat";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "0|reseptit.dat";
     * </pre>
     */
    public void poista(Resepti resepti) {
        int poistettavanIndeksi = reseptit.indexOf(resepti);
        
        // poistutaan jos poistettavaa ei löydetty
        if (poistettavanIndeksi < 0) return;
        
        this.reseptit.remove(poistettavanIndeksi);
        this.lkm--;
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
        if (kaytettavaHakusana.isBlank()) return this.reseptit;
        
        for (int i = 0; i < this.lkm; i++) {
            Resepti resepti = anna(i);
            if (resepti.onkoNimessa(kaytettavaHakusana)) loydetytReseptit.add(resepti);
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
     * Luo täydellisen kopion itsestänsä
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.lisaa(new Resepti(1, "Mustikkapiirakka"));
     * reseptit.lisaa(new Resepti(2, "Juustokakku"));
     * reseptit.toString() === "2|reseptit.dat";
     * 
     * Reseptit reseptitKopio = reseptit.clone();
     * reseptitKopio.toString() === "2|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("herkut.txt");
     * reseptit.toString() === "2|herkut.txt";
     * reseptitKopio.toString() === "2|reseptit.dat";
     * </pre>
     */
    public Reseptit clone() {
        Reseptit kopio = new Reseptit();
        kopio.juoksevaId = this.juoksevaId;
        kopio.lkm = this.lkm;
        kopio.tiedostoNimi = this.tiedostoNimi;
        
        // kopioidaan kaikki yksittäiset reseptit kopioon
        for (int i = 0; i < this.lkm; i++) {
            kopio.reseptit.add(this.reseptit.get(i).clone());
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * Vertailee ovatko Reseptit-oliot samoja
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit1 = new Reseptit();
     * Reseptit reseptit2 = new Reseptit();
     * reseptit1.equals(reseptit2) === true;
     * reseptit2.equals(reseptit1) === true;
     * 
     * reseptit1.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.equals(reseptit2) === false;
     * 
     * reseptit2.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.equals(reseptit2) === true;
     * 
     * reseptit1.lisaa(new Resepti(1, "Mustikkapiirakka"));
     * reseptit1.lisaa(new Resepti(2, "Suklaakakku"));
     * reseptit1.equals(reseptit2) === false;
     * 
     * reseptit2.lisaa(new Resepti(1, "Mustikkapiirakka"));
     * reseptit2.lisaa(new Resepti(2, "Suklaakakku"));
     * reseptit1.equals(reseptit2) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Reseptit verrattavaReseptit = (Reseptit)verrattava;
        
        if (verrattavaReseptit.juoksevaId != this.juoksevaId) return false;
        if (verrattavaReseptit.lkm != this.lkm) return false;
        if (!verrattavaReseptit.tiedostoNimi.equals(this.tiedostoNimi)) return false;
        
        // verrataan yksittäisiä reseptejä toisiinsa
        for (int i = 0; i < this.lkm; i++) {
            if (!verrattavaReseptit.reseptit.get(i).equals(this.reseptit.get(i))) return false;
        }
        
        return true;
    }
    
    
    @Override
    /**
     * Muodostaa hash-koodin
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit1 = new Reseptit();
     * Reseptit reseptit2 = new Reseptit();
     * reseptit1.hashCode() == reseptit2.hashCode() === true;
     * 
     * reseptit1.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.hashCode() == reseptit2.hashCode() === false;
     * 
     * reseptit2.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.hashCode() == reseptit2.hashCode() === true;
     * 
     * reseptit1.lisaa(new Resepti(1, "Mustikkapiirakka"));
     * reseptit1.lisaa(new Resepti(2, "Suklaakakku"));
     * reseptit1.hashCode() == reseptit2.hashCode() === false;
     * 
     * reseptit2.lisaa(new Resepti(1, "Mustikkapiirakka"));
     * reseptit2.lisaa(new Resepti(2, "Suklaakakku"));
     * reseptit1.hashCode() == reseptit2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.juoksevaId);
        hash = Hajautus.hajautusString(hash, this.tiedostoNimi);
        hash = Hajautus.hajautusInt(hash, this.lkm);
        
        for (int i = 0; i < this.lkm; i++) {
            hash = Hajautus.hajautusInt(hash, this.reseptit.get(i).hashCode());
        }
        
        return hash;
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
        
        Resepti mustikkapiirakka = reseptit.lisaa("Mustikkapiirakka");
        reseptit.lisaa("Pizza");
        reseptit.lisaa("Lihapiirakka");
        mustikkapiirakka.setKuvaus("helppo ja hyvä");
        System.out.println(reseptit + "\n");
        
        reseptit.tulostaReseptit(System.out);
        
        mustikkapiirakka.setHinta(2);
        mustikkapiirakka.setValmistusaika(2);
        mustikkapiirakka.setTahdet(3);
        mustikkapiirakka.setVaativuus(1);
        System.out.println(mustikkapiirakka);
        
        Reseptit kopioReseptit = reseptit.clone();
        System.out.println(kopioReseptit.equals(reseptit));
        
        System.out.println(reseptit.hashCode());
        System.out.println(kopioReseptit.hashCode());
        
        mustikkapiirakka.setKuvaus("vaikea ja huono");
        
        System.out.println(reseptit.anna(0).getKuvaus());
        System.out.println(kopioReseptit.anna(0).getKuvaus());
        
        System.out.println(reseptit.hashCode());
        System.out.println(kopioReseptit.hashCode());
    }
}
