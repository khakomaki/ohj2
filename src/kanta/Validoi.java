package kanta;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hakom
 * @version 30 Nov 2023
 *
 */
public class Validoi {

    /**
     * Kertoo voidaanko annettu nimi tallentaa (esim. tiedostonimenä)
     * 
     * @param nimi tutkittava nimi
     * @return voidaanko nimi tallentaa
     * 
     * @example
     * <pre name="test">
     * onkoNimiTallennettavissa("Mustikkakakku") === true;
     * onkoNimiTallennettavissa("Ääkköset ja välilyönnit") === true;
     * onkoNimiTallennettavissa("1234567890") === true;
     * onkoNimiTallennettavissa("Mustikkakakku|1|2|3|4") === false;
     * onkoNimiTallennettavissa("Mustikkakakku*") === false;
     * onkoNimiTallennettavissa("Mustikkakakku/") === true;
     * onkoNimiTallennettavissa("") === false;
     * onkoNimiTallennettavissa(null) === false;
     * </pre>
     */
    public static boolean onkoNimiTallennettavissa(String nimi) {
        if (nimi == null || nimi.length() < 1) return false;
        File tiedosto = new File(nimi);
        
        try {
            tiedosto.getCanonicalPath();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
    
    
    /**
     * Kertoo onko annetussa listassa useampi samaa merkkijonoa
     * 
     * @param merkkijonot lista merkkijonoista
     * @return onko listassa useampi samaa merkkijonoa
     * 
     * @example
     * <pre name="test">
     * #import java.util.List;
     * #import java.util.ArrayList;
     * List<String> nimet = new ArrayList<String>();
     * nimet.addAll(List.of("Pekka", "Pirkko", "Pekka", "Joonas", "Pasi"));
     * onkoDuplikaatteja(nimet) === true;
     * 
     * nimet.remove(0);
     * onkoDuplikaatteja(nimet) === false;
     * 
     * nimet.add("Pasi");
     * onkoDuplikaatteja(nimet) === true;
     * 
     * nimet.remove(0);
     * nimet.remove(0);
     * nimet.remove(0);
     * onkoDuplikaatteja(nimet) === true;
     * 
     * nimet.remove(0);
     * onkoDuplikaatteja(nimet) === false;
     * nimet.isEmpty() === false;
     * 
     * nimet.remove(0);
     * onkoDuplikaatteja(nimet) === false;
     * nimet.isEmpty() === true;
     * </pre>
     */
    public static boolean onkoDuplikaatteja(List<String> merkkijonot) {
        Set<String> uniikit = new HashSet<String>();
        
        for (String merkkijono : merkkijonot) {
            if (!uniikit.add(merkkijono)) return true; // ei lisää ja palauttaa false jos on jo setissä
        }
        
        return false;
    }
    
}
