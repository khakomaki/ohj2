package kanta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hakom
 * @version 27 Nov 2023
 *
 * Antaa asettaa arvoksi vain jonkin vaihtoehdoista.
 * Oletusarvon ei tarvitse olla vaihtoehdoissa, mutta siihen voidaan vaihtaa takaisin.
 *
 */
public class VaihtoehtoAttribuutti {
    
    private Integer valinta;
    private Integer oletusValinta;
    private String oletusString;
    private final Map<Integer, String> vaihtoehdot;
    
    
    /**
     * @param vaihtoehdot Map vaihtoehdoista ja niitä vastaavista String-muodoista
     * @param oletusarvo oletusarvo
     * @param oletusString oletusarvon String-arvo
     */
    public VaihtoehtoAttribuutti(Map<Integer, String> vaihtoehdot, Integer oletusarvo, String oletusString) {
        this.vaihtoehdot = Collections.unmodifiableMap(new HashMap<Integer, String>(vaihtoehdot));
        this.oletusValinta = oletusarvo;
        this.oletusString = oletusString;
        this.valinta = oletusarvo;
    }
    
    
    /**
     * Asettaa nykyisen valinnan
     * 
     * @param valinta miksikä valinnaksi koitetaan vaihtaa
     */
    public void setValinta(Integer valinta) {
        // jos annetaan sama kuin oletusvalinta
        if (valinta.equals(this.oletusValinta)) {
            this.valinta = valinta;
            return;
        }
        
        // vaihtaa jos valinta löytyy vaihtoehdoista
        if (vaihtoehdot.containsKey(valinta)) this.valinta = valinta;
    }
    
    
    /**
     * Antaa nykyisen valinnan
     * 
     * @return nykyinen valinta
     */
    public Integer getValinta() {
        return this.valinta;
    }
    
    
    /**
     * Antaa nykyistä valintaa vastaavan String-arvon
     * 
     * @return valintaa vastaava String-arvo
     */
    public String getValintaString() {
        if (valinta == this.oletusValinta) return this.oletusString;
        return this.vaihtoehdot.get(this.valinta);
    }
    
    
    @Override
    public String toString() {
        return "" +  this.valinta.intValue();
    }
    
}
