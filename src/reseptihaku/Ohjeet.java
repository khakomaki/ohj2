package reseptihaku;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kanta.Hajautus;
import kanta.Hallitsija;
import kanta.SailoException;

/**
 * @author hakom
 * @version 30 Oct 2023
 *
 * Ohjeet hallitsee ohje-olioita.
 */
public class Ohjeet implements Hallitsija<Ohje> {
	
    private String tiedostopolku    	= "reseptidata/";
    private String tiedostonimi			= "ohjeet";
    private int osioId              	= -1;
    private List<Ohje> ohjeet       	= new ArrayList<Ohje>();
    private Tietokanta tietokanta       = null;
    
    private static Ohje esimerkkiOhje   = new Ohje();
    
    /**
     * Alustukset testejä varten
     * 
     * @example
     * <pre name="testJAVA">
     * private Ohjeet kaikkiOhjeet;
     * private String tiedPolku;
     * private String tiedNimi;
     * private File tiedosto;
     * 
     * @Before
     * public void alusta() throws SailoException {
     * 	   tiedPolku = "testidata/";
     *     tiedNimi = "testiOhjeet";
     *     tiedosto = new File(tiedPolku + tiedNimi + ".db");
     *     tiedosto.delete();
     *     kaikkiOhjeet = new Ohjeet(1, tiedPolku, tiedNimi);
     * }
     * 
     * @After
     * public void siivoa() {
     *     tiedosto.delete();
     * }
     * </pre>
     */
    
