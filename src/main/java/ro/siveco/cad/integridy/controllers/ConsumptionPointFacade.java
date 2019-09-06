/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.ConsumptionPoint;

/**
 *
 * @author roxanam
 */
@Stateless
public class ConsumptionPointFacade extends AbstractFacade<ConsumptionPoint> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumptionPointFacade() {
        super(ConsumptionPoint.class);
    }
    
    public List<ConsumptionPoint> getByClientId(int clientId){
        Query query = getEntityManager().createNamedQuery("ConsumptionPoint.findByClientId", ConsumptionPoint.class);
        query.setParameter("clientId", clientId);
        List<ConsumptionPoint> cp = null;
        try{
            cp = (List<ConsumptionPoint>)query.getResultList();
        }
        catch(Exception ex){
            Logger.getLogger(ConsumptionPoint.class.getName()).log(Level.WARNING, null, ex.getMessage());
        }
        return cp;
    }
    
}
