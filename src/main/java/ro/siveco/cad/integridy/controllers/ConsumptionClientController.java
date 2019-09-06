/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.shiro.subject.Subject;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumptionRecord;
import ro.siveco.cad.integridy.entities.CumulConsumerD;
import ro.siveco.cad.integridy.entities.CumulConsumerM;
import ro.siveco.cad.integridy.entities.CumulConsumerW;
import ro.siveco.cad.integridy.entities.CumulConsumerY;

/**
 *
 * @author roxanam
 */
@Named("consumptionClientController")
@ViewScoped
public class ConsumptionClientController  implements Serializable{
   
    @EJB
    private DashboardService dashboardService;
    //client
    private ConsumerClient currentClient;
    private Date referenceDate;
    private String username;
    
    private boolean noChartDataCurrent;
    
    //current
    @EJB
    private ConsumptionRecordFacade consumptionRecordFacade;
    private List<ConsumptionRecord> currentItems;
    private ConsumptionRecord selectedCurrent;
    
    //daily
    @EJB
    private CumulClientDFacade cumulClientDFacade;
    private List<CumulConsumerD> dailyItems;
    private CumulConsumerD selectedDaily;
    
    
    //weekly
    @EJB 
    private CumulClientWFacade cumulClientWFacade;
    private List<CumulConsumerW> weeklyItems;
    private CumulConsumerW selectedWeekly;
    
    //monthly
     @EJB 
    private CumulClientMFacade cumulClientMFacade;
    private List<CumulConsumerM> montlyItems;
    private CumulConsumerM selectedMonthly;
    
    //yearly
     @EJB 
    private CumulClientYFacade cumulClientYFacade;
    private List<CumulConsumerY> yearlyItems;
    private CumulConsumerY selectedYesrly;
    
    private LineChartModel dateModelCurrent;
    private LineChartModel dateModelD;
    private LineChartModel dateModelW;
    private LineChartModel dateModelM;
    private LineChartModel dateModelY;
    
