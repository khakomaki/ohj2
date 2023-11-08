package reseptihaku;

import kanta.Hajautus;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class Ainesosa {
    
    private String ainesosaNimi = "ainesosa";
    private int ainesosa_id     = 0;
    
    
    /**
     * @param ainesosa ainesosan nimi
     * @param id ainesosan tunnus
     * 
     * @example
     * <pre name="test">
     * Ainesosa maito = new Ainesosa(1, "maitoa");
     * maito.toString() === "1|maitoa";
     * </pre>
     */
    public Ainesosa(int id, String ainesosa) {
        asetaNimi(ainesosa);
        this.ainesosa_id = id;
    }
    
    
    /**
     * @param ainesosa ainesosan nimi
     */
    public Ainesosa(String ainesosa) {
        asetaNimi(ainesosa);
    }
    
    
    private void asetaNimi(String nimi) {
        // ei tehdä muutoksia jos yritetään laittaa nimeksi null tai tyhjä merkkijono
        if (nimi == null || nimi.length() < 1) { return; }
        
        // asetetaan nimi pienellä kirjoitettuna
        this.ainesosaNimi = nimi.toLowerCase();
    }
    
    
    /**
     * @return ainesosan nimi
     * 
     * @example
     * <pre name="test">
     * Ainesosa korppujauhoja = new Ainesosa(8, "korppujauhoja");
     * korppujauhoja.getNimi() === "korppujauhoja";
     * </pre>
     */
    public String getNimi() {
        return this.ainesosaNimi;
    }
    
    
    /**
     * @param nimi minkä nimiseen verrataan
     * @return totuusarvo onko tämä kyseinen ainesosa
     * 
     * @example
     * <pre name="test">
     * Ainesosa kesakurpitsa = new Ainesosa(16, "kesäkurpitsa");
     * kesakurpitsa.oletko("KESÄKURPITSA") === true;
     * 
     * kesakurpitsa.oletko("banaani") === false;
     * </pre>
     */
    public boolean oletko(String nimi) {
        return nimi.toLowerCase().equals(this.ainesosaNimi);
    }
    
    
    /**
     * @param tunnus mihin tunnukseen sverrataan
     * @return totuusarvo onko tämä kyseinen ainesosa
     * 
     * Ainesosa kesakurpitsa = new Ainesosa(16, "kesäkurpitsa");
     * kesakurpitsa.oletko(16) === true;
     * kesakurpitsa.oletko(15) === false;
     */
    public boolean oletko(int tunnus) {
        return (tunnus == this.ainesosa_id);
    }
    
    
    /**
     * @return ainesosan tunnus
     * 
     * @example
     * <pre name="test">
     * Ainesosa korppujauhoja = new Ainesosa(8, "korppujauhoja");
     * korppujauhoja.getId() === 8;
     * </pre>
     */
    public int getId() {
        return this.ainesosa_id;
    }
    
    
    /**
     * @param id asetettava ainesosan tunnus
     * 
     * @example
     * <pre name="test">
     * Ainesosa ainesosa = new Ainesosa("paprika");
     * ainesosa.getId() === 0;
     * 
     * ainesosa.setId(2);
     * ainesosa.getId() === 2;
     * </pre>
     */
    public void setId(int id) {
        this.ainesosa_id = id;
    }
    
    
    @Override
    /**
     * Palauttaa kopion nykyisestä ainesosasta
     * 
     * 
     * @example
     * <pre name="test">
     * Ainesosa sokeri = new Ainesosa(15, "Sokeri");
     * sokeri.toString() === "15|sokeri";
     * 
     * Ainesosa sokeriKopio = sokeri.clone();
     * sokeriKopio.toString().equals(sokeri.toString()) === true;
     * 
     * sokeri.setId(20);
     * sokeriKopio.toString().equals(sokeri.toString()) === false;
     * 
     * sokeriKopio.setId(20);
     * sokeriKopio.toString().equals(sokeri.toString()) === true;
     * </pre>
     */
    public Ainesosa clone() {
        Ainesosa kopio = new Ainesosa("");
        kopio.ainesosa_id = this.ainesosa_id;
        kopio.ainesosaNimi = this.ainesosaNimi;
        return kopio;
    }
    
    
    @Override
    /**
     * @param verrattava mihin olioon verrataan nykyistä
     * @return ovatko verratava ja tämä Ainesosa samoja
     * 
     * @example
     * <pre name="test">
     * Ainesosa paaryna = new Ainesosa(15, "Päärynä");
     * paaryna.equals(new Ainesosa("Omena")) === false;
     * paaryna.equals(new Ainesosa("Päärynä")) === true;
     * paaryna.equals(new Ainesosa(14, "Päärynä")) === true;
     * paaryna.equals(new Ainesosa(15, "Päärynä")) === true;
     * paaryna.equals(new Ainesosa(15, "Omena")) === false;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) { return false; }
        if (verrattava.getClass() != this.getClass()) { return false; }
        
        Ainesosa verrattavaAinesosa = (Ainesosa)verrattava;
        if (!verrattavaAinesosa.ainesosaNimi.equals(this.ainesosaNimi)) { return false; }
        
        return true;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ainesosa ainesosa1 = new Ainesosa("Banaani");
     * Ainesosa ainesosa2 = new Ainesosa("Banaani");
     * Ainesosa ainesosa3 = new Ainesosa("Mango");
     * ainesosa1.hashCode() == ainesosa2.hashCode() === true;
     * ainesosa1.hashCode() == ainesosa3.hashCode() === false;
     * 
     * ainesosa1 = new Ainesosa(0, "banaani");
     * ainesosa2 = new Ainesosa(0, "BANAANI");
     * ainesosa1.hashCode() == ainesosa2.hashCode() === true;
     * 
     * ainesosa1 = new Ainesosa(0, "banaani");
     * ainesosa2 = new Ainesosa(1, "BANAANI");
     * ainesosa1.hashCode() == ainesosa2.hashCode() === true;
     * 
     * ainesosa1 = new Ainesosa(0, "banaani");
     * ainesosa2 = new Ainesosa(0, "bänaani");
     * ainesosa1.hashCode() == ainesosa2.hashCode() === false;
     * 
     * ainesosa1 = new Ainesosa(0, null);
     * ainesosa2 = new Ainesosa(0, "");
     * ainesosa1.hashCode() == ainesosa2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusString(hash, this.ainesosaNimi);
        return hash;
    }
    
    
    @Override
    /**
     * Palauttaa ainesosan tiedot muodossa "id|nimi" ( esim. "15|vehnäjauhoja" )
     * 
     * @example
     * <pre name="test">
     * Ainesosa peruna = new Ainesosa(12, "perunoita");
     * peruna.toString() === "12|perunoita";
     * 
     * Ainesosa kevatsipuli = new Ainesosa(100, "KEVÄTsipuli");
     * kevatsipuli.toString() === "100|kevätsipuli";
     * </pre>
     */
    public String toString() {
        return "" + this.ainesosa_id + "|" + this.ainesosaNimi;
    }
}
