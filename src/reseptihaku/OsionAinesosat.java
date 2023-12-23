package reseptihaku;

import java.io.OutputStream;
import java.io.PrintStream;
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
import kanta.TietueHallitsija;
import kanta.Validoi;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 * Osion ainesosat hallitsee osion ainesosa -olioita.
 */
public class OsionAinesosat extends TietueHallitsija<OsionAinesosa> implements Hallitsija<OsionAinesosa> {
	
	private String tiedostopolku    = "reseptidata/";
    private String tiedostonimi     = "ainesosat";
    private int osioId              = -1;
    private Tietokanta tietokanta 	= null;
    
    private static OsionAinesosa esimerkkiAinesosa = new OsionAinesosa();
    
    /**
     * Alustukset testejä varten
     * 
     * @example
     * <pre name="testJAVA">
     * private OsionAinesosat kaikkiAinesosat;
     * private String tiedPolku;
     * private String tiedNimi;
     * private File tiedosto;
     * 
     * @Before
     * public void alusta() throws SailoException {
     *     tiedPolku = "testidata/";
     *     tiedNimi = "testiAinesosat";
     *     tiedosto = new File(tiedPolku + tiedNimi + ".db");
     *     tiedosto.delete();
     *     kaikkiAinesosat = new OsionAinesosat(1, tiedPolku, tiedNimi);
     * }
     * 
     * @After
     * public void siivoa() {
     *     tiedosto.delete();
     * }
     * </pre>
     */
    
