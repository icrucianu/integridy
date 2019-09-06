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
import ro.siveco.cad.integridy.entities.CumulDsoH;

/**
 *
 * @author roxanam
 */
@Stateless
public class CumulDsoHFacade extends AbstractFacade<CumulDsoH> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CumulDsoHFacade() {
        super(CumulDsoH.class);
    }
    
    public List<CumulDsoH> getCumulDsoHbyDay(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        String sqlString = "select rec.* \n" +
                            "from  integridyschema.cumul_dso_h rec\n" +
                            "where 	date(rec.created_time) = to_date(?1, 'yyyy-MM-dd')\n" +
                            "order by rec.created_time";
        Query query = em.createNativeQuery(sqlString, CumulDsoH.class);
        query.setParameter(1, df.format(date));
        return query.getResultList();
    }
    public List<Object[]> getConsumedActivePowByDay(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        String sqlString = "select coalesce(rec.consumed_active_pow,0), rec.created_time \n" +
                            "from  integridyschema.cumul_dso_h rec\n" +
                            "where 	date(rec.created_time) = to_date(?1, 'yyyy-MM-dd')\n" +
                            "order by rec.created_time";
        Query query = em.createNativeQuery(sqlString);
        query.setParameter(1, df.format(date));
        return query.getResultList();
    }
    public List<Object[]> getConsumedActivePowByPeriod(Date startDate, Date endDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        String sqlString = "select coalesce(rec.consumed_active_pow,0), rec.created_time \n" +
                            "from  integridyschema.cumul_dso_h rec\n" +
                            "where 	rec.created_time between  ?1 and  ?2\n" +
                            "order by rec.created_time";
        Query query = em.createNativeQuery(sqlString);
        query.setParameter(1, startDate);
        query.setParameter(2, endDate);
        return query.getResultList();
    }
    public List<Object[]> getAverageConsumptionDaily(){
        String sqlString = "select avg(consumed_active_pow), to_char(created_time, 'HH24:MI')\n" +
                            "from integridyschema.cumul_dso_h\n" +
                            "group by to_char(created_time, 'HH24:MI')\n" +
                            "order by to_char(created_time, 'HH24:MI') asc";
        Query query = em.createNativeQuery(sqlString);
        return query.getResultList();
    }
     public List<Object[]> getAverageConsumptionDaily(int dayOfWeek){
        String sqlString = "select avg(consumed_active_pow), to_char(created_time, 'HH24:MI')\n" +
                            "from integridyschema.cumul_dso_h\n" +
                            "where extract(dow from created_time) = ?1\n" +
                            "group by to_char(created_time, 'HH24:MI')\n" +
                            "order by to_char(created_time, 'HH24:MI') asc";
        Query query = em.createNativeQuery(sqlString);
        query.setParameter(1,dayOfWeek);
        return query.getResultList();
    }
    
    
    // this is used in parentConroleer to compose the chart
    @Override
     public List<Object[]> getTotalConsumptionByClient(int clientId , Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<Object[]>  ret= null;
         // +/- 30 zile reference time
         if(refDate==null)
             return ret;
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.HOUR_OF_DAY, -12);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.HOUR_OF_DAY, 12);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select sum(rec.consumed_active_pow), rec.created_time\n" +
                            "from integridyschema.cumul_dso_h rec\n" +
                            "where date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd') " +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, clientId);
         query.setParameter(2, df.format(dateStart));
        query.setParameter(3, df.format(dateStop));
        ret = query.getResultList();
        return ret;
        
     }
}
