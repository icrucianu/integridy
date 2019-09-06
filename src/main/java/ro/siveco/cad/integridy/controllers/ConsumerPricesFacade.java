/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.ConsumerPrices;

/**
 *
 * @author roxanam
 */
@Stateless
public class ConsumerPricesFacade extends AbstractFacade<ConsumerPrices> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumerPricesFacade() {
        super(ConsumerPrices.class);
    }
    
    public List<ConsumerPrices> getValidConsumerPrices(Date refDate){
        String sqlString = "select * \n" +
                    "from integridyschema.consumer_prices\n" +
                    "where validity_start < ?1\n" +
                    "and (validity_end >  ?2 or validity_end is null)";
        Query query = getEntityManager().createNativeQuery(sqlString,ConsumerPrices.class);
        query.setParameter(1, refDate);
        query.setParameter(2, refDate );
        return query.getResultList();
    }
}
