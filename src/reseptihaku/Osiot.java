package reseptihaku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import kanta.Hajautus;
import kanta.Hallitsija;
import kanta.MerkkijonoKasittely;
import kanta.SailoException;
import kanta.Validoi;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 * Osiot hallitsee osio-olioita.
 */
public class Osiot implements Hallitsija<Osio> {

    private String tiedostonimi     = "resepti_osiot.dat";
    private String tiedostopolku    = "reseptidata/Reseptin nimi/";
    private int reseptiId           = -1;
    private List<Osio> osiot        = new ArrayList<Osio>();
    
    private static Osio esimerkkiOsio 	= new Osio();
    private Tietokanta tietokanta 		= null;
    
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
     * Hallitsee tietokannassa sijaitsevia Osio-olioita
     * 
     * @param reseptiID reseptin tunnus
     * @param tietokantaNimi tietokannan nimi
     * @throws SailoException jos tulee ongelmia
     */
    public Osiot(int reseptiID, String tietokantaNimi) throws SailoException {
        this.reseptiId = reseptiID;
        this.tietokanta = Tietokanta.alustaTietokanta(tietokantaNimi);
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys() ) {
            // haetaan tietokannan metadatasta omaa tietokantaa, luodaan jos ei ole
            DatabaseMetaData metadata = yhteys.getMetaData();
            
            try ( ResultSet taulu = metadata.getTables(null, null, "Osiot", null)) {
                // luodaan uusi taulu jos ei voida siirtyä seuraavaan
                if ( !taulu.next() ) {
                    try ( PreparedStatement sql = yhteys.prepareStatement(esimerkkiOsio.getLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch ( SQLException exception ) {
            throw new SailoException("Ongelmia osioiden luonnissa tietokannan kanssa: " + exception.getMessage());
        }
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
        this.tiedostonimi = tiedostonimi;
    }
    
    
    /**
     * Asettaa tiedostopolun johon osion tiedot tallennetaan
     * 
     * @param tiedostopolku mihin osion tiedot tallennetaan
     */
    public void setTiedostoPolku(String tiedostopolku) {
        // ei tee mitään jos null tai sama kuin oli
        if (tiedostopolku == null) return;
        
        this.tiedostopolku = tiedostopolku; // esim. reseptidata/Mustikkapiirakka/
        
        for (Osio osio : this.osiot) {
            osio.setTiedostopolku(this.tiedostopolku);
        }
    }
    
    
    /**
     * Asettaa reseptin tunnuksen, johon osio kuuluu
     * 
     * @param tunnus reseptin tunnus
     */
    public void setReseptiTunnus(int tunnus) {
        this.reseptiId = tunnus;
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
     * Osio pizzapohja = osiot.lisaa("Pizzapohja");
     * osiot.toString() === "1|resepti_osiot.dat";
     * pizzapohja.toString() === "-1|Pizzapohja";
     * 
     * Osio tomaattikastike = osiot.lisaa("Tomaattikastike");
     * osiot.toString() === "2|resepti_osiot.dat";
     * tomaattikastike.toString() === "-1|Tomaattikastike";
     * 
     * Osio taytteet = osiot.lisaa("Täytteet");
     * osiot.toString() === "3|resepti_osiot.dat";
     * taytteet.toString() === "-1|Täytteet";
     * 
     * osiot.lisaa("Täytteet (vaihtoehto 2)");
     * osiot.toString() === "4|resepti_osiot.dat";
     * 
     * osiot.lisaa("Täytteet (vaihtoehto 3)");
     * osiot.toString() === "5|resepti_osiot.dat";
     * </pre>
     */
    public Osio lisaa(String nimi) {
        Osio osio = new Osio(nimi);
        osiot.add(osio);
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
     * osiot.lisaa(kastike);
     * osiot.toString() === "1|resepti_osiot.dat";
     * </pre>
     */
    @Override
    public void lisaa(Osio osio) {
        if (osio == null) return;
        Osio lisattavaOsio = osio;
        this.osiot.add(lisattavaOsio);
    }
    
    
    /**
     * Lisää osion
     * 
     * @param osio lisättävä osio
     * 
     * @throws SailoException jos lisäämisen kanssa tulee ongelmia
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     * #import kanta.SailoException;
     * 
     * Collection<Osio> loytyneetOsiot = kaikkiOsiot.get();
     * loytyneetOsiot.size() === 0;
     * 
     * Osio osio1 = new Osio();
     * Osio osio2 = new Osio();
     * 
     * kaikkiOsiot.lisaa(osio1);
     * kaikkiOsiot.lisaa(osio2);
     * loytyneetOsiot = kaikkiOsiot.get();
     * loytyneetOsiot.size() === 0;
     * </pre>
     */
    public void lisaaOsio(Osio osio) throws SailoException {
        // asetetaan lisättävälle osiolle sama reseptitunnus
        osio.setReseptiID(this.reseptiId);
        
        // muodostaa yhteyden tietokantaan, pyytää osiolta lisäyslausekkeen ja suorittaa
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = osio.getLisayslauseke(yhteys) ) {
            sql.executeUpdate();
            
            // tarkistetaan saiko osio uuden tunnuksen
            try ( ResultSet tulokset = sql.getGeneratedKeys() ) {
                osio.tarkistaID(tulokset);
             }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia lisäyksessä tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Palauttaa kaikki tietokannasta löytyvät omat osiot
     * 
     * @return kokoelma luokan osioista
     * @throws SailoException jos hakemisessa tulee ongelmia
     */
    public Collection<Osio> get() throws SailoException {
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Osiot WHERE resepti_id = ?") ) {
            ArrayList<Osio> loydetytOsiot = new ArrayList<Osio>();
            sql.setInt(1, this.reseptiId);
            
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    Osio osio = new Osio();
                    osio.parse(tulokset);
                    loydetytOsiot.add(osio);
                }
            }
            return loydetytOsiot;
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osioiden haussa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa annetun osion
     * 
     * @param osio poistettava osio
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaOsio(Osio osio) throws SailoException {
        // muodostaa yhteyden tietokantaan, pyytää osiolta poistolausekkeen ja suorittaa
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = osio.getPoistolauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia poistossa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Luo osion.
     * Ei vielä lisää osioihin.
     */
    @Override
    public Osio luo() {
        return new Osio();
    }
    
    
    /**
     * Poistaa annetun osion
     * 
     * @param osio poistettava osio
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.lisaa("Pohja");
     * Osio tayte = new Osio("Täyte");
     * osiot.lisaa(tayte);
     * osiot.lisaa("Kuorrute");
     * osiot.toString() === "3|resepti_osiot.dat";
     * 
     * osiot.poista(tayte);
     * osiot.toString() === "2|resepti_osiot.dat";
     * 
     * osiot.poista(tayte);
     * osiot.toString() === "2|resepti_osiot.dat";
     * </pre>
     */
    @Override
    public void poista(Osio osio) {
        this.osiot.remove(osio);
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan Osio
     * @return indeksissä ollut Osio tai null
     */
    public Osio annaIndeksista(int indeksi) {
        return this.osiot.get(indeksi);
    }
    
    
    /**
     * Antaa osiot listana
     */
    @Override
    public List<Osio> anna() {
        return this.osiot;
    }
    
    
    /**
     * Kertoo onko osioissa tallentamattomia muutoksia
     * 
     * @return onko tallentamattomia muutoksia
     */
    public boolean onkoTallentamattomiaMuutoksia() {
        for (Osio osio : this.osiot) {
            if (osio.onkoTallentamattomiaMuutoksia()) return true;
        }
        return false;
    }
    
    
    /**
     * Kertoo voidaanko osiot tallentaa
     * 
     * @return virheteksti tai null jos voidaan tallentaa
     */
    public String voidaankoTallentaa() {
        // samoja osioiden nimiä varten
        List<String> osioNimet = new ArrayList<String>();
        
        // kysytään jokaseilta osiolta
        for (Osio osio : this.osiot) {
            String virhe = osio.voidaankoTallentaa();
            if (virhe != null) return virhe;    
            osioNimet.add(osio.getNimi());
        }

        if (osioNimet.size() <= 0) return "Ei ole yhtään osiota!";
        if (Validoi.onkoDuplikaatteja(osioNimet)) return "Sama osion nimi useaan kertaan!";
        
        return null;
    }
    
    
    /**
     * Lukee Osion tiedot tiedostosta
     * 
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        try (Scanner fi = new Scanner(new FileInputStream(new File(this.tiedostopolku + this.tiedostonimi)))) {
            while (fi.hasNext()) {
                String rivi = fi.nextLine().strip();
                
                // skipataan tyhjät ja kommenttirivit
                if (rivi.length() < 0 || rivi.charAt(0) == ';') continue;
                
                // käsketään osiota parsimaan tiedot ja lisätään nykyisiin osioihin
                Osio osio = new Osio();
                osio.parse(rivi);
                lisaa(osio);
                
                // käsketään osiota lukemaan omat tietonsa (ainesosat ja ohjeet)
                osio.setTiedostopolku(this.tiedostopolku);
                osio.lueTiedostosta();
            }
            
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
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
        // tarkitestaan voidaanko tallentaa
        String virhe = voidaankoTallentaa();
        if (virhe != null) throw new SailoException("Ei voida tallentaa: " + virhe);
        
        // käskee osioiden tallentamaan omat tietonsa (varmistetaan myös että osiot ovat saaneet uniikit tunnuksensa)
        for (Osio osio : this.osiot) {
            osio.setTiedostopolku(this.tiedostopolku);
            osio.tallenna();
        }
        
        File tiedosto = new File(this.tiedostopolku + this.tiedostonimi);
        File varmuuskopio = new File(this.tiedostopolku + MerkkijonoKasittely.vaihdaTiedostopaate(this.tiedostonimi, "bak"));
        
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
            // osioiden tiedot kirjoittajaan
            for (int i = 0; i < this.getLkm(); i++) {
                fo.print(this.reseptiId);
                fo.print('|');
                fo.println(annaIndeksista(i));
            }
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
        } catch (IOException exception) {
            throw new SailoException("Tiedostoon \"" + this.tiedostopolku + this.tiedostonimi + "\" kirjoittamisessa ongelma");
        }
    }
    
    
    /**
     * Luo mustikkapiiran osiot, muropohjan ja täytteen testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoMustikkapiirakanOsiot() {
        Osio muropohja = lisaa("Muropohja");
        muropohja.luoMuropohja();
        
        Osio tayte = lisaa("Täyte");
        tayte.luoTayte();
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.setTiedostoNimi("pizza_osiot.txt");
     * osiot.lisaa(new Osio("Pizzapohja"));
     * osiot.lisaa(new Osio("Tomaattikastike"));
     * osiot.lisaa(new Osio("Täytteet"));
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
        kopio.reseptiId = this.reseptiId;
        kopio.tiedostonimi = this.tiedostonimi;
        
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
     * osiot1.lisaa(new Osio("Pizzapohja"));
     * osiot1.lisaa(new Osio("Tomaattikastike"));
     * osiot1.equals(osiot2) === false;
     * 
     * osiot2.lisaa(new Osio("Pizzapohja"));
     * osiot2.lisaa(new Osio("Tomaattikastike"));
     * osiot1.equals(osiot2) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Osiot verrattavaOsiot = (Osiot)verrattava;
        if (!verrattavaOsiot.tiedostonimi.equals(this.tiedostonimi)) return false;
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
     * osiot1.lisaa(new Osio("Pizzapohja"));
     * osiot1.lisaa(new Osio("Tomaattikastike"));
     * osiot1.hashCode() == osiot2.hashCode() === false;
     * 
     * osiot2.lisaa(new Osio("Pizzapohja"));
     * osiot2.lisaa(new Osio("Tomaattikastike"));
     * osiot1.hashCode() == osiot2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautus(hash, this.tiedostonimi);
        
        for (Osio osio : this.osiot) {
            hash = Hajautus.hajautus(hash, osio.hashCode());
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
        sb.append(this.tiedostonimi);
        return sb.toString();
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    	Osiot osiot;
		try {
			osiot = new Osiot(1, "testidb");
			
	    	Osio osio1 = new Osio("Pizzapohja");
	    	Osio osio2 = new Osio("Tomaattikastike");
	    	osiot.lisaaOsio(osio1);
	    	osiot.lisaaOsio(osio2);
	    	System.out.println(osiot.get());
	    	
	    	osiot.poistaOsio(osio1);
	    	System.out.println(osiot.get());
	    	
		} catch (SailoException exception) {
			System.err.println(exception.getMessage());
		}
    	
    	/*
        Osiot pizzanOsiot = new Osiot();
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaa("Pizzapohja");
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaa("Tomaatti kastike");
        System.out.println(pizzanOsiot.toString());
        pizzanOsiot.lisaa("Täytteet");
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
        */
    }
}
