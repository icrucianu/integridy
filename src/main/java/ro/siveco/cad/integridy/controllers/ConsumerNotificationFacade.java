/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.entities.ConsumerNotification;

@Stateless
public class ConsumerNotificationFacade extends AbstractFacade<ConsumerNotification>{
    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumerNotificationFacade() {
        super(ConsumerNotification.class);
    }
    public List<ConsumerNotification> getNotificationsByClientId(int clientId){
        String sqlString = "SELECT * \n" +
                            "FROM integridyschema.consumer_notification \n" +
                            "where client_id = ?1\n" +
                            "and severity = ?2 and status = 1\n" +
                            "ORDER BY id DESC LIMIT 100";
        Query query = em.createNativeQuery(sqlString, ConsumerNotification.class);
        query.setParameter(1, clientId);
        query.setParameter(2, Constants.SEVERITY_NOTIFICATION);
        
        List<ConsumerNotification> resultList = query.getResultList();
        return resultList;
    }
    public List<ConsumerNotification> getAlertsByClientId(int clientId){
        String sqlString = "SELECT * \n" +
                            "FROM integridyschema.consumer_notification \n" +
                            "where client_id = ?1\n" +
                            "and severity <= ?2 and status = 1\n" +
                            "ORDER BY id DESC LIMIT 10";
        Query query = em.createNativeQuery(sqlString, ConsumerNotification.class);
        query.setParameter(1, clientId);
        query.setParameter(2, Constants.SEVERITY_WARNING);
        
        List<ConsumerNotification> resultList = query.getResultList();
        return resultList;
    }
    
    public long getNotificationsCount(Date refDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String sqlString = "select count(*)\n" +
                            "from integridyschema.consumer_notification rec\n" +
                            "where  date(rec.created_on)= to_date(?1, 'yyyy-MM-dd')\n";
         Query query = getEntityManager().createNativeQuery(sqlString, Long.class);
         query.setParameter(1, df.format(refDate));
         return (long)query.getSingleResult();
    }
}
