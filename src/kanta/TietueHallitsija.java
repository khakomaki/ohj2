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
     * @param kerroin kerroin tietueen koon kasvattamiseksi
     */
    public void setKerroin(double kerroin) {
        this.kerroin = kerroin;
    }
    
    
    /**
     * @param vakio vakio tietueen koon kasvattamiseksi
     */
    public void setVakio(int vakio) {
        this.vakio = vakio;
    }
    
    
    /**
     * Kasvattaa taulukkoa kertoimen ja vakion avulla.
     * 
     * Noudattaa kaavaa: maxLkm + maxLkm * ( kerroin - 1 ) + vakio
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
     * 
     * tietueet = new TietueHallitsija(); tietueet.setKerroin(1.0); tietueet.setVakio(2);
     * tietueet.lisaaTilaa() === 3;
     * tietueet.lisaaTilaa() === 5;
     * tietueet.lisaaTilaa() === 7;
     * tietueet.lisaaTilaa() === 9;
     * tietueet.lisaaTilaa() === 11;
     * tietueet.lisaaTilaa() === 13;
     * 
     * tietueet = new TietueHallitsija(); tietueet.setKerroin(1.5); tietueet.setVakio(3);
     * tietueet.lisaaTilaa() === 4;
     * tietueet.lisaaTilaa() === 9;
     * tietueet.lisaaTilaa() === 16;
     * tietueet.lisaaTilaa() === 27;
     * tietueet.lisaaTilaa() === 43;
     * tietueet.lisaaTilaa() === 67;
     * tietueet.lisaaTilaa() === 103;
     * tietueet.lisaaTilaa() === 157;
     * 
     * tietueet = new TietueHallitsija(); tietueet.setKerroin(2); tietueet.setVakio(-4);
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
     * 
     * tietueet = new TietueHallitsija(); tietueet.setKerroin(0); tietueet.setVakio(0);
     * tietueet.lisaaTilaa() === 2;
     * tietueet.lisaaTilaa() === 3;
     * tietueet.lisaaTilaa() === 4;
     * tietueet.lisaaTilaa() === 5;
     * tietueet.lisaaTilaa() === 6;
     * tietueet.lisaaTilaa() === 7;
     * tietueet.lisaaTilaa() === 8;
     * tietueet.lisaaTilaa() === 9;
     * </pre>
     */
    public int lisaaTilaa() {
        // kasvatetaan listaa kertoimen ja vakion avulla, palautetaan kokonaisluku
        int uusiMaxLkm = this.maxLkm + (int)(this.maxLkm * (this.kerroin - 1) + this.vakio);
        
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
     * olioHallitsija.setKerroin(1.0);
     * olioHallitsija.setVakio(1);
     * olioHallitsija.toString() === "0|1|1.0|1";
     * 
     * Object olio = new Object();
     * olioHallitsija.lisaa(olio);
     * olioHallitsija.toString() === "1|1|1.0|1";
     * 
     * olioHallitsija.lisaa(new Object());
     * olioHallitsija.toString() === "2|2|1.0|1";
     * 
     * olioHallitsija.lisaa(new Object());
     * olioHallitsija.toString() === "3|3|1.0|1";
     * </pre>
     */
    public void lisaa(Object olio) {
        if (!onkoTilaa()) { lisaaTilaa(); }
        this.oliot[lkm] = olio;
        this.lkm++;
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
     * 
     * tietueet.setKerroin(3.5);
     * tietueet.toString() === "4|13|3.5|0";
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
        
        // muutetaan kerrointa ja vakiota ja tulostetaan taas 10 olion lisäys
        tietueet.setKerroin(1);
        tietueet.setVakio(3);
        for (int i = 0; i < 10; i++) {
            tietueet.lisaa(new Object());
            System.out.println(tietueet.toString());
        }
    }
}
