package fxKanta;

import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;

/**
 * @author hakom
 * @version 24 Nov 2023
 *
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
    
}
