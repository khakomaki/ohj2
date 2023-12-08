package fxKanta;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * @author hakom
 * @version 24 Nov 2023
 *
 * Luokka yleisiä javafx-node funktioita varten.
 */
public class NodeKasittely {

    /**
     * Luo sarakkeen rajoitukset annetuilla parametreilla
     * 
     * @param minimiLeveys pienin sarakkeen rajoitteet
     * @param leveydenKasvu milloin sarakkeen leveys halutaan kasvavan
     * @param ryhmitys miten sarakkeen sisältö ryhmitellään
     * @return sarakkeen rajoitteet
     */
    public static ColumnConstraints luoSarakeRajoitteet(int minimiLeveys, Priority leveydenKasvu, HPos ryhmitys) {
        ColumnConstraints rajoite = new ColumnConstraints();
        rajoite.setMinWidth(minimiLeveys);
        rajoite.setHgrow(leveydenKasvu);
        rajoite.setHalignment(ryhmitys);
        return rajoite;
    }
    
    
    /**
     * Luo sarakkeen rajoitukset annetuilla parametreilla
     * 
     * @param minimiKorkeus pienin sarakkeen rajoitteet
     * @param korkeudenKasvu milloin sarakkeen leveys halutaan kasvavan
     * @param ryhmitys miten sarakkeen sisältö ryhmitellään
     * @return sarakkeen rajoitteet
     */
    public static RowConstraints luoRiviRajoitteet(int minimiKorkeus, Priority korkeudenKasvu, VPos ryhmitys) {
        RowConstraints rajoite = new RowConstraints();
        rajoite.setMinHeight(minimiKorkeus);
        rajoite.setVgrow(korkeudenKasvu);
        rajoite.setValignment(ryhmitys);
        return rajoite;
    }
    
}
