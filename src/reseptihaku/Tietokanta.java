package reseptihaku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author hakom
 * @version 12 Dec 2023
 *
 * Luokka SQLite-yhteyden muodostamista varten
 * 
 * <pre>
 * Tietokanta kanta = Tietokanta.alustaKanta("reseptit");
 * 
 * try ( Connection yhteys = kanta.annaTietokantaYhteys() ) {
 *    // yhteyden aikana tehtävät asiat
 * }
 * </pre>
 */
public class Tietokanta {
    
    private static HashMap<String, Tietokanta> tietokannat = new HashMap<String, Tietokanta>();
    private String tiedostoIlmanPaatetta = "";
    
    
    private Tietokanta(String nimi) {
        setTiedosto(nimi);
    }
    
    
    /**
     * Alustaa yhteyden tietokantaan
     * 
     * @param nimi tietokannan nimi
     * @return tietokannan tiedot
     */
    public static Tietokanta alustaTietokanta(String nimi) {
        Tietokanta tietokanta = tietokannat.get(nimi);
        if ( tietokanta != null ) return tietokanta;
        
        Tietokanta uusitietokanta = new Tietokanta(nimi);
        tietokannat.put(nimi, uusitietokanta);
        return uusitietokanta;
    }
    
    
    /**
     * Asettaa SQLite:n käyttämän tiedoston nimen
     * 
     * @param nimi tallennustiedoston nimi ilman päätettä
     */
    public void setTiedosto(String nimi) {
        this.tiedostoIlmanPaatetta = nimi;
    }
    
    
    /**
     * Antaa SQLiten käyttämän tallennustiedoston nimen
     * 
     * @return tiedoston nimi tiedostopäätteellä
     */
    public String getTiedostoNimi() {
        return this.tiedostoIlmanPaatetta + ".db";
    }
    
    
    /**
     * Avaa tietokantayhteyden
     * 
     * @return avattu yhteys
     * @throws SQLException jos yhteyden avaamisessa ilmenee ongelmia
     */
    public Connection annaTietokantaYhteys() throws SQLException {
        String driver = "org.sqlite.JDBC";
        
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException exception) {
            System.err.println("Virhe luokan " + driver + " lataamisessa: " + exception.getMessage());
        }
        
        return DriverManager.getConnection("jdbc:sqlite:" + getTiedostoNimi());
    }
    
}
