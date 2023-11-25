package fxKanta;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import kanta.Hallitsija;

/**
 * @author hakom
 * @version 24 Nov 2023
 * @param <T> minkä tyyppinen olio yhdistetään riviin
 *
 */
public class DynaaminenGridPane<T> extends GridPane {
    private Hallitsija<T> hallitsijaLuokka;
    private NodeLuonti<T> nodeluonti;
    private List<Label> otsikot;
    
    private int leveimmanRivinLeveys;
    
    /**
     * Luo dynaamisen GridPanen.
     * Päivittää GridPanen käskettäessä tai alkiota lisättäessä ja poistettaessa.
     * 
     * @param hallitsijaLuokka luokka jonka hallitsemia olioita ollaan käsittelemässä
     * @param nodeluonti mitkä nodet luodaan oliota lisättäessä
     */
    public DynaaminenGridPane(Hallitsija<T> hallitsijaLuokka, NodeLuonti<T> nodeluonti) {
        this.hallitsijaLuokka = hallitsijaLuokka;
        this.nodeluonti = nodeluonti;
        this.otsikot = new ArrayList<Label>();
        this.leveimmanRivinLeveys = 1; // vähintään painikerivi
    }
    
    
    /**
     * Lisää olion
     * 
     * @param olio lisättävä olio
     */
    public void lisaa(T olio) {
        this.hallitsijaLuokka.lisaa(olio); // antaa oliolle uniikin vaihenumeron
        paivita();
    }
    
    
    /**
     * Poistaa olion
     * 
     * @param olio poistettava olio
     */
    public void poista(T olio) {
        this.hallitsijaLuokka.poista(olio);
        paivita();
    }
    
    
    /**
     * Päivittää GridPanen rivit
     */
    public void paivita() {
        this.getChildren().clear(); // tyhjentää GridPanen
        
        // lisää otsikot
        for (int i = 0; i < this.otsikot.size(); i++) {
            this.add(this.otsikot.get(i), i, 0);
        }
        this.add(new Label(""), this.otsikot.size() + 1, 0); // tyhjä otsikko poistopainikkeille
        
        
        // lisää rivit paneeliin
        int rivi = 1;
        for (T olio : this.hallitsijaLuokka.anna()) {
            lisaaRiviGridPaneen(olio, rivi++);
        }
        
        // olion lisäyspainike
        Button lisaysPainike = new Button("+");
        lisaysPainike.setOnAction(e -> lisaa(this.hallitsijaLuokka.luo()));
        this.add(lisaysPainike, this.leveimmanRivinLeveys, rivi++);
    }
    
    
    /**
     * Lisää otsikot GridPaneen
     * 
     * @param sarakeOtsikot lista lisättävistä otsikoista (järjestyksessä vasemmalta oikealle, 0 -> n)
     */
    public void lisaaOtsikot(List<Label> sarakeOtsikot) {
        if (this.leveimmanRivinLeveys < sarakeOtsikot.size()) this.leveimmanRivinLeveys = sarakeOtsikot.size();
        
        this.otsikot = sarakeOtsikot;
    }
    
    
    /**
     * Lisää olion riville
     * 
     * @param olio lisättävän rivin olio
     * @param rivi mille riville lisätään
     */
    private void lisaaRiviGridPaneen(T olio, int rivi) {
        int sarake = 0;
        
        // luo rivin nodet
        List<Node> rivinNodet = this.nodeluonti.luoNodet(olio);
        
        // lisätään nodet jos ei null
        if (rivinNodet != null) {
            if (this.leveimmanRivinLeveys < rivinNodet.size()) this.leveimmanRivinLeveys = rivinNodet.size();
            for (Node node : rivinNodet) {
                this.add(node, sarake++, rivi);
            }
        }
        
        // lisätään poistopainike viimeiseen sarakkeeseen
        Button poistoPainike = new Button("x");
        poistoPainike.setOnAction(e -> poista(olio));
        this.add(poistoPainike, this.leveimmanRivinLeveys, rivi);
    }
    
    
}