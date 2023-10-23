package kanta;

/**
 * @author hakom
 * @version 23 Oct 2023
 *
 */
public class Satunnaisluku {
    /**
     * Arpoo satunnaisen kokonaisluvun halutulle välille
     * @param min minimi arvo
     * @param max maksimi arvo
     * @return satunnainen luku väliltä min, max
     */
    public static int rand(int min, int max) {
      double n = (max - min) * Math.random() + min;
      return (int)Math.round(n);
    }
}
