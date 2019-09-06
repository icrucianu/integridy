/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;
import ro.siveco.cad.integridy.entities.ActionLog;
import ro.siveco.cad.integridy.entities.Users;

/**
 *
 * @author roxanam
 */
@Stateless
public class ActionLogFacade  extends AbstractFacade<ActionLog> implements Serializable {
    
    
    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActionLogFacade() {
        super(ActionLog.class);
    }
    
    public List<ActionLog> getResultList(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ActionLog> q = cb.createQuery(ActionLog.class);
        Root<ActionLog> actionLog = q.from(ActionLog.class);
       
        q.select(actionLog);
        //order ascending date by default
        

//        //filter???
//        //filter
//        Predicate filterCondition = cb.conjunction();
//        for (Map.Entry<String, Object> filter : filters.entrySet()) {
//            if (!filter.getValue().equals("")) {
//                //try as string using like
//                Path<String> pathFilter = getStringPath(filter.getKey(), site, siteType);
//                if (pathFilter != null){
//                    filterCondition = cb.and(filterCondition, cb.like(pathFilter, "%"+filter.getValue()+"%"));
//                }else{
//                    //try as non-string using equal
//                    Path<?> pathFilterNonString = getPath(filter.getKey(), site, siteType);
//                    filterCondition = cb.and(filterCondition, cb.equal(pathFilterNonString, filter.getValue()));
//                }
//            }
//        }
//        q.where(filterCondition);
         //pagination
        TypedQuery<ActionLog> tq = getEntityManager().createQuery(q);
        if (pageSize >= 0){
            tq.setMaxResults(pageSize);
        }
        if (first >= 0){
            tq.setFirstResult(first);
        }
        return tq.getResultList();
        
    }
    
    public List<ActionLog> getResultList(int first, int pageSize, Users user, Date startDate, Date endDate){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ActionLog> q = cb.createQuery(ActionLog.class);
        Root<ActionLog> actionLog = q.from(ActionLog.class);
       
        q.select(actionLog);
        //order ascending date by default
        Path<?> path = actionLog.get("actionDate");
        q.orderBy(cb.desc(path));
         Predicate filterCondition = cb.conjunction();
        //filter
        if(user!=null){
            Path userPath = actionLog.get("userId");
            filterCondition = cb.and(filterCondition, cb.equal(userPath, user));
        }
        if(startDate!=null){
            Path pathDateStart = actionLog.get("actionDate");
            filterCondition = cb.and(filterCondition, cb.greaterThan(pathDateStart, startDate));
        }
         if(endDate!=null){
            Path pathDateStart = actionLog.get("actionDate");
            filterCondition = cb.and(filterCondition, cb.lessThan(pathDateStart, endDate));
        }
          q.where(filterCondition);
         //pagination
        TypedQuery<ActionLog> tq = getEntityManager().createQuery(q);
        if (pageSize >= 0){
            tq.setMaxResults(pageSize);
        }
        if (first >= 0){
            tq.setFirstResult(first);
        }
        return tq.getResultList();
        
    }
    
    public long count(Users user, Date startDate, Date endDate) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<ActionLog> actionLog = q.from(ActionLog.class);
         q.select(cb.count(actionLog)); 
       
        Predicate filterCondition = cb.conjunction();
      
        //filter
        if(user!=null){
            Path userPath = actionLog.get("userId");
            filterCondition = cb.and(filterCondition, cb.equal(userPath, user));
        }
        
         if(startDate!=null){
            Path pathDateStart = actionLog.get("actionDate");
            filterCondition = cb.and(filterCondition, cb.greaterThan(pathDateStart, startDate));
        }
         if(endDate!=null){
            Path pathDateStart = actionLog.get("actionDate");
            filterCondition = cb.and(filterCondition, cb.lessThan(pathDateStart, endDate));
        }
       
          q.where(filterCondition);
         //pagination
        TypedQuery<Long> tq = getEntityManager().createQuery(q);
       
        return tq.getSingleResult();
    }
    
    public long getUsersLoginCount(Date referenceDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String sqlString = "select count(*)\n" +
                            "from integridyschema.action_log rec\n" +
                            "where  date(rec.action_date)= to_date(?1, 'yyyy-MM-dd') \n"
                                + "and rec.action_name = 'Login'\n";
         Query query = getEntityManager().createNativeQuery(sqlString, Long.class);
         query.setParameter(1, df.format(referenceDate));
         return (long)query.getSingleResult();
       
       
    }
}
