package reseptihaku;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import kanta.Hajautus;
import kanta.Hallitsija;
import kanta.MerkkijonoKasittely;
import kanta.SailoException;

/**
 * @author hakom
 * @version 30 Oct 2023
 *
 * Ohjeet hallitsee ohje-olioita.
 */
public class Ohjeet implements Hallitsija<Ohje> {
    /**
     * Alustukset testejä varten
     * 
     * @example
     * <pre name="testJAVA">
     * private Ohjeet kaikkiOhjeet;
     * private String tiedNimi;
     * private File tiedosto;
     * 
     * @Before
     * public void alusta() throws SailoException {
     *     tiedNimi = "testiOhjeet";
     *     tiedosto = new File(tiedNimi + ".db");
     *     tiedosto.delete();
     *     kaikkiOhjeet = new Ohjeet(1, tiedNimi);
     * }
     * 
     * @After
     * public void siivoa() {
     *     tiedosto.delete();
     * }
     * </pre>
     */
    
    
    private String tiedostonimi     = "ohjeet.dat";
    private String tiedostopolku    = "reseptidata/Reseptin nimi/Osion nimi/";
    private int osioId              = -1;
    private int lkm                 = 0;
    private List<Ohje> ohjeet       = new ArrayList<Ohje>();
    private boolean muutettu        = false;
    
