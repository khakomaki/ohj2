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
    private int juoksevaNumero;
    
    
    /**
     * @param nimi suodattimen nimi
     * 
     * @example
     * <pre name="test">
     * Suodatin vaativuustaso = new Suodatin("Vaativuustaso");
     * vaativuustaso.toString() === "Vaativuustaso";
     * </pre>
     * 
     */
    public Suodatin(String nimi) {
        this.nimi = nimi;
        this.vaihtoehdot = new HashMap<Integer, String>();
        this.juoksevaNumero = 0;
    }
    
    
    /**
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(this.nimi);
        tulostaVaihtoehdot(out);
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
     * hinta.luoVaihtoehdot(new String[]{ "€", "€€", "€€€" });
     * 
     * hinta.getVastaavaVaihtoehto(1) === "€€";
     * hinta.getVastaavaVaihtoehto(2) === "€€€";
     * hinta.getVastaavaVaihtoehto(5) === null;
     * hinta.getVastaavaVaihtoehto(-1) === null;
     * </pre>
     */
    public String getVastaavaVaihtoehto(int avain) {
        return vaihtoehdot.get(avain);
    }
    
    
    /**
     * @param luku vaihtoehdon avain
     * @param vaihtoehto lisättävä vaihtoehto
     */
    private void lisaaVaihtoehto(int luku, String vaihtoehto) {
        this.vaihtoehdot.put(luku, vaihtoehto);
    }
    
    
    /**
     * Luo mahdolliset vaihtoehdot suodattimelle. Tyhjentää edelliset vaihtoehdot.
     * 
     * @param vaihtoehdotTaulukko mahdolliset vaihtoehdot "pienimmästä" "suurimpaan"
     */
    public void luoVaihtoehdot(String[] vaihtoehdotTaulukko) {
        this.vaihtoehdot.clear();
        
        for (int i = 0; i < vaihtoehdotTaulukko.length; i++) {
            // skippaa jos annettu vaihtoehto on null, tyhjämerkkijono tai pelkkää whitespacea
            if (vaihtoehdotTaulukko[i].isBlank()) { continue; }
            lisaaVaihtoehto(this.juoksevaNumero, vaihtoehdotTaulukko[i]);
            this.juoksevaNumero++;
        }
    }
    
    
    /**
     * @return suodattimen nimi
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * @param out tietovirta johon halutaan tulostaa
     */
    public void tulostaVaihtoehdot(PrintStream out) {
        for (String arvo: this.vaihtoehdot.values()) {
            out.print(arvo);
            out.print("\n");
        }
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     */
    public void tulostaVaihtoehdot(OutputStream os) {
        tulostaVaihtoehdot(new PrintStream(os));
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Suodatin hinta = new Suodatin("Hinta");
     * hinta.toString() === "Hinta";
     * 
     * hinta.luoVaihtoehdot(new String[]{ "☆", "☆☆", "☆☆☆", "☆☆☆☆", "☆☆☆☆☆" });
     * hinta.toString() === "Hinta|0:☆|1:☆☆|2:☆☆☆|3:☆☆☆☆|4:☆☆☆☆☆";
     * </pre>
     */
    public String toString() {
        StringBuilder merkkijono = new StringBuilder();
        merkkijono.append(this.nimi);
        merkkijono.append('|');
        for (Integer avain: this.vaihtoehdot.keySet()) {
            merkkijono.append(avain);
            merkkijono.append(":");
            merkkijono.append(this.vaihtoehdot.get(avain));
            merkkijono.append('|');
        }
        merkkijono.deleteCharAt(merkkijono.length() - 1);
        return merkkijono.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Suodatin hinnat = new Suodatin("Hinta");
        hinnat.luoVaihtoehdot(new String[]{ "€", "€€", "€€€" });
        hinnat.tulosta(System.out);
        
        System.out.println();
        
        Suodatin vaativuus = new Suodatin("Vaativuus");
        vaativuus.luoVaihtoehdot(new String[]{ 
                "helppo",
                "kohtalaisen helppo",
                "keskiverto",
                "kohtalaisen työläs",
                "työläs"
        });
        vaativuus.tulostaVaihtoehdot(System.out);
        
        System.out.println();
        System.out.println(vaativuus.getNimi());
        System.out.println(hinnat.getVastaavaVaihtoehto(3));
        System.out.println(hinnat.getVastaavaVaihtoehto(0));
        System.out.println(hinnat.toString());
    }

}
