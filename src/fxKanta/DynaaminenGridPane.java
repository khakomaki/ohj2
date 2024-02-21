package fxKanta;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import kanta.Hallitsija;

/**
 * @author hakom
 * @version 24 Nov 2023
 * @param <T> minkä tyyppinen olio yhdistetään riviin
 *
 * Dynaamisempi GridPane-node, johon voidaan lisätä rivejä antamalla olio ja sitä vastaavat javafx-nodet.
 * Luo javafx-nodeja automaattisesti oliota lisättäessä.
 * Voidaan määrittää onko GridPane muokattavissa poisto- ja lisäyspainikkeilla käyttöliittymästä.
 * Voidaan luoda otsikko-Labelit, joille ei tule poisto painikkeita.
 */
public class DynaaminenGridPane<T> extends GridPane {
    private Hallitsija<T> hallitsijaLuokka;
    private NodeLuonti<T> nodeluonti;
    private List<Label> otsikot;
    
    private RowConstraints riviRajoitteet           = new RowConstraints();
    private RowConstraints riviOtsikkoRajoitteet    = new RowConstraints();
    private int leveimmanRivinLeveys                = 0;
    private boolean muokattava                      = true;
    
    /**
     * Luo dynaamisen GridPanen.
     * Päivittää GridPanen käskettäessä tai alkiota lisättäessä ja poistettaessa.
     * Voidaan valita tuleeko lisäys- ja poistopainikkeet.
     * 
     * @param hallitsijaLuokka luokka jonka hallitsemia olioita ollaan käsittelemässä
     * @param nodeluonti mitkä nodet luodaan oliota lisättäessä
     * @param muokattava ovatko oliot muokattavissa lisäys- ja poistopainikkeilla
     */
    public DynaaminenGridPane(Hallitsija<T> hallitsijaLuokka, NodeLuonti<T> nodeluonti, boolean muokattava) {
        this.hallitsijaLuokka = hallitsijaLuokka;
        this.nodeluonti = nodeluonti;
        this.muokattava = muokattava;
        
        this.otsikot = new ArrayList<Label>();
        if (muokattava) this.leveimmanRivinLeveys = 1; // vähintään painikerivi
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
        this.getRowConstraints().clear(); // tyhjentää rivirajoitteet
        
        int rivi = 0;
        
        // otsikot
        if (0 < this.otsikot.size()) {
            this.getRowConstraints().add(this.riviOtsikkoRajoitteet);
            
            for (int i = 0; i < this.otsikot.size(); i++) {
                this.add(this.otsikot.get(i), i, 0);
            }
            this.add(new Label(""), this.otsikot.size() + 1, 0); // tyhjä otsikko poistopainikkeille
            rivi++;
        }
        
        // lisää rivit paneeliin
        for (T olio : this.hallitsijaLuokka.anna()) {
            lisaaRiviGridPaneen(olio, rivi++);
        }
        
        // olion lisäyspainike
        if (this.muokattava) {
            Button lisaysPainike = new Button("+");
            lisaysPainike.setOnAction(e -> lisaa(this.hallitsijaLuokka.luo()));
            this.getRowConstraints().add(this.riviRajoitteet);
            this.add(lisaysPainike, this.leveimmanRivinLeveys, rivi);
        }
    }
    
    
    /**
     * Lisää otsikot GridPaneen
     * 
     * @param sarakeOtsikot lista lisättävistä otsikoista (järjestyksessä vasemmalta oikealle, 0 -> n)
     */
    public void lisaaOtsikot(List<Label> sarakeOtsikot) {
        if (this.leveimmanRivinLeveys < sarakeOtsikot.size()) {
            this.leveimmanRivinLeveys = sarakeOtsikot.size();
        }
        
        this.otsikot = sarakeOtsikot;
    }
    
    
    /**
     * Asettaa rivirajoitteet kaikille riveille
     * 
     * @param rajoitteet asetettavat rajoitteet
     */
    public void asetaRiviRajoitteet(RowConstraints rajoitteet) {
        this.riviRajoitteet = rajoitteet;
    }
    
    
    /**
     * Asettaa rivirajoitteet otsikkoriville
     * 
     * @param rajoitteet asetettavat rajoitteet
     */
    public void asetaOtsikkoRajoitteet(RowConstraints rajoitteet) {
        this.riviOtsikkoRajoitteet = rajoitteet;
    }
    
    
    /**
     * Lisää olion riville
     * 
     * @param olio lisättävän rivin olio
     * @param rivi mille riville lisätään
     */
    private void lisaaRiviGridPaneen(T olio, int rivi) {
        int sarake = 0;
        
        // lisätään rivirajoitteet
        this.getRowConstraints().add(this.riviRajoitteet);
        
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
        if (muokattava) {
            Button poistoPainike = new Button("x");
            poistoPainike.setOnAction(e -> poista(olio));
            this.add(poistoPainike, this.leveimmanRivinLeveys, rivi);
        }
    }
    
    
}