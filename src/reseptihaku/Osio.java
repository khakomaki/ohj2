package reseptihaku;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Hajautus;
import kanta.SailoException;
import kanta.Validoi;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 * Reseptin osio koostuu ainesosista ja ohjeista.
 */
public class Osio {
    
	private int reseptiID					= -1;
    private int osioId                      = -1;
    private String nimi                     = "Osion nimi";
    private OsionAinesosat osionAinesosat	= null;
    private Ohjeet ohjeet					= null;
    
    /**
     * @param id osion tunnus
     * @param nimi osion nimi
     * 
     * @example
     * <pre name="test">
     * Osio pizzapohja = new Osio(2, "Pizzapohja");
     * pizzapohja.toString() === "-1|2|Pizzapohja";
     * </pre>
     */
    public Osio(int id, String nimi) {
        this.osioId = id;
        setUusiNimi(nimi);
        luoOsionOhjeet();
        luoOsionAinesosat();
    }
    
    
    /**
     * Ohje valitulla nimellä
     * 
     * @param nimi osion nimi
     * @example
     * <pre name="test">
     * Osio pizzapohja = new Osio("Pizzapohja");
     * pizzapohja.toString() === "-1|-1|Pizzapohja";
     * </pre>
     */
    public Osio(String nimi) {
        setUusiNimi(nimi);
        luoOsionOhjeet();
        luoOsionAinesosat();
    }
    
    
    /**
     * Oletus ohje
     * 
     * @example
     * <pre name="test">
     * Osio osio = new Osio();
     * osio.toString() === "-1|-1|Osion nimi";
     * </pre>
     */
    public Osio() {
        luoOsionOhjeet();
        luoOsionAinesosat();
    }
    
    
    /**
     * Asettaa reseptin tunnuksen johon osio kuuluu
     * 
     * @param id reseptin tunnus
     */
    public void setReseptiID(int id) {
    	this.reseptiID = id;
    }
    
    
    /**
     * Asettaa osion tunnuksen, jos sitä ei vielä ole asetettu.
     * 
     * @param id asetettava osion tunnus
     */
    private void setID(int id) {
    	if (id < this.osioId) return;
    	this.osioId = id;
    	this.osionAinesosat.setOsioId(this.osioId);
    	this.ohjeet.setOsioId(this.osioId);
    }
    
    
    /**
     * Antaa osion tunnuksen
     * 
     * @return osion tunnus
     */
    public int getID() {
    	return this.osioId;
    }
    
    
    /**
     * Luo osion ohjeet
     */
    private void luoOsionOhjeet() {
		this.ohjeet = new Ohjeet(this.osioId);
    }
    
    
    /**
     * Luo osion ainesosat
     */
    private void luoOsionAinesosat() {	
		this.osionAinesosat = new OsionAinesosat(this.osioId);
    }
    
    
    /**
     * Asettaa osion nimen.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null.
     * 
     * @param nimi osion nimi
     * 
     * @example
     * <pre name="test">
     * Osio kakkupohja = new Osio(8, "Kakkupohja");
     * kakkupohja.toString() === "-1|8|Kakkupohja";
     * 
     * kakkupohja.setUusiNimi("");
     * kakkupohja.toString() === "-1|8|Kakkupohja";
     * 
     * kakkupohja.setUusiNimi("Piirakkapohja");
     * kakkupohja.toString() === "-1|8|Piirakkapohja";
     * 
     * kakkupohja.setUusiNimi(null);
     * kakkupohja.toString() === "-1|8|Piirakkapohja";
     * </pre>
     */
    public void setUusiNimi(String nimi) {
        if (nimi == null) return;
        if (nimi.length() < 1) return;
        this.nimi = nimi;
    }
    
    
    /**
     * Antaa osion nimen
     * 
     * @return osion nimi
     * 
     * @example
     * <pre name="test">
     * Osio kakkupohja = new Osio(8, "Kakkupohja");
     * kakkupohja.getNimi() === "Kakkupohja";
     * </pre>
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * Asettaa tiedostopolun
     * 
     * @param tiedostopolku mihin tallennetaan ja luetaan
     */
    public void setTiedostopolku(String tiedostopolku) {
        this.ohjeet.setTiedostoPolku(tiedostopolku);
        this.osionAinesosat.setTiedostoPolku(tiedostopolku);
    }
    
    
    /**
     * Antaa osion ainesosat
     * 
     * @return osion ainesosat -olio
     */
    public OsionAinesosat annaOsionAinesosat() {
        return this.osionAinesosat;
    }
    
    
    /**
     * Antaa osion ohjeet
     * 
     * @return osion ohjeet
     */
    public Ohjeet annaOsionOhjeet() {
        return this.ohjeet;
    }
    
    
    /**
     * Luo ja lisää annetun mukaisen osion ainesosan
     * 
     * @param ainesosa ainesosan nimi
     * @param maara ainesosan määrä
     * @return luotu osion ainesosa
     */
    public OsionAinesosa lisaaAinesosa(String ainesosa, String maara) {
        OsionAinesosa osionAinesosa = new OsionAinesosa(ainesosa, maara);
        this.osionAinesosat.lisaa(osionAinesosa);
        return osionAinesosa;
    }
    
    
    /**
     * Lisää ainesosan
     * 
     * @param ainesosa lisättävä osion ainesosa
     */
    public void lisaaAinesosa(OsionAinesosa ainesosa) {
        this.osionAinesosat.lisaa(ainesosa);
    }
    
    
    /**
     * Poistaa osion ainesosan, jos sellainen löytyy
     * 
     * @param osionAinesosa poistettava osion ainesosa
     */
    public void poistaAinesosa(OsionAinesosa osionAinesosa) {
        this.osionAinesosat.poista(osionAinesosa);
    }
    
    
    /**
     * Lisää ohjeen
     * 
     * @param ohje lisättävä ohje
     */
    public void lisaaOhje(Ohje ohje) {
        this.ohjeet.lisaa(ohje);
    }
    
    
    /**
     * Luo ja lisää ohjeen
     * 
     * @param ohjeistus lisättävän ohjeen ohjeistusteksti
     */
    public void lisaaOhje(String ohjeistus) {
        Ohje ohje = new Ohje(ohjeistus);
        this.ohjeet.lisaa(ohje);
    }
    
    
    /**
     * Poistaa annetun ohjeen, jos sellainen löydetään
     * 
     * @param ohje poistettava ohje
     */
    public void poistaOhje(Ohje ohje) {
        this.ohjeet.poista(ohje);
    }
    
    
    /**
     * Antaa tietokannan SQL-luontilausekkeen Osiot-taululle
     * 
     * @return Osiot-taulukon luontilauseke
     */
    public String getLuontilauseke() {
        StringBuilder lauseke = new StringBuilder();
        lauseke.append("CREATE TABLE Osiot (");
        
        lauseke.append("resepti_id INTEGER, ");
        lauseke.append("osio_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        lauseke.append("nimi VARCHAR(100) NOT NULL, ");
        lauseke.append("FOREIGN KEY (resepti_id) REFERENCES Reseptit(resepti_id)");
        
        lauseke.append(")");
        return lauseke.toString();
    }
    
    
    /**
     * Antaa osion lisäyslausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return lisäyslauseke
     * @throws SQLException jos lausekkeen muodostamisessa ongelmia
     */
    public PreparedStatement getLisayslauseke(Connection yhteys) throws SQLException {
        PreparedStatement sql = yhteys.prepareStatement("INSERT INTO Osiot (resepti_id, nimi) VALUES (?, ?)");
        
        sql.setInt(1, this.reseptiID);
        sql.setString(2, this.nimi);
        
        return sql;
    }
    
    
    /**
     * Antaa osion poistolausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return poistolauseke
     * @throws SQLException jos lausekkeen muodostamisessa ongelmia
     */
    public PreparedStatement getPoistolauseke(Connection yhteys) throws SQLException {
        PreparedStatement sql = yhteys.prepareStatement("DELETE FROM Osiot WHERE osio_id = ?");
        
        // osio yksilöity PK = osio_id
        sql.setInt(1, this.osioId);
        
        return sql;
    }
    
    
    /**
     * Antaa osion päivityslausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return päivityslauseke
     * @throws SQLException jos lausekkeen muodostamisessa ilmenee ongelmia
     */
    public PreparedStatement getPaivityslauseke(Connection yhteys) throws SQLException {
    	PreparedStatement sql = yhteys.prepareStatement("UPDATE Osiot SET resepti_id = ?, nimi = ? WHERE osio_id = ?");
    	
    	sql.setInt(1, this.reseptiID);
    	sql.setString(2, this.nimi);
    	sql.setInt(3, this.osioId);
    	
    	return sql;
    }
    
    
    /**
     * Parsii annetuista tiedoista omat tietonsa
     * 
     * @param tulokset mistä tiedot saadaan
     * @throws SQLException jos tulee ongelmia
     */
    public void parse(ResultSet tulokset) throws SQLException {
    	setReseptiID(tulokset.getInt("resepti_id"));
    	setID(tulokset.getInt("osio_id"));
        setUusiNimi(tulokset.getString("nimi"));
    }
    
    
    /**
     * Tarkistaa tietokannan tiedoista onko tunnus muuttunut ja päivittää tarvittaessa omia tietoja
     * 
     * @param tulokset mistä tiedot katsotaan
     * @throws SQLException jos tulee ongelmia
     */
    public void tarkistaID(ResultSet tulokset) throws SQLException {
    	if (!tulokset.next()) return;
    	int ID = tulokset.getInt(1);
    	if (ID == this.osioId) return;
    	setID(ID);
    }
    
    
    /**
     * Tallentaa osion sisältämät ohjeet ja ainesosat
     * 
     * @throws SailoException jos tallentamisessa ilmenee ongelmia
     */
    public void tallenna() throws SailoException {
    	this.osionAinesosat.tallenna();
    	this.ohjeet.tallenna();
    }
    
    
    /**
     * Lukee osion sisältämät ohjeet ja ainesosat tietokannasta
     * 
     * @throws SailoException jos tietojen lukemisessa ilmenee ongelmia
     */
    public void lueTiedostosta() throws SailoException {
    	this.osionAinesosat.lueTiedostosta();
    	this.ohjeet.lueTiedostosta();
    }
    
    
    /**
     * Poistaa osion tiedot tietokannasta
     * 
     * @throws SailoException jos poistossa ilmenee ongelmia
     */
    public void poistaTietokannasta() throws SailoException {
    	this.osionAinesosat.poistaTietokannasta();
    	this.ohjeet.poistaTietokannasta();
    }
    
    
    /**
     * Kertoo onko osioon tullut muutoksia
     * 
     * @return onko osioon tullut muutoksia
     */
    public boolean onkoTallentamattomiaMuutoksia() {
        return true; // TODO poista kun ei käytetä enää muualla
    }
    
    
    /**
     * Koittaa parsia osion tiedot annetusta rivistä
     * 
     * @param rivi mistä koitetaan lukea osion tiedot
     */
    public void parse(String rivi) {
        if (rivi == null || rivi.length() < 1) return;
        
        StringBuilder sb = new StringBuilder(rivi);
        
        // ei huomioida ensimmäisen kentän tietoja (resepti_id)
        Mjonot.erota(sb, '|');
        
        setID(Mjonot.erota(sb, '|', this.osioId));
        setUusiNimi(Mjonot.erota(sb, '|', this.nimi));
    }
    
    
    /**
     * Kertoo voidaanko osio tallentaa
     * 
     * @return virheteksti tai null jos voidaan tallentaa
     */
    public String voidaankoTallentaa() {
        // onko nimi ok
        if (!Validoi.onkoNimiTallennettavissa(getNimi())) return "Osion nimi ei ole tallennettavissa!";
        
        // onko sekä ohjeet että ainesosat tyhjiä
        if (this.ohjeet.getLkm() <= 0 && this.osionAinesosat.size() <= 0) return "Osio ei sisällä yhtään ohjetta tai ainesosaa!";
        
        return null;
    }
    
    
    /**
     * Luo muropohja osion testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoMuropohja() {
        this.nimi = "Muropohja";
        lisaaAinesosa("voita", "100g");
        lisaaAinesosa("sokeria", "1dl");
        lisaaAinesosa("kananmunia", "1kpl");
        lisaaAinesosa("vehnäjauhoja", "2,5dl");
        lisaaAinesosa("leivinjauhetta", "1tl");
        
        lisaaOhje("Sekoita sokeri ja pehmeä voi");
        lisaaOhje("Lisää kananmuna ja vaahdota");
        lisaaOhje("Lisää leivinjauho vehnäjauhoihin");
        lisaaOhje("Sekoita jauhot vaahtoon");
        lisaaOhje("Painele taikina piirakkavuokaan");
    }
    
    
    /**
     * Luo muropohja osion testaamista varten
     * TODO: poista kun ei enää tarvita
     */
    public void luoTayte() {
        this.nimi = "Täyte";
        lisaaAinesosa("sokeria", "0,75dl");
        lisaaAinesosa("kananmunia", "1kpl");
        lisaaAinesosa("mustikoita", "300g");
        lisaaAinesosa("kermaviiliä", "200g");
        lisaaAinesosa("vanilliinisokeria", "1tl");
        
        lisaaOhje("Ripottele mustikat piirakkapohjan päälle");
        lisaaOhje("Sekoita sokeri, kermaviili ja kananmuna");
        lisaaOhje("Lisää vanilliinisokeri");
        lisaaOhje("Kaada kermaviiliseos piirakkapohjan päälle");
    }
    
    
    @Override
    /**
     * Luo kopion osioista
     * 
     * @example
     * <pre name="test">
     * Osio sampylat = new Osio("Sämpylät");
     * sampylat.lisaaAinesosa("vehnäjauhoja", "8dl");
     * sampylat.lisaaAinesosa("maito", "4dl");
     * sampylat.lisaaAinesosa("kuivahiiva", "1ps");
     * sampylat.lisaaOhje(new Ohje("Lämmitä maito 42-asteiseksi"));
     * sampylat.lisaaOhje(new Ohje("Sekoita kuivahiiva maitoon"));
     * sampylat.lisaaOhje(new Ohje("Sekoita jauhot maitoon"));
     * Osio sampylaKopio = sampylat.clone();
     * 
     * sampylaKopio.equals(sampylat) === true;
     * sampylaKopio.toString().equals(sampylat.toString()) === true;
     * sampylat.annaOsionAinesosat().equals(sampylaKopio.annaOsionAinesosat()) === true;
     * sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet()) === true;
     * 
     * sampylat.lisaaOhje("Anna kohota liinan alla");
     * sampylaKopio.equals(sampylat) === false;
     * sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet()) === false;
     * 
     * sampylaKopio.lisaaOhje("Anna kohota liinan alla");
     * sampylaKopio.equals(sampylat) === true;
     * sampylat.annaOsionOhjeet().equals(sampylaKopio.annaOsionOhjeet()) === true;
     * 
     * sampylat.setUusiNimi("Sämpylä");
     * sampylaKopio.toString().equals(sampylat.toString()) === false;
     * 
     * sampylaKopio.setUusiNimi("Sämpylä");
     * sampylaKopio.toString().equals(sampylat.toString()) === true;
     * </pre>
     */
    public Osio clone() {
        Osio kopio = new Osio();
        kopio.osioId = this.osioId;
        kopio.nimi = this.nimi;
        kopio.reseptiID = this.reseptiID;
        
        // kopioidaan osion omat ainesosat ja ohjeet
        kopio.osionAinesosat = this.osionAinesosat.clone();
        kopio.ohjeet = this.ohjeet.clone();
        
        return kopio;
    }
    
    
    @Override
    /**
     * Vertaa onko annettu osio sama kuin nykyinen
     * 
     * @example
     * <pre name="test">
     * Osio pohja1 = new Osio(1, "Kakkupohja");
     * Osio pohja2 = new Osio(1, "Kakkupohj");
     * Osio pohja3 = new Osio(2, "Kakkupohja");
     * 
     * pohja1.equals(pohja2) === false;
     * pohja1.equals(pohja3) === false;
     * pohja2.equals(pohja3) === false;
     * 
     * pohja2.setUusiNimi("Kakkupohja");
     * pohja1.equals(pohja2) === true;
     * pohja2.equals(pohja3) === false;
     * pohja2.equals(pohja1) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        Osio verrattavaOsio = (Osio)verrattava;
        if (!verrattavaOsio.nimi.equals(this.nimi)) return false;
        if (verrattavaOsio.osioId != this.osioId) return false;
        if (verrattavaOsio.reseptiID != this.reseptiID) return false;
        if (!verrattavaOsio.osionAinesosat.equals(this.osionAinesosat)) return false;
        if (!verrattavaOsio.ohjeet.equals(this.ohjeet)) return false;
        
        return true;
    }
    
    
    @Override
    /**
     * Antaa hash-arvon
     * 
     * @example
     * <pre name="test">
     * Osio pohja1 = new Osio(1, "Kakkupohja");
     * Osio pohja2 = new Osio(1, "Kakkupohj");
     * Osio pohja3 = new Osio(2, "Kakkupohja");
     * 
     * pohja1.hashCode() == pohja2.hashCode() === false;
     * pohja1.hashCode() == pohja3.hashCode() === false;
     * pohja2.hashCode() == pohja3.hashCode() === false;
     * 
     * pohja2.setUusiNimi("Kakkupohja");
     * pohja1.hashCode() == pohja2.hashCode() === true;
     * pohja1.hashCode() == pohja3.hashCode() === false;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautus(hash, this.nimi);
        hash = Hajautus.hajautusInt(hash, this.reseptiID, this.osioId, this.osionAinesosat.hashCode(), this.ohjeet.hashCode());
        return hash;
    }
    
    
    @Override
    /**
     * Osion tiedot String-muodossa "reseptitunnus | osiotunnus | osion nimi"
     * 
     * Osio pizzapohja = new Osio(13, "Hampurilaissämpylät");
     * pizzapohja.toString() === "-1|13|Hampurilaissämpylät";
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.reseptiID);
        sb.append('|');
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.nimi);
        return sb.toString();
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Osio lattytaikina = new Osio(1, "Lättytaikina");
        System.out.println(lattytaikina.toString());
        System.out.println(lattytaikina.getLuontilauseke());
        
        lattytaikina.lisaaAinesosa(new OsionAinesosa("maitoa", "5dl"));
        lattytaikina.lisaaAinesosa(new OsionAinesosa("kananmunia", "3kpl"));
        lattytaikina.lisaaAinesosa(new OsionAinesosa("sokeria", "2rkl"));
        lattytaikina.lisaaAinesosa(new OsionAinesosa("vehnäjauhoja", "3dl"));
        lattytaikina.lisaaAinesosa(new OsionAinesosa("suolaa", "1tl"));
        lattytaikina.lisaaAinesosa(new OsionAinesosa("voi", ""));
        lattytaikina.annaOsionAinesosat().tulostaOsionAinesosat(System.out);
        
        lattytaikina.lisaaOhje(new Ohje("Sekoita kuivat ainekset"));
        lattytaikina.lisaaOhje(new Ohje("Sulata voi"));
        lattytaikina.lisaaOhje(new Ohje("Lisää sulatettu voi jauhoihin"));
        lattytaikina.lisaaOhje(new Ohje("Lisää maito"));
        lattytaikina.lisaaOhje(new Ohje("Sekoita hyvin"));
        
        System.out.println("\n" + lattytaikina.toString());
        
        try {
			lattytaikina.tallenna();
		} catch (SailoException exception) {
			System.err.println(exception.getMessage());
		}
    }
}
