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
import ro.siveco.cad.integridy.entities.DsoNotification;

/**
 *
 * @author roxanam
 */
@Stateless
public class DsoNotificationFacade extends AbstractFacade<DsoNotification> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DsoNotificationFacade() {
        super(DsoNotification.class);
    }
    
    public List<DsoNotification> getDsoNotifications(){
        String sqlString = "SELECT * \n" +
                            "FROM integridyschema.dso_notification \n" +
                            "where severity = ?1 and status=1\n" +
                            "ORDER BY id desc LIMIT 10";
        Query query = em.createNativeQuery(sqlString, DsoNotification.class);
        query.setParameter(1, Constants.SEVERITY_NOTIFICATION);
        List<DsoNotification> resultList = query.getResultList();
        return resultList;
    }
    public List<DsoNotification> getDsoAlerts(){
        String sqlString = "SELECT * \n" +
                            "FROM integridyschema.dso_notification \n" +
                            "where severity <= ?1 and status=1\n" +
                            "ORDER BY id desc LIMIT 10";
        Query query = em.createNativeQuery(sqlString, DsoNotification.class);
        query.setParameter(1, Constants.SEVERITY_WARNING);
        List<DsoNotification> resultList = query.getResultList();
        return resultList;
    }
    public long getNotificationsCount(Date referenceDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String sqlString = "select count(*)\n" +
                            "from integridyschema.dso_notification rec\n" +
                            "where  date(rec.created_on)= to_date(?1, 'yyyy-MM-dd')\n";
         Query query = getEntityManager().createNativeQuery(sqlString, Long.class);
         query.setParameter(1, df.format(referenceDate));
         return (long)query.getSingleResult();
        
    }
}
