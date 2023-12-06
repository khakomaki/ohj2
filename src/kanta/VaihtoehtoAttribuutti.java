package kanta;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author hakom
 * @version 27 Nov 2023
 *
 * Antaa asettaa arvoksi vain jonkin vaihtoehdoista.
 * Oletusarvon ei tarvitse olla vaihtoehdoissa, mutta siihen voidaan vaihtaa takaisin.
 *
 */
public class VaihtoehtoAttribuutti {
    
    private String nimi;
    private Integer valinta;
    private Integer oletusValinta;
    private String oletusString;
    private final Map<Integer, String> vaihtoehdot;
    
    
    /**
     * @param nimi attribuutin nimi
     * @param vaihtoehdot Map vaihtoehdoista ja niitä vastaavista String-muodoista
     * @param oletusarvo oletusarvo
     * @param oletusString oletusarvon String-arvo
     * 
     * @example
     * <pre name="test">
     * #import java.util.Map;
     * #import java.util.HashMap;
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.toString() === "ei määritelty";
     * 
     * attribuutti.setValinta(0);
     * attribuutti.toString() === "ei määritelty";
     * 
     * attribuutti.setValinta(1);
     * attribuutti.toString() === "vähän";
     * 
     * attribuutti.setValinta(2);
     * attribuutti.toString() === "keskimääräisesti";
     * 
     * attribuutti.setValinta(3);
     * attribuutti.toString() === "paljon";
     * 
     * attribuutti.setValinta(3);
     * attribuutti.toString() === "paljon";
     * 
     * attribuutti.setValinta(-2);
     * attribuutti.toString() === "ei määritelty";
     * </pre>
     */
    public VaihtoehtoAttribuutti(String nimi, Map<Integer, String> vaihtoehdot, Integer oletusarvo, String oletusString) {
        this.nimi = nimi;
        this.vaihtoehdot = vaihtoehdot;
        this.oletusValinta = oletusarvo;
        this.oletusString = oletusString;
        this.valinta = oletusarvo;
    }
    
    
    /**
     * @param nimi attribuutin nimi
     * @param vaihtoehdot Map vaihtoehdoista ja niitä vastaavista String-muodoista
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map);
     * attribuutti.toString() === "";
     * 
     * attribuutti.setValinta(1);
     * attribuutti.toString() === "vähän";
     * 
     * attribuutti.setValinta(2);
     * attribuutti.toString() === "keskimääräisesti";
     * 
     * attribuutti.setValinta(3);
     * attribuutti.toString() === "paljon";
     * 
     * attribuutti.setValinta(3);
     * attribuutti.toString() === "paljon";
     * 
     * attribuutti.setValinta(-1);
     * attribuutti.toString() === "";
     * </pre>
     */
    public VaihtoehtoAttribuutti(String nimi, Map<Integer, String> vaihtoehdot) {
        this.nimi = nimi;
        this.vaihtoehdot = vaihtoehdot;
        
        this.oletusString = "";
        this.oletusValinta = -1;
        this.valinta = -1;
    }
    
    
    /**
     * Asettaa nykyisen valinnan
     * 
     * @param valinta miksikä valinnaksi koitetaan vaihtaa
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.getValinta() === -2;
     * 
     * attribuutti.setValinta(0);
     * attribuutti.getValinta() === -2;
     * 
     * attribuutti.setValinta(1);
     * attribuutti.getValinta() === 1;
     * 
     * attribuutti.setValinta(3);
     * attribuutti.getValinta() === 3;

     * attribuutti.setValinta(5);
     * attribuutti.getValinta() === 3;
     * 
     * attribuutti.setValinta(-2);
     * attribuutti.getValinta() === -2;
     * </pre>
     */
    public void setValinta(int valinta) {
        // jos annetaan sama kuin oletusvalinta
        if (onkoOletusValinta(valinta)) {
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
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.getValinta() === -2;
     * 
     * attribuutti.setValinta(0);
     * attribuutti.getValinta() === -2;
     * 
     * attribuutti.setValinta(1);
     * attribuutti.getValinta() === 1;
     * 
     * attribuutti.setValinta(3);
     * attribuutti.getValinta() === 3;

     * attribuutti.setValinta(5);
     * attribuutti.getValinta() === 3;
     * 
     * attribuutti.setValinta(-2);
     * attribuutti.getValinta() === -2;
     * </pre>
     */
    public int getValinta() {
        return this.valinta;
    }
    
    
    /**
     * Antaa valinnan arvon perusteella
     * 
     * @param arvo millä arvolla etsitään
     * @return löydetty avain tai null
     */
    public Integer getValinta(String arvo) {
        for (Entry<Integer, String> entry : this.vaihtoehdot.entrySet()) {
            if (entry.getValue().equals(arvo)) return entry.getKey();
        }
        
        // palautetaan oletusvalinta jos ei löytynyt
        return this.oletusValinta;
    }
    
    
    /**
     * Kertoo onko annettu arvo sama kuin oletusvalinta
     * 
     * @param verrattava mitä verrataan oletusvalintaan
     * @return onko annettu arvo yhtä kuin oletusvalinta
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.onkoOletusValinta(-3) === false;
     * attribuutti.onkoOletusValinta(-2) === true;
     * attribuutti.onkoOletusValinta(-1) === false;
     * attribuutti.onkoOletusValinta(0) === false;
     * attribuutti.onkoOletusValinta(1) === false;
     * attribuutti.onkoOletusValinta(2) === false;
     * </pre>
     */
    public boolean onkoOletusValinta(int verrattava) {
        return verrattava == this.oletusValinta;
    }
    
    
    /**
     * Antaa nykyistä valintaa vastaavan String-arvon
     * 
     * @return valintaa vastaava String-arvo
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.getValintaString() === "ei määritelty";
     * 
     * attribuutti.setValinta(2);
     * attribuutti.getValintaString() === "keskimääräisesti";
     * 
     * attribuutti.setValinta(3);
     * attribuutti.getValintaString() === "paljon";
     * </pre>
     */
    public String getValintaString() {
        if (valinta == this.oletusValinta) return this.oletusString;
        return this.vaihtoehdot.get(this.valinta);
    }
    
    
    /**
     * Antaa oletusvalinnan
     * 
     * @return oletusvalinta
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.getOletus() === -2;
     * 
     * attribuutti.setValinta(3);
     * attribuutti.getOletus() === -2;
     * </pre>
     */
    public int getOletus() {
        return this.oletusValinta;
    }
    
    
    /**
     * Antaa oletusvalinnan String-muodon
     * 
     * @return oletusvalinnan String
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.getOletusString() === "ei määritelty";
     * 
     * attribuutti.setValinta(2);
     * attribuutti.getOletusString() === "ei määritelty";
     * </pre>
     */
    public String getOletusString() {
        return this.oletusString;
    }
    
    
    /**
     * Palauttaa Mapin mahdollisista vaihtoehdoista
     * 
     * @return vaihtoehdot
     */
    public Map<Integer, String> getVaihtoehdot() {
        Map<Integer, String> map = new HashMap<Integer, String>(this.vaihtoehdot);
        map.put(this.oletusValinta, this.oletusString);
        return map;
    }
    
    
    /**
     * Antaa vaihtoehdot merkkijonona.
     * Erottaa vaihtoehdot rivinvaihdolla ja sisältää myös oletusvalinnan.
     * 
     * @return vaihtoehdot merkkijonona
     */
    public String getVaihtoehdotString() {
        // oletusvalinta
        StringBuilder sb = new StringBuilder(getOletusString());
        sb.append("\n");
        
        // vaihtoehdot
        for (Entry<Integer, String> entry : this.vaihtoehdot.entrySet()) {
            sb.append(entry.getValue());
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    
    /**
     * Muodostaa ObservableList-listan vaihtoehdoista.
     * Sisällyttää listaan myös tyhjän vaihtoehdon.
     * 
     * @return ObservableList vaihtoehdoista sisältäen oletusvalinnan
     */
    public ObservableList<VaihtoehtoAttribuutti> getVaihtoehdotList() {
        ObservableList<VaihtoehtoAttribuutti> lista = FXCollections.observableArrayList();
        
        // tyhjä vaihtoehto
        VaihtoehtoAttribuutti tyhjaVaihtoehto = new VaihtoehtoAttribuutti(getNimi(), this.vaihtoehdot, getOletus(), getOletusString());
        lista.add(tyhjaVaihtoehto);
        
        // vaihtoehdot
        for (Entry<Integer, String> entry : this.vaihtoehdot.entrySet()) {
            VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti(getNimi(), this.vaihtoehdot, getOletus(), getOletusString());
            attribuutti.setValinta(entry.getKey());
            lista.add(attribuutti);
        }
        
        return lista;
    }
    
    
    /**
     * Antaa attribuutille määritellyn nimen
     * 
     * @return attribuutin nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * Kertoo onko annettu nimi sama kuin attribuutin nimi
     * 
     * @param verrattavaNimi mihin nimeen verrataan
     * @return onko verrattava nimi sama kuin attribuutin
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("Määrä", map, -2, "ei määritelty");
     * 
     * attribuutti.getNimi() === "Määrä";
     * attribuutti.onkoNimi("hinta") === false;
     * attribuutti.onkoNimi("Määrä") === true;
     * attribuutti.onkoNimi("MÄÄRÄ") === true;
     * attribuutti.onkoNimi("määrä") === true;
     * attribuutti.onkoNimi("") === false;
     * attribuutti.onkoNimi(null) === false;
     * </pre>
     */
    public boolean onkoNimi(String verrattavaNimi) {
        if (verrattavaNimi == null) return false;
        return this.nimi.toLowerCase().equals(verrattavaNimi.toLowerCase());
    }
    
    
    @Override
    /**
     * VaihtoehtoAttribuutin nykyinen valinta String-muodossa.
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -2, "ei määritelty");
     * attribuutti.toString() === "ei määritelty";
     * 
     * attribuutti.setValinta(2);
     * attribuutti.toString() === "keskimääräisesti";
     * </pre>
     */
    public String toString() {
        return getValintaString();
    }
    
    
    /**
     * Vertailee onko annettu olio sama kuin nykyinen
     */
    @Override
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        VaihtoehtoAttribuutti verrattavaVA = (VaihtoehtoAttribuutti)verrattava;
        
        if (verrattavaVA.getNimi() != this.getNimi()) return false;
        if (verrattavaVA.getOletus() != this.getOletus()) return false;
        if (!verrattavaVA.getOletusString().equals(this.getOletusString())) return false;
        if (!verrattavaVA.getVaihtoehdot().equals(this.getVaihtoehdot())) return false;
        
        return true;
    }
    
    
    /**
     * Luo omista tiedoistaan hash-luvun
     */
    @Override
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusString(hash, this.getNimi());
        hash = Hajautus.hajautusInt(hash, this.getOletus());
        hash = Hajautus.hajautusString(hash, this.getOletusString());
        hash = Hajautus.hajautusInt(hash, this.vaihtoehdot.hashCode());
        
        return hash;
    }
    
    
    @Override
    /**
     * kopioi VaihtoehtoAttribuutin.
     * ei säilytä valintaa, tekee uuden samanlaisen HashMapin.
     */
    public VaihtoehtoAttribuutti clone() {
        return new VaihtoehtoAttribuutti(this.nimi, new HashMap<Integer, String>(this.vaihtoehdot), this.oletusValinta, this.oletusString);
    }
    
}
