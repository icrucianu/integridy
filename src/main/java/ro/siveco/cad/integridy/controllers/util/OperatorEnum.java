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
public enum OperatorEnum {
    BETWEEN("Between"),
    GT("GreaterThan"),
    LT("Less than"),
    GE("GreaterOrEqual"),
    LE("LessOrEqual"),
    EQ("Equal");

    private String id;

    OperatorEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }  
}
