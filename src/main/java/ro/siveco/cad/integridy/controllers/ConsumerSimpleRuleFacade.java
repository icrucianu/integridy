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
import ro.siveco.cad.integridy.entities.ConsumerSimpleRule;

/**
 *
 * @author roxanam
 */
@Stateless
public class ConsumerSimpleRuleFacade extends AbstractFacade<ConsumerSimpleRule> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumerSimpleRuleFacade() {
        super(ConsumerSimpleRule.class);
    }
    
    public List<ConsumerSimpleRule> getConsumerSimpleRuleByConsumer(Integer consumerClientId){
        Query query = getEntityManager().createNamedQuery("ConsumerSimpleRule.findByConsumerId", ConsumerSimpleRule.class);
        query.setParameter("consumerId", consumerClientId);
        return query.getResultList();
    }
    public List<ConsumerSimpleRule> getConsumerSimpleRuleGlobal(){
        Query query = getEntityManager().createNamedQuery("ConsumerSimpleRule.findBGlobalRules", ConsumerSimpleRule.class);
        return query.getResultList();
    }
    
}
