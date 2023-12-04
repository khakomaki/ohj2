package kanta;

/**
 * @author hakom
 * @version 4 Nov 2023
 *
 * Luokka luvun hajauttamiseen liittyvistä funktioista (hash)
 */
public class Hajautus {
    
    private final static int ALKULUKU = 31;
    
    /**
     * Lisää hajautettavaan lukuun lisättävän luvun vaikutuksen.
     * Pyrkii vähentämään törmäyksiä. 
     * Samoilla luvuilla tuottaa saman tuloksen ja järjestyksellä on merkitystä,
     * eli jos algoritmille syöttää esim. samat 2 lukua eri järjestyksessä, saadaan
     * eri tulos, ellei poikkeuksena satu olemaan juuri törmäävät luvut.
     * 
     * @param hajautettavaLuku mikä luku halutaan hajauttaa
     * @param lisattava mikä luku halutaan lisätä vaikuttamaan hajautettuun lukuun
     * @return hajautettu luku, johon vaikuttaa lisätyn luvun arvo
     * 
     * @example
     * <pre name="test">
     * hajautusInt(-1, 3) === -28;
     * hajautusInt(0, 3) === 3;
     * hajautusInt(1, 3) === 34;
     * hajautusInt(34, 3) === 1057;
     * hajautusInt(1057, 3) === 32770;
     * 
     * hajautusInt(6755, 5) === 209410;
     * hajautusInt(209410, 3) === 6491713;
     * hajautusInt(6755, 3) === 209408;
     * hajautusInt(209408, 5) === 6491653;
     * 
     * hajautusInt(2943, 3) === 91236;
     * hajautusInt(2942, 34) === 91236;
     * hajautusInt(2944, -28) === 91236;
     * </pre>
     */
    public static int hajautusInt(int hajautettavaLuku, int lisattava) {
        return ALKULUKU * hajautettavaLuku + lisattava;
    }
    
    
    /**
     * Lisää hajautettavaan lukuun lisättävän merkkijonon vaikutuksen.
     * Pyrkii vähentämään törmäyksiä. 
     * Samoilla merkkijonoilla tuottaa saman tuloksen ja järjestyksellä on merkitystä,
     * eli jos algoritmille syöttää esim. samat 2 merkkijonoa eri järjestyksessä, saadaan
     * eri tulos, ellei poikkeuksena satu olemaan juuri törmäävät merkkijonot.
     * 
     * @param hajautettavaLuku mikä luku halutaan hajauttaa
     * @param lisattava mikä merkkijono halutaan lisätä vaikuttamaan hajautettuun lukuun
     * @return hajautettu luku, johon vaikuttaa lisätyn merkkijonon arvo
     * 
     * @example
     * <pre name="test">
     * String sana1 = new String("vasara");
     * String sana2 = new String("naula");
     * String sana3 = new String("vasara");
     * hajautusString(1, sana1) == hajautusString(1, sana2) === false;
     * hajautusString(1, sana1) == hajautusString(1, sana3) === true;
     * 
     * int hajautusluku1 = hajautusString(1, "pallo");
     * hajautusluku1 = hajautusString(hajautusluku1, "pullo");
     * int hajautusluku2 = hajautusString(1, "pullo");
     * hajautusluku2 = hajautusString(hajautusluku2, "pallo");
     * hajautusluku1 == hajautusluku2 === false;
     * </pre>
     */
    public static int hajautusString(int hajautettavaLuku, String lisattava) {
        return ALKULUKU * hajautettavaLuku + lisattava.hashCode();
    }
}