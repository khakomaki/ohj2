package reseptihaku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import kanta.Hajautus;
import kanta.Hallitsija;
import kanta.MerkkijonoKasittely;
import kanta.SailoException;
import kanta.VaihtoehtoAttribuutti;
import kanta.Validoi;

/**
 * @author hakom
 * @version 19 Oct 2023
 *
 * Reseptit hallitsee resepti-olioita.
 */
public class Reseptit implements Hallitsija<Resepti> {

    private List<Resepti> reseptit      = new ArrayList<Resepti>();
    private String tiedostonimi         = "reseptit.dat";
    private String tiedostopolku        = "reseptidata/";
    private int lkm                     = 0;
    
    private final static Resepti esimerkkiresepti 	= new Resepti();
    private Tietokanta tietokanta 					= null;
    
    /**
     * Reseptit.
     * Hallitsee yksittäisiä reseptejä.
     * 
     * Alustaa tallennustiedoston nimellä "reseptit.dat".
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit.dat";
     * </pre>
     */
    public Reseptit() {
        //
    }
    
    
    /**
     * Hallitsee tietokannassa sijaitsevia Resepti-olioita
     * 
     * @param tietokantaNimi tietokannan nimi
     * @throws SailoException jos tulee ongelmia
     */
    public Reseptit(String tietokantaNimi) throws SailoException {
        this.tietokanta = Tietokanta.alustaTietokanta(tietokantaNimi);
        
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys() ) {
            // haetaan tietokannan metadatasta omaa tietokantaa, luodaan jos ei ole
            DatabaseMetaData metadata = yhteys.getMetaData();
            
            try ( ResultSet taulu = metadata.getTables(null, null, "Reseptit", null)) {
                // luodaan uusi taulu jos ei voida siirtyä seuraavaan
                if ( !taulu.next() ) {
                    try ( PreparedStatement sql = yhteys.prepareStatement(esimerkkiresepti.getLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch ( SQLException exception ) {
            throw new SailoException("Ongelmia reseptien luonnissa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Asettaa tiedostonimen johon tallennetaan.
     * Ei tee muutoksia jos annettu nimi on tyhjä merkkijono tai null
     * 
     * @param tiedostonimi tiedostonimi johon kirjoitetaan tiedot
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.toString() === "0|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("");
     * reseptit.toString() === "0|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("salaiset_reseptit.txt");
     * reseptit.toString() === "0|salaiset_reseptit.txt";
     * 
     * reseptit.setTiedostoNimi(null);
     * reseptit.toString() === "0|salaiset_reseptit.txt";
     * </pre>
     */
    public void setTiedostoNimi(String tiedostonimi) {
        // varmistetaan ettei annettu tiedostonimi ole null tai tyhjä merkkijono
        if (tiedostonimi == null) return;
        if (tiedostonimi.length() < 1) return;
        this.tiedostonimi = tiedostonimi;
    }
    
    
    /**
     * Asettaa reseptin tallennus polun
     * 
     * @param tiedostopolku mihin reseptit tallennetaan
     * @throws SailoException jos polun asettaminen aiheuttaa ongelmia
     */
    public void setTiedostoPolku(String tiedostopolku) throws SailoException {
        // ei tee mitään jos null tai sama kuin oli
        if (tiedostopolku == null) return;
        
        this.tiedostopolku = tiedostopolku; // esim. reseptidata/
        
        // luo tiedostopolun siltä varalta että sitä ei ole
        File dir = new File(this.tiedostopolku);
        dir.mkdirs();
        
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
     * reseptit.toString() === "3|reseptit.dat";
     * 
     * reseptit.anna(0).equals(resepti1) === true
     * reseptit.anna(1).equals(resepti2) === true;
     * reseptit.anna(2).equals(resepti3) === true;
     * </pre>
     */
    public Resepti lisaa(String nimi) {
        Resepti resepti = new Resepti(nimi);
        reseptit.add(resepti);
        this.lkm++;
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
     * reseptit.toString() === "1|reseptit.dat";
     * 
     * reseptit.lisaa(new Resepti());
     * reseptit.toString() === "2|reseptit.dat";
     * </pre>
     */
    @Override
    public void lisaa(Resepti resepti) {
        if (resepti == null) return;
        Resepti lisattavaResepti = resepti;
        reseptit.add(lisattavaResepti);
        this.lkm++;
    }
    
    
    /**
     * Lisää reseptin
     * 
     * @param resepti lisättävä resepti
     * 
     * @throws SailoException jos lisäämisen kanssa tulee ongelmia
     */
    public void lisaaResepti(Resepti resepti) throws SailoException {
        
        // muodostaa yhteyden tietokantaan, pyytää reseptiltä lisäyslausekkeen ja suorittaa
        try ( Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = resepti.getLisayslauseke(yhteys) ) {
            sql.executeUpdate();
            
            // tarkistetaan saiko resepti uuden tunnuksen
            try ( ResultSet tulokset = sql.getGeneratedKeys() ) {
                resepti.tarkistaID(tulokset);
             }
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia lisäyksessä tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Palauttaa kaikki tietokannasta löytyvät reseptit
     * 
     * @return kokoelma resepteistä
     * @throws SailoException jos hakemisessa tulee ongelmia
     */
    public Collection<Resepti> get() throws SailoException {
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
            throw new SailoException("Ongelmia reseptien haussa tietokannan kanssa: " + exception.getMessage());
        }
    }
    
    
    /**
     * Poistaa annetun reseptin
     * 
     * @param resepti poistettava resepti
     * @throws SailoException jos poistamisessa ilmenee ongelmia
     */
    public void poistaResepti(Resepti resepti) throws SailoException {
        // muodostaa yhteyden tietokantaan, pyytää osiolta poistolausekkeen ja suorittaa
        try (Connection yhteys = this.tietokanta.annaTietokantaYhteys(); PreparedStatement sql = resepti.getPoistolauseke(yhteys) ) {
            sql.executeUpdate();
            
        } catch (SQLException exception) {
            throw new SailoException("Ongelmia poistossa tietokannan kanssa: " + exception.getMessage());
        }
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
     * reseptit.toString() === "3|reseptit.dat";
     * 
     * reseptit.anna(-1) == null === true;
     * reseptit.anna(0) == resepti1 === true;
     * reseptit.anna(1) == resepti2 === true;
     * reseptit.anna(2) == resepti3 === true;
     * </pre>
     */
    public Resepti anna(int indeksi) {
        if (indeksi < 0 || this.lkm < indeksi) return null;
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
        
        // varmistaa että uusi resepti käyttää samaa Ainesosat-viitettä kuin luokka
        Resepti vaihdettavaResepti = uusiResepti;
        
        // etsii vaihdettavan indeksin, poistuu jos ei löydy
        int vanhaReseptiIndeksi = this.reseptit.indexOf(vanhaResepti);
        if (vanhaReseptiIndeksi < 0) return;
        
        this.reseptit.set(vanhaReseptiIndeksi, vaihdettavaResepti);
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
        if (this.lkm < maara) tulostettavaMaara = this.lkm;
        
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
        tulostaReseptit(os, this.lkm);
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
     * reseptit.toString() === "3|reseptit.dat";
     * 
     * reseptit.poista(resepti2);
     * reseptit.toString() === "2|reseptit.dat";
     * 
     * reseptit.poista(null);
     * reseptit.toString() === "2|reseptit.dat";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "1|reseptit.dat";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "1|reseptit.dat";
     * 
     * reseptit.poista(resepti3);
     * reseptit.toString() === "0|reseptit.dat";
     * 
     * reseptit.poista(resepti1);
     * reseptit.toString() === "0|reseptit.dat";
     * </pre>
     */
    @Override
    public void poista(Resepti resepti) {
        int poistettavanIndeksi = reseptit.indexOf(resepti);
        
        // poistutaan jos poistettavaa ei löydetty
        if (poistettavanIndeksi < 0) return;
        
        this.reseptit.remove(poistettavanIndeksi);
        this.lkm--;
    }
    
    
    /**
     * Palauttaa hakusanaa vastaavat reseptit.
     * Jos hakusana on tyhjä, palauttaa kaikki reseptit.
     * 
     * @param hakusana millä hakusanalla haetaan kaikki reseptit jotka sisältävät kyseisen sanan
     * @return kaikki reseptit jotka sisältävät hakusanan
     */
    public List<Resepti> etsiNimella(String hakusana) {
        // TODO listan palauttamisen sijaan esim. Stream
        List<Resepti> loydetytReseptit = new ArrayList<Resepti>();
        String kaytettavaHakusana = hakusana.strip();
        
        // tyhjällä hakusanalla palautetaan kaikki reseptit
        if (kaytettavaHakusana.isBlank()) return anna();
        
        for (Resepti resepti : this.reseptit) {
            if (resepti.onkoNimessa(kaytettavaHakusana)) loydetytReseptit.add(resepti);
        }
        
        return loydetytReseptit;
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
     */
    public List<Resepti> etsiNimella(
            String hakusana, 
            List<VaihtoehtoAttribuutti> minimit, 
            List<VaihtoehtoAttribuutti> maksimit, 
            String lajitteluPeruste,
            boolean kaanteinenJarjestys
        ) {
        
        // TODO listan palauttamisen sijaan esim. Stream
        List<Resepti> loydetytReseptit = new ArrayList<Resepti>();
        
        // käydään reseptit läpi
        for (Resepti resepti : this.reseptit) {
            if (!resepti.onkoNimessa(hakusana)) continue;
            List<VaihtoehtoAttribuutti> reseptinAttribuutit = resepti.getAttribuutit();
            boolean suodataPois = false;
            
            // minimit
            for (int i = 0; i < minimit.size(); i++) {
                VaihtoehtoAttribuutti attribuutti = reseptinAttribuutit.get(i);
                int minimi = minimit.get(i).getValinta();
                
                // skipataan käyttöliittymän oletusvalinnat
                if (attribuutti.onkoOletusValinta(minimi)) continue;
                
                // laitetaan suodattumaan pois jos ei täsmää tai on oletusvalinta
                if (attribuutti.getValinta() < minimi || attribuutti.onkoOletusValinta(attribuutti.getValinta())) suodataPois = true;
            }
            
            // maksimit
            for (int i = 0; i < maksimit.size(); i++) {
                VaihtoehtoAttribuutti attribuutti = reseptinAttribuutit.get(i);
                int maksimi = maksimit.get(i).getValinta();
                
                // skipataan käyttöliittymän oletusvalinnat
                if (attribuutti.onkoOletusValinta(maksimi)) continue;
                
                // laitetaan suodattumaan pois jos ei täsmää tai on oletusvalinta
                if (maksimi < attribuutti.getValinta() || attribuutti.onkoOletusValinta(attribuutti.getValinta())) suodataPois = true;
            }
            
            // pääsee lisäämään vain jos kaikki ehdot täyttyvät
            if (suodataPois) continue; 
            loydetytReseptit.add(resepti);
        }
        
        // tulosten lajittelu
        boolean lajiteltu = false;
        List<VaihtoehtoAttribuutti> attribuutit = Reseptit.getOletusAttribuutit();
        
        for (int i = 0; i < attribuutit.size(); i++) {
            // suoritetaan vertailu jos vastaa nimeä
            if (attribuutit.get(i).onkoNimi(lajitteluPeruste)) {
                // vertailija
                Comparator<Resepti> vertailija = new VertaileAttribuutteja(i);
                if (kaanteinenJarjestys) vertailija = vertailija.reversed(); // käänteinen
                Collections.sort(loydetytReseptit, vertailija);
                
                lajiteltu = true;
                break;
            }
        }
        
        // jos attribuuteista ei löytynyt lajittelu perustetta, lajitellaan nimellä
        if (!lajiteltu) {
            Comparator<Resepti> vertailija = new VertaileNimia();
            if (kaanteinenJarjestys) vertailija = vertailija.reversed(); // käänteinen
            Collections.sort(loydetytReseptit, vertailija);
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
     */
    public static List<VaihtoehtoAttribuutti> getOletusAttribuutit() {
        return esimerkkiresepti.getAttribuutit();
    }
    
    
    /**
     * Validoi voidaanko resepti tallentaa
     * 
     * @return virheteksti tai null jos voidaan tallentaa
     */
    public String voidaankoTallentaa() {
        // samoja reseptin nimiä varten
        List<String> reseptiNimet = new ArrayList<String>();
        
        // kysytään resepteiltä
        for (Resepti resepti : this.reseptit) {
            String virhe = resepti.voidaankoTallentaa();
            if (virhe != null) return virhe;
            reseptiNimet.add(resepti.getNimi());
        }
        if (Validoi.onkoDuplikaatteja(reseptiNimet)) return "Sama reseptin nimi useaan kertaan!";
        
        return null;
    }
    
    
    /**
     * Lukee Reseptien tiedot tiedostosta
     * 
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        try (Scanner fi = new Scanner(new FileInputStream(new File(this.tiedostopolku + this.tiedostonimi)))) {
            while (fi.hasNext()) {
                String rivi = fi.nextLine().strip();
                
                // skipataan tyhjät ja kommenttirivit
                if (rivi.length() < 0 || rivi.charAt(0) == ';') continue;
                
                // käsketään reseptiä parsimaan tiedot ja lisätään nykyisiin resepteihin
                Resepti resepti = new Resepti();
                resepti.parse(rivi);
                lisaa(resepti);
                
                // käsketään reseptin lukemaan tietonsa
                resepti.setTiedostopolku(this.tiedostopolku);
                resepti.lueTiedostosta();
            }
            
        } catch (FileNotFoundException exception) {
            throw new SailoException("Tiedostoa \"" + this.tiedostopolku + this.tiedostonimi + "\" ei saada avattua");
        }
    }
    
    
    /**
     * Tallentaa reseptien tiedot tiedostoon
     * 
     * @throws SailoException jos tallentaminen epäonnistuu
     */
    public void tallenna() throws SailoException {
        
        // tarkitestaan voidaanko tallentaa
        String virhe = voidaankoTallentaa();
        if (virhe != null) throw new SailoException("Ei voida tallentaa: " + virhe);
        
        // vaihdetaan nykyinen tiedosto varmuuskopioksi
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
        
        boolean tallennustiedostoLuotu = false;
        for (Resepti resepti : this.reseptit) {
            resepti.tallenna();
            tallennustiedostoLuotu = true;
        }
        
        // luodaan tyhjä tallennustiedosto jos ei ollut yhtään reseptiä joka loisi tiedoston
        if (!tallennustiedostoLuotu) {
            try {
                tiedosto.createNewFile();
            } catch (IOException exception) {
                throw new SailoException("Ei voida luoda tallennus-tiedostoa");
            }
        }
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
    
    
    @Override
    /**
     * Luo täydellisen kopion itsestänsä
     * 
     * @example
     * <pre name="test">
     * Reseptit reseptit = new Reseptit();
     * reseptit.lisaa(new Resepti("Mustikkapiirakka"));
     * reseptit.lisaa(new Resepti("Juustokakku"));
     * reseptit.toString() === "2|reseptit.dat";
     * 
     * Reseptit reseptitKopio = reseptit.clone();
     * reseptitKopio.toString() === "2|reseptit.dat";
     * 
     * reseptit.setTiedostoNimi("herkut.txt");
     * reseptit.toString() === "2|herkut.txt";
     * reseptitKopio.toString() === "2|reseptit.dat";
     * </pre>
     */
    public Reseptit clone() {
        Reseptit kopio = new Reseptit();
        kopio.lkm = this.lkm;
        kopio.tiedostonimi = this.tiedostonimi;
        
        // kopioidaan kaikki yksittäiset reseptit kopioon
        for (int i = 0; i < this.lkm; i++) {
            kopio.reseptit.add(this.reseptit.get(i).clone());
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
        
        if (verrattavaReseptit.lkm != this.lkm) return false;
        if (!verrattavaReseptit.tiedostonimi.equals(this.tiedostonimi)) return false;
        
        // verrataan yksittäisiä reseptejä toisiinsa
        for (int i = 0; i < this.lkm; i++) {
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
        hash = Hajautus.hajautus(hash, this.lkm);
        
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
     * reseptit.toString() === "0|reseptit.dat";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.lkm);
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
    	try {
			Reseptit reseptit = new Reseptit("testidb");
			
			Resepti resepti1 = new Resepti("Mansikkakakku");
			Resepti resepti2 = new Resepti("Juustokakku");
			reseptit.lisaaResepti(resepti1);
			reseptit.lisaaResepti(resepti2);
			System.out.println(reseptit.get());
			
			reseptit.poistaResepti(resepti1);
			System.out.println(reseptit.get());
			
		} catch (SailoException exception) {
			System.err.println(exception.getMessage());
		}
    	
    	/*
        Reseptit reseptit = new Reseptit();
        try {
            reseptit.setTiedostoPolku("lisääReseptejä/");
        } catch (SailoException e) {
            e.printStackTrace();
        }
        
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
        } catch (SailoException e) {
            e.printStackTrace();
        }
        */
    }
}