    /**
     * Hallitsee Ohje-oliota
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     * #import kanta.SailoException;
     * 
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.toString() === "ohjeet|-1|0";
     * </pre>
     */
    public Ohjeet() {
        //
    }
    
    
    /**
     * Hallitsee tietokannassa sijaitsevia Ohje-olioita
     * 
     * @param osioId osion tunnus
     */
    public Ohjeet(int osioId) {
        this.osioId = osioId;
    }
    
    
    /**
     * Hallitsee tietokannassa sijaitsevia Ohje-olioita
     * 
     * @param osioId osion tunnus
     * @param tiedostopolku tiedostopolku
     * @param tiedostonimi tiedon nimi
     */
    public Ohjeet(int osioId, String tiedostopolku, String tiedostonimi) {
    	this.osioId = osioId;
    	setTiedostoPolku(tiedostopolku);
    	setTiedostoNimi(tiedostonimi);
    }
    
    
    /**
     * Alustaa yhteyden tietokantaan.
     * Luo oman taulun jos sellaista ei vielä ole.
     * 
     * @throws SailoException jos tulee ongelmia
     */
    private void alustaTietokanta() throws SailoException {
    	Tietokanta.luoHakemisto(this.tiedostopolku);
        this.tietokanta = Tietokanta.alustaTietokanta(this.tiedostopolku + this.tiedostonimi);
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys() ) {
            // haetaan tietokannan metadatasta omaa tietokantaa, luodaan jos ei ole
            DatabaseMetaData metadata = yhteys.getMetaData();
            
            try ( ResultSet taulu = metadata.getTables(null, null, "Ohjeet", null)) {
                // luodaan uusi taulu jos ei voida siirtyä seuraavaan
                if ( !taulu.next() ) {
                    try ( PreparedStatement sql = yhteys.prepareStatement(esimerkkiOhje.getLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ohjeiden luonnissa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Asettaa osion tunnuksen johon ohjeet kuuluu
     * 
     * @param id osion tunnus, johon ohjeet kuuluu
     */
    public void setOsioId(int id) {
        this.osioId = id;
        
        for (Ohje ohje : this.ohjeet) {
        	ohje.setOsioId(this.osioId);
        }
    }
    
    
    /**
     * Lisää ohjeen ohjeisiin.
     * 
     * @param ohje lisättävä ohje
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet(15);
     * ohjeet.toString() === "ohjeet|15|0";
     * 
     * Ohje ohje1 = new Ohje("ensimmäinen", 100);
     * ohje1.getVaihe() === 100;
     * ohjeet.lisaa(ohje1);
     * ohje1.getVaihe() === 1;
     * ohjeet.toString() === "ohjeet|15|1";
     * 
     * Ohje ohje2 = new Ohje("toinen", 75);
     * ohje2.getVaihe() === 75;
     * ohjeet.lisaa(ohje2);
     * ohje2.getVaihe() === 2;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet|15|4";
     * 
     * ohjeet.lisaa(null);
     * ohjeet.toString() === "ohjeet|15|5";
     * </pre>
     */
    @Override
    public void lisaa(Ohje ohje) {
        Ohje lisattavaOhje = ohje;
        
        // luo uuden ohjeen jos annetaan null
        if (ohje == null) lisattavaOhje = new Ohje();
        this.ohjeet.add(lisattavaOhje);
        lisattavaOhje.setVaihe(this.ohjeet.size());
        lisattavaOhje.setOsioId(this.osioId);
    }
    
    
    /**
     * Lisää ohjeen
     * 
     * @param ohje lisättävä ohje
     * 
     * @throws SailoException jos lisäämisen kanssa tulee ongelmia
     */
    public void lisaaTietokantaan(Ohje ohje) throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        // asetetaan lisättävälle ohjeelle sama osiotunnus
        ohje.setOsioId(this.osioId);
        ohje.setVaihe(this.ohjeet.size() + 1);
        
        // muodostaa yhteyden tietokantaan, pyytää ohjeelta lisäyslausekkeen ja suorittaa
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = ohje.getLisayslauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ohjeen lisäyksessä tietokannan kanssa:\n" + exception.getMessage());
        }
        
        this.ohjeet.add(ohje);
    }
    
    
    /**
     * Palauttaa kaikki tietokannasta löytyvät omat ohjeet
     * 
     * @return kokoelma luokan ohjeista
     * @throws SailoException jos hakemisessa tulee ongelmia
     */
    public Collection<Ohje> get() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Ohjeet WHERE osio_id = ?") ) {
            ArrayList<Ohje> loydetytOhjeet = new ArrayList<Ohje>();
            sql.setInt(1, this.osioId);
            
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    Ohje ohje = new Ohje();
                    ohje.parse(tulokset);
                    loydetytOhjeet.add(ohje);
                }
            }
            return loydetytOhjeet;
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ohjeiden haussa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa annetun ohjeen ja päivittää tarvittavasti muiden ohjeiden vaiheita
     * 
     * @param ohje poistettava ohje
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaTietokannasta(Ohje ohje) throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        // muodostaa yhteyden tietokantaan, pyytää ohjeelta poistolausekkeen ja suorittaa
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = ohje.getPoistolauseke(yhteys) ) {
            
            // poistettava vaihe muistiin ja poistetaan ohje
            int poistettavaVaihe = ohje.getVaihe();
            sql.executeUpdate();
            
            // päivitetään tarvittavia vaiheita -1 pienemmiksi
            try (PreparedStatement sqlVaihepaivitys = Ohjeet.esimerkkiOhje.getVaiheidenPaivityslauseke(yhteys, this.osioId, poistettavaVaihe)) {
                sqlVaihepaivitys.setInt(1, this.osioId);
                sqlVaihepaivitys.setInt(2, poistettavaVaihe);
                sqlVaihepaivitys.executeUpdate();
            }
            
            for (int i = poistettavaVaihe; i < this.ohjeet.size(); i++) {
                this.ohjeet.get(i).setVaihe(this.ohjeet.get(i).getVaihe() - 1);
            }
            this.ohjeet.remove(ohje);
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ohjeen poistossa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa ohjeet tietokannasta
     * 
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaTietokannasta() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("DELETE FROM Ohjeet WHERE osio_id = ?")) {
            sql.setInt(1, this.osioId);
            sql.executeUpdate();
            
        } catch (SQLException exception) {
        	throw new SailoException("Ongelmia ohjeiden poistossa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Tallentaa nykyiset ohjeet tietokantaan
     * 
     * @throws SailoException jos tulee ongelmia tallentamisessa
     */
    public void tallenna() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
    	try (Connection yhteys = this.tietokanta.annaTietokantaYhteys()) {
        	// poistetaan nykyiset osion ohjeet
            try (PreparedStatement sqlPoisto = yhteys.prepareStatement("DELETE FROM Ohjeet WHERE osio_id = ?")) {
                sqlPoisto.setInt(1, this.osioId);
                sqlPoisto.executeUpdate();
            }
            
            // lisätään omat ohjeet tietokantaan, jos sellaisia on
            if (0 < this.ohjeet.size()) {
                try (PreparedStatement sqlLisays = Ohjeet.esimerkkiOhje.getMoniLisayslauseke(yhteys, this.ohjeet)) {
                	sqlLisays.executeUpdate();
                }
            }
            
    	} catch (SQLException exception) {
    		throw new SailoException("Ongelmia ohjeiden tallentamisessa tietokannan kanssa:\n" + exception.getMessage());
    	}
    }
    
    
    /**
     * Lukee omat ohjeet tietokannasta
     * 
     * @throws SailoException jos tulee ongelmia lukemisessa
     */
    public void lueTiedostosta() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Ohjeet WHERE osio_id = ?") ) {
            sql.setInt(1, this.osioId);
            
            // luodaan sql-kyselyn tulosten verran ohje-olioita
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    Ohje ohje = new Ohje();
                    ohje.parse(tulokset);
                    this.ohjeet.add(ohje);
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ohjeiden lukemisessa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Luo uuden ohjeen.
     * Ei lisää vielä ohjeisiin.
     */
    @Override
    public Ohje luo() {
        return new Ohje();
    }
    
    
    /**
     * Poistaa indeksistä löytyvän ohjeen ohjeista.
     * Päivittää tarvittaessa muiden vaiheita.
     * 
     * @param indeksi mistä indeksistä poistetaan
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet(2);
     * ohjeet.toString() === "ohjeet|2|0";
     * 
     * ohjeet.poista(-2);
     * ohjeet.poista(0);
     * ohjeet.poista(100);
     * ohjeet.toString() === "ohjeet|2|0";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet|2|1";
     * 
     * ohjeet.poista(0);
     * ohjeet.toString() === "ohjeet|2|0";
     * 
     * ohjeet.lisaa(new Ohje("ohje 1"));
     * ohjeet.lisaa(new Ohje("ohje 2"));
     * ohjeet.lisaa(new Ohje("ohje 3"));
     * ohjeet.anna(1).getOhjeistus() === "ohje 2";
     * 
     * ohjeet.poista(1);
     * ohjeet.anna(1).getOhjeistus() === "ohje 3";
     * </pre>
     */
    public void poista(int indeksi) {
        // ei tehdä mitään jos indeksi ei ole mieluisa
        if (indeksi < 0 || this.ohjeet.size() <= indeksi) return;
        
        ohjeet.remove(indeksi);
        paivitaVaiheet(indeksi);
    }
    
    
    /**
     * Poistaa annetun ohjeen omista ohjeista.
     * Päivittää tarvittaessa muiden vaiheita.
     * 
     * @param poistettava mikä ohje poistetaan
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet();
     * Ohje ohje1 = new Ohje("ohje 1"); ohjeet.lisaa(ohje1);
     * Ohje ohje2 = new Ohje("ohje 2"); ohjeet.lisaa(ohje2);
     * Ohje ohje3 = new Ohje("ohje 3"); ohjeet.lisaa(ohje3);
     * Ohje ohje4 = new Ohje("ohje 4"); ohjeet.lisaa(ohje4);
     * Ohje ohje5 = new Ohje("ohje 5"); ohjeet.lisaa(ohje5);
     * ohjeet.toString() === "ohjeet|-1|5";
     * 
     * ohjeet.anna(1).equals(ohje2) === true;
     * ohje2.getVaihe() === 2;
     * 
     * ohjeet.poista(ohje2);
     * ohjeet.toString() === "ohjeet|-1|4";
     * ohjeet.anna(1).equals(ohje2) === false;
     * 
     * ohjeet.poista(new Ohje("ohje 5"));
     * ohjeet.toString() === "ohjeet|-1|4";
     * 
     * ohjeet.poista(new Ohje("ohje 5", 4));
     * ohjeet.toString() === "ohjeet|-1|3";
     * 
     * ohjeet.poista(ohje2);
     * ohjeet.toString() === "ohjeet|-1|3";
     * </pre>
     */
    @Override
    public void poista(Ohje poistettava) {
        for (int i = 0; i < this.ohjeet.size(); i++) {
            if (this.ohjeet.get(i).equals(poistettava)) {
                ohjeet.remove(i);
                paivitaVaiheet(i);
                break;
            }
        }
    }
    
    
    /**
     * Antaa indeksistä löytyvän ohjeen tai null viitteen
     * 
     * @param indeksi mistä indeksistä halutaan ohje
     * @return indeksistä löytyvä ohje tai null
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.anna(0) === null;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.anna(0) == null === false;
     * </pre>
     */
    public Ohje anna(int indeksi) {
        if (indeksi < 0 || this.ohjeet.size() <= indeksi) return null;
        return ohjeet.get(indeksi);
    }
    
    
    /**
     * Antaa ohjeet listana
     */
    @Override
    public List<Ohje> anna() {
        return this.ohjeet;
    }
    
    
    /**
     * Asettaa tiedosto nimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
     * 
     * @param tiedostonimi tiedoston nimi johon kirjoitetaan tiedot
     */
    public void setTiedostoNimi(String tiedostonimi) {
        if (tiedostonimi == null || tiedostonimi.length() <= 0) return;
        this.tiedostonimi = tiedostonimi;
    }
    
    
    /**
     * Antaa tiedoston nimen
     * 
     * @return tiedoston nimi johon tallennetaan, null jos ei ole määritetty
     */
    public String getTiedostonimi() {
        return this.tiedostonimi;
    }
    
    
    /**
     * Asettaa tiedostopolun.
     * Ei tee mitään jos yritetään asettaa null tai sama kuin aiemmin.
     * 
     * @param tiedostopolku mihin polkuun tiedosto tallennetaan
     */
    public void setTiedostoPolku(String tiedostopolku) {
        if (tiedostopolku == null || this.tiedostopolku.equals(tiedostopolku)) return;
        this.tiedostopolku = tiedostopolku;
    }
    
    
    /**
     * Antaa tiedostpolun
     * 
     * @return tiedostopolku jossa ohjeiden tiedot säilytetään
     */
    public String getTiedostoPolku() {
    	return this.tiedostopolku;
    }
    
    
    /**
     * Kertoo onko ohjeisiin tullut tallentamattomia muutoksia
     * 
     * @return onko tallentamattomia muutoksia
     */
    public boolean onkoTallentamattomiaMuutoksia() {
        return true; // TODO poista kun ei käytetä muualla
    }
    
    
    /**
     * @return ohjeiden lukumäärä
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet();
     * 
     * ohjeet.getLkm() === 0;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.getLkm() === 1;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.getLkm() === 4;
     * </pre>
     */
    public int getLkm() {
        return this.ohjeet.size();
    }
    
    
    /**
     * Päivittää ohjeiden sisältämien ohjeiden vaihe numerot vastaamaan
     * taulukon järjestystä. Alkavat numerosta annetusta indeksista.
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * 
     * ohjeet.anna(0).getVaihe() === 1;
     * ohjeet.anna(1).getVaihe() === 2;
     * ohjeet.anna(2).getVaihe() === 3;
     * </pre>
     */
    private void paivitaVaiheet(int indeksista) {
        if (indeksista < 0) return;
        
        for (int i = indeksista; i < this.ohjeet.size(); i++) {
            Ohje ohje = this.ohjeet.get(i);
            ohje.setVaihe(i + 1);
        }
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet(2);
     * ohjeet.lisaa(new Ohje("Sekoita sokeri ja vehnäjauhot"));
     * ohjeet.lisaa(new Ohje("Lisää kananmuna"));
     * ohjeet.toString() === "ohjeet|2|2";
     * 
     * Ohjeet kopioOhjeet = ohjeet.clone();
     * kopioOhjeet.toString() === "ohjeet|2|2";
     * kopioOhjeet.anna(0).getOhjeistus() === "Sekoita sokeri ja vehnäjauhot";
     * kopioOhjeet.anna(1).getOhjeistus() === "Lisää kananmuna";
     * 
     * ohjeet.anna(1).setOhjeistus("Lisää vanilliinisokeri");
     * ohjeet.anna(1).getOhjeistus() === "Lisää vanilliinisokeri";
     * kopioOhjeet.anna(1).getOhjeistus() === "Lisää kananmuna";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet|2|4";
     * kopioOhjeet.toString() === "ohjeet|2|2";
     * </pre>
     */
    public Ohjeet clone() {
		Ohjeet kopio = new Ohjeet();
        kopio.osioId = this.osioId;
        kopio.tiedostopolku = this.tiedostopolku;
        
        for (Ohje ohje : this.ohjeet) {
            kopio.ohjeet.add(ohje.clone());
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet1 = new Ohjeet();
     * Ohjeet ohjeet2 = new Ohjeet();
     * ohjeet1.equals(ohjeet2) === true;
     * 
     * ohjeet1.lisaa(new Ohje());
     * ohjeet1.equals(ohjeet2) === false;
     * 
     * ohjeet2.lisaa(new Ohje());
     * ohjeet1.equals(ohjeet2) === true;
     * 
     * Ohje sokeri = new Ohje("Lisää sokeri");
     * Ohje kananmuna = new Ohje("Lisää kananmuna");
     * 
     * ohjeet2.lisaa(sokeri);
     * ohjeet1.equals(ohjeet2) === false;
     * 
     * ohjeet1.lisaa(kananmuna);
     * ohjeet1.equals(ohjeet2) === false;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Ohjeet verrattavaOhjeet = (Ohjeet)verrattava;
        if (verrattavaOhjeet.ohjeet.size() != this.ohjeet.size()) return false;
        if (verrattavaOhjeet.osioId != this.osioId) return false;
        if (!verrattavaOhjeet.tiedostopolku.equals(this.tiedostopolku)) return false;
        
        // verrataan yksittäiset ohjeet
        for (int i = 0; i < this.ohjeet.size(); i++) {
            if (!verrattavaOhjeet.ohjeet.get(i).equals(this.ohjeet.get(i))) return false;
        }
        
        return true;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet1 = new Ohjeet();
     * Ohjeet ohjeet2 = new Ohjeet();
     * ohjeet1.hashCode() == ohjeet2.hashCode() === true;
     * 
     * ohjeet1.lisaa(new Ohje());
     * ohjeet1.hashCode() == ohjeet2.hashCode() === false;
     * 
     * ohjeet2.lisaa(new Ohje());
     * ohjeet1.hashCode() == ohjeet2.hashCode() === true;
     * 
     * Ohje sokeri = new Ohje("Lisää sokeri");
     * Ohje kananmuna = new Ohje("Lisää kananmuna");
     * 
     * ohjeet2.lisaa(sokeri);
     * ohjeet1.hashCode() == ohjeet2.hashCode() === false;
     * 
     * ohjeet1.lisaa(kananmuna);
     * ohjeet1.hashCode() == ohjeet2.hashCode() === false;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautus(hash, this.osioId);
        hash = Hajautus.hajautus(hash, this.tiedostopolku);
        
        for (Ohje ohje : this.ohjeet) {
            hash = Hajautus.hajautus(hash, ohje.hashCode());
        }
        
        return hash;
    }
    
    
    @Override
    /**
     * Ohjeet-olion tiedot muodossa: "tiedostonimi|osio id|lukumäärä"
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.toString() === "ohjeet|1|0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTiedostonimi());
        sb.append('|');
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.ohjeet.size());
        return sb.toString();
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        try {
            Ohjeet ohjeet = new Ohjeet(1);
            ohjeet.setTiedostoPolku("esimerkkiOhjeet/");
            
            Ohje ohje1 = new Ohje("Lisää kananmunat");
            Ohje ohje2 = new Ohje("Lisää sokeri");
            Ohje ohje3 = new Ohje("Sekoita");
            Ohje ohje4 = new Ohje("Maista");
            Ohje ohje5 = new Ohje("Lisää mausteet");
            ohjeet.lisaa(ohje1);
            ohjeet.lisaa(ohje2);
            ohjeet.lisaa(ohje3);
            System.out.println(ohjeet.anna());
            System.out.println(ohjeet.get());
            System.out.println();
            
            ohjeet.tallenna();
            System.out.println(ohjeet.anna());
            System.out.println(ohjeet.get());
            System.out.println();
            
            ohjeet.poista(ohje1);
            ohjeet.poista(ohje2);
            ohjeet.lisaa(ohje4);
            ohjeet.lisaa(ohje5);
            ohjeet.tallenna();
            System.out.println(ohjeet.anna());
            System.out.println(ohjeet.get());
            System.out.println();
            
            Ohjeet ohjeetTiedostosta = new Ohjeet(1);
            ohjeetTiedostosta.setTiedostoPolku("esimerkkiOhjeet/");
            System.out.println(ohjeetTiedostosta.anna());
            System.out.println(ohjeetTiedostosta.get());
            System.out.println();
            
            ohjeetTiedostosta.lueTiedostosta();
            System.out.println(ohjeetTiedostosta.anna());
            System.out.println(ohjeetTiedostosta.get());
            
        } catch (SailoException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
