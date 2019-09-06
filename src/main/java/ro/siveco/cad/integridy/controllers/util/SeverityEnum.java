/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers.util;

/**
 *
 * @author roxanam
 */
public enum SeverityEnum {
    ALARM(1),
    WARNING(2),
    NOTIFICATION(3);
    
    private Integer id;
    SeverityEnum(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return this.id;
    }
    
}
