package reseptihaku;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Hajautus;

/**
 * @author hakom
 * @version 30 Oct 2023
 *
 * Ohje jolla on vaihe ja ohjeistus.
 */
public class Ohje {
    
    private int osioId          = -1;
    private int vaihe           = 1;
    private String ohjeistus    = "";
    
    /**
     * Yhden vaiheen ohjeistustekstin sisältävä olio
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.toString() === "-1|1|";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.toString() === "-1|1|Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus(null);
     * muropohja.toString() === "-1|1|Lisää vehnäjauhot";
     * </pre>
     */
    public Ohje() {
        this.ohjeistus = "";
    }
    
    
    /**
     * Yhden vaiheen ohjeistustekstin sisältävä olio
     * 
     * @param ohjeistus ohjeen sisältämä ohjeistusteksti
     */
    public Ohje(String ohjeistus) {
        setOhjeistus(ohjeistus);
    }
    
    
    /**
     * @param ohjeistus ohjeen sisältämä ohjeistusteksti
     * @param vaihe ohjeistustekstin vaihe
     */
    public Ohje(String ohjeistus, int vaihe) {
        setOhjeistus(ohjeistus);
        setVaihe(vaihe);
    }
    
    
    /**
     * Asettaa osion tunnuksen johon ohje kuuluu
     * 
     * @param id osion tunnus
     */
    public void setOsioId(int id) {
        this.osioId = id;
    }
    
    
    /**
     * @param ohjeistus ohjeen sisältämä ohjeistusteksti
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.toString() === "-1|1|";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.toString() === "-1|1|Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus(null);
     * muropohja.toString() === "-1|1|Lisää vehnäjauhot";
     * 
     * muropohja.setOhjeistus("");
     * muropohja.toString() === "-1|1|";
     * </pre>
     */
    public void setOhjeistus(String ohjeistus) {
        // ei anneta asettaa nullia
        if (ohjeistus == null) return;
        
        this.ohjeistus = ohjeistus;
    }
    
    
    /**
     * @return ohjeen ohjeistusteksti
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje();
     * muropohja.getOhjeistus() === "";
     * 
     * muropohja.setOhjeistus("Lisää vehnäjauhot");
     * muropohja.getOhjeistus() === "Lisää vehnäjauhot";
     * </pre>
     */
    public String getOhjeistus() {
        return this.ohjeistus;
    }
    
    
    /**
     * @return ohjeen vaihe
     * 
     * Ohje ohje = new Ohje();
     * ohje.getVaihe() === 1;
     * 
     * ohje = new Ohje("Lisää mustikat", 4)
     * ohje.getVaihe() === 4;
     */
    public int getVaihe() {
        return this.vaihe;
    }
    
    
    /**
     * @param vaihe asetettava vaihe
     * 
     * @example
     * <pre name="test">
     * Ohje ohje = new Ohje();
     * ohje.getVaihe() === 1;
     * 
     * ohje.setVaihe(10);
     * ohje.getVaihe() === 10;
     * 
     * ohje.setVaihe(-5);
     * ohje.getVaihe() === 10;
     * 
     * ohje.setVaihe(0);
     * ohje.getVaihe() === 10;
     * 
     * ohje.setVaihe(1);
     * ohje.getVaihe() === 1;
     * </pre>
     */
    public void setVaihe(int vaihe) {
        if (0 < vaihe) this.vaihe = vaihe;
    }
    
    
    /**
     * Antaa tietokannan SQL-luontilausekkeen Ohjeet-taululle
     * 
     * @return Ohjeet-taulukon luontilauseke
     */
    public String getLuontilauseke() {
        StringBuilder lauseke = new StringBuilder();
        lauseke.append("CREATE TABLE Ohjeet (");
        
        lauseke.append("osio_id INTEGER, ");
        lauseke.append("vaihe INTEGER, ");
        lauseke.append("ohjeistus VARCHAR(255), ");
        lauseke.append("PRIMARY KEY (osio_id, vaihe), ");
        lauseke.append("FOREIGN KEY (osio_id) REFERENCES Osiot(osio_id)");
        
        lauseke.append(")");
        return lauseke.toString();
    }
    
    
    /**
     * Antaa ohjeen lisäyslausekkeen
     * 
     * @param yhteys tietokantayhteys
     * @return lisäyslauseke
     * @throws SQLException jos lausekkeen muodostamisessa ongelmia
     */
    public PreparedStatement getLisayslauseke(Connection yhteys) throws SQLException {
        PreparedStatement sql = yhteys.prepareStatement("INSERT INTO Ohjeet (osio_id, vaihe, ohjeistus) VALUES (?, ?, ?)");
        
        sql.setInt(1, this.osioId);
        sql.setInt(2, this.vaihe);
        sql.setString(3, this.ohjeistus);
        
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
        setVaihe(tulokset.getInt("vaihe"));
        setOhjeistus(tulokset.getString("ohjeistus"));
    }
    
    
    /**
     * Parsii annetusta rivistä omat tietonsa
     * 
     * @param rivi parsittava rivi
     * 
     * @example
     * <pre name="test">
     * Ohje ohje = new Ohje();
     * ohje.parse("2|1|Lisää mansikat");
     * ohje.toString() === "2|1|Lisää mansikat";
     * 
     * ohje = new Ohje();
     * ohje.parse("15|Lisää mansikat|3");
     * ohje.toString() === "15|1|3";
     * </pre>
     */
    public void parse(String rivi) {
        if (rivi == null || rivi.length() < 1) return;
        
        StringBuilder sb = new StringBuilder(rivi);
        
        setOsioId(Mjonot.erota(sb, '|', this.osioId));
        setVaihe(Mjonot.erota(sb, '|', this.vaihe));
        setOhjeistus(Mjonot.erota(sb, '|', this.ohjeistus));
    }
    
    
    @Override
    /**
     * 
     * @example
     * <pre name="test">
     * Ohje muropohja = new Ohje("Sekoita sokeri ja jauhot", 2);
     * Ohje muropohjaKopio = muropohja.clone();
     * 
     * muropohja.getVaihe() == muropohjaKopio.getVaihe() === true;
     * muropohja.getOhjeistus().equals(muropohjaKopio.getOhjeistus()) === true;
     * muropohja.equals(muropohjaKopio) === true;
     * 
     * muropohja.setOhjeistus("Lisää sokeri");
     * muropohja.getOhjeistus().equals(muropohjaKopio.getOhjeistus()) === false;
     * muropohja.equals(muropohjaKopio) === false;
     * 
     * muropohja.setVaihe(1);
     * muropohja.getVaihe() == muropohjaKopio.getVaihe() === false;
     * </pre>
     */
    public Ohje clone() {
        Ohje kopio = new Ohje();
        kopio.ohjeistus = this.ohjeistus;
        kopio.vaihe = this.vaihe;
        return kopio;
    }
    
    
    @Override
    /**
     * 
     * @example
     * <pre name="test">
     * Ohje ohje1 = new Ohje();
     * Ohje ohje2 = new Ohje();
     * ohje1.equals(ohje2) === true;
     * ohje2.equals(ohje1) === true;
     * 
     * ohje1.setOhjeistus("Vatkaa kananmunat");
     * ohje1.equals(ohje2) === false;
     * ohje2.equals(ohje1) === false;
     * 
     * ohje2.setOhjeistus("Vatkaa kananmunat");
     * ohje1.equals(ohje2) === true;
     * ohje2.equals(ohje1) === true;
     * 
     * ohje2.setVaihe(12);
     * ohje1.equals(ohje2) === false;
     * ohje2.equals(ohje1) === false;
     * 
     * ohje1.setVaihe(11);
     * ohje1.equals(ohje2) === false;
     * ohje2.equals(ohje1) === false;
     * 
     * ohje1.setVaihe(12);
     * ohje1.equals(ohje2) === true;
     * ohje2.equals(ohje1) === true;
     * </pre>
     */
    public boolean equals(Object verrattava) {
        if (verrattava == null) return false;
        if (verrattava.getClass() != this.getClass()) return false;
        
        Ohje verrattavaOhje = (Ohje)verrattava;
        if (!verrattavaOhje.ohjeistus.equals(this.ohjeistus)) return false;
        if (verrattavaOhje.vaihe != this.vaihe) return false;
        return true;
    }
    

