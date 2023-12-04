package reseptihaku;

import fi.jyu.mit.ohj2.Mjonot;
import kanta.Hajautus;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 * Osion ainesosat koostuvat ainesosista ja niiden määristä.
 */
public class OsionAinesosa {
    
    private String ainesosaNimi = "ainesosa";
    private String maara        = "";
    
    
    /**
     * Luo oletus ainesosan oletus nimellä ja määrällä
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa oletus = new OsionAinesosa();
     * oletus.toString() === "ainesosa|";
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
     * peruna.toString() === "peruna|1kg";
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
     * peruna.toString() === "peruna|1kg";
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
     * peruna.toString() === "peruna|1kg";
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
     * peruna.toString() === "peruna|1kg";
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
     * peruna.toString() === "peruna|1kg";
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
     * Parsii osion ainesosat 
     * 
     * @param rivi mistä parsitaan osion ainesosan tiedot
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa peruna = new OsionAinesosa();
     * peruna.parse("1|peruna|300g");
     * peruna.toString() === "peruna|300g";
     * 
     * peruna.parse(null);
     * peruna.toString() === "peruna|300g";
     * 
     * peruna.parse("  15   |  keltainen peruna    |  1kg     ");
     * peruna.toString() === "keltainen peruna|1kg";
     * 
     * peruna.parse("15|peruna 300g");
     * peruna.toString() === "peruna 300g|1kg";
     * 
     * peruna.parse("17 pataatti 280g");
     * peruna.toString() === "peruna 300g|1kg";
     * 
     * peruna.parse("|");
     * peruna.toString() === "peruna 300g|1kg";
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
     * juusto.toString() === "juusto|2kg";
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
        hash = Hajautus.hajautusString(hash, this.ainesosaNimi);
        hash = Hajautus.hajautusString(hash, this.maara);
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
     * mozzarella.toString() === "Mozzarella|120g";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.ainesosaNimi);
        sb.append('|');
        sb.append(this.maara);
        return sb.toString();
    }
}
