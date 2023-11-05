package kanta;

/**
 * @author hakom
 * @version 18 Oct 2023
 *
 */
public class TietueHallitsija {
    
    private Object[] oliot = new Object[1];
    private int maxLkm =        1;
    private int lkm =           0;
    private final int VAKIO =         0;
    private final double KERROIN =    2;
    
    
    /**
     * Hallitsee tietuita.
     * 
     * Tilan kasvatus alustuu kertoimella 2 ja vakiolla 0.
     * Tila on alkutilanteessa 1.
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija tietuehallitsija = new TietueHallitsija();
     * tietuehallitsija.toString() === "0|1|2.0|0";
     * </pre>
     */
    public TietueHallitsija() {
        //
    }
    
    
    /**
     * Hallitsee tietuita.
     * Tilan kasvatus alustuu kertoimella 2 ja vakiolla 0.
     * 
     * @param maxLkm kuinka suureksi halutaan alustaa ( >1 ). 1 jos annettu arvo ei kelpaa.
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija tietueet = new TietueHallitsija(8);
     * tietueet.toString() === "0|8|2.0|0";
     * 
     * tietueet = new TietueHallitsija(0);
     * tietueet.toString() === "0|1|2.0|0";
     * 
     * tietueet = new TietueHallitsija(100);
     * tietueet.toString() === "0|100|2.0|0";
     * 
     * tietueet = new TietueHallitsija(-100);
     * tietueet.toString() === "0|1|2.0|0";
     * 
     * tietueet = new TietueHallitsija(1);
     * tietueet.toString() === "0|1|2.0|0";
     * </pre>
     */
    public TietueHallitsija(int maxLkm) {
        if (maxLkm < 1) { return; }
        this.maxLkm = maxLkm;
        this.oliot = new Object[this.maxLkm];
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
    public Object getOlio(int indeksi) {
        // palautetaan null jos yritetään ottaa määrittelemättömästä paikasta
        if (this.lkm < indeksi || indeksi < 0) { return null; }
        return this.oliot[indeksi];
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
     * TietueHallitsija tietueet = new TietueHallitsija();
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
        int uusiMaxLkm = this.maxLkm + (int)(this.maxLkm * (this.KERROIN - 1) + this.VAKIO);
        
        // vaihdetaan uuteen mnaksimi lukumäärään, jos se on vähintään yhden enemmän kuin alkuperäinen
        if (this.maxLkm + 1 <= uusiMaxLkm) { 
            this.maxLkm = uusiMaxLkm; 
        } else { this.maxLkm++; }
        
        // luodaan uusi lista uudella koolla, lisätään olemassaolevat olioviitteet ja käännetään viite siihen
        Object[] uudetOliot = new Object[this.maxLkm];
        for (int i = 0; i < this.lkm; i++) { uudetOliot[i] = this.oliot[i]; }
        this.oliot = uudetOliot;
        
        return this.maxLkm;
    }
    
    
    /**
     * @param olio mikä olio halutaan lisätä taulukkoon
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija olioHallitsija = new TietueHallitsija();
     * olioHallitsija.toString() === "0|1|2.0|0";
     * 
     * Object olio = new Object();
     * olioHallitsija.lisaa(olio);
     * olioHallitsija.toString() === "1|1|2.0|0";
     * 
     * olioHallitsija.lisaa(new Object());
     * olioHallitsija.toString() === "2|2|2.0|0";
     * 
     * olioHallitsija.lisaa(new Object());
     * olioHallitsija.toString() === "3|4|2.0|0";
     * </pre>
     */
    public void lisaa(Object olio) {
        if (!onkoTilaa()) { lisaaTilaa(); }
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
     * TietueHallitsija th = new TietueHallitsija();
     * th.lisaa(new Object());
     * th.lisaa(new Object());
     * 
     * th.toString() === "2|2|2.0|0";
     * th.poista(5) === 2;
     * th.poista(-1) === 2;
     * th.poista(1) === 1;
     * th.poista(1) === 1;
     * th.poista(0) === 0;
     * th.poista(0) === 0;
     * th.toString() === "0|2|2.0|0";
     * 
     * th = new TietueHallitsija(1);
     * th.toString() === "0|1|2.0|0";
     * th.poista(0);
     * th.toString() === "0|1|2.0|0";
     * </pre>
     */
    public int poista(int indeksi) {
        // poistutaan jos indeksi ei ole mieluisa
        if (indeksi < 0 || this.lkm <= indeksi) { return this.lkm; }
        
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
    public int poista(Object olio) {
        for (int i = 0; i < this.lkm; i++) {
            if (oliot[i].equals(olio)) { 
                return poista(i);
            }
        }
        return this.lkm;
    }
    
    
    @Override
    /**
     * Palauttaa TietueHallitsijan tiedot muodossa:
     * "lukumäärä|maxlukumäärä|kerroin|vakio"
     * 
     * @example
     * <pre name="test">
     * TietueHallitsija tietueet = new TietueHallitsija();
     * tietueet.toString() === "0|1|2.0|0";
     * 
     * tietueet = new TietueHallitsija(13);
     * tietueet.toString() === "0|13|2.0|0";
     * 
     * tietueet.lisaa(new Object());
     * tietueet.toString() === "1|13|2.0|0";
     * 
     * tietueet.lisaa(new Object());
     * tietueet.lisaa(new Object());
     * tietueet.lisaa(new Object());
     * tietueet.toString() === "4|13|2.0|0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.lkm);
        sb.append('|');
        sb.append(this.maxLkm);
        sb.append('|');
        sb.append(this.KERROIN);
        sb.append('|');
        sb.append(this.VAKIO);
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        TietueHallitsija tietueet = new TietueHallitsija();
        System.out.println(tietueet.toString());
        
        // lisätään 10 oliota ja tulostetaan jokaisen lisäyksen jälkenen
        for (int i = 0; i < 10; i++) {
            tietueet.lisaa(new Object());
            System.out.println(tietueet.toString());
        }
        
        tietueet = new TietueHallitsija();
        System.out.println("\n" + tietueet.toString());
        
        Object obj = new Object();
        tietueet.lisaa(obj);
        System.out.println(tietueet);
        tietueet.poista(obj);
        System.out.println(tietueet);
        
        tietueet.poista(5);
        System.out.println(tietueet);
    }
}
