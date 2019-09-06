/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.CumulConsumerD;

/**
 *
 * @author roxanam
 */
@Stateless
public class CumulClientDFacade extends AbstractFacade<CumulConsumerD> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CumulClientDFacade() {
        super(CumulConsumerD.class);
    }

    @Override
    public List<CumulConsumerD> getByClientAndRefDate(int clientId, Date refDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<CumulConsumerD>  ret= null;
         // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.DAY_OF_YEAR, -30);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.DAY_OF_YEAR, 30);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select rec.*\n" +
                            "from integridyschema.cumul_consumer_d rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd') " +
                            "order by rec.created_time";
        Query query = getEntityManager().createNativeQuery(sqlString, CumulConsumerD.class);
        query.setParameter(1, clientId);
        query.setParameter(2, df.format(dateStart));
        query.setParameter(3, df.format(dateStop));
        ret = query.getResultList();
        return ret;
    }
    
     @Override
     public List<Object[]> getTotalConsumptionByClient(int clientId , Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<Object[]>  ret= null;
         // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.DAY_OF_YEAR, -30);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.DAY_OF_YEAR, 30);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select coalesce(sum(rec.consumed_active_pow),0), rec.created_time\n" +
                            "from integridyschema.cumul_consumer_d rec,\n" +
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
      public List<Object[]> getTotalConsumptionByClientAndPeriod(int clientId , Date startDate, Date endDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<Object[]>  ret= null;
         // +/- 30 zile reference time

      
        String sqlString = "select coalesce(sum(rec.consumed_active_pow),0), rec.created_time\n" +
                            "from integridyschema.cumul_consumer_d rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and rec.created_time between ?2  and ?3 " +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, clientId);
         query.setParameter(2, startDate);
         query.setParameter(3, endDate);
         
        ret =query.getResultList();
        return ret;
        
     }
     
    public double getAvgPerWeekDayByClient(int weekday, int client, Date refDate){
        String sqlString = "select avg(rec.consumed_active_pow)\n" +
                            "from integridyschema.cumul_consumer_d rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where extract(dow from rec.created_time) = ?1\n" +
                            "		and rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?2";
        Query query = getEntityManager().createNativeQuery(sqlString);
        query.setParameter(1, weekday);
        query.setParameter(2, client);
        return (double)(query.getSingleResult()==null?0.:query.getSingleResult());
    }
    
    public List<CumulConsumerD> getCumulConsumerDByUsernameAndDate(String username, Date refDate){
        
        // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.DAY_OF_YEAR, -30);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.DAY_OF_YEAR, 30);
        Date dateStop = calendarStop.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        String sqlString = "select rec.* \n" +
                            "from integridyschema.users u,\n" +
                            "		integridyschema.consumption_point p,\n" +
                            "		integridyschema.consumer_client c,\n" +
                            "		integridyschema.smart_meter m,\n" +
                            "		integridyschema.cumul_consumer_d rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd') "+
                            "order by rec.id";
        Query query = getEntityManager().createNativeQuery(sqlString, CumulConsumerD.class);
        query.setParameter(1, username);
        query.setParameter(2, df.format(dateStart));
        query.setParameter(3, df.format(dateStop));
        return query.getResultList();
    }
    public List<Object[]> getTotalDailyConsumptionByUsername(String username, Date refDate){
        // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.DAY_OF_YEAR, -30);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.DAY_OF_YEAR, 30);
        Date dateStop = calendarStop.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sqlString = "select rec.* \n" +
                            "from integridyschema.users u,\n" +
                            "		integridyschema.consumption_point p,\n" +
                            "		integridyschema.consumer_client c,\n" +
                            "		integridyschema.smart_meter m,\n" +
                            "		integridyschema.cumul_consumer_d rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd')\n" +
                            //"group by rec.created_time	\n" +
                            "order by rec.created_time asc";
        Query query = getEntityManager().createNativeQuery(sqlString);
        
        query.setParameter(1, username);
        query.setParameter(2, format.format(dateStart));
        query.setParameter(3, format.format(dateStop));
        
        return query.getResultList();
    }
    
  @Override
     public List<CumulConsumerD> getByUsernameRefDateAndConsPoint(String username, Date refDate, int consumptionPointId){
         
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
                            "		integridyschema.cumul_consumer_d rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		rec.created_time between to_date(?2, 'YYYY-MM-DD HH24:MI:SS') and to_date(?3, 'YYYY-MM-DD HH24:MI:SS') and\n" +
                            "           p.id = ?4\n"+
                            "order by rec.device_number, rec.created_time";
        Query query = em.createNativeQuery(sqlString, CumulConsumerD.class);
        query.setParameter(1, username);
        query.setParameter(2, startDate);
        query.setParameter(3, stopDate);
        query.setParameter(4, consumptionPointId);
        
        List<CumulConsumerD> resultList = query.getResultList();
        return resultList;
    }
        
}
