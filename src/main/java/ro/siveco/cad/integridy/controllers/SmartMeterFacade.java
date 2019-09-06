/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.SmartMeter;

/**
 *
 * @author roxanam
 */
@Stateless
public class SmartMeterFacade extends AbstractFacade<SmartMeter> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SmartMeterFacade() {
        super(SmartMeter.class);
    }
    public List<SmartMeter> getByConsumptionPointId(int consumptionPointId){
        Query query = getEntityManager().createNamedQuery("SmartMeter.findByConsumptionPointId", SmartMeter.class);
        query.setParameter("consumptionPointId", consumptionPointId);
        return query.getResultList();
    }
    public List<SmartMeter> getByConsumerId(int consumerId){
        Query query = getEntityManager().createNamedQuery("SmartMeter.findByConsumerId", SmartMeter.class);
        query.setParameter("consumerId", consumerId);
        return query.getResultList();
    }
    
}
