package reseptihaku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Hajautus;
import kanta.MerkkijonoKasittely;
import kanta.SailoException;
import kanta.TietueHallitsija;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class OsionAinesosat extends TietueHallitsija<OsionAinesosa> {
    
    private String tiedostoNimi     = "osion_ainesosat.dat";
    private String polku            = "reseptidata/";
    private int osioId              = -1;
    private boolean muutettu        = false;
    
    
    /**
     * Hallitsee osion ainesosia ja määriä.
     * Alustuu oletuksena osio id:llä -1.
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat oletus = new OsionAinesosat();
     * oletus.toString() === "-1|osion_ainesosat.dat|0";
     * </pre>
     */
    public OsionAinesosat() {
        //
    }
    
    
    /**
     * Hallitsee osion ainesosia ja määriä
     * 
     * @param osioId mihin osioon ainesosat ja määrät luodaan
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat muropohjaAinesosat = new OsionAinesosat(2);
     * muropohjaAinesosat.toString() === "2|osion_ainesosat.dat|0";
     * </pre>
     */
    public OsionAinesosat(int osioId) {
        this.osioId = osioId;
    }
    
    
    /**
     * Asettaa osion tunnuksen, johon ainesosat kuuluvat
     * 
     * @param id osion tunnus, johon ainesosat kuuluvat
     */
    public void setOsioId(int id) {
        this.osioId = id;
    }
    
    
    /**
     * Asettaa tallennustiedoston nimen.
     * Ei anna asettaa tyhjää merkkijonoa tai null-viitettä.
     * 
     * @param tiedostonimi tiedoston nimi johon tallennetaan
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu nimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostoNimi = tiedostonimi;
        this.muutettu = true;
    }
    
    
    /**
     * Vaihtaa tiedostopolun
     * 
     * @param polku mihin polkuun tiedosto tallennetaan ja mistä sitä luetaan
     */
    public void setTiedostoPolku(String polku) {
        if (polku == null) return;
        this.muutettu = true;
    }
    
    
    /**
     * Lisää osion ainesosan.
     * Ei hyväksy null-viitettä.
     * 
     * @param osionAinesosa lisättävä osion ainesosa
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat muropohjaAinesosat = new OsionAinesosat(2);
     * muropohjaAinesosat.toString() === "2|osion_ainesosat.dat|0";
     * 
     * muropohjaAinesosat.lisaa(new OsionAinesosa("kananmuna", "2kpl"));
     * muropohjaAinesosat.lisaa(new OsionAinesosa("sokeri", "1dl"));
     * muropohjaAinesosat.toString() === "2|osion_ainesosat.dat|2";
     * muropohjaAinesosat.anna(0).getAinesosa() === "kananmuna";
     * muropohjaAinesosat.anna(1).getMaara() === "1dl";
     * </pre>
     */
    public void lisaa(OsionAinesosa osionAinesosa) {
        if (osionAinesosa == null) return;
        
        lisaaOlio(osionAinesosa);
        this.muutettu = true;
    }
    
    
    /**
     * Poistaa osion ainesosan, jos annettu ainesosa löytyy.
     * 
     * @param osionAinesosa poistettava osion ainesosa
     */
    public void poista(OsionAinesosa osionAinesosa) {
        poistaOlio(osionAinesosa);
        this.muutettu = true;
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan osion ainesosa
     * @return osion ainesosa halutusta indeksistä tai null
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat oat = new OsionAinesosat();
     * oat.anna(-5) == null === true;
     * oat.anna(0) == null === true;
     * 
     * oat.lisaa(new OsionAinesosa("Mansikka", "2dl"));
     * oat.anna(0).getAinesosa() === "Mansikka";
     * </pre>
     */
    public OsionAinesosa anna(int indeksi) {
        return getOlio(indeksi);
    }
    
    
    /**
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaOsionAinesosat(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < getLkm(); i++) {
            OsionAinesosa osionAinesosa = this.anna(i);
            
            // tulostaa ainesosista tunnusta vastaavan ainesosan nimen
            out.print(osionAinesosa.getAinesosa());
            out.print(" : ");
            
            // tulostaa ainesosan määrän ja rivin vaihdon
            out.print(osionAinesosa.getMaara());
            out.print("\n");
        }
    }
    
    
    /**
     * Lukee Osion Ainesosien tiedot tiedostosta
     * 
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        try (Scanner fi = new Scanner(new FileInputStream(new File(this.polku + this.tiedostoNimi)))) {
            while (fi.hasNext()) {
                String rivi = fi.nextLine().strip();
                
                // skipataan tyhjät ja kommenttirivit
                if (rivi.length() < 0 || rivi.charAt(0) == ';') continue;
                
                // skipataan jos rivin osioId ei ole sama kuin nykyisen
                StringBuilder rivinTiedot = new StringBuilder(rivi);
                int rivinOsioId = Mjonot.erota(rivinTiedot, '|', this.osioId - 1);
                if (rivinOsioId != this.osioId) continue;
                
                // käsketään osion ainesosan parsimaan tiedot ja lisätään nykyisiin ainesosiin
                OsionAinesosa osionAinesosa = new OsionAinesosa();
                osionAinesosa.parse(rivi);
                lisaa(osionAinesosa);
            }
            
            this.muutettu = false;
            
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.polku + this.tiedostoNimi + "\" ei saada avattua");
        }
    }
    
    
    /**
     * Tallentaa osion ainesosat tiedostoon.
     * Varmuuskopioi tallennustiedoston ennen kuin tallentaa uudestaan ainesosat.
     * Luo tallennustiedoston jos sellaista ei vielä ollut.
     * 
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!this.muutettu) return;
        
        File tiedosto = new File(this.polku + this.tiedostoNimi);
        File varmuuskopio = new File(this.polku + MerkkijonoKasittely.vaihdaTiedostopaate(this.tiedostoNimi, "bak"));
        
        // koitetaan poistaa edellistä varmuuskopiota
        // heitetään virhe jos sellainen on olemassa eikä voida poistaa
        if (!varmuuskopio.delete() && varmuuskopio.exists()) {
            throw new SailoException("Ei voida poistaa varmuuskopio-tiedostoa");
        }
        
        // koitetaan luoda tiedosto jos sellaista ei vielä ole
        if (!tiedosto.exists()) {
            try {
                tiedosto.createNewFile();
                
            } catch (IOException exception) {
                throw new SailoException("Ei voida luoda tallennus-tiedostoa");
            }
        }
        
        // koitetaan nimetä olemassaoleva tiedosto varmuuskopioksi
        if (!tiedosto.renameTo(varmuuskopio)) {
            throw new SailoException("Ei voida nimetä uudelleen tallennus-tiedostoa");
        }
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(tiedosto.getCanonicalPath()))) {
            
            // kopioidaan vanhan tiedoston rivit uuteen, jätetään nykyisen osion rivit huomioitta
            try (Scanner fi = new Scanner(new FileInputStream(varmuuskopio))) {
                while (fi.hasNext()) {
                    String rivi = fi.nextLine();
                    
                    // skipataan tyhjät ja kommenttirivit
                    if (rivi.isBlank() || rivi.charAt(0) == ';') continue;
                    
                    // parsii rivin osioId:n, jos ei löydy niin asettaa arvoksi varmasti eri kuin nykyinen osioId
                    StringBuilder riviTiedot = new StringBuilder(rivi);
                    int rivinOsioId = Mjonot.erotaInt(riviTiedot, this.osioId - 1);
                    
                    // syöttää alkuperäisen rivin, jos ei ole sama osio mitä ollaan tallentamassa
                    if (rivinOsioId != this.osioId) {
                        fo.println(rivi);
                    }
                }
            }
            // syöttää jokaisen ohjeen tiedot omalle rivilleen
            for (int i = 0; i < this.getLkm(); i++) {
                fo.print(this.osioId);
                fo.print('|');
                fo.println(anna(i));
            }
            
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.polku + this.tiedostoNimi + "\" ei saada avattua");
            
        } catch (IOException exception) {
            throw new SailoException("Tiedostoon \"" + this.polku + this.tiedostoNimi + "\" kirjoittamisessa ongelma");
            
        }
        
        this.muutettu = false;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * OsionAinesosat oat = new OsionAinesosat(55);
     * oat.lisaa(new OsionAinesosa("riisi", "5dl"));
     * oat.lisaa(new OsionAinesosa("soijakastike", "2rkl"));
     * 
     * OsionAinesosat oatKopio = oat.clone();
     * oat.equals(oatKopio) === true;
     * oat.toString() === "55|osion_ainesosat.dat|2";
     * 
     * oat.lisaa(new OsionAinesosa("sipuli", "1kpl"));
     * oat.equals(oatKopio) === false;
     * 
     * oatKopio.lisaa(new OsionAinesosa("sipuli", "1kpl"));
     * oat.equals(oatKopio) === true;
     * </pre>
     */
    public OsionAinesosat clone() {
        OsionAinesosat kopio = new OsionAinesosat();
        kopio.osioId = this.osioId;
        kopio.tiedostoNimi = this.tiedostoNimi;
        
        // kopioidaan kaikki alkiot kopioon
        // TODO: käske TietueHallitsijan kopioimaan
        for (int i = 0; i < this.getLkm(); i++) {
            kopio.lisaa(this.anna(i));
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * OsionAinesosat oa1 = new OsionAinesosat(1);
     * OsionAinesosat oa2 = new OsionAinesosat(1);
     * oa1.equals(oa2) === true;
     * 
     * oa1.setTiedostoNimi("oa.txt");
     * oa1.equals(oa2) === false;
     * 
     * oa2.setTiedostoNimi("oa.txt");
     * oa1.equals(oa2) === true;
     * 
     * oa1.lisaa(new OsionAinesosa("mustikka", "2dl"));
     * oa1.equals(oa2) === false;
     * 
     * oa2.lisaa(new OsionAinesosa("mustikka", "2dl"));
     * oa1.equals(oa2) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        
        OsionAinesosat verrattavaOA = (OsionAinesosat)verrattava;
        if (verrattavaOA.osioId != this.osioId) return false;
        if (!verrattavaOA.tiedostoNimi.equals(this.tiedostoNimi)) return false;
        if (verrattavaOA.getLkm() != this.getLkm()) return false;
        
        // verrataan alkioita keskenään
        for (int i = 0; i < this.getLkm(); i++) {
            if (!verrattavaOA.anna(i).equals(this.anna(i))) return false;
        }
        
        return true;
    }
    
    
    @Override
    /**
     * @return hajautusluku
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat oa1 = new OsionAinesosat(1);
     * OsionAinesosat oa2 = new OsionAinesosat(1);
     * oa1.hashCode() == oa2.hashCode() === true;
     * 
     * oa1.setTiedostoNimi("oa.txt");
     * oa1.hashCode() == oa2.hashCode() === false;
     * 
     * oa2.setTiedostoNimi("oa.txt");
     * oa1.hashCode() == oa2.hashCode() === true;
     * 
     * oa1.lisaa(new OsionAinesosa("mustikka", "2dl"));
     * oa1.hashCode() == oa2.hashCode() === false;
     * 
     * oa2.lisaa(new OsionAinesosa("mustikka", "2dl"));
     * oa1.hashCode() == oa2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.osioId);
        hash = Hajautus.hajautusString(hash, this.tiedostoNimi);
        
        for (int i = 0; i < this.getLkm(); i++) {
            hash = Hajautus.hajautusInt(hash, this.anna(i).hashCode());
        }
        
        return hash;
    }
    
    
    @Override
    /**
     * Palauttaa tiedot muodossa:
     * "osion id|tiedostonimi|lukumäärä"
     * 
     * @example
     * <pre name="test">
     * OsionAinesosat oa = new OsionAinesosat(1);
     * oa.toString() === "1|osion_ainesosat.dat|0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.tiedostoNimi);
        sb.append('|');
        sb.append(getLkm());
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        int nykyisenOsionId = 1;
        OsionAinesosat osionAinesosat = new OsionAinesosat(nykyisenOsionId);
        System.out.println(osionAinesosat.toString());
        
        osionAinesosat.setTiedostoNimi("soppa.txt");
        osionAinesosat.lisaa(new OsionAinesosa("perunoita", "500g"));
        osionAinesosat.lisaa(new OsionAinesosa("porkkanoita", "2kpl"));
        osionAinesosat.lisaa(new OsionAinesosa("sipulia", "1kpl"));
        osionAinesosat.lisaa(new OsionAinesosa("suolaa", "3/4rkl"));
        
        System.out.println(osionAinesosat.toString());
        
        System.out.println("\nAinesosa tunnukset ja määrät:");
        osionAinesosat.tulostaOsionAinesosat(System.out);
        
        try {
            osionAinesosat.tallenna();
        } catch (SailoException e) {
            System.out.println(e.getMessage());
        }
        
        OsionAinesosat tiedostosta = new OsionAinesosat();
        tiedostosta.setTiedostoNimi("soppa.txt");
        try {
            tiedostosta.lueTiedostosta();
        } catch (SailoException e) {
            System.out.println(e.getMessage());
        }
    }
}
