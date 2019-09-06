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
import ro.siveco.cad.integridy.entities.CumulDsoD;

/**
 *
 * @author roxanam
 */
@Stateless
public class CumulDsoDFacade extends AbstractFacade<CumulDsoD> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CumulDsoDFacade() {
        super(CumulDsoD.class);
    }
    
    
    // this is used in parentConroleer to compose the chart
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
      
        String sqlString = "select sum(rec.consumed_active_pow), rec.created_time\n" +
                            "from integridyschema.cumul_dso_d rec\n" +
                            "where date(rec.created_time) between to_date(?1,'yyyy-MM-dd') and to_date(?2,'yyyy-MM-dd') " +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time";
        
        Query query = getEntityManager().createNativeQuery(sqlString);
        query.setParameter(1, df.format(dateStart));
        query.setParameter(2, df.format(dateStop));
        ret =query.getResultList();
        return ret;
        
     }
     
      public List<CumulDsoD> getCumulDsoDbyDay(Date date){
       
        // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(date);
        calendarStart.add(Calendar.DAY_OF_YEAR, -30);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(date);
        calendarStop.add(Calendar.DAY_OF_YEAR, 30);
        Date dateStop = calendarStop.getTime();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sqlString = "select rec.* \n" +
                            "from  integridyschema.cumul_dso_d rec\n" +
                            "where 	date(rec.created_time) between to_date(?1,'yyyy-MM-dd HH24:MI:SS') and to_date(?2,'yyyy-MM-dd HH24:MI:SS')\n" +
                            "order by rec.created_time";
        Query query = em.createNativeQuery(sqlString, CumulDsoD.class);
        query.setParameter(1, format.format(dateStart));
        query.setParameter(2, format.format(dateStop));
        return query.getResultList();
    }
      public List<CumulDsoD> getCumulDsoDbyDay(Date dateStart, Date dateStop){
       
        // +/- 30 zile reference time
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sqlString = "select rec.* \n" +
                            "from  integridyschema.cumul_dso_d rec\n" +
                            "where 	date(rec.created_time) between to_date(?1,'yyyy-MM-dd HH24:MI:SS') and to_date(?2,'yyyy-MM-dd HH24:MI:SS')\n" +
                            "order by rec.created_time";
        Query query = em.createNativeQuery(sqlString, CumulDsoD.class);
        query.setParameter(1, format.format(dateStart));
        query.setParameter(2, format.format(dateStop));
        return query.getResultList();
    }
      
     public List<CumulDsoD> getCumulDsoDByTimeInterval(Date dateStart, Date dateStop){
       
        // +/- 30 zile reference time
       
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sqlString = "select rec.* \n" +
                            "from  integridyschema.cumul_dso_d rec\n" +
                            "where 	date(rec.created_time) between to_date(?1,'yyyy-MM-dd HH24:MI:SS') and to_date(?2,'yyyy-MM-dd HH24:MI:SS')\n" +
                            "order by rec.created_time";
        Query query = em.createNativeQuery(sqlString, CumulDsoD.class);
        query.setParameter(1, format.format(dateStart));
        query.setParameter(2, format.format(dateStop));
        return query.getResultList();
    } 
     
     public List<Object[]> getConsumedActivePowByTimeInterval(Date dateStart, Date dateStop){
       
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sqlString = "select coalesce(rec.consumed_active_pow,0), rec.created_time \n" +
                            "from  integridyschema.cumul_dso_d rec\n" +
                            "where 	date(rec.created_time) between to_date(?1,'yyyy-MM-dd') and to_date(?2,'yyyy-MM-dd')\n" +
                            "order by rec.created_time";
        Query query = em.createNativeQuery(sqlString);
        query.setParameter(1, format.format(dateStart));
        query.setParameter(2, format.format(dateStop));
        return query.getResultList();
    }   
     
    public Double getAvgPerWeekDay(int weekday){
        String sqlString = "select avg(rec.consumed_active_pow)\n" +
                            "from integridyschema.cumul_dso_d rec\n" +
                            "where extract(dow from rec.created_time) = ?1"  ;
        Query query = getEntityManager().createNativeQuery(sqlString);
        query.setParameter(1, weekday);
        return (Double)(query.getSingleResult()==null?0:query.getSingleResult());
    }
}
