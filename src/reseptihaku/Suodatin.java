package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

/**
 * @author hakom
 * @version 11 Oct 2023
 *
 */
public class Suodatin {
    
    private String nimi = "";
    private HashMap<Integer, String> vaihtoehdot;
    
    
    /**
     * @param nimi suodattimen nimi
     * @param vaihtoehdot suodattimen vaihtoehdot
     * 
     * @example
     * <pre name="test">
     * Suodatin hinta = new Suodatin("Hinta");
     * hinta.lisaaVaihtoehto(1, "€");
     * hinta.lisaaVaihtoehto(2, "€€");
     * hinta.lisaaVaihtoehto(3, "€€€");
     * 
     * hinta.toString() === "Hinta|1:€|2:€€|3:€€€";
     * </pre>
     */
    public Suodatin(String nimi, HashMap<Integer, String> vaihtoehdot) {
        this.nimi = nimi;
        this.vaihtoehdot = vaihtoehdot;
    }
    
    
    /**
     * @param nimi suodattimen nimi
     * 
     * @example
     * <pre name="test">
     * Suodatin vaativuustaso = new Suodatin("Vaativuustaso");
     * vaativuustaso.toString() === "Vaativuustaso";
     * 
     * vaativuustaso.lisaaVaihtoehto(5, "työläs");
     * vaativuustaso.toString() === "Vaativuustaso|5:työläs"
     * </pre>
     * 
     */
    public Suodatin(String nimi) {
        this.nimi = nimi;
        this.vaihtoehdot = new HashMap<Integer, String>();
    }
    
    
    /**
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(this.nimi);
        vaihtoehdot.forEach((avain, arvo) -> out.println(avain + ": " + arvo));
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * @param avain vaihtoehtoa vastaava luku
     * @return lukua vastaava vaihtoehto
     * 
     * @example
     * <pre name="test">
     * Suodatin hinta = new Suodatin("Hinta");
     * hinta.lisaaVaihtoehto(1, "€");
     * hinta.lisaaVaihtoehto(2, "€€");
     * hinta.lisaaVaihtoehto(3, "€€€");
     * 
     * hinta.getVastaavaVaihtoehto(2) === "€€";
     * hinta.getVastaavaVaihtoehto(3) === "€€€";
     * hinta.getVastaavaVaihtoehto(5) === null;
     * hinta.getVastaavaVaihtoehto(0) === null;
     * </pre>
     */
    public String getVastaavaVaihtoehto(int avain) {
        return vaihtoehdot.get(avain);
    }
    
    
    /**
     * @param luku vaihtoehdon avain
     * @param vaihtoehto lisättävä vaihtoehto
     * 
     * @example
     * <pre name="test">
     * Suodatin hinta = new Suodatin("Hinta");
     * hinta.toString() === "Hinta";
     * 
     * hinta.lisaaVaihtoehto(1, "€");
     * hinta.toString() === "Hinta|1:€";
     * 
     * hinta.lisaaVaihtoehto(2, "€€");
     * hinta.toString() === "Hinta|1:€|2:€€";
     * </pre>
     */
    public void lisaaVaihtoehto(int luku, String vaihtoehto) {
        this.vaihtoehdot.put(luku, vaihtoehto);
    }
    
    
    /**
     * @return suodattimen nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Suodatin hinta = new Suodatin("Hinta");
     * hinta.toString() === "Hinta";
     * 
     * hinta.lisaaVaihtoehto(1, "€");
     * hinta.toString() === "Hinta|1:€";
     * 
     * hinta.lisaaVaihtoehto(2, "€€");
     * hinta.toString() === "Hinta|1:€|2:€€";
     * 
     * hinta = new Suodatin("");
     * hinta.toString() === "";
     * 
     * hinta.lisaaVaihtoehto(-5, "☆");
     * hinta.toString() === "|-5:☆";
     * </pre>
     */
    public String toString() {
        StringBuilder merkkijono = new StringBuilder(this.nimi + "|");
        vaihtoehdot.forEach((avain, arvo) -> merkkijono.append(avain + ":" + arvo + "|"));
        merkkijono.deleteCharAt(merkkijono.length() - 1);
        return merkkijono.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        HashMap<Integer, String> hinnat = new HashMap<Integer, String>();
        hinnat.put(1, "€");
        hinnat.put(2, "€€");
        hinnat.put(3, "€€€");
        Suodatin hinta = new Suodatin("Hinta", hinnat);
        
        HashMap<Integer, String> vaativuustasot = new HashMap<Integer, String>();
        vaativuustasot.put(1, "helppo");
        vaativuustasot.put(2, "kohtalaisen helppo");
        vaativuustasot.put(3, "keskiverto");
        vaativuustasot.put(4, "kohtalaisen työläs");
        vaativuustasot.put(5, "työläs");
        Suodatin vaativuus = new Suodatin("Vaativuus", vaativuustasot);
        
        hinta.tulosta(System.out);
        vaativuus.tulosta(System.out);
        
        System.out.println();
        System.out.println(vaativuus.getNimi());
        System.out.println(hinta.getVastaavaVaihtoehto(3));
        System.out.println(hinta.getVastaavaVaihtoehto(0));
        System.out.println(hinta.toString());
    }

}
