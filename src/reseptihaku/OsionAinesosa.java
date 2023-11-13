package reseptihaku;

import kanta.Hajautus;

/**
 * @author hakom
 * @version 15 Oct 2023
 *
 */
public class OsionAinesosa {
    
    private int ainesosa_id     = 0;
    private String maara        = "";
    
    
    /**
     * Luo osion ainesosan
     * 
     * @param ainesosa_id osion ainesosan tunnus
     * @param maara osion ainesosan määrä
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa osionAinesosa = new OsionAinesosa(7, "5dl");
     * osionAinesosa.toString() === "7|5dl";
     * </pre>
     */
    public OsionAinesosa(int ainesosa_id, String maara) {
        this.ainesosa_id = ainesosa_id;
        this.maara = maara;
    }
    
    
    /**
     * Luo osion ainesosan
     * 
     * @param ainesosa_id osion ainesosan tunnus
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa osionAinesosa = new OsionAinesosa(7);
     * osionAinesosa.toString() === "7|";
     * </pre>
     */
    public OsionAinesosa(int ainesosa_id) {
        this.ainesosa_id = ainesosa_id;
    }
    
    
    /**
     * Palauttaa osion ainesosan tunnuksen
     * 
     * @return ainesosan tunnus
     */
    public int getId() {
        return this.ainesosa_id;
    }
    
    
    /**
     * palauttaa osion ainesosan määrän
     * 
     * @return ainesosan määrä
     */
    public String getMaara() {
        return this.maara;
    }
    
    
    /**
     * Asettaa osion kyseisen ainesosan määrän
     * 
     * @param maara asetettava määrä
     */
    public void setMaara(String maara) {
        if (maara == null) { return; }
        this.maara = maara;
    }
    
    
    @Override
    /**
     * Luo täydellisen kopion osion ainesosasta
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa juusto = new OsionAinesosa(200, "2kg");
     * juusto.toString() === "200|2kg";
     * 
     * OsionAinesosa juustoKopio = juusto.clone();
     * juustoKopio.toString().equals(juusto.toString()) === true;
     * 
     * juustoKopio.setMaara("3kg");
     * juustoKopio.toString().equals(juusto.toString()) === false;
     * </pre>
     */
    public OsionAinesosa clone() {
        OsionAinesosa kopio = new OsionAinesosa(0);
        kopio.ainesosa_id = this.ainesosa_id;
        kopio.maara = this.maara;
        return kopio;
    }
    
    
    @Override
    /**
     * Muodostaa hash-koodin osion ainesosalle
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa juusto1 = new OsionAinesosa(200, "2kg");
     * OsionAinesosa juusto2 = new OsionAinesosa(201, "2kg");
     * OsionAinesosa juusto3 = new OsionAinesosa(200, "3kg");
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
        hash = Hajautus.hajautusInt(hash, this.ainesosa_id);
        hash = Hajautus.hajautusString(hash, this.maara);
        return hash;
    }
    
    
    @Override
    /**
     * Vertailee onko olio sama kuin itse
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa juusto1 = new OsionAinesosa(200, "2kg");
     * OsionAinesosa juusto2 = new OsionAinesosa(201, "2kg");
     * OsionAinesosa juusto3 = new OsionAinesosa(200, "3kg");
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
        if (verrattava == null) { return false; }
        if (verrattava.getClass() != this.getClass()) { return false; }
        
        OsionAinesosa verrattavaOA = (OsionAinesosa)verrattava;
        if (verrattavaOA.ainesosa_id != this.ainesosa_id) { return false; }
        if (verrattavaOA.maara != this.maara) { return false; }
        
        return true;
    }
    
    
    @Override
    /**
     * Palauttaa tietonsa merkkijonona
     * 
     * @example
     * <pre name="test">
     * OsionAinesosa mozzarella = new OsionAinesosa(20501, "120g");
     * mozzarella.toString() === "20501|120g";
     * </pre>
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.ainesosa_id);
        sb.append('|');
        sb.append(this.maara);
        return sb.toString();
    }
}
