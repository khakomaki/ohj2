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
import kanta.Validoi;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 * Osiot hallitsee osio-olioita.
 */
public class Osiot implements Hallitsija<Osio> {

	private String tiedostopolku    = "reseptidata/";
    private String tiedostonimi     = "osiot";
    private int reseptiId           = -1;
    private List<Osio> osiot        = new ArrayList<Osio>();
    private Tietokanta tietokanta 	= null;
    
    private static Osio esimerkkiOsio = new Osio();
    
    /**
     * Alustukset testejä varten
     * 
     * @example
     * <pre name="testJAVA">
     * private Osiot kaikkiOsiot;
     * private String tiedPolku;
     * private String tiedNimi;
     * private File tiedosto;
     * 
     * @Before
     * public void alusta() throws SailoException {
     *     tiedPolku = "testidata/";
     *     tiedNimi = "testiOsiot";
     *     tiedosto = new File(tiedPolku + tiedNimi + ".db");
     *     tiedosto.delete();
     *     kaikkiOsiot = new Osiot(1, tiedPolku, tiedNimi);
     * }
     * 
     * @After
     * public void siivoa() {
     *     tiedosto.delete();
     * }
     * </pre>
     */
    
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
     * @throws SailoException jos jotain menee pieleen
     */
    public Osiot(int reseptinTunnus) throws SailoException {
        this.reseptiId = reseptinTunnus;
        alustaTietokanta();
    }
    
    
    /**
     * Hallitsee tietokannassa sijaitsevia Osio-olioita
     * 
     * @param reseptiID reseptin tunnus
     * @param tiedostopolku missä polussa sijaitsee
     * @param tiedostonimi tiedoston nimi
     */
    public Osiot(int reseptiID, String tiedostopolku, String tiedostonimi) {
        this.reseptiId = reseptiID;
        setTiedostoPolku(tiedostopolku);
        setTiedostoNimi(tiedostonimi);
    }
    
    
    /**
     * Alustaa yhteyden tietokantaan.
     * Luo oman taulun jos sellaista ei vielä ole.
     * 
     * @throws SailoException jos tietokannan alustamisessa ilmenee ongelmia
     */
    private void alustaTietokanta() throws SailoException {
    	Tietokanta.luoHakemisto(this.tiedostopolku);
        this.tietokanta = Tietokanta.alustaTietokanta(this.tiedostopolku + this.tiedostonimi);
        
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
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osioiden luonnissa tietokannan kanssa:\n" + exception.getMessage());
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
     * osiot.toString() === "0|osiot";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostonimi = tiedostonimi;
    }
    
    
    /**
     * Asettaa tiedostopolun johon osion tiedot tallennetaan.
     * Ei tee mitään jos null tai sama kuin oli
     * 
     * @param tiedostopolku mihin osion tiedot tallennetaan
     */
    public void setTiedostoPolku(String tiedostopolku) {
        if (tiedostopolku == null || this.tiedostopolku.equals(tiedostopolku)) return;
        this.tiedostopolku = tiedostopolku;
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
        
        for (Osio osio : this.osiot) {
        	osio.setReseptiID(this.reseptiId);
        }
    }
    
    
    /**
     * Antaa osioiden lukumäärän
     * 
     * @return osioiden lukumäärä
     */
    public int getLkm() {
        return this.osiot.size();
    }
    
    
    /**
     * Lisää osion
     * 
     * @param nimi minkä niminen osio lisätään
     * @return lisätty osio
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|osiot";
     * Osio pizzapohja = osiot.lisaa("Pizzapohja");
     * osiot.toString() === "1|osiot";
     * pizzapohja.toString() === "-1|-1|Pizzapohja";
     * 
     * Osio tomaattikastike = osiot.lisaa("Tomaattikastike");
     * osiot.toString() === "2|osiot";
     * tomaattikastike.toString() === "-1|-1|Tomaattikastike";
     * 
     * Osio taytteet = osiot.lisaa("Täytteet");
     * osiot.toString() === "3|osiot";
     * taytteet.toString() === "-1|-1|Täytteet";
     * 
     * osiot.lisaa("Täytteet (vaihtoehto 2)");
     * osiot.toString() === "4|osiot";
     * 
     * osiot.lisaa("Täytteet (vaihtoehto 3)");
     * osiot.toString() === "5|osiot";
     * </pre>
     */
    public Osio lisaa(String nimi) {
        Osio osio = new Osio(nimi);
        osio.setReseptiID(this.reseptiId);
        osio.setTiedostopolku(this.tiedostopolku);
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
     * osiot.toString() === "0|osiot";
     * 
     * Osio kastike = new Osio("Kastike");
     * osiot.lisaa(kastike);
     * osiot.toString() === "1|osiot";
     * </pre>
     */
    @Override
    public void lisaa(Osio osio) {
        if (osio == null) return;
        Osio lisattavaOsio = osio;
        osio.setReseptiID(this.reseptiId);
        osio.setTiedostopolku(this.tiedostopolku);
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
     * Osio osio1 = new Osio("Muropohja");
     * Osio osio2 = new Osio("Täyte");
     * 
     * kaikkiOsiot.lisaaTietokantaan(osio1);
     * kaikkiOsiot.lisaaTietokantaan(osio2);
     * loytyneetOsiot = kaikkiOsiot.get();
     * loytyneetOsiot.size() === 2;
     * 
     * kaikkiOsiot.poistaTietokannasta(osio1);
     * loytyneetOsiot = kaikkiOsiot.get();
     * loytyneetOsiot.size() === 1;
     * </pre>
     */
    public void lisaaTietokantaan(Osio osio) throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        // asetetaan lisättävälle osiolle sama reseptitunnus
        osio.setReseptiID(this.reseptiId);
        osio.setTiedostopolku(this.tiedostopolku);
        
        // muodostaa yhteyden tietokantaan, pyytää osiolta lisäyslausekkeen ja suorittaa
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = osio.getLisayslauseke(yhteys) ) {
            sql.executeUpdate();
            
            // tarkistetaan saiko osio uuden tunnuksen
            try ( ResultSet tulokset = sql.getGeneratedKeys() ) {
                osio.tarkistaID(tulokset);
             }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osion lisäyksessä tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Palauttaa kaikki tietokannasta löytyvät omat osiot
     * 
     * @return kokoelma luokan osioista
     * @throws SailoException jos hakemisessa tulee ongelmia
     */
    public Collection<Osio> get() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
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
            throw new SailoException("Ongelmia osioiden haussa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa annetun osion
     * 
     * @param osio poistettava osio
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaTietokannasta(Osio osio) throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        // muodostaa yhteyden tietokantaan, pyytää osiolta poistolausekkeen ja suorittaa
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = osio.getPoistolauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osion poistossa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Päivittää osion tiedot
     * 
     * @param osio päivitettävä osio
     * @throws SailoException jos osion päivittämisessä ilmenee ongelmia
     */
    private void paivitaTietokannassa(Osio osio) throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
    	osio.setTiedostopolku(this.tiedostopolku);
    	
    	// muodostaa yhteyden tietokantaan, pyytää osiolta päivityslausekkeen ja suorittaa
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = osio.getPaivityslauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osion päivityksessä tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa kaikki osion sisältämät tiedot tietokannasta
     * 
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaTietokannasta() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
    	// käskee ohjeita ja ainesosia poistamaan tietonsa
    	for (Osio osio : this.osiot) {
    		osio.poistaTietokannasta();
    	}
    	
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("DELETE FROM Osiot WHERE resepti_id = ?")) {
            sql.setInt(1, this.reseptiId);
            sql.executeUpdate();
            
        } catch (SQLException exception) {
        	throw new SailoException("Ongelmia osioiden poistossa tietokannan kanssa:\n" + exception.getMessage());
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
     * #THROWS SailoException
     * Osiot osiot = new Osiot();
     * osiot.lisaa("Pohja");
     * Osio tayte = new Osio("Täyte");
     * osiot.lisaa(tayte);
     * osiot.lisaa("Kuorrute");
     * osiot.toString() === "3|osiot";
     * 
     * osiot.poista(tayte);
     * osiot.toString() === "2|osiot";
     * 
     * osiot.poista(tayte);
     * osiot.toString() === "2|osiot";
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
        return true; // TODO poista kun ei käytetä enää missään
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
     * Tallentaa osioiden tiedot tietokantaan
     * 
     * @throws SailoException jos tallentamisessa tulee ongelmia
     */
    public void tallenna() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
    	String virhe = voidaankoTallentaa();
    	if (virhe != null) throw new SailoException("Ongelmia osioiden tallentamisessa: " + virhe);
    	
    	for (Osio osio : this.osiot) {
    		// lisää tai päivittää osion
    		if (osio.getID() < 0) { // TODO lisää myös jos vaihdettu tietokantaa
    			lisaaTietokantaan(osio);
    		} else {
    			paivitaTietokannassa(osio);
    		}
    		// tallentaa osion omat tiedot
    		osio.tallenna();
    	}
    }
    
    
    /**
     * Lukee reseptin osiot tietokannasta
     * 
     * @throws SailoException jos tietojen lukemisessa tulee ongelmia
     */
    public void lueTiedostosta() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Osiot WHERE resepti_id = ?") ) {
            sql.setInt(1, this.reseptiId);
            
            // luodaan sql-kyselyn tulosten verran ohje-olioita
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    Osio osio = new Osio();
                    osio.parse(tulokset);
                    lisaa(osio);
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osioiden lukemisessa tietokannan kanssa:\n" + exception.getMessage());
        }
        
        // lukee osioiden omat tiedot tietokannasta
        for (Osio osio : this.osiot) {
        	osio.lueTiedostosta();
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
    @Override
    public Osiot clone() {
        Osiot kopio = null;
        try {
            kopio = new Osiot();
            kopio.reseptiId = this.reseptiId;
            kopio.tiedostonimi = this.tiedostonimi;
            
            for (Osio osio : this.osiot) {
                kopio.osiot.add(osio.clone());
            }
        } catch (Exception exception) {
        	System.err.println(exception.getMessage());
        }
        
        return kopio;
    }
    
    
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
    @Override
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
    @Override
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautus(hash, this.tiedostonimi);
        
        for (Osio osio : this.osiot) {
            hash = Hajautus.hajautus(hash, osio.hashCode());
        }
        
        return hash;
    }
    
    
    /**
     * Palauttaa tiedot muodossa "tiedostonimi"
     * 
     * @example
     * <pre name="test">
     * Osiot osiot = new Osiot();
     * osiot.toString() === "0|osiot";
     * </pre>
     */
    @Override
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
			osiot = new Osiot(1);
			osiot.setTiedostoPolku("esimerkkiOsiot/");
	    	Osio osio1 = osiot.lisaa("Pizzapohja");
	    	osio1.lisaaAinesosa("pizzajauhoha", "450g");
	    	osio1.lisaaAinesosa("vettä", "3dl");
	    	
	    	Osio osio2 = osiot.lisaa("Tomaattikastike");
	    	osio2.lisaaAinesosa("tomaatti", "500g");
	    	osio2.lisaaOhje("murskaa tomaatit");
	    	
	    	System.out.println(osiot.anna());
	    	System.out.println(osiot.get());
	    	System.out.println();
	    	
	    	osiot.tallenna();
	    	System.out.println(osiot.anna());
	    	System.out.println(osiot.get());
	    	System.out.println();
	    	
	    	osiot.poista(osio1);
	    	System.out.println(osiot.anna());
	    	System.out.println(osiot.get());
	    	System.out.println();
	    	
	    	Osiot osiot2 = new Osiot(1);
	    	osiot2.setTiedostoPolku("esimerkkiOsiot/");
	    	System.out.println(osiot2.anna());
	    	System.out.println(osiot2.get());
	    	System.out.println();
	    	
	    	osiot2.lueTiedostosta();
	    	System.out.println(osiot2.anna());
	    	System.out.println(osiot2.get());
	    	
		} catch (SailoException exception) {
			System.err.println(exception.getMessage());
		}
    }
}
