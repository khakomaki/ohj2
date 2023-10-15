package reseptihaku;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class Ainesosa {
    
    private String ainesosaNimi;
    private int ainesosa_id;
    
    
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
        this.ainesosaNimi = ainesosa.toLowerCase();
        this.ainesosa_id = id;
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
