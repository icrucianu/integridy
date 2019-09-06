/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.entities.ActionLog;
import ro.siveco.cad.integridy.entities.Users;

/**
 *
 * @author roxanam
 */
@Named("actionLogController")
@ViewScoped
public class ActionLogController implements Serializable {
    
    @EJB
    private ActionLogFacade actionLogFacade;
    
    private List<ActionLog> actionLogList;
    private ActionLog selected;
    
    //consumers registered
    @EJB
    private ConsumerClientFacade consumerCLientFacade;
    private Long consumersNumber;
    
    
    @EJB
    private DashboardService dashboardService;
    private Date referenceDate;
    
    //records in selected day
    @EJB
    private ConsumptionRecordFacade consumptionRecordFacade;
    private Long registerdConsumptionRecords;
    
    
    //notifications number in selected day
    @EJB
    private ConsumerNotificationFacade consumerNotificationFacade;
    private Long consumerNotificationsNumber;
    
    //notifications DSO
    @EJB
    private DsoNotificationFacade dsoNotificationFacade;
    private Long dsoNotificationsNumber;
    
    
    
    //distinct users logged in in selected day 
    private Long usersLoggedInNumber;
    //lazy loading

    private LazyDataModel<ActionLog> model;
    
    //filter criteria
    private Date startDate;
    private Date endDate;
    private Users selectedUser;
    
    
    private String reportName = "ActionLog";
    
    @PostConstruct
    public void init(){
//        actionLogList = actionLogFacade.findAll();
        try {
            this.model = new LazyDataModel<ActionLog>(){
                private static final long    serialVersionUID    = 1L;

                @Override
                public List<ActionLog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    List<ActionLog> result = actionLogFacade.getResultList(first, pageSize, sortField, sortOrder, filters);
                    model.setRowCount(actionLogFacade.count());
                    return result;
                }
                
            };
        }catch(Exception e){
                    
                    };
                    
       //usage reports number
       getUsageReports();
       
    }
    
    private void getUsageReports() {
        referenceDate = dashboardService.getCurrentDate();
        try {
            consumersNumber = (long) consumerCLientFacade.count();
            registerdConsumptionRecords = consumptionRecordFacade.getRecordCount(referenceDate);
            consumerNotificationsNumber = consumerNotificationFacade.getNotificationsCount(referenceDate);
            dsoNotificationsNumber = dsoNotificationFacade.getNotificationsCount(referenceDate);
            usersLoggedInNumber = actionLogFacade.getUsersLoginCount(referenceDate);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void filter(){
          try {
            this.model = new LazyDataModel<ActionLog>(){
                private static final long    serialVersionUID    = 1L;

                @Override
                public List<ActionLog> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    List<ActionLog> result = actionLogFacade.getResultList(first, pageSize, selectedUser, startDate, endDate);
                    model.setRowCount((int)actionLogFacade.count(selectedUser, startDate, endDate));
                    return result;
                }
                
                
            };
        }catch(Exception e){
                    
                    };
    }

    public LazyDataModel<ActionLog> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<ActionLog> model) {
        this.model = model;
    }
    
    

    public List<ActionLog> getActionLogList() {
        return actionLogList;
    }

    public void setActionLogList(List<ActionLog> actionLogList) {
        this.actionLogList = actionLogList;
    }

    public ActionLog getSelected() {
        return selected;
    }

    public void setSelected(ActionLog selected) {
        this.selected = selected;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

    public Long getConsumersNumber() {
        return consumersNumber;
    }

    public void setConsumersNumber(Long consumersNumber) {
        this.consumersNumber = consumersNumber;
    }

    public Long getRegisterdConsumptionRecords() {
        return registerdConsumptionRecords;
    }

    public void setRegisterdConsumptionRecords(Long registerdConsumptionRecords) {
        this.registerdConsumptionRecords = registerdConsumptionRecords;
    }

    public Long getConsumerNotificationsNumber() {
        return consumerNotificationsNumber;
    }

    public void setConsumerNotificationsNumber(Long consumerNotificationsNumber) {
        this.consumerNotificationsNumber = consumerNotificationsNumber;
    }

    public Long getDsoNotificationsNumber() {
        return dsoNotificationsNumber;
    }

    public void setDsoNotificationsNumber(Long dsoNotificationsNumber) {
        this.dsoNotificationsNumber = dsoNotificationsNumber;
    }

    public Long getUsersLoggedInNumber() {
        return usersLoggedInNumber;
    }

    public void setUsersLoggedInNumber(Long usersLoggedInNumber) {
        this.usersLoggedInNumber = usersLoggedInNumber;
    }
    
    
    
    
    
}
