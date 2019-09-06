/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers.util;

import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author roxanam
 */

public enum PriceType {
    
    UNDIFFERENTIATED("Undifferentiated"),
    DIFFERENTIATED("Differentiated");
    
    private String id;
    
    PriceType(String id){
        this.id=id;
    }
    
    public String getPriceType(){
        return id;
    }
}
