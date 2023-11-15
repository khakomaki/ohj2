package kanta;

/**
 * @author hakom
 * @version 15 Nov 2023
 *
 */
public class SailoException extends Exception {
    
    // poikkeuskäsittelijän versio
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Muodostaa poikkeuksen annetulla viestillä
     * 
     * @param viesti poikkeuksen heittämä viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}