    private List<Ohje> ohjeidenOhjeet   = new ArrayList<Ohje>();
    private static Ohje esimerkkiOhje   = new Ohje();
    private Tietokanta tietokanta       = null;
    private int vaihe                   = 1;
    
    
    /**
     * Hallitsee Ohje-oliota
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.toString() === "ohjeet.dat|-1|0";
     * </pre>
     */
    public Ohjeet() {
        //
    }
    
    
    /**
     * Hallitsee Ohje-oliota
     * 
     * @param osioId osion tunnus, johon ohjeet kuuluu
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.toString() === "ohjeet.dat|1|0";
     * </pre>
     */
    public Ohjeet(int osioId) {
        this.osioId = osioId;
    }
    
    
    /**
     * Hallitsee tietokannassa sijaitsevia Ohje-olioita
     * 
     * @param osioId osion tunnus
     * @param tietokantaNimi tietokannan nimi
     * @throws SailoException jos tulee ongelmia
     */
    public Ohjeet(int osioId, String tietokantaNimi) throws SailoException {
        this.osioId = osioId;
        this.tietokanta = Tietokanta.alustaTietokanta(tietokantaNimi);
        
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
            
        } catch ( SQLException exception ) {
            throw new SailoException("Ongelmia ohjeiden luonnissa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Asettaa osion tunnuksen johon ohjeet kuuluu
     * 
     * @param id osion tunnus, johon ohjeet kuuluu
     */
    public void setOsioId(int id) {
        this.osioId = id;
    }
    
    
    /**
     * Lisää ohjeen ohjeisiin.
     * 
     * @param ohje lisättävä ohje
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(15);
     * ohjeet.toString() === "ohjeet.dat|15|0";
     * 
     * Ohje ohje1 = new Ohje("ensimmäinen", 100);
     * ohje1.getVaihe() === 100;
     * ohjeet.lisaa(ohje1);
     * ohje1.getVaihe() === 1;
     * ohjeet.toString() === "ohjeet.dat|15|1";
     * 
     * Ohje ohje2 = new Ohje("toinen", 75);
     * ohje2.getVaihe() === 75;
     * ohjeet.lisaa(ohje2);
     * ohje2.getVaihe() === 2;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet.dat|15|4";
     * 
     * ohjeet.lisaa(null);
     * ohjeet.toString() === "ohjeet.dat|15|5";
     * </pre>
     */
    @Override
    public void lisaa(Ohje ohje) {
        Ohje lisattavaOhje = ohje;
        
        // luo uuden ohjeen jos annetaan null
        if (ohje == null) lisattavaOhje = new Ohje();
        this.ohjeet.add(lisattavaOhje);
        this.lkm++;
        lisattavaOhje.setVaihe(this.lkm);
        
        this.muutettu = true;
    }
    
    
    /**
     * Lisää ohjeen
     * 
     * @param ohje lisättävä ohje
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
     * Collection<Ohje> loytyneetOhjeet = kaikkiOhjeet.get();
     * loytyneetOhjeet.size() === 0;
     * 
     * Ohje ohje1 = new Ohje();
     * Ohje ohje2 = new Ohje();
     * 
     * kaikkiOhjeet.lisaa(ohje1);
     * kaikkiOhjeet.lisaa(ohje2);
     * loytyneetOhjeet = kaikkiOhjeet.get();
     * loytyneetOhjeet.size() === 0;
     * </pre>
     */
    public void lisaaOhje(Ohje ohje) throws SailoException {
        // asetetaan lisättävälle ohjeelle sama osiotunnus
        ohje.setOsioId(this.osioId);
        ohje.setVaihe(this.vaihe++);
        
        // muodostaa yhteyden tietokantaan, pyytää ohjeelta lisäyslausekkeen ja suorittaa
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = ohje.getLisayslauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia lisäyksessä tietokannan kanssa: " + exception.getMessage());
        }
        
        this.ohjeidenOhjeet.add(ohje);
    }
    
    
    /**
     * Palauttaa kaikki tietokannasta löytyvät omat ohjeet
     * 
     * @return kokoelma luokan ohjeista
     * @throws SailoException jos hakemisessa tulee ongelmia
     */
    public Collection<Ohje> get() throws SailoException {
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
            throw new SailoException("Ongelmia ohjeiden haussa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa annetun ohjeen ja päivittää tarvittavasti muiden ohjeiden vaiheita
     * 
     * @param ohje poistettava ohje
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaOhje(Ohje ohje) throws SailoException {
        // muodostaa yhteyden tietokantaan, pyytää ohjeelta poistolausekkeen ja suorittaa
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = ohje.getPoistolauseke(yhteys) ) {
            
            // poistettava vaihe muistiin ja poistetaan ohje
            int poistettavaVaihe = ohje.getVaihe();
            sql.executeUpdate();
            
            // päivitetään tarvittavia vaiheita -1 pienemmiksi
            try (PreparedStatement sqlVaihepaivitys = yhteys.prepareStatement("UPDATE Ohjeet SET vaihe = vaihe - 1 WHERE osio_id = ? AND vaihe > ?")) {
                sqlVaihepaivitys.setInt(1, this.osioId);
                sqlVaihepaivitys.setInt(2, poistettavaVaihe);
                sqlVaihepaivitys.executeUpdate();
            }
            
            for (int i = poistettavaVaihe; i < this.ohjeidenOhjeet.size(); i++) {
                this.ohjeidenOhjeet.get(i).setVaihe(this.ohjeidenOhjeet.get(i).getVaihe() - 1);
            }
            this.vaihe--;
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia poistossa tietokannan kanssa: " + exception.getMessage());
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
     * 
     * @param indeksi mistä indeksistä poistetaan
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(2);
     * ohjeet.toString() === "ohjeet.dat|2|0";
     * 
     * ohjeet.poista(-2);
     * ohjeet.poista(0);
     * ohjeet.poista(100);
     * ohjeet.toString() === "ohjeet.dat|2|0";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "ohjeet.dat|2|1";
     * 
     * ohjeet.poista(0);
     * ohjeet.toString() === "ohjeet.dat|2|0";
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
        if (indeksi < 0 || lkm <= indeksi) return;
        
        ohjeet.remove(indeksi);
        this.lkm--;
        paivitaVaiheet(indeksi);
        
        this.muutettu = true;
    }
    
    
    /**
     * @param poistettava mikä ohje poistetaan
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * Ohje ohje1 = new Ohje("ohje 1"); ohjeet.lisaa(ohje1);
     * Ohje ohje2 = new Ohje("ohje 2"); ohjeet.lisaa(ohje2);
     * Ohje ohje3 = new Ohje("ohje 3"); ohjeet.lisaa(ohje3);
     * Ohje ohje4 = new Ohje("ohje 4"); ohjeet.lisaa(ohje4);
     * Ohje ohje5 = new Ohje("ohje 5"); ohjeet.lisaa(ohje5);
     * ohjeet.toString() === "ohjeet.dat|-1|5";
     * 
     * ohjeet.anna(1).equals(ohje2) === true;
     * ohje2.getVaihe() === 2;
     * 
     * ohjeet.poista(ohje2);
     * ohjeet.toString() === "ohjeet.dat|-1|4";
     * ohjeet.anna(1).equals(ohje2) === false;
     * 
     * ohjeet.poista(new Ohje("ohje 5"));
     * ohjeet.toString() === "ohjeet.dat|-1|4";
     * 
     * ohjeet.poista(new Ohje("ohje 5", 4));
     * ohjeet.toString() === "ohjeet.dat|-1|3";
     * 
     * ohjeet.poista(ohje2);
     * ohjeet.toString() === "ohjeet.dat|-1|3";
     * </pre>
     */
    @Override
    public void poista(Ohje poistettava) {
        for (int i = 0; i < this.lkm; i++) {
            if (this.ohjeet.get(i).equals(poistettava)) {
                ohjeet.remove(i);
                this.lkm--;
                paivitaVaiheet(i);
                this.muutettu = true;
                break;
            }
        }
    }
    
    
    /**
     * @param indeksi mistä indeksistä halutaan ohje
     * @return indeksistä löytyvä ohje tai null
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.anna(0) === null;
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.anna(0) == null === false;
     * </pre>
     */
    public Ohje anna(int indeksi) {
        if (indeksi < 0 || this.lkm <= indeksi) return null;
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
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.toString() === "ohjeet.dat|-1|0";
     * 
     * ohjeet.setTiedostoNimi("ohjeistukset.txt");
     * ohjeet.toString() === "ohjeistukset.txt|-1|0";
     * 
     * ohjeet.setTiedostoNimi("");
     * ohjeet.toString() === "ohjeistukset.txt|-1|0";
     * 
     * ohjeet.setTiedostoNimi(null);
     * ohjeet.toString() === "ohjeistukset.txt|-1|0";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostonimi = tiedostonimi;
        
        this.muutettu = true;
    }
    
    
    /**
     * @return tiedoston nimi johon tallennetaan, null jos ei ole määritetty
     * 
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet();
     * ohjeet.getTiedostonimi() === "ohjeet.dat";
     * 
     * ohjeet.setTiedostoNimi("uudet_ohjeet.txt");
     * ohjeet.getTiedostonimi() === "uudet_ohjeet.txt";
     * </pre>
     */
    public String getTiedostonimi() {
        return this.tiedostonimi;
    }
    
    
    /**
     * Asettaa tiedostopolun
     * 
     * @param tiedostopolku mihin polkuun tiedosto tallennetaan
     */
    public void setTiedostoPolku(String tiedostopolku) {
        // ei tee mitään jos null tai sama kuin oli
        if (tiedostopolku == null) return;

        this.tiedostopolku = tiedostopolku; // esim. reseptidata/Mustikkapiirakka/Muropohja/
        this.muutettu = true;
    }
    
    
    /**
     * Kertoo onko ohjeisiin tullut tallentamattomia muutoksia
     * 
     * @return onko tallentamattomia muutoksia
     */
    public boolean onkoTallentamattomiaMuutoksia() {
        return this.muutettu;
    }
    
    
    /**
     * Lukee ohjeet tiedostosta
     * 
     * @throws SailoException jos tiedoston lukeminen epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        
        try (Scanner fi = new Scanner(new FileInputStream(new File(this.tiedostopolku + this.tiedostonimi)))) {
            while (fi.hasNext()) {
                String rivi = fi.nextLine().strip();
                
                // skipataan tyhjät ja kommenttirivit
                if (rivi.length() < 0 || rivi.charAt(0) == ';') continue;
                
                // käsketään ohjeen parsimaan tiedot ja lisätään ohjeisiin
                Ohje ohje = new Ohje();
                ohje.parse(rivi);
                lisaa(ohje);
            }
            
            this.muutettu = false;
            
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
        }
    }
    
    
    /**
     * Tallentaa tiedostoon ohjeiden muutokset.
     * Vaihtaa nykyisen tallennustiedoston varmuuskopioksi.
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
            // ohjeiden tiedot kirjoittajaan
            for (Ohje ohje : this.ohjeet) {
                fo.print(this.osioId);
                fo.print('|');
                fo.println(ohje);
            }
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
        } catch (IOException exception) {
            throw new SailoException("Tiedostoon \"" + this.tiedostopolku + this.tiedostonimi + "\" kirjoittamisessa ongelma");
        }
        
        this.muutettu = false;
    }
    
    
    /**
     * @return ohjeiden lukumäärä
     * 
     * @example
     * <pre name="test">
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
        return this.lkm;
    }
    
    
    /**
     * Päivittää ohjeiden sisältämien ohjeiden vaihe numerot vastaamaan
     * taulukon järjestystä. Alkavat numerosta annetusta indeksista.
     * 
     * @example
     * <pre name="test">
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
        
        for (int i = indeksista; i < this.lkm; i++) {
            Ohje ohje = this.ohjeet.get(i);
            ohje.setVaihe(i + 1);
        }
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     */
    public void tulostaOhjeet(OutputStream os) {
        PrintStream out = new PrintStream(os);
        for (int i = 0; i < this.lkm; i++) {
            // vaihe numero
            out.print(i + 1);
            out.print(" ");
            out.print(this.ohjeet.get(i).getOhjeistus());
            out.print("\n");
        }
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
     * Ohjeet ohjeet = new Ohjeet(2);
     * ohjeet.setTiedostoNimi("mustikkapiirakka.txt");
     * ohjeet.lisaa(new Ohje("Sekoita sokeri ja vehnäjauhot"));
     * ohjeet.lisaa(new Ohje("Lisää kananmuna"));
     * ohjeet.toString() === "mustikkapiirakka.txt|2|2";
     * 
     * Ohjeet kopioOhjeet = ohjeet.clone();
     * kopioOhjeet.toString() === "mustikkapiirakka.txt|2|2";
     * kopioOhjeet.anna(0).getOhjeistus() === "Sekoita sokeri ja vehnäjauhot";
     * kopioOhjeet.anna(1).getOhjeistus() === "Lisää kananmuna";
     * 
     * ohjeet.anna(1).setOhjeistus("Lisää vanilliinisokeri");
     * ohjeet.anna(1).getOhjeistus() === "Lisää vanilliinisokeri";
     * kopioOhjeet.anna(1).getOhjeistus() === "Lisää kananmuna";
     * 
     * ohjeet.lisaa(new Ohje());
     * ohjeet.lisaa(new Ohje());
     * ohjeet.toString() === "mustikkapiirakka.txt|2|4";
     * kopioOhjeet.toString() === "mustikkapiirakka.txt|2|2";
     * </pre>
     */
    public Ohjeet clone() {
        Ohjeet kopio = new Ohjeet();
        kopio.lkm = this.lkm;
        kopio.osioId = this.osioId;
        kopio.tiedostonimi = this.tiedostonimi;
        
        for (int i = 0; i < this.lkm; i++) {
            kopio.ohjeet.add(this.ohjeet.get(i).clone());
        }
        
        return kopio;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
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
        if (verrattavaOhjeet.lkm != this.lkm) return false;
        if (verrattavaOhjeet.osioId != this.osioId) return false;
        if (!verrattavaOhjeet.tiedostonimi.equals(this.tiedostonimi)) return false;
        
        // verrataan yksittäiset ohjeet
        for (int i = 0; i < this.lkm; i++) {
            if (!verrattavaOhjeet.ohjeet.get(i).equals(this.ohjeet.get(i))) return false;
        }
        
        return true;
    }
    
    
    @Override
    /**
     * @example
     * <pre name="test">
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
        hash = Hajautus.hajautus(hash, this.tiedostonimi);
        
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
     * Ohjeet ohjeet = new Ohjeet(1);
     * ohjeet.toString() === "ohjeet.dat|1|0";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.tiedostonimi);
        sb.append('|');
        sb.append(this.osioId);
        sb.append('|');
        sb.append(this.lkm);
        return sb.toString();
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        try {
            Ohjeet ohjeet = new Ohjeet(1, "testiohjeet");
            System.out.println(ohjeet.get());
            
            Ohje ohje1 = new Ohje("Lisää kananmunat");
            Ohje ohje2 = new Ohje("Lisää sokeri");
            Ohje ohje3 = new Ohje("Sekoita");
            Ohje ohje4 = new Ohje("Maista");
            Ohje ohje5 = new Ohje("Lisää mausteet");
            ohjeet.lisaaOhje(ohje1);
            ohjeet.lisaaOhje(ohje2);
            ohjeet.lisaaOhje(ohje3);
            System.out.println(ohjeet.get());
            
            ohjeet.poistaOhje(ohje2);
            System.out.println(ohjeet.get());
            
            ohjeet.poistaOhje(ohje1);
            System.out.println(ohjeet.get());
            
            ohjeet.lisaaOhje(ohje4);
            System.out.println(ohjeet.get());
            
            ohjeet.poistaOhje(ohje3);
            System.out.println(ohjeet.get());
            
            ohjeet.poistaOhje(ohje4);
            System.out.println(ohjeet.get());
            
            ohjeet.lisaaOhje(ohje5);
            System.out.println(ohjeet.get());
            
        } catch (SailoException e) {
            e.printStackTrace();
        }
        
        /*
        Ohjeet ohjeet = new Ohjeet(5);
        ohjeet.setTiedostoNimi("ohjeet.txt");
        
        Ohje ohje1 = new Ohje("Ripottele mustikat piirakkapohjan päälle");
        Ohje ohje2 = new Ohje("Sekoita sokeri, kermaviili ja kananmuna");
        Ohje ohje3 = new Ohje("Lisää vanilliinisokeri");
        
        ohjeet.lisaa(ohje1);
        ohjeet.lisaa(ohje2);
        ohjeet.lisaa(ohje3);
        
        System.out.println(ohjeet.toString());
        ohjeet.tulostaOhjeet(System.out);
        
        ohjeet.poista(0);
        System.out.println();
        
        System.out.println("osio 5 ohjeet: " + ohjeet);
        ohjeet.tulostaOhjeet(System.out);
        
        
        Ohjeet toisetOhjeet = new Ohjeet(4);
        toisetOhjeet.setTiedostoNimi("ohjeet.txt");
        toisetOhjeet.lisaa(new Ohje("Sekoita sokeri ja pehmeä voi"));
        toisetOhjeet.lisaa(new Ohje("Lisää kananmuna ja vaahdota"));
        toisetOhjeet.lisaa(new Ohje("Lisää leivinjauho vehnäjauhoihin"));
        toisetOhjeet.lisaa(new Ohje("Sekoita jauhot vaahtoon"));
        toisetOhjeet.lisaa(new Ohje("Painele taikina piirakkavuokaan"));
        System.out.println();
        System.out.println("osio 4 ohjeet: " + toisetOhjeet);
        toisetOhjeet.tulostaOhjeet(System.out);
        
        
        // tallentaa
        try {
            toisetOhjeet.tallenna();
            ohjeet.tallenna();
        } catch (SailoException exception) {
            System.out.println(exception.getMessage());
        }
        
        // lukee tallennetusta tiedostosta
        Ohjeet ohjeetTiedostosta = new Ohjeet(5);
        ohjeetTiedostosta.setTiedostoNimi("ohjeet.txt");
        
        Ohjeet ohjeetTiedostosta2 = new Ohjeet(4);
        ohjeetTiedostosta2.setTiedostoNimi("ohjeet.txt");
        
        try {
            ohjeetTiedostosta.lueTiedostosta();
            ohjeetTiedostosta2.lueTiedostosta();
        } catch (SailoException exception) {
            System.out.println(exception.getMessage());
        }
        
        System.out.println();
        System.out.println("osio 5 ohjeet tiedostosta: " + ohjeetTiedostosta);
        ohjeetTiedostosta.tulostaOhjeet(System.out);
        
        System.out.println();
        
        System.out.println("osio 4 ohjeet tiedostosta: " + ohjeetTiedostosta2);
        ohjeetTiedostosta2.tulostaOhjeet(System.out);
        */
    }
}
