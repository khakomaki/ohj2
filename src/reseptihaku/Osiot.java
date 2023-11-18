package reseptihaku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import kanta.Hajautus;
import kanta.MerkkijonoKasittely;
import kanta.SailoException;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 */
public class Osiot {

    private String tiedostoNimi     = "resepti_osiot.dat";
    private String polku            = "reseptidata/";
    private int juoksevaId          = 1;
    private int reseptiId           = -1;
    private ArrayList<Osio> osiot   = new ArrayList<Osio>();
    private boolean muutettu        = true;
    
    /**
     * Luo osiot
     */
    public Osiot() {
        //
    }
    
    
    /**
     * Luo osiot
     * 
     * @param reseptinTunnus mille reseptille osiot luodaan
     */
    public Osiot(int reseptinTunnus) {
        this.reseptiId = reseptinTunnus;
    }
    
    
    /**
     * Asettaa tiedosto nimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
     * 
     * @param tiedostonimi tiedoston nimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|resepti_osiot.dat";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostoNimi = tiedostonimi;
        this.muutettu = true;
    }
    
    
    /**
     * Asettaa tiedostopolun johon osion tiedot tallennetaan
     * 
     * @param tiedostopolku mihin osion tiedot tallennetaan
     */
    public void setTiedostoPolku(String tiedostopolku) {
        if (tiedostopolku == null) return;
        this.polku = tiedostopolku;
        this.muutettu = true;
    }
    
    
    /**
     * @return osioiden lukumäärä
     */
    public int getLkm() {
        return this.osiot.size();
    }
    
    
    /**
     * @param nimi minkä niminen osio lisätään
     * @return lisätty osio
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|resepti_osiot.dat";
     * Osio pizzapohja = osiot.lisaaOsio("Pizzapohja");
     * osiot.toString() === "1|resepti_osiot.dat";
     * pizzapohja.toString() === "1|Pizzapohja";
     * 
     * Osio tomaattikastike = osiot.lisaaOsio("Tomaattikastike");
     * osiot.toString() === "2|resepti_osiot.dat";
     * tomaattikastike.toString() === "2|Tomaattikastike";
     * 
     * Osio taytteet = osiot.lisaaOsio("Täytteet");
     * osiot.toString() === "3|resepti_osiot.dat";
     * taytteet.toString() === "3|Täytteet";
     * 
     * osiot.lisaaOsio("Täytteet (vaihtoehto 2)");
     * osiot.toString() === "4|resepti_osiot.dat";
     * 
     * osiot.lisaaOsio("Täytteet (vaihtoehto 3)");
     * osiot.toString() === "5|resepti_osiot.dat";
     * </pre>
     */
    public Osio lisaaOsio(String nimi) {
        Osio osio = new Osio(this.juoksevaId, nimi);
        osiot.add(osio);
        this.juoksevaId++;
        this.muutettu = true;
        return osio;
    }
    
    
    /**
     * Lisää yhden osion.
     * Ei tee mitään null-viitteelle.
     * 
     * @param osio lisättävä osio
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|resepti_osiot.dat";
     * 
     * Osio kastike = new Osio("Kastike");
     * osiot.lisaaOsio(kastike);
     * osiot.toString() === "1|resepti_osiot.dat";
     * </pre>
     */
    public void lisaaOsio(Osio osio) {
        if (osio == null) return;
        Osio lisattavaOsio = osio;
        this.osiot.add(lisattavaOsio);
        this.muutettu = true;
    }
    
    
    /**
     * Poistaa annetun osion
     * 
     * @param osio poistettava osio
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.lisaaOsio("Pohja");
     * Osio tayte = new Osio("Täyte");
     * osiot.lisaaOsio(tayte);
     * osiot.lisaaOsio("Kuorrute");
     * osiot.toString() === "3|resepti_osiot.dat";
     * 
     * osiot.poistaOsio(tayte);
     * osiot.toString() === "2|resepti_osiot.dat";
     * 
     * osiot.poistaOsio(tayte);
     * osiot.toString() === "2|resepti_osiot.dat";
     * </pre>
     */
    public void poistaOsio(Osio osio) {
        this.osiot.remove(osio);
        this.muutettu = true;
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan Osio
     * @return indeksissä ollut Osio tai null
     */
    public Osio annaIndeksista(int indeksi) {
        return this.osiot.get(indeksi);
    }
    
    
    /**
     * Lukee Osion tiedot tiedostosta
     * 
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        try (Scanner fi = new Scanner(new FileInputStream(new File(this.polku + this.tiedostoNimi)))) {
            while (fi.hasNext()) {
                String rivi = fi.nextLine().strip();
                
                // skipataan tyhjät ja kommenttirivit
                if (rivi.length() < 0 || rivi.charAt(0) == ';') continue;
                
                // käsketään osiota parsimaan tiedot ja lisätään nykyisiin osioihin
                Osio osio = new Osio();
                osio.parse(rivi);
                lisaaOsio(osio);
            }
            
            this.muutettu = false;
            
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.polku + this.tiedostoNimi + "\" ei saada avattua");
        }
    }
    
    
    /**
     * Tallentaa osiot tiedostoon.
     * Varmuuskopioi tallennustiedoston ennen kuin tallentaa uudestaan osiot.
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
            // syöttää jokaisen osion tiedot omalle rivilleen
            for (int i = 0; i < this.getLkm(); i++) {
                fo.print(this.reseptiId);
                fo.print('|');
                fo.println(annaIndeksista(i));
            }
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.polku + this.tiedostoNimi + "\" ei saada avattua");
            
        } catch (IOException exception) {
            throw new SailoException("Tiedostoon \"" + this.polku + this.tiedostoNimi + "\" kirjoittamisessa ongelma");
            
        }
        
        // käskee osioiden tallentamaan omat tietonsa
        for (Osio osio : this.osiot) {
            osio.tallenna();
        }
        
        this.muutettu = false;
    }
    
    
    /**
     * Luo mustikkapiiran osiot, muropohjan ja täytteen testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoMustikkapiirakanOsiot() {
        Osio muropohja = lisaaOsio("Muropohja");
        muropohja.luoMuropohja();
        
        Osio tayte = lisaaOsio("Täyte");
        tayte.luoTayte();
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.setTiedostoNimi("pizza_osiot.txt");
     * osiot.lisaaOsio(new Osio("Pizzapohja"));
     * osiot.lisaaOsio(new Osio("Tomaattikastike"));
     * osiot.lisaaOsio(new Osio("Täytteet"));
     * 
     * Osiot kopioOsiot = osiot.clone();
     * kopioOsiot.toString().equals(osiot.toString()) === true;
     * 
     * osiot.annaIndeksista(1).getNimi() === "Tomaattikastike";
     * kopioOsiot.annaIndeksista(1).getNimi() === "Tomaattikastike";
     * 
     * osiot.annaIndeksista(1).setUusiNimi("Tulinen kastike");
     * osiot.annaIndeksista(1).getNimi() === "Tulinen kastike";
     * kopioOsiot.annaIndeksista(1).getNimi() === "Tomaattikastike";
     * 
     * </pre>
     */
    public Osiot clone() {
        Osiot kopio = new Osiot();
        kopio.juoksevaId = this.juoksevaId;
        kopio.tiedostoNimi = this.tiedostoNimi;
        
        // kopioidaan yksittäiset osiot
        for (int i = 0; i < this.osiot.size(); i++) {
            kopio.osiot.add(this.osiot.get(i).clone());
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Osiot osiot1 = new Osiot();
     * Osiot osiot2 = new Osiot();
     * osiot1.equals(osiot2) === true;
     * 
     * osiot1.setTiedostoNimi("pizza_osiot.txt");
     * osiot1.equals(osiot2) === false;
     * 
     * osiot2.setTiedostoNimi("pizza_osiot.txt");
     * osiot1.equals(osiot2) === true;
     * 
     * osiot1.lisaaOsio(new Osio("Pizzapohja"));
     * osiot1.lisaaOsio(new Osio("Tomaattikastike"));
     * osiot1.equals(osiot2) === false;
     * 
     * osiot2.lisaaOsio(new Osio("Pizzapohja"));
     * osiot2.lisaaOsio(new Osio("Tomaattikastike"));
     * osiot1.equals(osiot2) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Osiot verrattavaOsiot = (Osiot)verrattava;
        if (verrattavaOsiot.juoksevaId != this.juoksevaId) return false;
        if (!verrattavaOsiot.tiedostoNimi.equals(this.tiedostoNimi)) return false;
        if (verrattavaOsiot.osiot.size() != this.osiot.size()) return false;
        
        // käsketään yksittäisten osioiden vertaamaan toisiaan keskenään
        for (int i = 0; i < this.osiot.size(); i++) {
            if (!verrattavaOsiot.osiot.get(i).equals(this.osiot.get(i))) return false;
        }
        
        return true;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Osiot osiot1 = new Osiot();
     * Osiot osiot2 = new Osiot();
     * osiot1.hashCode() == osiot2.hashCode() === true;
     * 
     * osiot1.setTiedostoNimi("pizza_osiot.txt");
     * osiot1.hashCode() == osiot2.hashCode() === false;
     * 
     * osiot2.setTiedostoNimi("pizza_osiot.txt");
     * osiot1.hashCode() == osiot2.hashCode() === true;
     * 
     * osiot1.lisaaOsio(new Osio("Pizzapohja"));
     * osiot1.lisaaOsio(new Osio("Tomaattikastike"));
     * osiot1.hashCode() == osiot2.hashCode() === false;
     * 
     * osiot2.lisaaOsio(new Osio("Pizzapohja"));
     * osiot2.lisaaOsio(new Osio("Tomaattikastike"));
     * osiot1.hashCode() == osiot2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusInt(hash, this.juoksevaId);
        hash = Hajautus.hajautusString(hash, this.tiedostoNimi);
        
        for (int i = 0; i < this.osiot.size(); i++) {
            hash = Hajautus.hajautusInt(hash, this.osiot.get(i).hashCode());
        }
        
        return hash;
    }
    
    
    @Override
    /**
     * Palauttaa tiedot muodossa "tiedostonimi"
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|resepti_osiot.dat";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.osiot.size());
        sb.append('|');
        sb.append(this.tiedostoNimi);
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osiot pizzanOsiot = new Osiot();
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaaOsio("Pizzapohja");
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaaOsio("Tomaatti kastike");
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaaOsio("Täytteet");
        System.out.println(pizzanOsiot.toString());
        
        Osio pizzapohja = pizzanOsiot.annaIndeksista(0);
        Osio tomaattikastike = pizzanOsiot.annaIndeksista(1);
        Osio taytteet = pizzanOsiot.annaIndeksista(2);
        System.out.println(pizzapohja + "\n" + tomaattikastike + "\n" + taytteet);
        
        pizzanOsiot = new Osiot();
        
        try {
            pizzanOsiot.tallenna();
        } catch (SailoException e) {
            System.out.println(e.getMessage());
        }
        
        Osiot osiotTiedostosta = new Osiot();
        
        try {
            osiotTiedostosta.lueTiedostosta();
        } catch (SailoException e) {
            System.out.println(e.getMessage());;
        }
        
        System.out.println(osiotTiedostosta);
    }
}
