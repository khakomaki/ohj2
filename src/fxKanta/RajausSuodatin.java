package fxKanta;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import kanta.VaihtoehtoAttribuutti;

/**
 * @author hakom
 * @version 27 Nov 2023
 *
 * Määrittelee annetulle VaihtoehtoAttribuutille minimin ja maksimin.
 * Pitää huolen ettei voida valita maksimia korkeampaa minimiä tai minimiä alempaa maksimia.
 *
 */
public class RajausSuodatin {
    
    private VaihtoehtoAttribuutti suodatin;
    private VaihtoehtoAttribuutti minimi;
    private VaihtoehtoAttribuutti maksimi;
    private Map<Integer, String> minimiVaihtoehdot;
    private Map<Integer, String> maksimiVaihtoehdot;
    
    /**
     * @param suodatin olemassa olevat vaihtoehdot
     * 
     * @example
     * <pre name="test">
     * #import java.util.Map;
     * #import java.util.HashMap;
     * #import javafx.collections.ObservableList;
     * #import kanta.VaihtoehtoAttribuutti;
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(0), new String("tosi vähän"));
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * map.put(new Integer(4), new String("tosi paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -1, "ei valittu");
     * RajausSuodatin suodatin = new RajausSuodatin(attribuutti);
     * suodatin.toString() === "ei valittu|ei valittu";
     * 
     * suodatin.setMinimi(2);
     * suodatin.setMaksimi(3);
     * suodatin.toString() === "keskimääräisesti|paljon";
     * </pre>
     */
    public RajausSuodatin(VaihtoehtoAttribuutti suodatin) {
        this.suodatin = suodatin;
        this.minimi = suodatin.clone();
        this.maksimi = suodatin.clone();
        this.minimiVaihtoehdot = new HashMap<Integer, String>(minimi.getVaihtoehdot());
        this.maksimiVaihtoehdot = new HashMap<Integer, String>(maksimi.getVaihtoehdot());
    }
    
    
    /**
     * Antaa minimin vaihtoehdot.
     * Sisällyttää aina oletusvaihtoehdon.
     * 
     * @return vaihtoehdot minimiarvolle
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(0), new String("tosi vähän"));
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * map.put(new Integer(4), new String("tosi paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -1, "ei valittu");
     * RajausSuodatin suodatin = new RajausSuodatin(attribuutti);
     * suodatin.getMinimiVaihtoehdot() === "ei valittu\ntosi vähän\nvähän\nkeskimääräisesti\npaljon\ntosi paljon\n";
     * </pre>
     */
    public String getMinimiVaihtoehdot() {
        return this.minimi.getVaihtoehdotString();
    }
    
    
    /**
     * Antaa listan minimivaihtoehdoista
     * 
     * @return lista minimivaihtoehdoista
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(0), new String("tosi vähän"));
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * map.put(new Integer(4), new String("tosi paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -1, "ei valittu");
     * RajausSuodatin suodatin = new RajausSuodatin(attribuutti);
     * 
     * ObservableList<VaihtoehtoAttribuutti> lista = suodatin.getMinimiVaihtoehdotList();
     * lista.get(0).toString() === "ei valittu";
     * lista.get(1).toString() === "tosi vähän";
     * lista.get(2).toString() === "vähän";
     * lista.get(3).toString() === "keskimääräisesti";
     * lista.get(4).toString() === "paljon";
     * lista.get(5).toString() === "tosi paljon";
     * 
     * suodatin.setMinimi(3);
     * lista = suodatin.getMinimiVaihtoehdotList();
     * lista.get(0).toString() === "ei valittu";
     * lista.get(1).toString() === "tosi vähän";
     * lista.get(2).toString() === "vähän";
     * lista.get(3).toString() === "keskimääräisesti";
     * lista.get(4).toString() === "paljon";
     * lista.get(5).toString() === "tosi paljon";
     * 
     * suodatin.setMaksimi(3);
     * lista = suodatin.getMinimiVaihtoehdotList();
     * lista.get(0).toString() === "ei valittu";
     * lista.get(1).toString() === "tosi vähän";
     * lista.get(2).toString() === "vähän";
     * lista.get(3).toString() === "keskimääräisesti";
     * lista.get(4).toString() === "paljon";
     * </pre>
     */
    public ObservableList<VaihtoehtoAttribuutti> getMinimiVaihtoehdotList() {
        return this.minimi.getVaihtoehdotList();
    }
    
    
    /**
     * Antaa maksimin vaihtoehdot
     * 
     * @return vaihtoehdot maksimiarvolle
     */
    public String getMaksimiVaihtoehdot() {
        return this.maksimi.getVaihtoehdotString();
    }
    
    
    /**
     * Antaa listan maksimivaihtoehdoista
     * 
     * @return lista maksimivaihtoehdoista
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(0), new String("tosi vähän"));
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * map.put(new Integer(4), new String("tosi paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -1, "ei valittu");
     * RajausSuodatin suodatin = new RajausSuodatin(attribuutti);
     * 
     * ObservableList<VaihtoehtoAttribuutti> lista = suodatin.getMaksimiVaihtoehdotList();
     * lista.get(0).toString() === "ei valittu";
     * lista.get(1).toString() === "tosi vähän";
     * lista.get(2).toString() === "vähän";
     * lista.get(3).toString() === "keskimääräisesti";
     * lista.get(4).toString() === "paljon";
     * lista.get(5).toString() === "tosi paljon";
     * </pre>
     */
    public ObservableList<VaihtoehtoAttribuutti> getMaksimiVaihtoehdotList() {
        return this.maksimi.getVaihtoehdotList();
    }
    
    
    /**
     * Asettaa minimivalinnan
     * 
     * @param valinta asetettava valinta
     */
    public void setMinimi(int valinta) {
        this.minimi.setValinta(valinta);
        
        // poistaa minimiä alemmat vaihtoehdot maksimin vaihtoehdoista
        paivitaMaksimi();
    }
    
    
    /**
     * Päivittää maksimin vaihtoehdot
     */
    private void paivitaMaksimi() {
        this.maksimiVaihtoehdot.clear();
        
        // lisää takaisin vain annettua arvoa suuremmat tai yhtäsuuret arvot
        for (Integer valinta : this.suodatin.getVaihtoehdot().keySet()) {
            if (this.minimi.getValinta() <= valinta) this.maksimiVaihtoehdot.put(valinta, this.suodatin.getVaihtoehdot().get(valinta));
        }
    }
    
    
    /**
     * Asettaa minimiksi annetun attribuutin valinnan
     * 
     * @param attribuutti nykyinen valinta
     */
    public void setMinimi(VaihtoehtoAttribuutti attribuutti) {
        if (attribuutti == null) return;
        setMinimi(attribuutti.getValinta());
    }
    
    
    /**
     * Asettaa maksimivalinnan
     * 
     * @param valinta asetettava valinta
     */
    public void setMaksimi(int valinta) {
        this.maksimi.setValinta(valinta);
        
        // poistaa maksimia ylemmät vaihtoehdot minimin vaihtoehdoista
        paivitaMinimi();
    }
    
    
    /**
     * Päivittää minimin vaihtoehdot
     */
    private void paivitaMinimi() {
        this.minimiVaihtoehdot.clear();
        
        // lisää takaisin vain annettua arvoa pienemmät tai yhtäsuuret arvot
        for (Integer valinta : this.suodatin.getVaihtoehdot().keySet()) {
            if (valinta <= this.maksimi.getValinta()) this.minimiVaihtoehdot.put(valinta, this.suodatin.getVaihtoehdot().get(valinta));
        }
    }
    
    
    /**
     * Asettaa maksimiksi annetun attribuutin valinnan
     * 
     * @param attribuutti nykyinen valinta
     */
    public void setMaksimi(VaihtoehtoAttribuutti attribuutti) {
        if (attribuutti == null) return;
        setMaksimi(attribuutti.getValinta());
    }
    
    
    /**
     * Antaa valitun minimiarvon
     * 
     * @return valittu minimi
     */
    public VaihtoehtoAttribuutti getMinimi() {
        return this.minimi;
    }
    
    
    /**
     * Antaa valitun maksimiarvon
     * 
     * @return valittu maksimi
     */
    public VaihtoehtoAttribuutti getMaksimi() {
        return this.maksimi;
    }
    
    
    /**
     * Asettaa minimin ja maksimin oletusarvoon
     */
    public void setOletus() {
        setMinimi(this.minimi.getOletus());
        setMaksimi(this.maksimi.getOletus());
    }
    
    
    @Override
    /**
     * RajausSuodattimen minimi ja maksimi.
     * 
     * @example
     * <pre name="test">
     * Map<Integer, String> map = new HashMap<Integer, String>();
     * map.put(new Integer(0), new String("tosi vähän"));
     * map.put(new Integer(1), new String("vähän"));
     * map.put(new Integer(2), new String("keskimääräisesti"));
     * map.put(new Integer(3), new String("paljon"));
     * map.put(new Integer(4), new String("tosi paljon"));
     * VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -1, "ei valittu");
     * RajausSuodatin suodatin = new RajausSuodatin(attribuutti);
     * suodatin.toString() === "ei valittu|ei valittu";
     * 
     * suodatin.setMinimi(2);
     * suodatin.setMaksimi(3);
     * suodatin.toString() === "keskimääräisesti|paljon";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.minimi.getValintaString());
        sb.append("|");
        sb.append(this.maksimi.getValintaString());
        return sb.toString();
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    @SuppressWarnings("removal")
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(new Integer(0), new String("tosi vähän"));
        map.put(new Integer(1), new String("vähän"));
        map.put(new Integer(2), new String("keskimääräisesti"));
        map.put(new Integer(3), new String("paljon"));
        map.put(new Integer(4), new String("tosi paljon"));
        VaihtoehtoAttribuutti attribuutti = new VaihtoehtoAttribuutti("määrä", map, -1, "ei valittu");
        RajausSuodatin suodatin = new RajausSuodatin(attribuutti);
        
        System.out.println("Minimi: " + suodatin.getMinimiVaihtoehdotList());
        System.out.println("Maksimi: " + suodatin.getMaksimiVaihtoehdotList());
                
        int minimiValinta = 2;
        suodatin.setMinimi(2);
        System.out.println("\nAsetetaan minimi-valinnaksi luku " + minimiValinta + " (" + suodatin.getMinimi() + "):");
        System.out.println("Minimi: " + suodatin.getMinimiVaihtoehdotList());
        System.out.println("Maksimi: " + suodatin.getMaksimiVaihtoehdotList());
        
        int maksimiValinta = 2;
        suodatin.setMaksimi(2);
        System.out.println("\nAsetetaan maksimi-valinnaksi luku " + maksimiValinta + " (" + suodatin.getMaksimi() + "):");
        System.out.println("Minimi: " + suodatin.getMinimiVaihtoehdotList());
        System.out.println("Maksimi: " + suodatin.getMaksimiVaihtoehdotList());
    }
}
