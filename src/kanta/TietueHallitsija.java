package kanta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author hakom
 * @version 18 Oct 2023
 * @param <T> Tietueen hallitsijan tyyppi
 *
 * Oma olioiden hallinta luokka. 
 * Mahdollisuus määritellä kuinka olioille allokoidaan lisää tilaa.
 */
public class TietueHallitsija<T> implements Iterable<T> {
    
    private T[] oliot;
    private int maxLkm =            1;
    private int lkm =               0;
    
    private final int vakio =       0;
    private final double kerroin =  2;
    
    
    /**
     * Hallitsee tietuita.
     * 
     * Tilan kasvatus alustuu kertoimella 2 ja vakiolla 0.
     * Tila on alkutilanteessa 1.
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> tietuehallitsija = new TietueHallitsija<Object>();
     * tietuehallitsija.toString() === "0|1|2.0|0";
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public TietueHallitsija() {
        this.oliot = (T[]) new Object[this.maxLkm];
    }
    
    
    /**
     * Hallitsee tietuita.
     * Tilan kasvatus alustuu kertoimella 2 ja vakiolla 0.
     * 
     * @param maxLkm kuinka suureksi halutaan alustaa ( >1 ). 1 jos annettu arvo ei kelpaa.
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> tietueet = new TietueHallitsija<Object>(8);
     * tietueet.toString() === "0|8|2.0|0";
     * 
     * tietueet = new TietueHallitsija<Object>(0);
     * tietueet.toString() === "0|1|2.0|0";
     * 
     * tietueet = new TietueHallitsija<Object>(100);
     * tietueet.toString() === "0|100|2.0|0";
     * 
     * tietueet = new TietueHallitsija<Object>(-100);
     * tietueet.toString() === "0|1|2.0|0";
     * 
     * tietueet = new TietueHallitsija<Object>(1);
     * tietueet.toString() === "0|1|2.0|0";
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public TietueHallitsija(int maxLkm) {
        if (maxLkm < 1) return;
        this.maxLkm = maxLkm;
        this.oliot = (T[]) new Object[this.maxLkm];
    }
    
    
    /**
     * @return totuusarvo mahtuuko taulukkoon lisää olio-viitteitä
     */
    public boolean onkoTilaa() {
        return (this.lkm < this.maxLkm);
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan olio
     * @return indeksissä oleva olio, tai muuten null
     */
    public T getOlio(int indeksi) {
        // palautetaan null jos yritetään ottaa määrittelemättömästä paikasta
        if (this.lkm < indeksi || indeksi < 0) return null;
        return this.oliot[indeksi];
    }
    
    
    /**
     * Antaa oliot listana
     * 
     * @return lista olioista
     */
    public List<T> getOliotListana() {
        List<T> olioLista = new ArrayList<T>();
        
        for (T olio : this) {
            olioLista.add(olio);
        }
        
        return olioLista;
    }
    
    
    /**
     * @return olioiden lukumäärä
     */
    public int getLkm() {
        return this.lkm;
    }
    
    
    /**
     * @return olioiden maksimi lukumäärä
     */
    public int getMaxLkm() {
        return this.maxLkm;
    }
    
    
    /**
     * Kasvattaa taulukkoa kertoimen ja vakion avulla.
     * 
     * Noudattaa kaavaa: maxLkm + maxLkm * ( KERROIN - 1 ) + VAKIO
     * 
     * Mahdollisista kaavan tuottamista desimaaliluvuista jätetään desimaaliosa pois.
     * Varmistaa että taulukkoa kasvatetaan vähintään yhden yksikön verran.
     * @return uusi maksimi olioiden lukumäärä
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> tietueet = new TietueHallitsija<Object>();
     * tietueet.lisaaTilaa() === 2;
     * tietueet.lisaaTilaa() === 4;
     * tietueet.lisaaTilaa() === 8;
     * tietueet.lisaaTilaa() === 16;
     * tietueet.lisaaTilaa() === 32;
     * tietueet.lisaaTilaa() === 64;
     * </pre>
     */
    public int lisaaTilaa() {
        // kasvatetaan listaa kertoimen ja vakion avulla, palautetaan kokonaisluku
        int uusiMaxLkm = this.maxLkm + (int)(this.maxLkm * (this.kerroin - 1) + this.vakio);
        
        // vaihdetaan uuteen mnaksimi lukumäärään, jos se on vähintään yhden enemmän kuin alkuperäinen
        if (this.maxLkm + 1 <= uusiMaxLkm) { 
            this.maxLkm = uusiMaxLkm; 
        } else { 
            this.maxLkm++; 
        }
        
        // luodaan uusi lista uudella koolla, lisätään olemassaolevat olioviitteet ja käännetään viite siihen
        @SuppressWarnings("unchecked")
        T[] uudetOliot = (T[]) new Object[this.maxLkm];
        
        for (int i = 0; i < this.lkm; i++) { 
            uudetOliot[i] = this.oliot[i]; 
        }
        
        this.oliot = uudetOliot;
        
        return this.maxLkm;
    }
    
    
    /**
     * @param olio mikä olio halutaan lisätä taulukkoon
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> olioHallitsija = new TietueHallitsija<Object>();
     * olioHallitsija.toString() === "0|1|2.0|0";
     * 
     * Object olio = new Object();
     * olioHallitsija.lisaaOlio(olio);
     * olioHallitsija.toString() === "1|1|2.0|0";
     * 
     * olioHallitsija.lisaaOlio(new Object());
     * olioHallitsija.toString() === "2|2|2.0|0";
     * 
     * olioHallitsija.lisaaOlio(new Object());
     * olioHallitsija.toString() === "3|4|2.0|0";
     * </pre>
     */
    public void lisaaOlio(T olio) {
        if (!onkoTilaa()) lisaaTilaa();
        this.oliot[lkm] = olio;
        this.lkm++;
    }
    
    
    /**
     * Poistaa olion annetusta indeksistä
     * 
     * @param indeksi mistä indeksistä poistetaan olio
     * @return olioiden lukumäärä mahdollisen poiston jälkeen
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> th = new TietueHallitsija<Object>();
     * th.lisaaOlio(new Object());
     * th.lisaaOlio(new Object());
     * 
     * th.toString() === "2|2|2.0|0";
     * th.poistaOlio(5) === 2;
     * th.poistaOlio(-1) === 2;
     * th.poistaOlio(1) === 1;
     * th.poistaOlio(1) === 1;
     * th.poistaOlio(0) === 0;
     * th.poistaOlio(0) === 0;
     * th.toString() === "0|2|2.0|0";
     * 
     * th = new TietueHallitsija<Object>(1);
     * th.toString() === "0|1|2.0|0";
     * th.poistaOlio(0);
     * th.toString() === "0|1|2.0|0";
     * </pre>
     */
    public int poistaOlio(int indeksi) {
        // poistutaan jos indeksi ei ole mieluisa
        if (indeksi < 0 || this.lkm <= indeksi) return this.lkm;
        
        // siirretään indeksistä eteenpäin kaikkia taaksepäin
        for (int i = indeksi + 1; i < this.lkm; i++ ) {
            this.oliot[i - 1] = this.oliot[i];
        }
        
        // vähennetään ennen kuin palautetaan
        return --this.lkm;
    }
    
    
    /**
     * Poistaa listasta ensimmäisen vastaan, annettua oliota vastaavan olion
     * 
     * @param olio poistettava olio
     * @return olioiden lukumäärä mahdollisen poiston jälkeen
     */
    public int poistaOlio(T olio) {
        for (int i = 0; i < this.lkm; i++) {
            if (oliot[i].equals(olio)) { 
                return poistaOlio(i);
            }
        }
        return this.lkm;
    }
    
    
    /**
     * Luo iteraattorin itselleen
     * 
     * @return iteraattori
     */
    @Override
    public Iterator<T> iterator() {
        return new TietueHallitsijaIteraattori<T>(this);
    }
    
    
    @Override
    /**
     * Palauttaa TietueHallitsijan tiedot muodossa:
     * "lukumäärä|maxlukumäärä|kerroin|vakio"
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> tietueet = new TietueHallitsija<Object>();
     * tietueet.toString() === "0|1|2.0|0";
     * 
     * tietueet = new TietueHallitsija<Object>(13);
     * tietueet.toString() === "0|13|2.0|0";
     * 
     * tietueet.lisaaOlio(new Object());
     * tietueet.toString() === "1|13|2.0|0";
     * 
     * tietueet.lisaaOlio(new Object());
     * tietueet.lisaaOlio(new Object());
     * tietueet.lisaaOlio(new Object());
     * tietueet.toString() === "4|13|2.0|0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.lkm);
        sb.append('|');
        sb.append(this.maxLkm);
        sb.append('|');
        sb.append(this.kerroin);
        sb.append('|');
        sb.append(this.vakio);
        return sb.toString();
    }
    
    
    /**
     * Vertaa onko annettu olio sama kuin nykyinen
     * 
     * @param verrattava mihin verrataan
     */
    @Override
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        if ( !((TietueHallitsija<?>)verrattava).oliot.equals(this.oliot) ) return false;
        @SuppressWarnings("unchecked") // voidaan tehdä typecast, koska on jo poistuttu jos ei ole oikeaa tyyppiä
        TietueHallitsija<T> verrattavaTietueHallitsija = (TietueHallitsija<T>)verrattava;
        if (verrattavaTietueHallitsija.getLkm() != this.getLkm()) return false;
        if (verrattavaTietueHallitsija.maxLkm != this.maxLkm) return false;
        if (verrattavaTietueHallitsija.kerroin != this.kerroin) return false;
        if (verrattavaTietueHallitsija.vakio != this.vakio) return false;
        
        for (int i = 0; i < this.getLkm(); i++) {
            if (!verrattavaTietueHallitsija.oliot[i].equals(this.oliot[i])) return false;
        }
        
        // on sama jos ei ole vielä palautettu false
        return true;
    }
    
    
    /**
     * Muodostaa hash-luvun tietuehallitsijalle
     */
    @Override
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.lkm);
        hash = Hajautus.hajautusInt(hash, this.maxLkm);
        // hash = Hajautus.hajautusInt(hash, this.KERROIN); TODO
        hash = Hajautus.hajautusInt(hash, this.vakio);
        
        for (T olio : this) {
            hash = Hajautus.hajautusInt(hash, olio.hashCode());
        }
        
        return hash;
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        TietueHallitsija<Object> tietueet = new TietueHallitsija<Object>();
        System.out.println(tietueet.toString());
        
        // lisätään 10 oliota ja tulostetaan jokaisen lisäyksen jälkenen
        for (int i = 0; i < 10; i++) {
            tietueet.lisaaOlio(new Object());
            System.out.println(tietueet.toString());
        }
        
        tietueet = new TietueHallitsija<Object>();
        System.out.println("\n" + tietueet.toString());
        
        Object obj = new Object();
        tietueet.lisaaOlio(obj);
        System.out.println(tietueet);
        tietueet.poistaOlio(obj);
        System.out.println(tietueet);
        
        tietueet.poistaOlio(5);
        System.out.println(tietueet);
    }
}
