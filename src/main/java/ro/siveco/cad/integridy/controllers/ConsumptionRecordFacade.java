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
import ro.siveco.cad.integridy.entities.ConsumptionRecord;

/**
 *
 * @author roxanam
 */
@Stateless
public class ConsumptionRecordFacade extends AbstractFacade<ConsumptionRecord> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsumptionRecordFacade() {
        super(ConsumptionRecord.class);
    }
    public List<ConsumptionRecord> getByUsernameAndPeriod(String username, String startDate, String stopDate){
        String sqlString = "select rec.* \n" +
                            "from integridyschema.users u,\n" +
                            "		integridyschema.consumption_point p,\n" +
                            "		integridyschema.consumer_client c,\n" +
                            "		integridyschema.smart_meter m,\n" +
                            "		integridyschema.consumption_record rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		rec.created_time between to_date(?2, 'YYYY-MM-DD HH24:MI:SS') and to_date(?3, 'YYYY-MM-DD HH24:MI:SS')\n" +
                            "order by rec.device_number, rec.created_time";
        Query query = em.createNativeQuery(sqlString, ConsumptionRecord.class);
        query.setParameter(1, username);
        query.setParameter(2, startDate);
        query.setParameter(3, stopDate);
        
        List<ConsumptionRecord> resultList = query.getResultList();
        return resultList;
    }
     public List<ConsumptionRecord> getByUsernameRefDate(String username, Date refDate){
         
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
                            "		integridyschema.consumption_record rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		rec.created_time between to_date(?2, 'YYYY-MM-DD HH24:MI:SS') and to_date(?3, 'YYYY-MM-DD HH24:MI:SS')\n" +
                            "order by rec.device_number, rec.created_time";
        Query query = em.createNativeQuery(sqlString, ConsumptionRecord.class);
        query.setParameter(1, username);
        query.setParameter(2, startDate);
        query.setParameter(3, stopDate);
        
        List<ConsumptionRecord> resultList = query.getResultList();
        return resultList;
    }
     
     //@Override
     public List<ConsumptionRecord> getByUsernameRefDateAndConsPoint(String username, Date refDate, int consumptionPointId){
         
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
                            "		integridyschema.consumption_record rec\n" +
                            "where 	u.username = ?1 and\n" +
                            "		c.user_id = u.id and\n" +
                            "		p.client_id = c.id and\n" +
                            "		m.consumption_point_id = p.id and \n" +
                            "		rec.device_number = m.id \n" +
                            "		and\n" +
                            "		rec.created_time between to_date(?2, 'YYYY-MM-DD HH24:MI:SS') and to_date(?3, 'YYYY-MM-DD HH24:MI:SS') and\n" +
                            "           p.id = ?4\n"+
                            "order by rec.device_number, rec.created_time";
        Query query = em.createNativeQuery(sqlString, ConsumptionRecord.class);
        query.setParameter(1, username);
        query.setParameter(2, startDate);
        query.setParameter(3, stopDate);
        query.setParameter(4, consumptionPointId);
        
        List<ConsumptionRecord> resultList = query.getResultList();
        return resultList;
    }
     @Override
     public List<ConsumptionRecord> getByClientAndRefDate(int clientId, Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String sqlString = "select rec.*\n" +
                            "from integridyschema.consumption_record rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and date(rec.created_time)= to_date(?2, 'yyyy-MM-dd')\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString, ConsumptionRecord.class);
         query.setParameter(1, clientId);
         query.setParameter(2, df.format(refDate));
         return query.getResultList();
     }
     
     
     public List<ConsumptionRecord> getByClientAndMonth(int clientId, int month, int year){
        
         String sqlString = "select rec.*\n" +
                            "from integridyschema.consumption_record rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and date_part('month', rec.created_time)= ?2\n" +
                            "		and date_part('year', rec.created_time)= ?3\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString, ConsumptionRecord.class);
         query.setParameter(1, clientId);
         query.setParameter(2, month);
         query.setParameter(3, year);
         return query.getResultList();
     }
     
     @Override
     public List<Object[]> getTotalConsumptionByClient(int clientId , Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String sqlString = "select coalesce(sum(rec.consumed_active_pow),0), rec.created_time\n" +
                            "from integridyschema.consumption_record rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and date(rec.created_time)= to_date(?2, 'yyyy-MM-dd')\n" +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, clientId);
         query.setParameter(2, df.format(refDate));
         
         return query.getResultList();
     }
     
     public List<Object[]> getTotalConsumptionByClientIdAndPeriod(int clientId , Date startDate, Date endDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String sqlString = "select coalesce(sum(rec.consumed_active_pow),0), rec.created_time\n" +
                            "from integridyschema.consumption_record rec,\n" +
                            "		integridyschema.smart_meter sm,\n" +
                            "		integridyschema.consumption_point cp,\n" +
                            "		integridyschema.consumer_client cc\n" +
                            "where rec.device_number = sm.id\n" +
                            "		and sm.consumption_point_id = cp.id\n" +
                            "		and cp.client_id = cc.id\n" +
                            "		and cc.id = ?1\n" +
                            "		and rec.created_time between ?2 and ?3 \n" +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, clientId);
         query.setParameter(2, startDate);
         query.setParameter(3, endDate);
         
         return query.getResultList();
     }
     
     public List<Object[]> getAvgDayConsumptonByClient(int clientId , Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String sqlString = "select avg(total_ap), to_char(rec_time, 'HH24:MI' )\n" +
                            "from\n" +
                            "		(select sum(rec.consumed_active_pow) total_ap, rec.created_time rec_time\n" +
                            "		from integridyschema.consumption_record rec,\n" +
                            "				integridyschema.consumer_client c,\n" +
                            "				integridyschema.consumption_point p,\n" +
                            "				integridyschema.smart_meter sm\n" +
                            "		where 	c.id=?1 and\n" +
                            "				p.client_id= c.id and\n" +
                            "				sm.consumption_point_id = p.id and\n" +
                            "				rec.device_number = sm.id\n" +
                            "		group by rec.created_time order by 2)	total	\n" +
                            "group by to_char(rec_time, 'HH24:MI' )		\n" +
                            "order by 2 asc";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, clientId);
         return query.getResultList();
     }
     
     public List<Object[]> getAvgDayConsumptonByClientOnDoW(int clientId , Date refDate, int dow){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         String sqlString = "select avg(total_ap), to_char(rec_time, 'HH24:MI' )\n" +
                            "from\n" +
                            "		(select sum(rec.consumed_active_pow) total_ap, rec.created_time rec_time\n" +
                            "		from integridyschema.consumption_record rec,\n" +
                            "				integridyschema.consumer_client c,\n" +
                            "				integridyschema.consumption_point p,\n" +
                            "				integridyschema.smart_meter sm\n" +
                            "		where 	c.id=?1 and\n" +
                            "				p.client_id= c.id and\n" +
                            "				sm.consumption_point_id = p.id and\n" +
                            "				rec.device_number = sm.id and\n" +
                            "                           extract(dow from created_time) = ?2\n" +
                            "		group by rec.created_time order by 2)	total	\n" +
                            "group by to_char(rec_time, 'HH24:MI' )		\n" +
                            "order by 2 asc";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, clientId);
         query.setParameter(2, dow);
         return query.getResultList();
     }
     
    public long getRecordCount(Date refDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String sqlString = "select count(*)\n" +
                            "from integridyschema.consumption_record rec\n" +
                            "where  date(rec.created_time)= to_date(?1, 'yyyy-MM-dd')\n";
         Query query = getEntityManager().createNativeQuery(sqlString, Long.class);
         query.setParameter(1, df.format(refDate));
         return (long)query.getSingleResult();
    }
}
