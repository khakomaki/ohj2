package kanta;

import java.util.List;

/**
 * @author hakom
 * @version 24 Nov 2023
 * @param <T> Luokan hallitsema olio
 *
 */
public interface Hallitsija<T> {
    
    /**
     * Lisää annetun olion
     * 
     * @param olio lisättävä luokan hallitsema olio
     */
    public void lisaa(T olio);
    
    
    /**
     * Luo uuden olion
     * 
     * @return luotu olio
     */
    public T luo();
    
    
    /**
     * Poistaa annetun olion
     * 
     * @param olio poistettava luokan hallitsema olio
     */
    public void poista(T olio);
    
    
    /**
     * Antaa hallitut oliot listana
     * 
     * @return lista hallituista olioista
     */
    public List<T> anna();
    
}