    @PostConstruct
    public void init(){
        //to do get user name from userservice
        //username = getCurrentUserUsername();
        username = "userb4";
        referenceDate = dashboardService.getCurrentDate();
        
        //for current data request 
        // referenceDate -/+24h
//         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        referenceDate = dashboardService.getCurrentDate();
//        Calendar calendarStart = Calendar.getInstance();
//        calendarStart.setTime(referenceDate);
//        calendarStart.add(Calendar.HOUR, -24);
//        Date startDate = calendarStart.getTime();
//        
//        Calendar calendarStop = Calendar.getInstance();
//        calendarStop.setTime(referenceDate);
//        calendarStop.add(Calendar.HOUR, 24);
//        Date stopDate = calendarStop.getTime();
//        
//        
//        currentItems = consumptionRecordFacade.getByUsernameAndPeriod(username, df.format(startDate), df.format(stopDate));
        noChartDataCurrent = true;
        getCurrentItems();
        createDateModel();
    }
    private void createDateModel(){
        currentItems = getCurrentItems();
        if(currentItems==null || currentItems.size()==0){
            noChartDataCurrent = true;
            return;
        } else {
            noChartDataCurrent = false;
        }
            
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         referenceDate = dashboardService.getCurrentDate();
        
        //for current data request 
        // referenceDate -/+24h
        
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(referenceDate);
        calendarStart.add(Calendar.HOUR, -12);
        Date startDate = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(referenceDate);
        calendarStop.add(Calendar.HOUR, 12);
        Date stopDate = calendarStop.getTime();
        
//        List<NoActivitiesInPeriod> lnap = getTimeSeries(1000l, startDate, stopDate);
//        if(lnap == null || lnap.size()<2){
//            noChartDataCurrent = true;
//        }
  
         dateModelCurrent = new LineChartModel();
//        if (lnap != null && lnap.size() > 2) {

            LineChartSeries series1=null;

//           series1= new LineChartSeries();
//           series1.setLabel("Client consumption device " + 3);
            int deviceNumber = 0;
           
            for (ConsumptionRecord rec : currentItems) {
               if(rec.getDeviceNumber()!=deviceNumber){
                   if(series1 !=null){
                       dateModelCurrent.addSeries(series1);
                   }
                   deviceNumber = rec.getDeviceNumber();
                   series1= new LineChartSeries();
                   series1.setLabel("Client consumption device " + rec.getDeviceNumber());
               } 
//                if(rec.getDeviceNumber()==3)
                    series1.set(df.format(rec.getCreatedTime()), rec.getConsumedActivePow());
            }
            if(series1 !=null){
                       dateModelCurrent.addSeries(series1);
                   }

//            dateModelCurrent.addSeries(series1);
        
        dateModelCurrent.setTitle("Consumed active power " + df.format(startDate) + " - " + df.format(stopDate));
        dateModelCurrent.setZoom(true);
        dateModelCurrent.getAxis(AxisType.Y).setLabel("Consumed Active Pow ");
        dateModelCurrent.setShadow(false);
        dateModelCurrent.setAnimate(true);
        dateModelCurrent.setLegendPosition("ne");
       
//        dateModel.setSeriesColors();
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-30);
//        axis.setMax("20 23:59:59");
        axis.setTickFormat("%#d,%H:%#M:%S");
        dateModelCurrent.getAxes().put(AxisType.X, axis);

    }
    public ConsumerClient getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(ConsumerClient currentClient) {
        this.currentClient = currentClient;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public List<ConsumptionRecord> getCurrentItems() {
        
      if( !referenceDate.equals(dashboardService.getCurrentDate()) || currentItems == null)  {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         referenceDate = dashboardService.getCurrentDate();
        
        //for current data request 
        // referenceDate -/+24h
        
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(referenceDate);
        calendarStart.add(Calendar.HOUR, -12);
        Date startDate = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(referenceDate);
        calendarStop.add(Calendar.HOUR, 12);
        Date stopDate = calendarStop.getTime();
//        createDateModel();
        return consumptionRecordFacade.getByUsernameAndPeriod(username, df.format(startDate), df.format(stopDate));
        
      }
      else
          return currentItems;
       
    }

    public void setCurrentItems(List<ConsumptionRecord> currentItems) {
        this.currentItems = currentItems;
    }

    public List<CumulConsumerW> getWeeklyItems() {
        return weeklyItems;
    }

    public void setWeeklyItems(List<CumulConsumerW> weeklyItems) {
        this.weeklyItems = weeklyItems;
    }

    public List<CumulConsumerM> getMontlyItems() {
        return montlyItems;
    }

    public void setMontlyItems(List<CumulConsumerM> montlyItems) {
        this.montlyItems = montlyItems;
    }

    public List<CumulConsumerY> getYearlyItems() {
        return yearlyItems;
    }

    public void setYearlyItems(List<CumulConsumerY> yearlyItems) {
        this.yearlyItems = yearlyItems;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ConsumptionRecord getSelectedCurrent() {
        return selectedCurrent;
    }

    public void setSelectedCurrent(ConsumptionRecord selectedCurrent) {
        this.selectedCurrent = selectedCurrent;
    }

    public CumulConsumerD getSelectedDaily() {
        return selectedDaily;
    }

    public void setSelectedDaily(CumulConsumerD selectedDaily) {
        this.selectedDaily = selectedDaily;
    }

    public CumulConsumerW getSelectedWeekly() {
        return selectedWeekly;
    }

    public void setSelectedWeekly(CumulConsumerW selectedWeekly) {
        this.selectedWeekly = selectedWeekly;
    }

    public CumulConsumerM getSelectedMonthly() {
        return selectedMonthly;
    }

    public void setSelectedMonthly(CumulConsumerM selectedMonthly) {
        this.selectedMonthly = selectedMonthly;
    }

    public CumulConsumerY getSelectedYesrly() {
        return selectedYesrly;
    }

    public void setSelectedYesrly(CumulConsumerY selectedYesrly) {
        this.selectedYesrly = selectedYesrly;
    }

    public boolean isNoChartDataCurrent() {
        return noChartDataCurrent;
    }

    public void setNoChartDataCurrent(boolean noChartDataCurrent) {
        this.noChartDataCurrent = noChartDataCurrent;
    }
    
    public String getCurrentUserUsername(){
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if(subject!=null){
            return subject.getPrincipal().toString();
        }else{
            return "User";
        }
    }

    public LineChartModel getDateModelCurrent() {
        return dateModelCurrent;
    }

    public void setDateModelCurrent(LineChartModel dateModelCurrent) {
        this.dateModelCurrent = dateModelCurrent;
    }
    
}
