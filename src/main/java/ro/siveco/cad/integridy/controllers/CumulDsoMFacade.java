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
import ro.siveco.cad.integridy.entities.CumulDsoM;

/**
 *
 * @author roxanam
 */
@Stateless
public class CumulDsoMFacade extends AbstractFacade<CumulDsoM> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CumulDsoMFacade() {
        super(CumulDsoM.class);
    }
    
     // this is used in parentConroleer to compose the chart
    
     public List<CumulDsoM> getConsumptionByDay( Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<CumulDsoM>  ret= null;
         // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.MONTH, -12);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.MONTH, 12);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select rec.*\n" +
                            "from integridyschema.cumul_dso_m rec\n" +
                            "where date(rec.created_time) between to_date(?1,'yyyy-MM-dd') and to_date(?2,'yyyy-MM-dd') " +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString, CumulDsoM.class);
         query.setParameter(1, df.format(dateStart));
        query.setParameter(2, df.format(dateStop));
        ret =query.getResultList();
        return ret;
        
     }
    // this is used in parentConroleer to compose the chart
    @Override
     public List<Object[]> getTotalConsumptionByClient(int clientId , Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<Object[]>  ret= null;
         // +/- 12 luni reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.MONTH, -12);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.MONTH, 12);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select sum(rec.consumed_active_pow), rec.created_time\n" +
                            "from integridyschema.cumul_dso_m rec\n" +
                            "where date(rec.created_time) between to_date(?2,'yyyy-MM-dd') and to_date(?3,'yyyy-MM-dd') " +
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
