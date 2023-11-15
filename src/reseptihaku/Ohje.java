package reseptihaku;

import kanta.Hajautus;

/**
 * @author hakom
 * @version 30 Oct 2023
 *
 */
public class Ohje {
    
    private int vaihe           = 1;
    private String ohjeistus    = "";
    
    /**
     * Yhden vaiheen ohjeistustekstin sisältävä olio
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.toString() === "1|";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.toString() === "1|Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus(null);
     * muropohja.toString() === "1|Lisää vehnäjauhot";
     * </pre>
     */
    public Ohje() {
        this.ohjeistus = "";
    }
    
    
    /**
     * Yhden vaiheen ohjeistustekstin sisältävä olio
     * 
     * @param ohjeistus ohjeen sisältämä ohjeistusteksti
     */
    public Ohje(String ohjeistus) {
        setOhjeistus(ohjeistus);
    }
    
    
    /**
     * @param ohjeistus ohjeen sisältämä ohjeistusteksti
     * @param vaihe ohjeistustekstin vaihe
     */
    public Ohje(String ohjeistus, int vaihe) {
        setOhjeistus(ohjeistus);
        setVaihe(vaihe);
    }
    
    
    /**
     * @param ohjeistus ohjeen sisältämä ohjeistusteksti
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.toString() === "1|";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.toString() === "1|Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus(null);
     * muropohja.toString() === "1|Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus("");
     * muropohja.toString() === "1|";
     * </pre>
     */
    public void setOhjeistus(String ohjeistus) {
        // ei anneta asettaa nullia
        if (ohjeistus == null) return;
        
        this.ohjeistus = ohjeistus;
    }
    
    
    /**
     * @return ohjeen ohjeistusteksti
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.getOhjeistus() === "";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.getOhjeistus() === "Lisää vehnäjauhot";
     * </pre>
     */
    public String getOhjeistus() {
        return this.ohjeistus;
    }
    
    
    /**
     * @return ohjeen vaihe
     * 
     * Ohje ohje = new Ohje();
     * ohje.getVaihe() === 1;
     * 
     * ohje = new Ohje("Lisää mustikat", 4)
     * ohje.getVaihe() === 4;
     */
    public int getVaihe() {
        return this.vaihe;
    }
    
    
    /**
     * @param vaihe asetettava vaihe
     * 
     * @example
     * <pre name="test">
     * Ohje ohje = new Ohje();
     * ohje.getVaihe() === 1;
     * 
     * ohje.setVaihe(10);
     * ohje.getVaihe() === 10;
     * 
     * ohje.setVaihe(-5);
     * ohje.getVaihe() === 10;
     * 
     * ohje.setVaihe(0);
     * ohje.getVaihe() === 10;
     * 
     * ohje.setVaihe(1);
     * ohje.getVaihe() === 1;
     * </pre>
     */
    public void setVaihe(int vaihe) {
        if (0 < vaihe) this.vaihe = vaihe;
    }
    
    
    @Override
    /**
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje("Sekoita sokeri ja jauhot", 2);
     * Ohje muropohjaKopio = muropohja.clone();
     * 
     * muropohja.getVaihe() == muropohjaKopio.getVaihe() === true;
     * muropohja.getOhjeistus().equals(muropohjaKopio.getOhjeistus()) === true;
     * muropohja.equals(muropohjaKopio) === true;
     * 
     * muropohja.setOhjeistus("Lisää sokeri");
     * muropohja.getOhjeistus().equals(muropohjaKopio.getOhjeistus()) === false;
     * muropohja.equals(muropohjaKopio) === false;
     * 
     * muropohja.setVaihe(1);
     * muropohja.getVaihe() == muropohjaKopio.getVaihe() === false;
     * </pre>
     */
    public Ohje clone() {
        Ohje kopio = new Ohje();
        kopio.ohjeistus = this.ohjeistus;
        kopio.vaihe = this.vaihe;
        return kopio;
    }
    
    
    @Override
    /**
     * 
     * @example
     * <pre name="test">
     * Ohje ohje1 = new Ohje();
     * Ohje ohje2 = new Ohje();
     * ohje1.equals(ohje2) === true;
     * ohje2.equals(ohje1) === true;
     * 
     * ohje1.setOhjeistus("Vatkaa kananmunat");
     * ohje1.equals(ohje2) === false;
     * ohje2.equals(ohje1) === false;
     * 
     * ohje2.setOhjeistus("Vatkaa kananmunat");
     * ohje1.equals(ohje2) === true;
     * ohje2.equals(ohje1) === true;
     * 
     * ohje2.setVaihe(12);
     * ohje1.equals(ohje2) === false;
     * ohje2.equals(ohje1) === false;
     * 
     * ohje1.setVaihe(11);
     * ohje1.equals(ohje2) === false;
     * ohje2.equals(ohje1) === false;
     * 
     * ohje1.setVaihe(12);
     * ohje1.equals(ohje2) === true;
     * ohje2.equals(ohje1) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        
        Ohje verrattavaOhje = (Ohje)verrattava;
        if (!verrattavaOhje.ohjeistus.equals(this.ohjeistus)) return false;
        if (verrattavaOhje.vaihe != this.vaihe) return false;
        return true;
    }
    

    @Override
    /**
     * 
     * @example
     * <pre name="test">
     * Ohje ohje1 = new Ohje();
     * Ohje ohje2 = new Ohje();
     * 
     * ohje1.hashCode() == ohje2.hashCode() === true;
     * 
     * ohje1.setOhjeistus("Vatkaa kananmunat");
     * ohje1.hashCode() == ohje2.hashCode() === false;
     * 
     * ohje2.setOhjeistus("Vatkaa kananmunat");
     * ohje1.hashCode() == ohje2.hashCode() === true;
     * 
     * ohje2.setVaihe(12);
     * ohje1.hashCode() == ohje2.hashCode() === false;
     * 
     * ohje1.setVaihe(11);
     * ohje1.hashCode() == ohje2.hashCode() === false;
     * 
     * ohje1.setVaihe(12);
     * ohje1.hashCode() == ohje2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.vaihe);
        hash = Hajautus.hajautusString(hash, this.ohjeistus);
        return hash;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ohje ohje = new Ohje();
     * ohje.toString() === "1|";
     * 
     * ohje = new Ohje("Lisää sokeri", 7);
     * ohje.toString() === "7|Lisää sokeri";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.vaihe);
        sb.append('|');
        sb.append(this.ohjeistus);
        return sb.toString();
    }
}
