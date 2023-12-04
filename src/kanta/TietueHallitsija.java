package kanta;

import java.util.Iterator;

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
    private int maxLkm =        1;
    private int lkm =           0;
    
    // tilan kasvatus
    private int vakio =         0;
    private double kerroin =    2;
    
    
    /**
     * Hallitsee tietuita.
     * 
     * Tilan kasvatus alustuu kertoimella 2 ja vakiolla 0.
     * Tila on alkutilanteessa 1.
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> tietuehallitsija = new TietueHallitsija<Object>();
     * tietuehallitsija.toString() === "0|2.0|0";
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public TietueHallitsija() {
        this.oliot = (T[]) new Object[this.maxLkm];
    }
    
    
    /**
     * Hallitsee tietueita.
     * Tilan kasvatus saadaan määritellä.
     * 
     * @param kerroin missä suhteessa tilaa kasvatetaan
     * @param vakio millä vakiolla tilaa kasvatetaan
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> tietueet = new TietueHallitsija<Object>(2.0, 0);
     * tietueet.toString() === "0|2.0|0";
     * 
     * tietueet = new TietueHallitsija<Object>(8.0, 5);
     * tietueet.toString() === "0|8.0|5";
     * 
     * tietueet = new TietueHallitsija<Object>(-1.5, 100);
     * tietueet.toString() === "0|-1.5|100";
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public TietueHallitsija(double kerroin, int vakio) {
        this.kerroin = kerroin;
        this.vakio = vakio;
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
    public T get(int indeksi) {
        // palautetaan null jos yritetään ottaa määrittelemättömästä paikasta
        if (this.lkm < indeksi || indeksi < 0) return null;
        return this.oliot[indeksi];
    }
    
    
    /**
     * @return olioiden lukumäärä
     */
    public int size() {
        return this.lkm;
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
     * TietueHallitsija<Object> tietueet = new TietueHallitsija<Object>(2.0, 0);
     * tietueet.lisaaTilaa() === 2;
     * tietueet.lisaaTilaa() === 4;
     * tietueet.lisaaTilaa() === 8;
     * tietueet.lisaaTilaa() === 16;
     * tietueet.lisaaTilaa() === 32;
     * tietueet.lisaaTilaa() === 64;
     * 
     * tietueet = new TietueHallitsija<Object>(1.0, 2);
     * tietueet.lisaaTilaa() === 3;
     * tietueet.lisaaTilaa() === 5;
     * tietueet.lisaaTilaa() === 7;
     * tietueet.lisaaTilaa() === 9;
     * tietueet.lisaaTilaa() === 11;
     * tietueet.lisaaTilaa() === 13;
     * 
     * tietueet = new TietueHallitsija<Object>(1.5, 3);
     * tietueet.lisaaTilaa() === 4;
     * tietueet.lisaaTilaa() === 9;
     * tietueet.lisaaTilaa() === 16;
     * tietueet.lisaaTilaa() === 27;
     * tietueet.lisaaTilaa() === 43;
     * tietueet.lisaaTilaa() === 67;
     * tietueet.lisaaTilaa() === 103;
     * tietueet.lisaaTilaa() === 157;
     * 
     * tietueet = new TietueHallitsija<Object>(2, -4);
     * tietueet.lisaaTilaa() === 2;
     * tietueet.lisaaTilaa() === 3;
     * tietueet.lisaaTilaa() === 4;
     * tietueet.lisaaTilaa() === 5;
     * tietueet.lisaaTilaa() === 6;
     * tietueet.lisaaTilaa() === 8;
     * tietueet.lisaaTilaa() === 12;
     * tietueet.lisaaTilaa() === 20;
     * tietueet.lisaaTilaa() === 36;
     * tietueet.lisaaTilaa() === 68;
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
        
        // luo uuden taulukon uudella koolla ja lisää olemassaolevat olioviitteet siihen
        @SuppressWarnings("unchecked")
        T[] uudetOliot = (T[]) new Object[this.maxLkm];
        
        for (int i = 0; i < this.lkm; i++) { 
            uudetOliot[i] = this.oliot[i]; 
        }
        
        // viitteen kääntö
        this.oliot = uudetOliot;
        
        return this.maxLkm;
    }
    
    
    /**
     * @param olio mikä olio halutaan lisätä taulukkoon
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> olioHallitsija = new TietueHallitsija<Object>();
     * olioHallitsija.toString() === "0|2.0|0";
     * 
     * Object olio = new Object();
     * olioHallitsija.add(olio);
     * olioHallitsija.toString() === "1|2.0|0";
     * 
     * olioHallitsija.add(new Object());
     * olioHallitsija.toString() === "2|2.0|0";
     * 
     * olioHallitsija.add(new Object());
     * olioHallitsija.toString() === "3|2.0|0";
     * </pre>
     */
    public void add(T olio) {
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
     * th.add(new Object());
     * th.add(new Object());
     * 
     * th.toString() === "2|2.0|0";
     * th.remove(5) === 2;
     * th.remove(-1) === 2;
     * th.remove(1) === 1;
     * th.remove(1) === 1;
     * th.remove(0) === 0;
     * th.remove(0) === 0;
     * th.toString() === "0|2.0|0";
     * 
     * th = new TietueHallitsija<Object>();
     * th.toString() === "0|2.0|0";
     * th.remove(0);
     * th.toString() === "0|2.0|0";
     * </pre>
     */
    public int remove(int indeksi) {
        // poistutaan jos indeksi ei ole mieluisa
        if (indeksi < 0 || this.lkm <= indeksi) return this.lkm;
        
        // siirretään indeksistä eteenpäin olevia olioita 1 taaksepäin
        for (int i = indeksi + 1; i < this.lkm; i++ ) {
            this.oliot[i - 1] = this.oliot[i];
        }
        
        // vaihdetaan viimeinen indeksi null-viitteeksi
        this.oliot[this.lkm - 1] = null;
        
        // vähennetään ennen kuin palautetaan
        return --this.lkm;
    }
    
    
    /**
     * Poistaa listasta ensimmäisen vastaan, annettua oliota vastaavan olion
     * 
     * @param olio poistettava olio
     * @return olioiden lukumäärä mahdollisen poiston jälkeen
     */
    public int remove(T olio) {
        for (int i = 0; i < this.lkm; i++) {
            if (oliot[i].equals(olio)) { 
                return remove(i);
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
     * "lukumäärä|kerroin|vakio"
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija<Object> tietueet = new TietueHallitsija<Object>();
     * tietueet.toString() === "0|2.0|0";
     * 
     * tietueet.add(new Object());
     * tietueet.toString() === "1|2.0|0";
     * 
     * tietueet.add(new Object());
     * tietueet.add(new Object());
     * tietueet.add(new Object());
     * tietueet.toString() === "4|2.0|0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.lkm);
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
        if (verrattavaTietueHallitsija.size() != this.size()) return false;
        if (verrattavaTietueHallitsija.maxLkm != this.maxLkm) return false;
        if (verrattavaTietueHallitsija.kerroin != this.kerroin) return false;
        if (verrattavaTietueHallitsija.vakio != this.vakio) return false;
        
        for (int i = 0; i < this.size(); i++) {
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
            tietueet.add(new Object());
            System.out.println(tietueet.toString());
        }
        
        tietueet = new TietueHallitsija<Object>();
        System.out.println("\n" + tietueet.toString());
        
        Object obj = new Object();
        tietueet.add(obj);
        System.out.println(tietueet);
        tietueet.remove(obj);
        System.out.println(tietueet);
        
        tietueet.remove(5);
        System.out.println(tietueet);
    }
}
