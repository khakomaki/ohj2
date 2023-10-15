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
        this.ainesosaNimi = ainesosa;
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
     * </pre>
     */
    public String toString() {
        return "" + this.ainesosa_id + "|" + this.ainesosaNimi;
    }
}
