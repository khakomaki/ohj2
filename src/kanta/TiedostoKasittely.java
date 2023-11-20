package kanta;

import java.io.File;

/**
 * @author hakom
 * @version 20 Nov 2023
 *
 */
public class TiedostoKasittely {
    
    /**
     * Yrittää poistaa annetun tiedoston
     * 
     * @param tiedosto poistettava tiedosto
     * @throws SailoException jos poistaminen ei onnistu
     */
    public static void poistaTiedosto(File tiedosto) throws SailoException {
        // heitetään virhe jos sellainen on olemassa eikä voida poistaa
        if (!tiedosto.delete() && tiedosto.exists()) {
            throw new SailoException("Ei voida poistaa tiedostoa \"" + tiedosto.getName() + "\"");
        }
    }
    
}
