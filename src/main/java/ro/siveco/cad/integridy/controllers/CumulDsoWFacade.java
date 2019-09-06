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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.CumulDsoW;

/**
 *
 * @author roxanam
 */
@Stateless
public class CumulDsoWFacade extends AbstractFacade<CumulDsoW> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CumulDsoWFacade() {
        super(CumulDsoW.class);
    }
      public List<CumulDsoW> getCumulDsoWbyDay(Date date){
       
        // +/- 20 reference time
        LocalDate refDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
        LocalDate startDate = refDate.plus(-20, ChronoUnit.WEEKS);
        LocalDate stopDate = refDate.plus(20, ChronoUnit.WEEKS);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      
        String sqlString = "select rec.* \n" +
                            "from  integridyschema.cumul_dso_w rec\n" +
                            "where 	date(rec.created_time) between to_date(?1,'yyyy-MM-dd') and to_date(?2,'yyyy-MM-dd')\n" +
                            "order by rec.created_time";
        
        Query query = em.createNativeQuery(sqlString, CumulDsoW.class);
        query.setParameter(1, startDate.format(formatter) );
        query.setParameter(2, stopDate.format(formatter));
        return query.getResultList();
    }
      
     @Override
     public List<Object[]> getTotalConsumptionByClient(int clientId , Date refDate){
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         List<Object[]>  ret= null;
         // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(refDate);
        calendarStart.add(Calendar.MONTH, -6);
        Date dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(refDate);
        calendarStop.add(Calendar.MONTH, 6);
        Date dateStop = calendarStop.getTime();
      
        String sqlString = "select sum(coalesce(rec.consumed_active_pow,0)), rec.created_time\n" +
                            "from integridyschema.cumul_dso_w rec\n" +
                            "where date(rec.created_time) between to_date(?1,'yyyy-MM-dd') and to_date(?2,'yyyy-MM-dd') " +
                            "group by rec.created_time	\n" +
                            "order by rec.created_time";
         Query query = getEntityManager().createNativeQuery(sqlString);
         query.setParameter(1, df.format(dateStart));
        query.setParameter(2, df.format(dateStop));
        ret =query.getResultList();
        return ret;
        
     }
}
