/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ro.siveco.cad.integridy.entities.GeneratedValuesInterval;

/**
 *
 * @author roxanam
 */
@Stateless
public class GeneratedValuesIntervalFacade extends AbstractFacade<GeneratedValuesInterval> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GeneratedValuesIntervalFacade() {
        super(GeneratedValuesInterval.class);
    }
    
}
