/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.ConsumerRecommendation;

/**
 *
 * @author roxanam
 */
@Stateless
public class ConsumerRecommendationFacade extends AbstractFacade<ConsumerRecommendation>{

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumerRecommendationFacade() {
        super(ConsumerRecommendation.class);
    }
    
    public List<ConsumerRecommendation> getByClientId(int clientId){
        Query query = getEntityManager().createNamedQuery("ConsumerRecommendation.findByClientId", ConsumerRecommendation.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
    
    
    public boolean isAlreadyIssued(ConsumerRecommendation cr){
        String sqlString = "select 'x'\n" +
                            "from integridyschema.consumer_recommendation\n" +
                            "where client_id=?1\n" +
                            "and status is null\n" ;
        if(cr.getReduceIntervalStart()!=null){
            sqlString+="and reduce_interval_start = '" + cr.getReduceIntervalStart()+"'\n" ;
        }
        if(cr.getReduceIntervalStop()!=null)
            sqlString+="and reduce_interval_stop = '" + cr.getReduceIntervalStop()+"'\n";
        if(cr.getReduceDayOfWeek()!=null)
            sqlString+="and reduce_day_of_week = " + cr.getReduceDayOfWeek();
            
        Query query = getEntityManager().createNativeQuery(sqlString);
        query.setParameter(1, cr.getClientId().getId());
        List<Object> result = query.getResultList();
        if(result==null) 
            return false;
        if(result.isEmpty()) 
            return false;
        return true;
    }
    
}
