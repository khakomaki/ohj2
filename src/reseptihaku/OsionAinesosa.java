package reseptihaku;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Hajautus;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 * Osion ainesosat koostuvat ainesosista ja niiden määristä.
 */
public class OsionAinesosa {
    
	private int osioId 			= -1;
    private String ainesosaNimi = "ainesosa";
    private String maara        = "";
    
    
    /**
     * Luo oletus ainesosan oletus nimellä ja määrällä
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa oletus = new OsionAinesosa();
     * oletus.toString() === "-1|ainesosa|";
     * </pre>
     */
    public OsionAinesosa() {
        //
    }
    
    
    /**
     * Luo osion ainesosan määrällä
     * 
     * @param ainesosa ainesosan nimi
     * @param maara ainesosan määrä
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa peruna = new OsionAinesosa("peruna", "1kg");
     * peruna.toString() === "-1|peruna|1kg";
     * </pre>
     */
    public OsionAinesosa(String ainesosa, String maara) {
        setAinesosa(ainesosa);
        setMaara(maara);
    }
    
    
    /**
     * Palauttaa osion ainesosan nimen
     * 
     * @return ainesosan nimi
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa peruna = new OsionAinesosa("peruna", "1kg");
     * peruna.toString() === "-1|peruna|1kg";
     * peruna.getAinesosa() === "peruna";
     * </pre>
     */
    public String getAinesosa() {
        return this.ainesosaNimi;
    }
    
    
    /**
     * Asettaa osion ainesosan nimen.
     * Ei aseta null-viitteeksi.
     * 
     * @param ainesosa asetettava ainesosan nimi
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa peruna = new OsionAinesosa("peruna", "1kg");
     * peruna.toString() === "-1|peruna|1kg";
     * peruna.getAinesosa() === "peruna";
     * 
     * peruna.setAinesosa("makkara");
     * peruna.getAinesosa() === "makkara";
     * 
     * peruna.setAinesosa(null);
     * peruna.getAinesosa() === "makkara";
     * 
     * peruna.setAinesosa("");
     * peruna.getAinesosa() === "makkara";
     * </pre>
     */
    public void setAinesosa(String ainesosa) {
        // ei tehdä muutoksia jos yritetään laittaa nimeksi null tai tyhjä merkkijono
        if (ainesosa == null || ainesosa.length() < 1) return;
        this.ainesosaNimi = ainesosa;
    }
    
    
    /**
     * Palauttaa osion ainesosan määrän
     * 
     * @return ainesosan määrä
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa peruna = new OsionAinesosa("peruna", "1kg");
     * peruna.toString() === "-1|peruna|1kg";
     * peruna.getMaara() === "1kg";
     * </pre>
     */
    public String getMaara() {
        return this.maara;
    }
    
    
    /**
     * Asettaa osion kyseisen ainesosan määrän.
     * Ei aseta null-viitteeksi
     * 
     * @param maara asetettava määrä
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa peruna = new OsionAinesosa("peruna", "1kg");
     * peruna.toString() === "-1|peruna|1kg";
     * peruna.getMaara() === "1kg";
     * 
     * peruna.setMaara("2kg");
     * peruna.getMaara() === "2kg";
     * 
     * peruna.setMaara(null);
     * peruna.getMaara() === "2kg";
     * 
     * peruna.setMaara("");
     * peruna.getMaara() === "";
     * </pre>
     */
    public void setMaara(String maara) {
        if (maara == null) return;
        this.maara = maara;
    }
    
    
    /**
     * Antaa osion tunnuksen johon ainesosa kuuluu
     * 
     * @return osion tunnus
     */
    public int getOsioId() {
    	return this.osioId;
    }
    
    
    /**
     * Asettaa osion tunnuksen johon ainesosa kuuluu
     * 
     * @param id osion tunnus
     */
    public void setOsioId(int id) {
    	this.osioId = id;
    }
    
    
    /**
     * Antaa tietokannan SQL-luontilausekkeen Ainesosat-taululle
     * 
     * @return Ainesosat-taulukon luontilauseke
     */
    public String getLuontilauseke() {
        StringBuilder lauseke = new StringBuilder();
        lauseke.append("CREATE TABLE Ainesosat (");
        
        lauseke.append("osio_id INTEGER, ");
        lauseke.append("nimi VARCHAR(100), ");
        lauseke.append("maara VARCHAR(100), ");
        lauseke.append("PRIMARY KEY (osio_id, nimi), ");
        lauseke.append("FOREIGN KEY (osio_id) REFERENCES Osiot(osio_id)");
        
        lauseke.append(")");
        return lauseke.toString();
    }
    
    
    /**
     * Antaa ainesosan lisäyslausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return lisäyslauseke
     * @throws SQLException jos lausekkeen muodostamisessa ongelmia
     */
    public PreparedStatement getLisayslauseke(Connection yhteys) throws SQLException {
        PreparedStatement sql = yhteys.prepareStatement("INSERT INTO Ainesosat (osio_id, nimi, maara) VALUES (?, ?, ?)");
        
        sql.setInt(1, this.osioId);
        sql.setString(2, this.ainesosaNimi);
        sql.setString(3, this.maara);
        
        return sql;
    }
    
    
    /**
     * Antaa ainesosan poistolausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return poistolauseke
     * @throws SQLException jos lausekkeen muodostamisessa ongelmia
     */
    public PreparedStatement getPoistolauseke(Connection yhteys) throws SQLException {
        PreparedStatement sql = yhteys.prepareStatement("DELETE FROM Ainesosat WHERE osio_id = ? AND nimi = ?");
        
        // ainesosa yksilöity PK = osio_id, nimi
        sql.setInt(1, this.osioId);
        sql.setString(2, this.ainesosaNimi);
        
        return sql;
    }
    
    
    /**
     * Parsii annetuista tiedoista omat tietonsa
     * 
     * @param tulokset mistä tiedot saadaan
     * @throws SQLException jos tulee ongelmia
     */
    public void parse(ResultSet tulokset) throws SQLException {
        setOsioId(tulokset.getInt("osio_id"));
        setAinesosa(tulokset.getString("nimi"));
        setMaara(tulokset.getString("maara"));
    }
    
    
    /**
     * Parsii osion ainesosat 
     * 
     * @param rivi mistä parsitaan osion ainesosan tiedot
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa peruna = new OsionAinesosa();
     * peruna.parse("1|peruna|300g");
     * peruna.toString() === "-1|peruna|300g";
     * 
     * peruna.parse("  15   |  keltainen peruna    |  1kg     ");
     * peruna.toString() === "-1|keltainen peruna|1kg";
     * 
     * peruna.parse("15|peruna 300g");
     * peruna.toString() === "-1|peruna 300g|1kg";
     * 
     * peruna.parse("17 pataatti 280g");
     * peruna.toString() === "-1|peruna 300g|1kg";
     * 
     * peruna.parse("|");
     * peruna.toString() === "-1|peruna 300g|1kg";
     * </pre>
     */
    public void parse(String rivi) {
        if (rivi == null || rivi.length() < 1) return;
        
        StringBuilder sb = new StringBuilder(rivi);
        
        // ei tallenna ensimmäisen kentän tietoja (osio_id)
        Mjonot.erota(sb, '|');
        
        setAinesosa(Mjonot.erota(sb, '|', this.ainesosaNimi));
        setMaara(Mjonot.erota(sb, '|', this.maara));
    }
    
    
    @Override
    /**
     * Luo täydellisen kopion osion ainesosasta
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa juusto = new OsionAinesosa("juusto", "2kg");
     * juusto.toString() === "-1|juusto|2kg";
     * 
     * OsionAinesosa juustoKopio = juusto.clone();
     * juustoKopio.toString().equals(juusto.toString()) === true;
     * 
     * juustoKopio.setMaara("3kg");
     * juustoKopio.toString().equals(juusto.toString()) === false;
     * </pre>
     */
    public OsionAinesosa clone() {
        OsionAinesosa kopio = new OsionAinesosa();
        kopio.ainesosaNimi = this.ainesosaNimi;
        kopio.maara = this.maara;
        return kopio;
    }
    
    
    @Override
    /**
     * Muodostaa hash-koodin osion ainesosalle
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa juusto1 = new OsionAinesosa("juusto", "2kg");
     * OsionAinesosa juusto2 = new OsionAinesosa("sinihomejuusto", "2kg");
     * OsionAinesosa juusto3 = new OsionAinesosa("juusto", "3kg");
     * 
     * juusto1.hashCode() == juusto2.hashCode() === false;
     * juusto1.hashCode() == juusto3.hashCode() === false;
     * juusto2.hashCode() == juusto3.hashCode() === false;
     * 
     * juusto3.setMaara("2kg");
     * juusto1.hashCode() == juusto3.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautusObject(hash, this.ainesosaNimi, this.maara);
        return hash;
    }
    
    
    @Override
    /**
     * Vertailee onko olio sama kuin itse
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa juusto1 = new OsionAinesosa("juusto", "2kg");
     * OsionAinesosa juusto2 = new OsionAinesosa("sinihomejuusto", "2kg");
     * OsionAinesosa juusto3 = new OsionAinesosa("juusto", "3kg");
     * 
     * juusto1.equals(juusto2) === false;
     * juusto1.equals(juusto3) === false;
     * juusto2.equals(juusto3) === false;
     * 
     * juusto3.setMaara("2kg");
     * juusto1.equals(juusto3) === true;
     * juusto2.equals(juusto3) === false;
     * juusto3.equals(juusto1) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        
        OsionAinesosa verrattavaOA = (OsionAinesosa)verrattava;
        if (!verrattavaOA.ainesosaNimi.equals(this.ainesosaNimi)) return false;
        if (!verrattavaOA.maara.equals(this.maara)) return false;
        
        return true;
    }
    
    
    @Override
    /**
     * Palauttaa tietonsa merkkijonona
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa mozzarella = new OsionAinesosa("Mozzarella", "120g");
     * mozzarella.toString() === "-1|Mozzarella|120g";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.ainesosaNimi);
        sb.append('|');
        sb.append(this.maara);
        return sb.toString();
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        OsionAinesosa oa1 = new OsionAinesosa("juusto", "2kg");
        OsionAinesosa oa2 = new OsionAinesosa("sinihomejuusto", "2kg");
        System.out.println(oa1.hashCode());
        System.out.println(oa2.hashCode());
        
        System.out.println(oa1.getLuontilauseke());
    }
}
