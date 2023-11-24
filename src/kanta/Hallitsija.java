package kanta;

/**
 * @author hakom
 * @version 24 Nov 2023
 * @param <T> Luokan hallitsema olio
 *
 */
public interface Hallitsija<T> {
    
    /**
     * @param olio lisättävä luokan hallitsema olio
     */
    public void lisaa(T olio);
    
    
    /**
     * @param olio poistettava luokan hallitsema olio
     */
    public void poista(T olio);
    
}
