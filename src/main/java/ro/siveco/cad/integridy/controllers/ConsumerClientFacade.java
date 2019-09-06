/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.ConsumerClient;

/**
 *
 * @author roxanam
 */
@Stateless
public class ConsumerClientFacade extends AbstractFacade<ConsumerClient> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumerClientFacade() {
        super(ConsumerClient.class);
    }
    public ConsumerClient getClientByUserName(String userName){
        String sqlString = "select c.*\n" +
                            "from integridyschema.consumer_client c,\n" +
                            "	 integridyschema.users	u\n" +
                            "where u.username = ?1\n" +
                            "	  and u.id = c.user_id ";
        Query query = getEntityManager().createNativeQuery(sqlString, ConsumerClient.class);
        query.setParameter(1, userName);
        ConsumerClient cc = null;
        try{
            cc=(ConsumerClient)query.getSingleResult();
        }
        catch(Exception ex)
        {
             Logger.getLogger(ConsumerClientFacade.class.getName()).log(Level.WARNING, null, ex.getMessage());
        }
        return cc;
       
    }
    
}
