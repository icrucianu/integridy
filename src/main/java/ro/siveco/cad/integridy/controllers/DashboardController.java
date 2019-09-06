package ro.siveco.cad.integridy.controllers;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.*;

import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.controllers.util.EnergyProductionEnum;
import ro.siveco.cad.integridy.controllers.util.GraphicalStepEnum;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import ro.siveco.cad.integridy.entities.ActionLog;

import ro.siveco.cad.integridy.entities.ConsumerNotification;

import ro.siveco.cad.integridy.entities.DsoNotification;
import ro.siveco.cad.integridy.service.impl.ExternalCommunicationServiceImpl;

@Named(value = "dashboardController")
@SessionScoped
public class DashboardController implements Serializable {
    
    @EJB
    private CumulClientDFacade cumulClientDFacade;
    
    @EJB
    private ConsumerClientFacade consumerClientFacade;
    
    @EJB
    private ConsumerNotificationFacade clientNotificationFacade;
    
      
    @EJB
    private DsoNotificationFacade dsoNotificationFacade;
    
    @EJB
    private DashboardService dashboardService;
    
    @EJB
    private ActionLogFacade actionLogFacade;     
    
    @EJB
    private UsersFacade usersFacade;
    
    ExternalCommunicationServiceImpl externalCommunicationService = new ExternalCommunicationServiceImpl();
    
    private Date startDate;
    private Date endDate;
    private Date defaultStartDate;
    private Date defaultEndDate;
    private Date endMaxDate;
    private boolean startDateChanged, endDateChanged, projectionChanged;
    private String currentPage = "Dashboard";
    private int graphicalStep;
    private PieChartModel totalEnergyProductionPieChart;
    private BarChartModel totalEnergyProductionConsumptionChart;
    private LineChartModel clientConsumptionEvolutionChart;
    private boolean noTotalEnergyProductionPieChartData = true;// if true, no chart will be displayed
    private boolean totalEnergyProductionPieChartLoaded = false;// a flag to prevent multiple calls to DB
    
    private List<ConsumerNotification> clientNotificationList;
    private List<ConsumerNotification> clientAlertList;
    
    
    
    private List<DsoNotification> dsoNotificationList;
    private List<DsoNotification> dsoAlertList;
    private DsoNotification selectedDsoNotification;
    private DsoNotification selectedDsoAlert;
    private int clientId;
    
    @PostConstruct
    public void init() {
        endMaxDate = new Date();
        //default startDate and endDate
        endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.HOUR, -24);
        startDate = calendar.getTime();
        
