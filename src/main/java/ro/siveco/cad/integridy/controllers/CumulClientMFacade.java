/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.CumulConsumerM;

/**
 *
 * @author roxanam
 */
@Stateless
public class CumulClientMFacade extends AbstractFacade<CumulConsumerM> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CumulClientMFacade() {
        super(CumulConsumerM.class);
    }

    @Override
    public List<CumulConsumerM> getByClientAndRefDate(int clientId, Date refDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<CumulConsumerM>  ret= null;
         // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        
        calendarStart.add(Calendar.MONTH, -12);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.MONTH, 12);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select rec.* \n" +
                            "from integridyschema.cumul_consumer_m rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd') " +
                            "order by rec.created_time";
        
        Query query = getEntityManager().createNativeQuery(sqlString,CumulConsumerM.class);
        query.setParameter(1, clientId);
        query.setParameter(2, df.format(dateStart));
        query.setParameter(3, df.format(dateStop));
        ret =query.getResultList();
        return ret;
    }
    
    
    
    public List<CumulConsumerM> getCumulClientMByUsernameAndDate(String userName, Date date){
        LocalDate refDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startDate = refDate.plus(-12, ChronoUnit.MONTHS);
        LocalDate stopDate = refDate.plus(12, ChronoUnit.MONTHS);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      
        String sqlString = "select rec.* \n" +
                            "from  integridyschema.users u, "+
                            "       integridyschema.cumul_consumer_m rec,\n" +
                            "		integridyschema.smart_meter m,\n" +
                            "		integridyschema.consumption_point p,\n" +
                            "		integridyschema.consumer_client c\n" +
                             "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and \n" +
                            " date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd')\n" +
                            "order by rec.created_time";
        
        Query query = em.createNativeQuery(sqlString, CumulConsumerM.class);
        query.setParameter(1, userName);
        query.setParameter(2, startDate.format(formatter) );
        query.setParameter(3, stopDate.format(formatter));
        return query.getResultList();
    }
    public List<Object[]> getTotalCumlClientMByUserName(String userName, Date date){
        LocalDate refDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
        LocalDate startDate = refDate.plus(-12, ChronoUnit.MONTHS);
        LocalDate stopDate = refDate.plus(12, ChronoUnit.MONTHS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String sqlString = "select sum(rec.consumed_active_pow), rec.created_time \n" +
                            "from integridyschema.users u,\n" +
                            "		integridyschema.consumption_point p,\n" +
                            "		integridyschema.consumer_client c,\n" +
                            "		integridyschema.smart_meter m,\n" +
                            "		integridyschema.cumul_consumer_m rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd')\n" +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time asc";
        Query query = getEntityManager().createNativeQuery(sqlString);
        
        query.setParameter(1, userName);
        query.setParameter(2, startDate.format(formatter) );
        query.setParameter(3, stopDate.format(formatter));
        
        return query.getResultList();
    }
    
    @Override
     public List<CumulConsumerM> getByUsernameRefDateAndConsPoint(String username, Date refDate, int consumptionPointId){
         
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.HOUR_OF_DAY, -12);
        Date startDate = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.HOUR_OF_DAY, 12);
        Date stopDate = calendarStop.getTime();
        
        String sqlString = "select rec.* \n" +
                            "from integridyschema.users u,\n" +
                            "		integridyschema.consumption_point p,\n" +
                            "		integridyschema.consumer_client c,\n" +
                            "		integridyschema.smart_meter m,\n" +
                            "		integridyschema.cumul_consumer_m rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		rec.created_time between to_date(?2, 'YYYY-MM-DD HH24:MI:SS') and to_date(?3, 'YYYY-MM-DD HH24:MI:SS') and\n" +
                            "           p.id = ?4\n"+
                            "order by rec.device_number, rec.created_time";
        Query query = em.createNativeQuery(sqlString, CumulConsumerM.class);
        query.setParameter(1, username);
        query.setParameter(2, startDate);
        query.setParameter(3, stopDate);
        query.setParameter(4, consumptionPointId);
        
        List<CumulConsumerM> resultList = query.getResultList();
        return resultList;
    }
     
     @Override
     public List<Object[]> getTotalConsumptionByClient(int clientId , Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<Object[]>  ret= null;
         // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        
        calendarStart.add(Calendar.MONTH, -12);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.MONTH, 12);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select sum(rec.consumed_active_pow), rec.created_time\n" +
                            "from integridyschema.cumul_consumer_m rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd') " +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, clientId);
         query.setParameter(2, df.format(dateStart));
           
       
        query.setParameter(3, df.format(dateStop));
        ret =query.getResultList();
        return ret;
        
     }
     
}