    /**
     * Hallitsee osion ainesosia ja määriä.
     * Alustuu oletuksena osio id:llä -1.
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * OsionAinesosat oletus = new OsionAinesosat();
     * oletus.toString() === "-1|ainesosat|0";
     * </pre>
     */
    public OsionAinesosat() throws SailoException {
        super(2.0, 0);
        alustaTietokanta();
    }
    
    
    /**
     * Hallitsee osion ainesosia ja määriä
     * 
     * @param osioId mihin osioon ainesosat ja määrät luodaan
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * OsionAinesosat muropohjaAinesosat = new OsionAinesosat(2);
     * muropohjaAinesosat.toString() === "2|ainesosat|0";
     * </pre>
     */
    public OsionAinesosat(int osioId) throws SailoException {
        super(2.0, 0);
        this.osioId = osioId;
        alustaTietokanta();
    }
    
    
    /**
     * Hallitsee tietokannassa sijaitsevia ainesosa-olioita
     * 
     * @param osioId osion tunnus
     * @param tietokantaNimi tietokannan nimi
     * @throws SailoException jos tulee ongelmia
     */
    public OsionAinesosat(int osioId, String tiedostopolku, String tiedostonimi) throws SailoException {
        super(2.0, 0);
    	this.osioId = osioId;
    	setTiedostoPolku(tiedostopolku);
    	setTiedostoNimi(tiedostonimi);
    	alustaTietokanta();
    }
    
    
    /**
     * Alustaa oman tietokannan.
     * Luo oman taulun jos sellaista ei vielä ole.
     * 
     * @throws SailoException jos tulee ongelmia tietokannan kanssa
     */
    private void alustaTietokanta() throws SailoException {
    	Tietokanta.luoHakemisto(this.tiedostopolku);
        this.tietokanta = Tietokanta.alustaTietokanta(tiedostopolku + tiedostonimi);
        
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
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ainesosien luonnissa tietokannan kanssa:\n" + exception.getMessage());
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
    }
    
    
    /**
     * Vaihtaa tiedostopolun.
     * Ei tee mitään jos yritetään asettaa null tai sama kuin aiemmin.
     * 
     * @param tiedostopolku mihin polkuun tiedosto tallennetaan ja mistä sitä luetaan
     */
    public void setTiedostoPolku(String tiedostopolku) throws SailoException {
        if (tiedostopolku == null || this.tiedostopolku.equals(tiedostopolku)) return;
        this.tiedostopolku = tiedostopolku;
        alustaTietokanta();
    }
    
    
    /**
     * Lisää osion ainesosan.
     * Ei hyväksy null-viitettä.
     * 
     * @param osionAinesosa lisättävä osion ainesosa
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * OsionAinesosat muropohjaAinesosat = new OsionAinesosat(2);
     * muropohjaAinesosat.toString() === "2|ainesosat|0";
     * 
     * muropohjaAinesosat.lisaa(new OsionAinesosa("kananmuna", "2kpl"));
     * muropohjaAinesosat.lisaa(new OsionAinesosa("sokeri", "1dl"));
     * muropohjaAinesosat.toString() === "2|ainesosat|2";
     * muropohjaAinesosat.anna(0).getAinesosa() === "kananmuna";
     * muropohjaAinesosat.anna(1).getMaara() === "1dl";
     * </pre>
     */
    @Override
    public void lisaa(OsionAinesosa osionAinesosa) {
        if (osionAinesosa == null) return;
        osionAinesosa.setOsioId(this.osioId);
        add(osionAinesosa);
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
            throw new SailoException("Ongelmia ainesosan lisäyksessä tietokannan kanssa:\n" + exception.getMessage());
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
            throw new SailoException("Ongelmia ainesosien haussa tietokannan kanssa:\n" + exception.getMessage());
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
            throw new SailoException("Ongelmia ainesosan poistossa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa ainesosat tietokannasta
     * 
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaTietokannasta() throws SailoException {
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("DELETE FROM Ainesosat WHERE osio_id = ?")) {
            sql.setInt(1, this.osioId);
            sql.executeUpdate();
            
        } catch (SQLException exception) {
        	throw new SailoException("Ongelmia ainesosien poistossa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Tallentaa nykyiset ainesosat tietokantaan
     * 
     * @throws SailoException jos tallentamisessa ilmenee ongelmia
     */
    public void tallenna() throws SailoException {
    	// varmistetaan ettei ole samaa ainesosaa useampaan kertaan
    	List<String> ainesosaNimet = new ArrayList<String>();
    	for (OsionAinesosa ainesosa : this) {
    		ainesosaNimet.add(ainesosa.getAinesosa());
    	}
    	if (Validoi.onkoDuplikaatteja(ainesosaNimet)) throw new SailoException("Samassa osiossa on sama ainesosa useaan kertaan!");
    	
    	try (Connection yhteys = this.tietokanta.annaTietokantaYhteys()) {
        	// poistetaan nykyiset osion ohjeet
            try (PreparedStatement sqlPoisto = yhteys.prepareStatement("DELETE FROM Ainesosat WHERE osio_id = ?")) {
                sqlPoisto.setInt(1, this.osioId);
                sqlPoisto.executeUpdate();
            }
            
            // lisätään omat ohjeet tietokantaan, jos sellaisia on
            if (0 < this.size()) {
                try (PreparedStatement sqlLisays = OsionAinesosat.esimerkkiAinesosa.getMoniLisayslauseke(yhteys, this.anna())) {
                	sqlLisays.executeUpdate();
                }
            }
            
    	} catch (SQLException exception) {
    		throw new SailoException("Ongelmia ainesosien tallentamisessa tietokannan kanssa:\n" + exception.getMessage());
    	}
    }
    
    
    /**
     * Lukee omaa osion tunnusta vastaavat tiedot tietokannasta
     * 
     * @throws SailoException jos tietokannasta lukemisessa ilmenee ongelmia
     */
    public void lueTiedostosta() throws SailoException {
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Ainesosat WHERE osio_id = ?") ) {
            sql.setInt(1, this.osioId);
            
            // luodaan sql-kyselyn tulosten verran ainesosa-olioita
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    OsionAinesosa ainesosa = new OsionAinesosa();
                    ainesosa.parse(tulokset);
                    this.add(ainesosa);
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia ainesosien lukemisessa tietokannan kanssa:\n" + exception.getMessage());
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
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan osion ainesosa
     * @return osion ainesosa halutusta indeksistä tai null
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
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
     * Tulostaa ainesosat tietoineen annettuun tietovirtaan
     * 
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaOsionAinesosat(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (OsionAinesosa ainesosa : this) {
            out.print(ainesosa.getAinesosa());
            out.print(" : ");
            out.print(ainesosa.getMaara());
            out.print("\n");
        }
    }
    
    
    /**
     * Kertoo onko ohjeisiin tullut tallentamattomia muutoksia
     * 
     * @return onko tallentamattomia muutoksia
     */
    public boolean onkoTallentamattomiaMuutoksia() {
        return true; // TODO poista kun ei käytetä muualla
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * OsionAinesosat oat = new OsionAinesosat(55);
     * oat.lisaa(new OsionAinesosa("riisi", "5dl"));
     * oat.lisaa(new OsionAinesosa("soijakastike", "2rkl"));
     * 
     * OsionAinesosat oatKopio = oat.clone();
     * oat.equals(oatKopio) === true;
     * oat.toString() === "55|ainesosat|2";
     * 
     * oat.lisaa(new OsionAinesosa("sipuli", "1kpl"));
     * oat.equals(oatKopio) === false;
     * 
     * oatKopio.lisaa(new OsionAinesosa("sipuli", "1kpl"));
     * oat.equals(oatKopio) === true;
     * </pre>
     */
    public OsionAinesosat clone() {
    	OsionAinesosat kopio = null;
    	try {
            kopio = new OsionAinesosat();
            kopio.osioId = this.osioId;
            kopio.tiedostonimi = this.tiedostonimi;
            
            for (OsionAinesosa oa : this) {
                kopio.lisaa(oa);
            }
            
    	} catch (SailoException exception) {
    		System.err.println(exception.getMessage());
    	}

        return kopio;
    }
    
    
    /**
     * @example
     * <pre name="test">
     * #THROWS SailoException
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
     * #THROWS SailoException
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
     * #THROWS SailoException
     * OsionAinesosat oa = new OsionAinesosat(1);
     * oa.toString() === "1|ainesosat|0";
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
			OsionAinesosat oat = new OsionAinesosat(1, "reseptidata/", "testiAinesosat");
			
			OsionAinesosa oa1 = new OsionAinesosa("porkkanaa", "300g");
			OsionAinesosa oa2 = new OsionAinesosa("perunaa", "3kpl");
			oat.lisaa(oa1);
			oat.lisaa(oa2);
			System.out.println(oat.anna());
			System.out.println(oat.get());
			System.out.println();
			
			oat.tallenna();
			System.out.println(oat.anna());
			System.out.println(oat.get());
			System.out.println();
			
			oat.poista(oa1);
			System.out.println(oat.anna());
			System.out.println(oat.get());
			System.out.println();
			
			oat.tallenna();
			System.out.println(oat.anna());
			System.out.println(oat.get());
			System.out.println();
			
			OsionAinesosat oat2 = new OsionAinesosat(1, "reseptidata/", "testiAinesosat");
			System.out.println(oat2.anna());
			System.out.println(oat2.get());
			System.out.println();
			
			oat2.lueTiedostosta();
			System.out.println(oat2.anna());
			System.out.println(oat2.get());
			
		} catch (SailoException exception) {
			System.err.println(exception.getMessage());
		}
    }
}