        defaultStartDate = startDate;
        defaultEndDate = endDate;
        dashboardService.setCurrentDate(startDate);
        if (isConsumer()) {
            populateClientConsumptionEvolutionChart();
        }
        //daca este logat client
        if (isConsumer()) {
           
            clientId = consumerClientFacade.getClientByUserName(getCurrentUserUsername()).getId();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.CURRENT_CONSUMER, consumerClientFacade.getClientByUserName(getCurrentUserUsername()));
            setClientNotificationList(clientNotificationFacade.getNotificationsByClientId(clientId));
            setClientAlertList(clientNotificationFacade.getAlertsByClientId(clientId));
        } else {
            dsoNotificationList = dsoNotificationFacade.getDsoNotifications();
            dsoAlertList = dsoNotificationFacade.getDsoAlerts();
        }
        
    }
    
    private void populateClientConsumptionEvolutionChart() {
        ChartSeries currentSeries = new ChartSeries();
        ChartSeries lastYearSeries = new ChartSeries();
//        String username = getCurrentUserUsername();
//        int clientID = consumerClientFacade.getClientByUserName("userb4").getId();
        int clientID = consumerClientFacade.getClientByUserName(getCurrentUserUsername()).getId();
        currentSeries.set("Mon", 100);
        currentSeries.set("Tue", 100);
        currentSeries.set("Wed", 100);
        currentSeries.set("Thu", 100);
        currentSeries.set("Fri", 100);
        currentSeries.set("Sat", 100);
        currentSeries.set("Sun", 100);
        
        lastYearSeries.set("Mon", cumulClientDFacade.getAvgPerWeekDayByClient(1, clientID, new Date()));
        lastYearSeries.set("Tue", cumulClientDFacade.getAvgPerWeekDayByClient(2, clientID, new Date()));
        lastYearSeries.set("Wed", cumulClientDFacade.getAvgPerWeekDayByClient(3, clientID, new Date()));
        lastYearSeries.set("Thu", cumulClientDFacade.getAvgPerWeekDayByClient(4, clientID, new Date()));
        lastYearSeries.set("Fri", cumulClientDFacade.getAvgPerWeekDayByClient(5, clientID, new Date()));
        lastYearSeries.set("Sat", cumulClientDFacade.getAvgPerWeekDayByClient(6, clientID, new Date()));
        lastYearSeries.set("Sun", cumulClientDFacade.getAvgPerWeekDayByClient(0, clientID, new Date()));
        
        currentSeries.setLabel(ResourceBundle.getBundle(Constants.BUNDLE).getString("currentWeekClientConsumption"));
        lastYearSeries.setLabel(ResourceBundle.getBundle(Constants.BUNDLE).getString("correspondingWeekLastYearClientConsumption"));
        
        clientConsumptionEvolutionChart = new LineChartModel();
        clientConsumptionEvolutionChart.setExtender("chartExtender2");
        clientConsumptionEvolutionChart.addSeries(currentSeries);
        clientConsumptionEvolutionChart.addSeries(lastYearSeries);
        clientConsumptionEvolutionChart.setLegendPosition("ne");// to display the legend
        clientConsumptionEvolutionChart.setShowPointLabels(true);
        clientConsumptionEvolutionChart.getAxes().put(AxisType.X, new CategoryAxis(ResourceBundle.getBundle(Constants.BUNDLE).getString("weekDays")));
        clientConsumptionEvolutionChart.setAnimate(Constants.ANIMATE_CHART);
        Axis yAxis = clientConsumptionEvolutionChart.getAxis(AxisType.Y);
        yAxis.setLabel(ResourceBundle.getBundle(Constants.BUNDLE).getString("KWHLabel"));
        yAxis.setMin(0);
        
    }
    public void onDsoDismis(int notificationId, int type){
        DsoNotification notification = dsoNotificationFacade.find(notificationId);
        if(notification!=null){
            notification.setStatus(2);
            dsoNotificationFacade.edit(notification);
            if(type==1)
                dsoNotificationList = dsoNotificationFacade.getDsoNotifications();
            else
                dsoAlertList = dsoNotificationFacade.getDsoAlerts();
        }
       
    }
    
    public void onClientNotificationDismiss(int notificationId, int type){
        ConsumerNotification notification = clientNotificationFacade.find(notificationId);
        if(notification!=null){
            notification.setStatus(2);
            clientNotificationFacade.edit(notification);
            if(type == 2)//alerts
                setClientAlertList(clientNotificationFacade.getAlertsByClientId(clientId));
            else
                setClientNotificationList(clientNotificationFacade.getNotificationsByClientId(clientId));
        }
    }
    public boolean isNoChartData() {
        if (!totalEnergyProductionPieChartLoaded) {
            createTotalEnergyProductionPieChartModel();
            totalEnergyProductionPieChartLoaded = true;
        }
        return noTotalEnergyProductionPieChartData;
    }
    
    private void createTotalEnergyProductionPieChartModel() {
        try {
            Map<EnergyProductionEnum, String> valuesMap = externalCommunicationService.getNewestRomanianEnergyInfo();
            if (valuesMap.isEmpty()) {
                noTotalEnergyProductionPieChartData = true;
            } else {
                noTotalEnergyProductionPieChartData = false;
                totalEnergyProductionPieChart = new PieChartModel();
                totalEnergyProductionPieChart.setShadow(false);
                totalEnergyProductionPieChart.setLegendPosition("ne");
                totalEnergyProductionPieChart.setShowDataLabels(true);
                
                totalEnergyProductionConsumptionChart = new BarChartModel();
                ChartSeries consumptionSeries = new ChartSeries();
                ChartSeries productionSeries = new ChartSeries();
                
                consumptionSeries.setLabel(ResourceBundle.getBundle(Constants.BUNDLE).getString("consumption"));
                productionSeries.setLabel(ResourceBundle.getBundle(Constants.BUNDLE).getString("production"));
                
                for (EnergyProductionEnum entry : valuesMap.keySet()) {
                    switch (entry) {
                        case NUCLEAR:
                            totalEnergyProductionPieChart.set(ResourceBundle.getBundle(Constants.BUNDLE).getString("nuclear"), Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case WIND:
                            totalEnergyProductionPieChart.set(ResourceBundle.getBundle(Constants.BUNDLE).getString("wind"), Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case HYDRO:
                            totalEnergyProductionPieChart.set(ResourceBundle.getBundle(Constants.BUNDLE).getString("hydro"), Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case HYDROCARBS:
                            totalEnergyProductionPieChart.set(ResourceBundle.getBundle(Constants.BUNDLE).getString("hydrocarbs"), Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case COAL:
                            totalEnergyProductionPieChart.set(ResourceBundle.getBundle(Constants.BUNDLE).getString("coal"), Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case PV:
                            totalEnergyProductionPieChart.set(ResourceBundle.getBundle(Constants.BUNDLE).getString("pv"), Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case BIOMASS:
                            totalEnergyProductionPieChart.set(ResourceBundle.getBundle(Constants.BUNDLE).getString("biomass"), Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case PRODUCTION:
                            productionSeries.set(" ", Integer.parseInt(valuesMap.get(entry)));
                            break;
                        case CONSUMPTION:
                            consumptionSeries.set(" ", Integer.parseInt(valuesMap.get(entry)));
                            break;
                    }
                }
                
                totalEnergyProductionConsumptionChart.addSeries(productionSeries);
                totalEnergyProductionConsumptionChart.addSeries(consumptionSeries);
                totalEnergyProductionConsumptionChart.setZoom(true);
                totalEnergyProductionConsumptionChart.getAxis(AxisType.Y).setLabel(ResourceBundle.getBundle(Constants.BUNDLE).getString("MWLabel"));
                totalEnergyProductionConsumptionChart.setShadow(false);
                totalEnergyProductionConsumptionChart.setAnimate(Constants.ANIMATE_CHART);
                totalEnergyProductionConsumptionChart.setLegendPosition("ne");// to display the legend
                totalEnergyProductionConsumptionChart.setBarWidth(100);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    
//    public String getCurrentUserUsername() {
//        String loggedUsername = securityService.getLoggedUsername();
//        return loggedUsername != null ? loggedUsername : "";
//    }
    /**
     * Returns the current user's username
     *
     * @return
     */
    public String getCurrentUserUsername() {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if (subject != null) {
            return subject.getPrincipal().toString();
        } else {
            return "User";
        }
    }
     public String getCurrentUserRole() {
         Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if(isUserClient())
            return "Consumer";
        else if(isUserAdmin())
            return "Administrator";
        else if(isUserDso())
            return "DSO";
        else 
            return "";
            
    }
    public boolean isUserDso() {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if (subject != null) {
            if (subject.hasRole("dso")) {
                return true;
            }
        }
        return false;
    }
    public boolean isUserAdmin() {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if (subject != null) {
            if (subject.hasRole("admin")) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserAdminOrDso() {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if (subject != null) {
            if (subject.hasRole("admin") || subject.hasRole("dso")) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isUserClient() {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if (subject != null) {
            if (subject.hasRole("consumer")) {
                return true;
            }
        }
        return false;
    }
    
    public void onStartDateChanged(SelectEvent event) {
        if (!defaultStartDate.equals((Date) event.getObject())) {
            startDateChanged = true;
            dashboardService.setCurrentDate(startDate);
        }
    }
    
    public boolean filtersChanged() {
        if (startDateChanged || endDateChanged || projectionChanged) {
            return true;
        }
        return false;
    }
    
    public void applyIntegridyFilters() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.START_DATE, startDate);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.END_DATE, endDate);
        
        defaultStartDate = startDate;
        defaultEndDate = endDate;
        
//        RequestContext.getCurrentInstance().execute("location.reload()");
        

        startDateChanged = false;
        endDateChanged = false;
        projectionChanged = false;
        endMaxDate = new Date();
        
        reloadCurrentPage();
        
    }

//    public boolean isUserAdminOrManager() {
//        String loggedUserRole = userService.findByUsername(securityService.getLoggedUsername()).getRole();
//        return UserRolesEnum.getByName(loggedUserRole) == UserRolesEnum.ADMINISTRATOR ||
//                UserRolesEnum.getByName(loggedUserRole) == UserRolesEnum.DSO;
//    }
//
    public void reloadCurrentPage(){
          try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            
          
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(uri);
        } catch (IOException ioe) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ioe);
        }
    }
    public boolean isConsumer() {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if (subject != null) {
            if (subject.hasRole("consumer")) {
                return true;
            }
        }
        return false;
        
    }
    
    public void navigate() {
        String pageName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pageName");
        if (pageName != null) {
            setCurrentPage(pageName);
            //activity log
            actionLogRegister("Navigate", pageName);
        }
    }
    private void actionLogRegister(String actionName, String pageName) {
        try {
            //login action to be logged
            ActionLog action = new ActionLog();
            action.setActionDate(new Date());
            action.setActionName(actionName);
            action.setPage(pageName);
            action.setUserId(usersFacade.findByUserName(getCurrentUserUsername()));
            actionLogFacade.create(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void redirectDashboard() throws IOException {
        currentPage = "Dashboard";
        reloadPage("/index.xhtml");
    }
    
    public Date getEndMaxDate() {
        return endMaxDate;
    }
    
    public void setEndMaxDate(Date endMaxDate) {
        this.endMaxDate = endMaxDate;
    }
    
    public Date getDefaultStartDate() {
        return defaultStartDate;
    }
    
    public void setDefaultStartDate(Date defaultStartDate) {
        this.defaultStartDate = defaultStartDate;
    }
    
    public Date getDefaultEndDate() {
        return defaultEndDate;
    }
    
    public void setDefaultEndDate(Date defaultEndDate) {
        this.defaultEndDate = defaultEndDate;
    }
    
    public boolean isStartDateChanged() {
        return startDateChanged;
    }
    
    public void setStartDateChanged(boolean startDateChanged) {
        this.startDateChanged = startDateChanged;
    }
    
    public boolean isEndDateChanged() {
        return endDateChanged;
    }
    
    public void setEndDateChanged(boolean endDateChanged) {
        this.endDateChanged = endDateChanged;
    }
    
    public String getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
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
    
    public String getGraphicalIntervalStr() {
        GraphicalStepEnum enumStep = GraphicalStepEnum.getByValue(graphicalStep);
        if (enumStep != null) {
            switch (enumStep) {
                case STEP_15m:
                    return "15 minutes";
                case STEP_1H:
                    return "1 hour";
                case STEP_6H:
                    return "6 hours";
                case STEP_12H:
                    return "12 hours";
                case STEP_1D:
                    return "1 day";
                case STEP_1W:
                    return "1 week";
                case STEP_1M:
                    return "1 month";
                case STEP_3M:
                    return "3 months";
                case STEP_6M:
                    return "6 months";
                case STEP_1Y:
                    return "1 year";
                default:
                    return "";
            }
        }
        return "";
    }
    
    public PieChartModel getTotalEnergyProductionPieChart() {
        if (totalEnergyProductionPieChart == null) {
            createTotalEnergyProductionPieChartModel();
        }
        return totalEnergyProductionPieChart;
    }
    
    public void setTotalEnergyProductionPieChart(PieChartModel totalEnergyProductionPieChart) {
        this.totalEnergyProductionPieChart = totalEnergyProductionPieChart;
    }
    
    public boolean isNoTotalEnergyProductionPieChartData() {
        return noTotalEnergyProductionPieChartData;
    }
    
    public void setNoTotalEnergyProductionPieChartData(boolean noTotalEnergyProductionPieChartData) {
        this.noTotalEnergyProductionPieChartData = noTotalEnergyProductionPieChartData;
    }
    
    public boolean isTotalEnergyProductionPieChartLoaded() {
        return totalEnergyProductionPieChartLoaded;
    }
    
    public void setTotalEnergyProductionPieChartLoaded(boolean totalEnergyProductionPieChartLoaded) {
        this.totalEnergyProductionPieChartLoaded = totalEnergyProductionPieChartLoaded;
    }
    
    public BarChartModel getTotalEnergyProductionConsumptionChart() {
        return totalEnergyProductionConsumptionChart;
    }
    
    public void setTotalEnergyProductionConsumptionChart(BarChartModel totalEnergyProductionConsumptionChart) {
        this.totalEnergyProductionConsumptionChart = totalEnergyProductionConsumptionChart;
    }
    
    public LineChartModel getClientConsumptionEvolutionChart() {
        return clientConsumptionEvolutionChart;
    }
    
    public void setClientConsumptionEvolutionChart(LineChartModel clientConsumptionEvolutionChart) {
        this.clientConsumptionEvolutionChart = clientConsumptionEvolutionChart;
    }
    
    public int getGraphicalStep() {
        return graphicalStep;
    }
    
    public void setGraphicalStep(int graphicalStep) {
        if (graphicalStep != this.graphicalStep) {
            projectionChanged = true;
            this.graphicalStep = graphicalStep;
        }
    }
    
    public List<ConsumerNotification> getClientNotificationList() {
        return clientNotificationList;
    }
    
    public void setClientNotificationList(List<ConsumerNotification> clientNotificationList) {
        this.clientNotificationList = clientNotificationList;
    }
    
    public List<ConsumerNotification> getClientAlertList() {
        return clientAlertList;
    }
    
    public void setClientAlertList(List<ConsumerNotification> clientAlertList) {
        this.clientAlertList = clientAlertList;
    }
    
    public List<DsoNotification> getDsoNotificationList() {
        return dsoNotificationList;
    }
    
    public void setDsoNotificationList(List<DsoNotification> dsoNotificationList) {
        this.dsoNotificationList = dsoNotificationList;
    }
    
    public List<DsoNotification> getDsoAlertList() {
        return dsoAlertList;
    }
    
    public void setDsoAlertList(List<DsoNotification> dsoAlertList) {
        this.dsoAlertList = dsoAlertList;
    }

    public DsoNotification getSelectedDsoNotification() {
        return selectedDsoNotification;
    }

    public void setSelectedDsoNotification(DsoNotification selectedDsoNotification) {
        this.selectedDsoNotification = selectedDsoNotification;
    }

    public DsoNotification getSelectedDsoAlert() {
        return selectedDsoAlert;
    }

    public void setSelectedDsoAlert(DsoNotification selectedDsoAlert) {
        this.selectedDsoAlert = selectedDsoAlert;
    }
    
    public String signOut(){
        actionLogRegister("Logout", "login.xhtml");
        SecurityUtils.getSubject().logout();
       
        return "/faces/login.xhtml";
    }
    
    public void reloadPage(String pageName) throws IOException {
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        String requestURI = ((HttpServletRequest) ec.getRequest()).getRequestURI();
//        if (pageName == null) {// refresh current page
//            ec.redirect(requestURI);
//        } else {// redirect to pageName
//            requestURI = "/" + requestURI.split("/")[1] + "/" + pageName;
//            ec.redirect(requestURI);
//        }
//        noTotalEnergyProductionPieChartData = true;// if true, no chart will be displayed
//        totalEnergyProductionPieChartLoaded = false;// a flag to prevent multiple calls to DB
        try {
            FacesContext fCtx = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
            String sessionId = session.getId();
            
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath() + "/faces" + pageName + ";jsessionid=" + sessionId);
        } catch (IOException ioe) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ioe);
        }
        
    }
}