    @Override
    /**
     * 
     * @example
     * <pre name="test">
     * Ohje ohje1 = new Ohje();
     * Ohje ohje2 = new Ohje();
     * 
     * ohje1.hashCode() == ohje2.hashCode() === true;
     * 
     * ohje1.setOhjeistus("Vatkaa kananmunat");
     * ohje1.hashCode() == ohje2.hashCode() === false;
     * 
     * ohje2.setOhjeistus("Vatkaa kananmunat");
     * ohje1.hashCode() == ohje2.hashCode() === true;
     * 
     * ohje2.setVaihe(12);
     * ohje1.hashCode() == ohje2.hashCode() === false;
     * 
     * ohje1.setVaihe(11);
     * ohje1.hashCode() == ohje2.hashCode() === false;
     * 
     * ohje1.setVaihe(12);
     * ohje1.hashCode() == ohje2.hashCode() === true;
     * </pre>
     */
    public int hashCode() {
        int hash = 1;
        hash = Hajautus.hajautus(hash, this.vaihe);
        hash = Hajautus.hajautus(hash, this.ohjeistus);
        return hash;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ohje ohje = new Ohje();
     * ohje.toString() === "-1|1|";
     * 
     * ohje = new Ohje("Lisää sokeri", 7);
     * ohje.toString() === "-1|7|Lisää sokeri";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.vaihe);
        sb.append('|');
        sb.append(this.ohjeistus);
        return sb.toString();
    }
    
    
    /**
     * Testipääohjelma
     * 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ohje ohje = new Ohje();
        System.out.println(ohje.getLuontilauseke());
    }
}
