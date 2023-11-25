package fxKanta;

import java.util.List;

import javafx.scene.Node;

/**
 * @author hakom
 * @version 25 Nov 2023
 * @param <T> mille oliolle nodet luodaan
 *
 */
public interface NodeLuonti<T> {
    
    /**
     * Luo nodet oliolle
     * 
     * @param olio mille oliolle nodet luodaan
     * @return lista luotavista nodeista
     */
    public List<Node> luoNodet(T olio);
    
}
