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
     * hajautus(-1, 3) === -28;
     * hajautus(0, 3) === 3;
     * hajautus(1, 3) === 34;
     * hajautus(34, 3) === 1057;
     * hajautus(1057, 3) === 32770;
     * 
     * hajautus(6755, 5) === 209410;
     * hajautus(209410, 3) === 6491713;
     * hajautus(6755, 3) === 209408;
     * hajautus(209408, 5) === 6491653;
     * 
     * hajautus(2943, 3) === 91236;
     * hajautus(2942, 34) === 91236;
     * hajautus(2944, -28) === 91236;
     * </pre>
     */
    public static int hajautus(int hajautettavaLuku, int lisattava) {
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
     * hajautus(1, sana1) == hajautus(1, sana2) === false;
     * hajautus(1, sana1) == hajautus(1, sana3) === true;
     * 
     * int hajautusluku1 = hajautus(1, "pallo");
     * hajautusluku1 = hajautus(hajautusluku1, "pullo");
     * int hajautusluku2 = hajautus(1, "pullo");
     * hajautusluku2 = hajautus(hajautusluku2, "pallo");
     * hajautusluku1 == hajautusluku2 === false;
     * </pre>
     */
    public static int hajautus(int hajautettavaLuku, Object lisattava) {
        return ALKULUKU * hajautettavaLuku + lisattava.hashCode();
    }
    
    
    /**
     * Lisää hajautettavaan lukuun lisättävien olioiden vaikutuksen.
     * 
     * @param hajautettavaLuku mikä luku halutaan hajauttaa
     * @param lisattavat mitkä oliot halutaan lisätä vaikuttamaan hajautettuun lukuun
     * @return hajautettu luku, johon vaikuttaa lisättyjen olioiden arvot
     */
    public static int hajautusObject(int hajautettavaLuku, Object... lisattavat) {
        int hash = hajautettavaLuku;
        
        for (Object lisattava : lisattavat) {
            hash = hajautus(hash, lisattava);
        }
        
        return hash;
    }
    
    
    /**
     * Lisää hajautettavaan lukuun lisättävien olioiden vaikutuksen.
     * 
     * @param hajautettavaLuku mikä luku halutaan hajauttaa
     * @param lisattavat mitkä kokonaisluvut halutaan lisätä vaikuttamaan hajautettuun lukuun
     * @return hajautettu luku, johon vaikuttaa lisättyjen kokonaislukujen arvot
     */
    public static int hajautusInt(int hajautettavaLuku, int... lisattavat) {
        int hash = hajautettavaLuku;
        
        for (int lisattava : lisattavat) {
            hash = hajautus(hash, lisattava);
        }
        
        return hash;
    }
}