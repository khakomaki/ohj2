package reseptihaku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
import kanta.TietueHallitsija;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 * Osion ainesosat hallitsee osion ainesosa -olioita.
 */
public class OsionAinesosat extends TietueHallitsija<OsionAinesosa> implements Hallitsija<OsionAinesosa> {
    /**
     * Alustukset testejä varten
     * 
     * @example
     * <pre name="testJAVA">
     * private OsionAinesosat kaikkiAinesosat;
     * private String tiedNimi;
     * private File tiedosto;
     * 
     * @Before
     * public void alusta() throws SailoException {
     *     tiedNimi = "testiAinesosat";
     *     tiedosto = new File(tiedNimi + ".db");
     *     tiedosto.delete();
     *     kaikkiAinesosat = new OsionAinesosat(1, tiedNimi);
     * }
     * 
     * @After
     * public void siivoa() {
     *     tiedosto.delete();
     * }
     * </pre>
     */
	
    private String tiedostonimi     = "osion_ainesosat.dat";
    private String tiedostopolku    = "reseptidata/Reseptin nimi/Osion nimi/";
    private int osioId              = -1;
    private boolean muutettu        = true;
    
    private static OsionAinesosa esimerkkiAinesosa 	= new OsionAinesosa();
    private Tietokanta tietokanta 					= null;
    
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
        super(2.0, 0);
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
        super(2.0, 0);
        this.osioId = osioId;
    }
    
    
    /**
     * Hallitsee tietokannassa sijaitsevia ainesosa-olioita
     * 
     * @param osioId osion tunnus
     * @param tietokantaNimi tietokannan nimi
     * @throws SailoException jos tulee ongelmia
     */
    public OsionAinesosat(int osioId, String tietokantaNimi) throws SailoException {
        super(2.0, 0);
    	this.osioId = osioId;
        this.tietokanta = Tietokanta.alustaTietokanta(tietokantaNimi);
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys() ) {
            // haetaan tietokannan metadatasta omaa tietokantaa, luodaan jos ei ole
            DatabaseMetaData metadata = yhteys.getMetaData();
            
            try ( ResultSet taulu = metadata.getTables(null, null, "Ainesosat", null)) {
                // luodaan uusi taulu jos ei voida siirtyä seuraavaan
                if ( !taulu.next() ) {
                    try ( PreparedStatement sql = yhteys.prepareStatement(esimerkkiAinesosa.getLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch ( SQLException exception ) {
            throw new SailoException("Ongelmia ainesosien luonnissa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Asettaa osion tunnuksen, johon ainesosat kuuluvat
     * 
     * @param id osion tunnus, johon ainesosat kuuluvat
     */
    public void setOsioId(int id) {
        this.osioId = id;
        
        for (OsionAinesosa oa : this) {
        	oa.setOsioId(id);
        }
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
        this.tiedostonimi = tiedostonimi;
        this.muutettu = true;
    }
    
    
    /**
     * Vaihtaa tiedostopolun
     * 
     * @param tiedostopolku mihin polkuun tiedosto tallennetaan ja mistä sitä luetaan
     */
    public void setTiedostoPolku(String tiedostopolku) {
        if (tiedostopolku == null) return;
        
        this.tiedostopolku = tiedostopolku; // esim. reseptidata/Mustikkapiirakka/Muropohja/
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
    @Override
    public void lisaa(OsionAinesosa osionAinesosa) {
        if (osionAinesosa == null) return;
        osionAinesosa.setOsioId(this.osioId);
        add(osionAinesosa);
        this.muutettu = true;
    }
    
    
    /**
     * Lisää ainesosan
     * 
     * @param ainesosa lisättävä ainesosa
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
     * Collection<OsionAinesosa> loytyneetAinesosat = kaikkiAinesosat.get();
     * loytyneetAinesosat.size() === 0;
     * 
     * OsionAinesosa ainesosa1 = new OsionAinesosa("Mansikka", "5dl");
     * OsionAinesosa ainesosa2 = new OsionAinesosa("Mustikka", "2dl");
     * 
     * kaikkiAinesosat.lisaaAinesosa(ainesosa1);
     * kaikkiAinesosat.lisaaAinesosa(ainesosa2);
     * loytyneetAinesosat = kaikkiAinesosat.get();
     * loytyneetAinesosat.size() === 2;
     * 
     * kaikkiAinesosat.poistaAinesosa(ainesosa1);
     * loytyneetAinesosat = kaikkiAinesosat.get();
     * loytyneetAinesosat.size() === 1;
     * </pre>
     */
    public void lisaaAinesosa(OsionAinesosa ainesosa) throws SailoException {
        // asetetaan lisättävälle ainesosalle sama osiotunnus
    	ainesosa.setOsioId(this.osioId);
        
        // muodostaa yhteyden tietokantaan, pyytää ainesosalta lisäyslausekkeen ja suorittaa
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = ainesosa.getLisayslauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia lisäyksessä tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Palauttaa kaikki tietokannasta löytyvät omat ainesosat
     * 
     * @return kokoelma luokan ainesosista
     * @throws SailoException jos hakemisessa tulee ongelmia
     */
    public Collection<OsionAinesosa> get() throws SailoException {
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Ainesosat WHERE osio_id = ?") ) {
            ArrayList<OsionAinesosa> loydetytAinesosat = new ArrayList<OsionAinesosa>();
            sql.setInt(1, this.osioId);
            
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    OsionAinesosa ainesosa = new OsionAinesosa();
                    ainesosa.parse(tulokset);
                    loydetytAinesosat.add(ainesosa);
                }
            }
            return loydetytAinesosat;
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ainesosien haussa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa annetun ainesosan
     * 
     * @param ainesosa poistettava ainesosa
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaAinesosa(OsionAinesosa ainesosa) throws SailoException {
        // muodostaa yhteyden tietokantaan, pyytää ohjeelta poistolausekkeen ja suorittaa
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = ainesosa.getPoistolauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia poistossa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Luo osion ainesosan.
     * Ei lisää vielä osion ainesosiin.
     */
    @Override
    public OsionAinesosa luo() {
        return new OsionAinesosa();
    }
    
    
    /**
     * Poistaa osion ainesosan, jos annettu ainesosa löytyy.
     * 
     * @param osionAinesosa poistettava osion ainesosa
     */
    @Override
    public void poista(OsionAinesosa osionAinesosa) {
        remove(osionAinesosa);
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
        return get(indeksi);
    }
    
    
    /**
     * Antaa osion ainesosat listana
     */
    @Override
    public List<OsionAinesosa> anna() {
        List<OsionAinesosa> lista = new ArrayList<OsionAinesosa>();
        
        // lisätään ainesosat listaan
        for (OsionAinesosa oa : this) {
            lista.add(oa);
        }
        
        return lista;
    }
    
    
    /**
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaOsionAinesosat(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < size(); i++) {
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
     * Kertoo onko osion ainesosiin tullut tallentamattomia muutoksia
     * 
     * @return onko tallentamattomia muutoksia
     */
    public boolean onkoTallentamattomiaMuutoksia() {
        return this.muutettu;
    }
    
    
    /**
     * Lukee Osion Ainesosien tiedot tiedostosta
     * 
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        try (Scanner fi = new Scanner(new FileInputStream(new File(this.tiedostopolku + this.tiedostonimi)))) {
            while (fi.hasNext()) {
                String rivi = fi.nextLine().strip();
                
                // skipataan tyhjät ja kommenttirivit
                if (rivi.length() < 0 || rivi.charAt(0) == ';') continue;
                
                // käsketään osion ainesosan parsimaan tiedot ja lisätään nykyisiin ainesosiin
                OsionAinesosa osionAinesosa = new OsionAinesosa();
                osionAinesosa.parse(rivi);
                lisaa(osionAinesosa);
            }
            
            this.muutettu = false;
            
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
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
        if (!onkoTallentamattomiaMuutoksia()) return;
        
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
            // ainesosien tiedot kirjoittajaan
            for (int i = 0; i < this.size(); i++) {
                fo.print(this.osioId);
                fo.print('|');
                fo.println(anna(i));
            }
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
        } catch (IOException exception) {
            throw new SailoException("Tiedostoon \"" + this.tiedostopolku + this.tiedostonimi + "\" kirjoittamisessa ongelma");
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
        kopio.tiedostonimi = this.tiedostonimi;
        
        // kopioidaan kaikki alkiot kopioon
        for (OsionAinesosa oa : this) {
            kopio.lisaa(oa);
        }
        
        return kopio;
    }
    
    
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
    @Override
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        
        OsionAinesosat verrattavaOA = (OsionAinesosat)verrattava;
        if (verrattavaOA.osioId != this.osioId) return false;
        if (!verrattavaOA.tiedostonimi.equals(this.tiedostonimi)) return false;
        if (verrattavaOA.size() != this.size()) return false;
        
        // verrataan alkioita keskenään
        for (int i = 0; i < this.size(); i++) {
            if (!verrattavaOA.anna(i).equals(this.anna(i))) return false;
        }
        
        return true;
    }
    
    
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
    @Override
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautus(hash, this.osioId);
        hash = Hajautus.hajautus(hash, this.tiedostonimi);
        
        for (OsionAinesosa oa : this) {
            hash = Hajautus.hajautus(hash, oa.hashCode());
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
        sb.append(this.tiedostonimi);
        sb.append('|');
        sb.append(size());
        return sb.toString();
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    	try {
			OsionAinesosat oat = new OsionAinesosat(1, "testidb");
			
			OsionAinesosa oa1 = new OsionAinesosa("porkkanaa", "300g");
			OsionAinesosa oa2 = new OsionAinesosa("perunaa", "3kpl");
			oat.lisaaAinesosa(oa1);
			oat.lisaaAinesosa(oa2);
			System.out.println(oat.get());
			
			oat.poistaAinesosa(oa1);
			System.out.println(oat.get());
			
			oat.poistaAinesosa(oa2);
			System.out.println(oat.get());
			
		} catch (SailoException exception) {
			System.err.println(exception.getMessage());
		}
    	
    	/*
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
        */
    }
}
