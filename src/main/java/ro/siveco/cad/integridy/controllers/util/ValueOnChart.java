/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers.util;

import java.io.Serializable;

/**
 *
 * @author roxanam
 */
public class ValueOnChart implements Serializable{
    private String time;//timestamp format YYYY-MM-DD HH:mm:ss
    private Double value;

    public ValueOnChart(String time, Double value) {
        this.time = time;
        this.value = value;
    }

    public ValueOnChart() {
    }

    
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    
    
    
}
