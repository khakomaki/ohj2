package kanta;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author hakom
 * @version 4 Dec 2023
 * @param <T> iteroitavien olioden tyyppi
 *
 * TietueHallitsija-luokan iteraattori
 */
public class TietueHallitsijaIteraattori<T> implements Iterator<T> {
    
    private final TietueHallitsija<T> tietueHallitsija;
    private int nykyinenKohta;
    
    /**
     * Iteraattori, joka alustetaan kohtaan ennen taulukon ensimmäistä kohtaa
     * 
     * @param tietueHallitsija mille iteraattori tehdään
     */
    public TietueHallitsijaIteraattori(TietueHallitsija<T> tietueHallitsija) {
        this.tietueHallitsija = tietueHallitsija;
        this.nykyinenKohta = -1;
    }
    
    
    /**
     * Kertoo onko olemassa seuraavaa oliota
     * 
     * @return onko olemassa seuraava olio
     */
    @Override
    public boolean hasNext() {
        return this.nykyinenKohta < tietueHallitsija.size() - 1;
    }
    
    
    /**
     * Antaa seuraavan kohdan olion
     */
    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException("Ei ole seuraavaa elementtiä!");
        return tietueHallitsija.get(++this.nykyinenKohta); // kasvatetaan ensin ja annetaan sen kohdan olio
    }
}
