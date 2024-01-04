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
import java.util.Comparator;
import java.util.List;

import kanta.Hajautus;
import kanta.Hallitsija;
import kanta.SailoException;
import kanta.VaihtoehtoAttribuutti;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 * Reseptit hallitsee resepti-olioita.
 */
public class Reseptit implements Hallitsija<Resepti> {

    private List<Resepti> reseptit	= new ArrayList<Resepti>();
    private String tiedostonimi     = "reseptit";
    private String tiedostopolku    = "reseptidata/";
    private Tietokanta tietokanta 	= null;
    
    private final static Resepti esimerkkiresepti = new Resepti();
    
    /**
     * Hallitsee resepti-olioita
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit";
     * </pre>
     */
    public Reseptit() {
        //
    }
    
    
    /**
     * Alustaa reseptien tietokannan
     * #import kanta.SailoException;
     * 
     * @throws SailoException jos tietokannan alustamisessa ilmenee ongelmia
     */
    private void alustaTietokanta() throws SailoException {
    	Tietokanta.luoHakemisto(this.tiedostopolku);
        this.tietokanta = Tietokanta.alustaTietokanta(this.tiedostopolku + this.tiedostonimi);
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys() ) {
            // haetaan tietokannan metadatasta omaa tietokantaa, luodaan jos ei ole
            DatabaseMetaData metadata = yhteys.getMetaData();
            
            try ( ResultSet taulu = metadata.getTables(null, null, "Reseptit", null)) {
                // luodaan uusi taulu jos ei voida siirtyä seuraavaan
                if ( !taulu.next() ) {
                    try ( PreparedStatement sql = yhteys.prepareStatement(Reseptit.esimerkkiresepti.getLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia reseptien luonnissa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Asettaa tiedostonimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono, null tai sama kuin aiemmin
     * 
     * @param tiedostonimi tiedostonimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit";
     * 
     * reseptit.setTiedostoNimi("");
     * reseptit.toString() === "0|reseptit";
     * 
     * reseptit.setTiedostoNimi("salaiset_reseptit.txt");
     * reseptit.toString() === "0|salaiset_reseptit.txt";
     * 
     * reseptit.setTiedostoNimi(null);
     * reseptit.toString() === "0|salaiset_reseptit.txt";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        if (tiedostonimi == null || this.tiedostonimi.equals(tiedostonimi)) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostonimi = tiedostonimi;
    }
    
    
    /**
     * Asettaa reseptin tallennuspolun.
     * Ei tee mitään jos annettu null tai sama kuin aiemmin.
     * 
     * @param tiedostopolku mihin reseptit tallennetaan
     */
    public void setTiedostoPolku(String tiedostopolku) {
        if (tiedostopolku == null || this.tiedostopolku.equals(tiedostopolku)) return;        
        this.tiedostopolku = tiedostopolku;
        for (Resepti resepti : this.reseptit) {
            resepti.setTiedostopolku(this.tiedostopolku);
        }
    }
    
    
    /**
     * Luo uuden reseptin nimellä ja palauttaa sen.
     * Antaa tunnuksen juoksevalla id:llä.
     * 
     * @param nimi reseptin nimi
     * @return luotu resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.toString() === "3|reseptit";
     * 
     * reseptit.anna(0).equals(resepti1) === true
     * reseptit.anna(1).equals(resepti2) === true;
     * reseptit.anna(2).equals(resepti3) === true;
     * </pre>
     */
    public Resepti lisaa(String nimi) {
        Resepti resepti = new Resepti(nimi);
        resepti.setTiedostopolku(this.tiedostopolku);
        reseptit.add(resepti);
        return resepti;
    }
    
    
    /**
     * Lisää uuden reseptin.
     * Varmistaa että tunnukset pysyvät kasvavassa järjestyksessä.
     * 
     * @param resepti lisättävä resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * 
     * Resepti mustikkapiirakka = new Resepti("Mustikkapiirakka");
     * reseptit.lisaa(mustikkapiirakka);
     * reseptit.toString() === "1|reseptit";
     * 
     * reseptit.lisaa(new Resepti());
     * reseptit.toString() === "2|reseptit";
     * </pre>
     */
    @Override
    public void lisaa(Resepti resepti) {
        if (resepti == null) return;
        resepti.setTietokanta(this.tietokanta);
        resepti.setTiedostopolku(this.tiedostopolku);
        reseptit.add(resepti);
    }
    
    
    /**
     * Palauttaa kaikki tietokannasta löytyvät reseptit
     * 
     * @return kokoelma resepteistä
     * @throws SailoException jos hakemisessa tulee ongelmia
     */
    public Collection<Resepti> get() throws SailoException {
        // varmistaa että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Reseptit") ) {
            ArrayList<Resepti> loydetytReseptit = new ArrayList<Resepti>();
            
            // suoritetaan kysely ja parsitaan tuloksista Resepti-olioita
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    Resepti resepti = new Resepti();
                    resepti.parse(tulokset);
                    loydetytReseptit.add(resepti);
                }
            }
            return loydetytReseptit;
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia reseptien haussa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Antaa tietokannasta olevien reseptien tunnukset
     * 
     * @return lista reseptitunnuksista
     * @throws SailoException jos tulee ongelmia tietokannan kanssa
     */
    private List<Integer> getReseptiTunnukset() throws SailoException {
        List<Integer> tunnukset = new ArrayList<>();
        
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT resepti_id FROM Reseptit")) {
            
            try (ResultSet tulokset = sql.executeQuery()) {
                while (tulokset.next()) {
                    tunnukset.add(tulokset.getInt("resepti_id"));
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osiotunnuksien hakemisessa tietokannan kanssa:\n" + exception.getMessage());
        }
        
        return tunnukset;
    }
    
    
    /**
     * Luo reseptin.
     * Ei vielä lisää resepteihin.
     */
    @Override
    public Resepti luo() {
        return new Resepti();
    }
    
    
    /**
     * Antaa annetussa indeksissä olevan reseptin.
     * Palauttaa null jos indeksi ei ollut mieluisa.
     * 
     * @param indeksi mistä indeksistä yritetään ottaa Resepti
     * @return indeksissä ollut Resepti tai null
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.toString() === "3|reseptit";
     * 
     * reseptit.anna(-1) == null === true;
     * reseptit.anna(0) == resepti1 === true;
     * reseptit.anna(1) == resepti2 === true;
     * reseptit.anna(2) == resepti3 === true;
     * </pre>
     */
    public Resepti anna(int indeksi) {
        if (indeksi < 0 || this.reseptit.size() < indeksi) return null;
        return reseptit.get(indeksi);
    }
    
    
    /**
     * Antaa reseptit listana
     */
    @Override
    public List<Resepti> anna() {
        return this.reseptit;
    }
    
    
    /**
     * Vaihtaa vanhan resepti-viitteen tilalle uuden.
     * Ei tee mitään jos jompi kumpi resepteistä on null-viite tai vaihdettavaa reseptiä ei löydy.
     * 
     * @param vanhaResepti minkä reseptin tilalle vaihdetaan
     * @param uusiResepti tilalle vaihdettava resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.anna(1) == resepti2 === true;
     * 
     * Resepti resepti4 = new Resepti("Kasvispiirakka");
     * reseptit.vaihdaResepti(resepti4, resepti2);
     * 
     * reseptit.vaihdaResepti(resepti2, resepti4);
     * reseptit.anna(1) == resepti4 === true;
     * 
     * reseptit.vaihdaResepti(resepti2, resepti1);
     * reseptit.anna(1) == resepti4 === true;
     * 
     * reseptit.vaihdaResepti(resepti1, resepti1);
     * reseptit.anna(0) == resepti1 === true;
     * 
     * reseptit.vaihdaResepti(resepti3, resepti1);
     * reseptit.anna(2) == resepti1 === true;
     * 
     * reseptit.vaihdaResepti(resepti3, null);
     * reseptit.anna(2) == resepti1 === true;
     * 
     * reseptit.vaihdaResepti(null, resepti1);
     * reseptit.anna(2) == resepti1 === true;
     * </pre>
     */
    public void vaihdaResepti(Resepti vanhaResepti, Resepti uusiResepti) {
        if (vanhaResepti == null || uusiResepti == null) return;
        
        // etsii vaihdettavan indeksin, poistuu jos ei löydy
        int vanhaReseptiIndeksi = this.reseptit.indexOf(vanhaResepti);
        if (vanhaReseptiIndeksi < 0) return;
        
        // varmistaa tiedostopolun ja tietokannan olevan samoja
        uusiResepti.setTietokanta(this.tietokanta);
    	uusiResepti.setTiedostopolku(this.tiedostopolku);
		
        this.reseptit.set(vanhaReseptiIndeksi, uusiResepti);
    }
    
    
    /**
     * Tulostaa annetun määrän verran reseptejä
     * 
     * @param os tietovirta johon halutaan tulostaa
     * @param maara kuinka monta reseptia tulostetaan
     */
    public void tulostaReseptit(OutputStream os, int maara) {
        int tulostettavaMaara = maara;
        
        // ei tulosta mitään jos annettu luku on negatiivinen
        if (maara < 0) return;
        
        // tulostaa lukumäärän verran jos annettu määrä olisi enemmän
        if (this.reseptit.size() < maara) tulostettavaMaara = this.reseptit.size();
        
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < tulostettavaMaara; i++) {
            out.print(this.anna(i));
            out.print("\n");
        }
    }
    
    
    /**
     * Tulostaa kaikki reseptit
     * 
     * @param os tietovirta johon halutaan tulostaa
     */
    public void tulostaReseptit(OutputStream os) {
        tulostaReseptit(os, this.reseptit.size());
    }
    
    
    /**
     * Poistaa annetun reseptin resepteistä
     * 
     * @param resepti poistettava resepti
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * Resepti resepti1 = reseptit.lisaa("Mustikkapiirakka");
     * Resepti resepti2 = reseptit.lisaa("Lihapiirakka");
     * Resepti resepti3 = reseptit.lisaa("Kinkkupiirakka");
     * reseptit.toString() === "3|reseptit";
     * 
     * reseptit.poista(resepti2);
     * reseptit.toString() === "2|reseptit";
     * 
     * reseptit.poista(null);
     * reseptit.toString() === "2|reseptit";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "1|reseptit";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "1|reseptit";
     * 
     * reseptit.poista(resepti3);
     * reseptit.toString() === "0|reseptit";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "0|reseptit";
     * </pre>
     */
    @Override
    public void poista(Resepti resepti) {
        int poistettavanIndeksi = reseptit.indexOf(resepti);
        
        // poistutaan jos poistettavaa ei löydetty
        if (poistettavanIndeksi < 0) return;
        
        this.reseptit.remove(poistettavanIndeksi);
    }
    
    
	/**
	 * Hakee reseptejä annetulla hakusanalla ja suodattimilla.
	 * Jos suodatin on asetettu, ei sisällytä oletusvalintaa vastaavia hakutuloksissa.
	 * Jos annettua lajittelu perustetta vastaavaa attribuuttia ei löydetä, lajitellaan nimen perusteella.
	 * 
	 * @param hakusana millä hakusanalla reseptiä haetaan
	 * @param minimit suodattimet attribuuttien minimiarvoille
	 * @param maksimit suodattimet attribuuttien maksimiarvoille
	 * @param lajitteluPeruste minkä perusteella lajitellaan tulokset
	 * @param kaanteinenJarjestys annetaanko tulokset käänteisessä järjestyksessä
	 * @return lista löydetyistä resepteistä
	 * @throws SailoException jos jotain menee pieleen
	 */
    public List<Resepti> etsiNimella(
    		String hakusana, 
            List<VaihtoehtoAttribuutti> minimit, 
            List<VaihtoehtoAttribuutti> maksimit, 
            String lajitteluPeruste,
            boolean kaanteinenJarjestys
        ) throws SailoException {
        
        // varmistaa että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
    	
    	List<Resepti> loydetytReseptit = new ArrayList<Resepti>();
    	
    	try (Connection yhteys = this.tietokanta.annaTietokantaYhteys()) {
    		// muodostaa kysely lausekkeen
    		StringBuilder sqlKysely = new StringBuilder();
    		sqlKysely.append("SELECT * FROM Reseptit WHERE nimi LIKE ?");
    		
    		// minimi rajoitteet
    		for (VaihtoehtoAttribuutti minimi : minimit) {
    			if (!minimi.onkoOletusValinta(minimi.getValinta())) {
    				sqlKysely.append(" AND ");
    				sqlKysely.append(Reseptit.esimerkkiresepti.getTietokantaNimi(minimi.getNimi()));
    				sqlKysely.append(" >= ?");
    			}
    		}
    		// maksimi rajoitteet
    		for (VaihtoehtoAttribuutti maksimi : maksimit) {
    			if (!maksimi.onkoOletusValinta(maksimi.getValinta())) {
    				sqlKysely.append(" AND ");
    				sqlKysely.append(Reseptit.esimerkkiresepti.getTietokantaNimi(maksimi.getNimi()));
    				sqlKysely.append(" <= ?");
    			}
    		}
    		
    		// asettaa lajittelu perusteen, välttää injektiot kysymällä esimerkkireseptiltä
    		String lajittelu = "nimi";
    		if (lajitteluPeruste != null) {
    			lajittelu = Reseptit.esimerkkiresepti.getTietokantaNimi(lajitteluPeruste);
    		}
    		sqlKysely.append(" ORDER BY ");
    		sqlKysely.append(lajittelu);
    		if (kaanteinenJarjestys) sqlKysely.append(" DESC");
    		
    		// suorittaa kyselyn ja parsii reseptit hakutuloksista
    		try (PreparedStatement sql = yhteys.prepareStatement(sqlKysely.toString())){
    			int parametri = 1;
    			sql.setString(parametri++, "%" + hakusana.strip() + "%");
    			
    			// minimi rajoitteet
    			for (VaihtoehtoAttribuutti minimi : minimit) {
    				int valinta = minimi.getValinta();
    				if (!minimi.onkoOletusValinta(valinta)) {
    					sql.setInt(parametri++, valinta);
    				}
    			}
    			// maksimi rajoitteet
    			for (VaihtoehtoAttribuutti maksimi : maksimit) {
    				int valinta = maksimi.getValinta();
    				if (!maksimi.onkoOletusValinta(valinta)) {
    					sql.setInt(parametri++, valinta);
    				}
    			}
    			
    			try (ResultSet hakuTulokset = sql.executeQuery()) {
    				while (hakuTulokset.next()) {
    					Resepti resepti = new Resepti();
    					resepti.parse(hakuTulokset);
    					resepti.lueTiedostosta();
    					loydetytReseptit.add(resepti);
    				}
    			}
    		}
    		
    	} catch (SQLException exception) {
    		throw new SailoException("Ongelmia reseptien hakemisessa tietokannasta:\n" + exception.getMessage());
    	}
    	
    	return loydetytReseptit;
    }
    
    
    /**
     * Vertailee reseptien nimiä keskenään pienellä kirjoitettuna.
     */
    class VertaileNimia implements Comparator<Resepti> {
        @Override
        public int compare(Resepti resepti1, Resepti resepti2) {
            String nimi1 = resepti1.getNimi().toLowerCase();
            String nimi2 = resepti2.getNimi().toLowerCase();
            return nimi1.compareTo(nimi2);
        }
    }
    
    
    /**
     * Vertailee reseptejä keskenään annetun attribuutin perusteella.
     * Attribuutit haetaan annetuilta resepteiltä ennalta määritellyn indeksin perusteella.
     */
    class VertaileAttribuutteja implements Comparator<Resepti> {
        private final int attribuutinIndeksi;
        
        /**
         * Attribuutin vertailija.
         * Ei anneta laittaa alle 0 indeksiä.
         * 
         * @param indeksi missä indeksissä olevaa attribuuttia vertaillaan
         */
        public VertaileAttribuutteja(int indeksi) {
            if (indeksi < 0) {
                this.attribuutinIndeksi = 0;
            } else {
                this.attribuutinIndeksi = indeksi;
            }
        }
        
        @Override
        public int compare(Resepti resepti1, Resepti resepti2) {
            // palautetaan että ovat samoja jos annettua indeksiä vastaavaa attribuuttia ei ole
            if (resepti1.getAttribuutit().size() < this.attribuutinIndeksi || resepti2.getAttribuutit().size() < this.attribuutinIndeksi) return 0;
            
            // attribuutit
            VaihtoehtoAttribuutti va1 = resepti1.getAttribuutit().get(this.attribuutinIndeksi);
            VaihtoehtoAttribuutti va2 = resepti2.getAttribuutit().get(this.attribuutinIndeksi);
            int integer1 = va1.getValinta();
            int integer2 = va2.getValinta();
            
            // oletusvalinta muita valintoja myöhemmin
            if (va1.onkoOletusValinta(integer1) && va2.onkoOletusValinta(integer2)) return 0;
            if (va1.onkoOletusValinta(integer1)) return 1;
            if (va2.onkoOletusValinta(integer2)) return -1;
            
            // Integer vertailu
            return Integer.compare(integer1, integer2);
        }
    }
    
    
    /**
     * Antaa reseptin oletusattribuutit
     * 
     * @return reseptin oletusattribuutit
     * @throws SailoException jos jotain menee pieleen
     */
    public static List<VaihtoehtoAttribuutti> getOletusAttribuutit() throws SailoException {    	
        return Reseptit.esimerkkiresepti.getAttribuutit();
    }
    
    
    /**
     * Luo lisää mustikkapiirakan resepteihin testaamista varten.
     * TODO: poista kun ei enää tarvita
     * @return mustikkapiirakka resepti
     */
    public Resepti lisaaMustikkapiirakka() {
        Resepti mustikkapiirakka = new Resepti("");
        mustikkapiirakka.luoMustikkapiirakka();
        lisaa(mustikkapiirakka);
        return mustikkapiirakka;
    }
    
    
    /**
     * Tallentaa reseptit tietokantaan
     * 
     * @throws SailoException jos tallentamisessa ilmenee ongelmia
     */
    public void tallenna() throws SailoException {
        // varmistetaan että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        List<Integer> reseptiTunnukset = new ArrayList<Integer>();
        
        for (Resepti resepti : this.reseptit) {
    		resepti.tallenna();
    		reseptiTunnukset.add(resepti.getID());
    	}
        
        // poistaa tietokannasta listasta puuttuvat reseptit
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("DELETE FROM Reseptit WHERE resepti_id = ?")) {
            for (Integer reseptiID : getReseptiTunnukset()) {
                if (!reseptiTunnukset.contains(reseptiID)) {
                    sql.setInt(1, reseptiID);
                    sql.executeUpdate();
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia osion poistossa tietokannan kanssa:\n" + exception.getMessage());
        }
    }
    
    
    /**
     * Lukee reseptien tiedot tietokannasta
     * 
     * @throws SailoException jos tietojen lukemisessa ilmenee ongelmia
     */
    public void lueTiedostosta() throws SailoException {
        // varmistaa että tietokanta on alustettu
        if (this.tietokanta == null) alustaTietokanta();
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Reseptit") ) {
            
            // luodaan sql-kyselyn tulosten verran resepti-olioita
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while (tulokset.next()) {
                    Resepti resepti = new Resepti();
                    resepti.parse(tulokset);
                    lisaa(resepti);
                }
            }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia reseptien lukemisessa tietokannan kanssa:\n" + exception.getMessage());
        }
        
        // lukee osioiden omat tiedot tietokannasta
        for (Resepti resepti : this.reseptit) {
        	resepti.lueTiedostosta();
        }
    }
    
    
    @Override
    /**
     * Luo täydellisen kopion itsestänsä
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.lisaa(new Resepti("Mustikkapiirakka"));
     * reseptit.lisaa(new Resepti("Juustokakku"));
     * reseptit.toString() === "2|reseptit";
     * 
     * Reseptit reseptitKopio = reseptit.clone();
     * reseptitKopio.toString() === "2|reseptit";
     * 
     * reseptit.setTiedostoNimi("herkut.txt");
     * reseptit.toString() === "2|herkut.txt";
     * reseptitKopio.toString() === "2|reseptit";
     * </pre>
     */
    public Reseptit clone() {
    	Reseptit kopio = new Reseptit();
        kopio.tiedostonimi = this.tiedostonimi;
        for (Resepti resepti : this.reseptit) {
            kopio.reseptit.add(resepti.clone());
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * Vertailee ovatko Reseptit-oliot samoja
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit1 = new Reseptit();
     * Reseptit reseptit2 = new Reseptit();
     * reseptit1.equals(reseptit2) === true;
     * reseptit2.equals(reseptit1) === true;
     * 
     * reseptit1.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.equals(reseptit2) === false;
     * 
     * reseptit2.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.equals(reseptit2) === true;
     * 
     * reseptit1.lisaa(new Resepti("Mustikkapiirakka"));
     * reseptit1.lisaa(new Resepti("Suklaakakku"));
     * reseptit1.equals(reseptit2) === false;
     * 
     * reseptit2.lisaa(new Resepti("Mustikkapiirakka"));
     * reseptit2.lisaa(new Resepti("Suklaakakku"));
     * reseptit1.equals(reseptit2) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Reseptit verrattavaReseptit = (Reseptit)verrattava;
        
        if (verrattavaReseptit.reseptit.size() != this.reseptit.size()) return false;
        if (!verrattavaReseptit.tiedostopolku.equals(this.tiedostopolku)) return false;
        if (!verrattavaReseptit.tiedostonimi.equals(this.tiedostonimi)) return false;
        
        // verrataan yksittäisiä reseptejä toisiinsa
        for (int i = 0; i < this.reseptit.size(); i++) {
            if (!verrattavaReseptit.reseptit.get(i).equals(this.reseptit.get(i))) return false;
        }
        
        return true;
    }
    
    
    @Override
    /**
     * Muodostaa hash-koodin
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit1 = new Reseptit();
     * Reseptit reseptit2 = new Reseptit();
     * reseptit1.hashCode() == reseptit2.hashCode() === true;
     * 
     * reseptit1.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.hashCode() == reseptit2.hashCode() === false;
     * 
     * reseptit2.setTiedostoNimi("jälkiruoat.txt");
     * reseptit1.hashCode() == reseptit2.hashCode() === true;
     * 
     * reseptit1.lisaa(new Resepti("Mustikkapiirakka"));
     * reseptit1.lisaa(new Resepti("Suklaakakku"));
     * reseptit1.hashCode() == reseptit2.hashCode() === false;
     * 
     * reseptit2.lisaa(new Resepti("Mustikkapiirakka"));
     * reseptit2.lisaa(new Resepti("Suklaakakku"));
     * reseptit1.hashCode() == reseptit2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautus(hash, this.tiedostonimi);
        
        for (Resepti resepti : this.reseptit) {
            hash = Hajautus.hajautus(hash, resepti.hashCode());
        }
        
        return hash;
    }
    
    
    @Override
    /**
     * Tiedot muodossa "lukumäärä|tiedostonimi"
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.reseptit.size());
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
    	Reseptit reseptit = new Reseptit();
        
        Resepti kinkkukiusaus = new Resepti("Kinkkukiusaus");
        Osio valmistus = new Osio("Valmistus");
        valmistus.lisaaAinesosa("Pakaste peruna-sipuli sekoitusta", "1kg");
        valmistus.lisaaAinesosa("Kinkkusuikaleita", "150g");
        valmistus.lisaaAinesosa("Suolaa", "1tl");
        valmistus.lisaaAinesosa("Mustapippuria", "1tl");
        valmistus.lisaaAinesosa("Ruokakermaa", "2 prk");
        valmistus.lisaaOhje("Kaada osa pakastesekoituksesta vuokaan");
        valmistus.lisaaOhje("Levitä kinkkusuikaleet vuokaan");
        valmistus.lisaaOhje("Kaada loput pakastesekoituksesta vuokaan");
        valmistus.lisaaOhje("Lisää mausteet ja ruokakerma");
        valmistus.lisaaOhje("Paista 200°C noin 1h");
        kinkkukiusaus.lisaaOsio(valmistus);
        kinkkukiusaus.setKuvaus("Halpaa arkiruokaa pienellä vaivalla");
        kinkkukiusaus.setTahdet(1);
        kinkkukiusaus.setValmistusaika(3);
        reseptit.lisaa(kinkkukiusaus);
        
        Resepti lihapullat = new Resepti("Lihapullat");
        Osio valmistusLihap = new Osio("Valmistus");
        valmistusLihap.lisaaAinesosa("jauhelihaa", "400g");
        valmistusLihap.lisaaAinesosa("sipulikeittoaineksia", "1ps");
        valmistusLihap.lisaaAinesosa("kananmunia", "1kpl");
        valmistusLihap.lisaaAinesosa("kermaviiliä", "1prk");
        valmistusLihap.lisaaAinesosa("mustapippuria", "");
        valmistusLihap.lisaaAinesosa("korppujauhoja", "1/2dl");
        valmistusLihap.lisaaOhje("Sekoita sipulikeittoainekset, korppujauhot, kermaviili, kananamuna ja mustapippuri keskenään");
        valmistusLihap.lisaaOhje("Lisää jauheliha ja sekoita tasaiseksi");
        valmistusLihap.lisaaOhje("Kostuta kädet viileällä vedellä ja pyöritä halutun kokoisia lihapullia");
        valmistusLihap.lisaaOhje("Paista 225°C noin 15min");
        lihapullat.lisaaOsio(valmistusLihap);
        lihapullat.setKuvaus("Sopivat hyvin usean eri lisukkeen, kuten perunamuusin tai ranskalaisten kanssa. Sopivat todella hyvin myös lihapullapastaan. Kermaviilin sijasta voi käyttää 1,5dl vettä.");
        lihapullat.setHinta(1);
        lihapullat.setTahdet(5);
        lihapullat.setValmistusaika(3);
        lihapullat.setVaativuus(2);
        reseptit.lisaa(lihapullat);
        
        Resepti mustikkamuffinit = new Resepti("Mustikkamuffinit");
        Osio valmistusMuff = new Osio("Valmistus");
        valmistusMuff.lisaaAinesosa("vehnäjauhoja", "3.45dl");
        valmistusMuff.lisaaAinesosa("sokeria", "2.1dl");
        valmistusMuff.lisaaAinesosa("leivinjauhetta", "1.5tl");
        valmistusMuff.lisaaAinesosa("voita", "110g");
        valmistusMuff.lisaaAinesosa("kananmunia", "2kpl");
        valmistusMuff.lisaaAinesosa("kardemummaa", "1tl");
        valmistusMuff.lisaaAinesosa("vaniljasokeria", "1tl");
        valmistusMuff.lisaaAinesosa("maustamatonta jogurttia", "1.5dl");
        valmistusMuff.lisaaAinesosa("mustikoita", "2dl");
        valmistusMuff.lisaaAinesosa("muffinivuokia", "10kpl");
        valmistusMuff.lisaaOhje("Sekoita kuivat ainekset keskenään");
        valmistusMuff.lisaaOhje("Erilliseen kulhoon, lisää sulatettu voi, jonka on annettu jäähtyä ennen lisäystä");
        valmistusMuff.lisaaOhje("Lisää kananmunat ja jogurtti sulatettuun voihin");
        valmistusMuff.lisaaOhje("Nopeasti sekoittaen lisää nestemäinen seos kuiviin aineksiin, vältä turhaa sekoitusta");
        valmistusMuff.lisaaOhje("Kääntele mustikat taikinan joukkoon");
        valmistusMuff.lisaaOhje("Jaa muffinivuokiin");
        valmistusMuff.lisaaOhje("Paista 173°C 28min 30s");
        mustikkamuffinit.lisaaOsio(valmistusMuff);
        mustikkamuffinit.setHinta(2);
        mustikkamuffinit.setValmistusaika(3);
        mustikkamuffinit.setVaativuus(5);
        reseptit.lisaa(mustikkamuffinit);
            
        try {    
            reseptit.tallenna();
        } catch (SailoException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
