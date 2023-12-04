package kanta;

/**
 * @author hakom
 * @version 18 Nov 2023
 *
 * Luokka yleisistä merkkijonon käsittelyyn liittyvistä funktioista.
 */
public class MerkkijonoKasittely {
    
    /**
     * Vaihtaa tiedoston tiedostopäätteen tai asettaa sellaisen.
     * Jos tiedostopäätteen jättää tyhjäksi, saa tiedoston nimen ilman tiedostopäätettä.
     * 
     * @param tiedostonimi tiedoston nimi tiedostopäätteellä tai ilman
     * @param tiedostopaate haluttu tiedoston pääte, ilman pistettä
     * @return tiedoston nimi, jossa on vaihdettu tai lisätty annettu tiedostopääte
     * 
     * @example
     * <pre name="test">
     * vaihdaTiedostopaate("tekstiä.txt", "exe") === "tekstiä.exe";
     * vaihdaTiedostopaate("tekstiä.txt", "") === "tekstiä";
     * vaihdaTiedostopaate("tekstiä.txt", null) === "tekstiä";
     * vaihdaTiedostopaate("tekstiä", "exe") === "tekstiä.exe";
     * vaihdaTiedostopaate("tekstiä", ".exe") === "tekstiä..exe";
     * vaihdaTiedostopaate("tekstiä", "..exe") === "tekstiä...exe";
     * vaihdaTiedostopaate("     tekstiä   ", "   exe      ") === "tekstiä.exe";
     * vaihdaTiedostopaate("tuotteet.tiedot.tekstiä.txt", "exe") === "tuotteet.tiedot.tekstiä.exe";
     * vaihdaTiedostopaate("tekstiä.txt", "oma.tiedosto.pääte") === "tekstiä.oma.tiedosto.pääte";
     * vaihdaTiedostopaate("", "exe") === ".exe";
     * vaihdaTiedostopaate(null, "exe") === null;
     * vaihdaTiedostopaate(null, null) === null;
     * vaihdaTiedostopaate("", "") === "";
     * </pre>
     */
    public static String vaihdaTiedostopaate(String tiedostonimi, String tiedostopaate) {
        if (tiedostonimi == null) return null;
        
        int viimeinenPiste = tiedostonimi.lastIndexOf('.');
        
        StringBuilder uusiTiedostonimi = new StringBuilder();
        
        // jos on valmiina tiedostopääte, jätetään se pois
        if (viimeinenPiste < 0) {
            // ei ole valmista tiedostopäätettä
            uusiTiedostonimi.append(tiedostonimi.strip());
        } else {
            // on valmis tiedostopääte
            uusiTiedostonimi.append(tiedostonimi.strip().substring(0, viimeinenPiste)); // viimeinen piste jää pois
        }
        
        // jos tiedostopääte on tyhjä, palautetaan tiedostonimi ilman pistettä ja päätettä
        if (tiedostopaate == null || tiedostopaate.length() <= 0) return uusiTiedostonimi.toString();
        
        // muuten lisätään piste ja uusi tiedostopääte
        uusiTiedostonimi.append('.');
        uusiTiedostonimi.append(tiedostopaate.strip());
        return uusiTiedostonimi.toString();
    }
    
}
