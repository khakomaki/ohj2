package reseptihaku;

/**
 * @author hakom
 * @version 30 Oct 2023
 *
 */
public class Ohje {
    
    private String ohjeistus;
    
    /**
     * Yhden vaiheen ohjeistustekstin sisältävä olio
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.toString() === "";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.toString() === "Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus(null);
     * muropohja.toString() === "Lisää vehnäjauhot";
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
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.toString() === "";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.toString() === "Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus(null);
     * muropohja.toString() === "Lisää vehnäjauhot";
     * </pre>
     */
    public void setOhjeistus(String ohjeistus) {
        // ei anneta asettaa nullia
        if (ohjeistus == null) { return; }
        
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
    
    
    @Override
    public String toString() {
        return this.ohjeistus;
    }
}
