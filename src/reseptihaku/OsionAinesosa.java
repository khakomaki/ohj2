package reseptihaku;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class OsionAinesosa {
    
    private int ainesosa_id;
    private String maara;
    
    
    /**
     * @param ainesosa_id osion ainesosan tunnus
     * @param maara osion ainesosan määrä
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa osionAinesosa = new OsionAinesosa(7, "5dl");
     * osionAinesosa.toString() === "7|5dl";
     * </pre>
     */
    public OsionAinesosa(int ainesosa_id, String maara) {
        this.ainesosa_id = ainesosa_id;
        this.maara = maara;
    }
    
    
    /**
     * @param ainesosa_id osion ainesosan tunnus
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa osionAinesosa = new OsionAinesosa(7);
     * osionAinesosa.toString() === "7|null";
     * </pre>
     */
    public OsionAinesosa(int ainesosa_id) {
        this.ainesosa_id = ainesosa_id;
    }
    
    
    @Override
    public String toString() {
        return "" + this.ainesosa_id + "|" + this.maara;
    }
}
