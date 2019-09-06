/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumerRecommendation;

/**
 *
 * @author roxanam
 */
@Named("consumerRecommendationsAdminController")
@ViewScoped
public class ConsumerRecommendationsAdminController implements Serializable {
    
    @EJB
    private ro.siveco.cad.integridy.controllers.ConsumerRecommendationFacade ejbFacade;
    
    @EJB
    private ConsumerClientFacade consumerClientFacade;
    private List<ConsumerClient> consumerClientList;
    
    @EJB 
    private ConsumerRecommendationFacade consumerRecommendationFacade;
    private ConsumerRecommendation consumerRecommendation;
    
    @EJB
    private ConsumptionRecordFacade consumptionRecordFacade;
    private List<Object[]> avgTotalConsumptionCurrent;
    
    @EJB
    private CumulClientDFacade cumulClientDFacade;
    
    private List<Object[]> avgTotalConsumptionByDayOfWeek;
  
    
    @EJB
    private CumulDsoHFacade cumulDsoHFacade;
    
    @EJB
    private CumulDsoDFacade cumulDsoDFacade;
    //daily
    private ArrayList<Object[]> avgConsumptionDaily;
    //specific day of week
    private List<Object[]> avgConsumptionByDayOfWeek;
    //for week
    private List<Object[]> avgConsumptionWeekly;
    
    private LineChartModel dateModelCurrentAvg;
    private boolean noDataCurrentAvg;
    
    
    private LineChartModel dataModelWeeklyAvg = new LineChartModel();
    
    private LineChartModel dateModelCurrentAvgDow = new LineChartModel();
    private boolean noDataCurrentAvgDow=true;
    
    private List<String> weekDays=null;
    private String weekDay;
    private Integer radioSelection=null;
    
    private List<ConsumerRecommendation> consumerRecommendationList;
    private List<ConsumerRecommendation> selectedConsumerRecommendationList;
    
    private List<ConsumerRecommendation> specialDayRecommendationList = new ArrayList<>();
    private boolean hasSpecialDayRecomandationData = false;
    private boolean hasWeeklyRecomandationData = false;
    private List<ConsumerRecommendation> weekRecommendationList=  new ArrayList<>();

    private ConsumerRecommendation selectedRecommendation;
    
    private List<ConsumerClient> itemsAvailableSelectOne;

    private ConsumerClient selectedConsumer;
    
    
    public void onSelectRecommendation(){
        
    }
    
    public void onUnselectRecommendation(){
        selectedRecommendation=null;
    }
    
    public Integer getRadioSelection() {
        return radioSelection;
    }

    public void setRadioSelection(Integer radioSelection) {
        this.radioSelection = radioSelection;
    }
    public void onRadioChangeSelect(){
        System.out.println("xxxxxxxxxx  Radio Selection " + radioSelection);
        //get avg consumption for dso for a specific week day 
        noDataCurrentAvgDow=true;
        avgConsumptionByDayOfWeek = cumulDsoHFacade.getAverageConsumptionDaily(radioSelection);
        if(avgConsumptionByDayOfWeek!=null)
            noDataCurrentAvgDow = false;
        
        createDataModelForDayOfWeek();
        
    }
   
    

    @PostConstruct
    public void init(){
        consumerRecommendationList = consumerRecommendationFacade.findAll();
        initWeekDays();
      //  consumerRecommendationList = new ArrayList<>();
        noDataCurrentAvg = false;
        consumerClientList = consumerClientFacade.findAll();
        avgConsumptionDaily = new ArrayList<>(cumulDsoHFacade.getAverageConsumptionDaily());
        createDataModel();
        getAvgWeeklyConsumptionData();
        itemsAvailableSelectOne=consumerClientFacade.findAll();
    }

    public LineChartModel getDataModelWeeklyAvg() {
        return dataModelWeeklyAvg;
    }

    public void setDataModelWeeklyAvg(LineChartModel dataModelWeeklyAvg) {
        this.dataModelWeeklyAvg = dataModelWeeklyAvg;
    }
   
    private void initWeekDays(){
        weekDays = new ArrayList<>();
        weekDays.add("Sunday");
        weekDays.add("Monday");
        weekDays.add("Tuesday");
        weekDays.add("Wednesday");
        weekDays.add("Thursday");
        weekDays.add("Friday");
        weekDays.add("Saturday");
        
        
    }
    
    public void getAvgWeeklyConsumptionData(){
        
        avgConsumptionWeekly = new ArrayList<>();
        ChartSeries weekSeries = new ChartSeries();
     
//        String username = getCurrentUserUsername();
        int clientID = consumerClientFacade.getClientByUserName("userb4").getId();
        Double dayValue = 0.;
        int i = 0;
        for(String weekDay: weekDays){
            dayValue = cumulDsoDFacade.getAvgPerWeekDay(weekDays.indexOf(weekDay));
            weekSeries.set(weekDay, dayValue);
            avgConsumptionWeekly.add(new Object[2]);
            avgConsumptionWeekly.get(i)[0]= dayValue;
            avgConsumptionWeekly.get(i)[1]= weekDay;
            i++;
        }
        
        weekSeries.setLabel("DSO consum saptamanal");
        dataModelWeeklyAvg.setExtender("chartExtender3");
        dataModelWeeklyAvg.addSeries(weekSeries);
        dataModelWeeklyAvg.setLegendPosition("ne");
        dataModelWeeklyAvg.setShowPointLabels(true);
        dataModelWeeklyAvg.getAxes().put(AxisType.X, new CategoryAxis("Week days"));
        dataModelWeeklyAvg.setAnimate(true);
        Axis yAxis = dataModelWeeklyAvg.getAxis(AxisType.Y);
        yAxis.setLabel("KWH");
        yAxis.setMin(0);
    }
    
    public ArrayList<Object[]> extractPeaks(List<Object[]> results){
        ArrayList<Object[]> peaks=null;
        if (results != null && !results.isEmpty()) {
            peaks =new ArrayList<>();
            int ii = 0;
            for (Object[] res : results) {
                if (ii == 0) {
                    peaks.add(res);
                    
                }

                if (ii == (results.size() - 1)) {
                    peaks.add(res);
                }

                if (ii > 4 && ii < results.size() - 4) {
                    // maximum 

                    if (((Double) results.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] < (Double) res[0])) {
                        peaks.add(res);
                    }

                    // minimums
                    
                    if (((Double) results.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] > (Double) res[0])) {
                        peaks.add(res);
                    }
                    
                   

                } // if ii 4
                 if(ii>=results.size() - 4){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=ii;li<results.size()-1;li++ ){
                         if((Double)res[0] < (Double)results.get(li)[0]){
                             isMax=false;
                         }
                         else
                             isMin=false;
                     }
                     //min
                     if(((Double) results.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] > (Double) res[0])
                             && isMin){
                         peaks.add(res);
                     }
                     //max
                     if (((Double) results.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] < (Double) res[0])
                             && isMax){
                         peaks.add(res);
                     }
                     
                     
                 }
                 
                 if(ii>0 && ii<4){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=1;li<4;li++ ){
                         if((Double)res[0] < (Double)results.get(li)[0]){
                             isMax=false;
                         }
                         else
                             isMin=false;
                     }
                     if (   isMax
                            && ((Double) results.get(ii + 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] < (Double) res[0])) {
                        peaks.add(res);
                    }

                    // minimums
                    
                    if  (   isMin
                            && ((Double) results.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] > (Double) res[0])) {
                        peaks.add(res);
                    }
                 }
                ii++;
            } // for
            
            
        }
        return peaks;
        
    }
    
    public ArrayList<Object[]> extractMaxPeeks(List<Object[]> results){
        ArrayList<Object[]> peaks=null;
        if (results != null && !results.isEmpty()) {
            peaks =new ArrayList<>();
            int ii = 0;
            for (Object[] res : results) {
                if (ii == 0) {
                    peaks.add(res);
                }

                if (ii == (results.size() - 1)) {
                    peaks.add(res);
                }

                if (ii > 4 && ii < results.size() - 4) {
                    // maximum 

                    if (((Double) results.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] < (Double) res[0])) {
                        peaks.add(res);
                    }
                } // if ii 4
                 if(ii>results.size() - 4){
                     boolean isMax = true;
                     for(int li=ii;li<results.size()-1;li++ ){
                         if((Double)res[0] < (Double)results.get(li)[0]){
                             isMax=false;
                             break;
                         }
                     }
                     //max
                     if (((Double) results.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] < (Double) res[0])
                             && isMax){
                         peaks.add(res);
                     }
                 }
                 
                 if(ii>0 && ii<4){
                     boolean isMax = true;
                   
                     for(int li=1;li<4;li++ ){
                         if((Double)res[0] < (Double)results.get(li)[0]){
                             isMax=false;
                             break;
                         }
                     }
                     if (   isMax
                            && ((Double) results.get(ii + 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] < (Double) res[0])) {
                        peaks.add(res);
                    }

                 }
                ii++;
            } // for
        }
        return peaks;
        
    }
    
    public void createDataModelForDayOfWeek(){
         List<Object[]> peaksDSOList;
        /////////////////////////////////////////////////////////////////
        peaksDSOList = extractPeaks(avgConsumptionByDayOfWeek);
        /////////////////////////////////////////////////////////////////////
        dateModelCurrentAvgDow = new LineChartModel();
        dateModelCurrentAvgDow.setExtender("chartExtender3");
        LineChartSeries series1 = new LineChartSeries();
        for(Object[] val: avgConsumptionByDayOfWeek){
            series1.set(val[1],(Double)val[0] );
        }
        
        LineChartSeries series2 = new LineChartSeries();
        for(Object[] val:peaksDSOList){
            series2.set(val[1],(Double)val[0] );
        }
        
        series1.setLabel("Average Consumed active power "  );
        series2.setLabel("Peak active power " );
           
        dateModelCurrentAvgDow.addSeries(series1);
        dateModelCurrentAvgDow.addSeries(series2);
        noDataCurrentAvgDow = false;
            dateModelCurrentAvgDow.setTitle("DSO - Consumed active power - Avg" );
            dateModelCurrentAvgDow.setZoom(true);
            dateModelCurrentAvgDow.getAxis(AxisType.Y).setLabel("Consumed Active Pow - Avg");
            dateModelCurrentAvgDow.setShadow(false);
            dateModelCurrentAvgDow.setAnimate(true);
            dateModelCurrentAvgDow.setLegendPosition("ne");
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
//            axis.setMin("00:00");
//            axis.setMax("23:00");
           
            axis.setTickFormat("%H:%M");
            dateModelCurrentAvgDow.getAxes().put(AxisType.X, axis);
    }
    
    public void createDataModel(){
        List<Object[]> peaksDSOList;
        /////////////////////////////////////////////////////////////////
        peaksDSOList = extractPeaks(avgConsumptionDaily);
        /////////////////////////////////////////////////////////////////////
        dateModelCurrentAvg = new LineChartModel();
        dateModelCurrentAvg.setExtender("chartExtender3");
        LineChartSeries series1 = new LineChartSeries();
        for(Object[] val:avgConsumptionDaily){
            series1.set(val[1],(Double)val[0] );
        }
        
        LineChartSeries series2 = new LineChartSeries();
        for(Object[] val:peaksDSOList){
            series2.set(val[1],(Double)val[0] );
        }
        
        series1.setLabel("Average Consumed active power " );
        series2.setLabel("Peak active power " );
           
        dateModelCurrentAvg.addSeries(series1);
        dateModelCurrentAvg.addSeries(series2);
        noDataCurrentAvg = false;
            dateModelCurrentAvg.setTitle("DSO - Consumed active power - Avg" );
            dateModelCurrentAvg.setZoom(true);
            dateModelCurrentAvg.getAxis(AxisType.Y).setLabel("Consumed Active Pow - Avg");
            dateModelCurrentAvg.setShadow(false);
            dateModelCurrentAvg.setAnimate(true);
            dateModelCurrentAvg.setLegendPosition("ne");
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
//            axis.setMin("00:00");
//            axis.setMax("23:00");
           
            axis.setTickFormat("%H:%M");
            dateModelCurrentAvg.getAxes().put(AxisType.X, axis);
    }
    
//    public void issueConsumerRecommendationsOnDailyConsumption(ConsumerClient consumer){
//        //get dso avg daily consumption
//       
//        //get peaks 
//        List<Object[]> peaksDSOList = new ArrayList<>();
//        /////////////////////////////////////////////////////////////////
//        peaksDSOList = extractPeeks(avgConsumptionDaily);
//        /////////////////////////////////////////////////////////////////////
//        //get ordered avg list
//        ArrayList<Object[]> orderredList = (ArrayList)((ArrayList)avgConsumptionDaily).clone();
//       Collections.sort(orderredList, (Object[] o1, Object[] o2) -> ((Double)o2[0]).compareTo((Double)o1[0]));
//        
//        ////////
//        //get avg consumption per hour
//        avgTotalConsumptionCurrent = consumptionRecordFacade.getAvgDayConsumptonByClient(consumer.getId(), new Date());
//        
//        //get         //create a recommenndation based on local maximimum points and local minimum points
//    }
//    public void onChangeWeekDay(){
//      
//        
//        System.out.println(weekDay);
//        int indexDay = weekDays.indexOf(weekDay);
//        
//    }
    public void issueConsumerRecommendationsOnSpecialDayConsumption(){
        
        hasSpecialDayRecomandationData = true;
        
//        dateModelCurrentAvgDow
        ArrayList<Object[]> orderredDsoSDList =  new ArrayList<>(avgConsumptionByDayOfWeek);
        Collections.sort(orderredDsoSDList, (Object[] o1, Object[] o2) -> ((Double)o2[0]).compareTo((Double)o1[0]));
        //get max
        Object[] maxPoint = orderredDsoSDList.get(0);
        //get min 
        Object[] minPoint = orderredDsoSDList.get(orderredDsoSDList.size()-1);
        
        
        DateTimeFormatter timeFormat=DateTimeFormatter.ofPattern("HH:mm");
        
        LocalTime maxTime = LocalTime.parse((String)maxPoint[1]);
        LocalTime maxTime1 = maxTime.plusMinutes(-30);
        LocalTime maxTime2 = maxTime.plusMinutes(30);
        
        LocalTime minTime = LocalTime.parse((String)minPoint[1]);
        LocalTime minTime1 = minTime.plusMinutes(-30);
        LocalTime minTime2 = minTime.plusMinutes(30);
        
        ArrayList<Object[]> avgConsumptionConsumer = null;
        ArrayList<Object[]> consumerAvgMaxPeaks=null;
    
        
        LocalTime crtLocalTime = null;
        specialDayRecommendationList.clear();
        long i = 1;
        for(ConsumerClient c:consumerClientList){
            //for each consumer get avg consumption(last month consumption avg)
            avgConsumptionConsumer = new ArrayList<>(consumptionRecordFacade.getAvgDayConsumptonByClientOnDoW(c.getId(), new Date(),  radioSelection));
            consumerAvgMaxPeaks = extractMaxPeeks(avgConsumptionConsumer);
            if(consumerAvgMaxPeaks==null ){
                continue;}
            
           
            for(Object[] peak: consumerAvgMaxPeaks){
                crtLocalTime = LocalTime.parse((String)peak[1]);
                 if(crtLocalTime.isAfter(maxTime1) && crtLocalTime.isBefore(maxTime2)){
                     consumerRecommendation = new ConsumerRecommendation();
                     Double quantity = (Double)peak[0]*0.2;
                     String consumerRecommendationStr = "Please consider reducing  consumption on " + weekDays.get(radioSelection) +"s between hours "+timeFormat.format(maxTime1) +" - " + timeFormat.format(maxTime2)+
                                                    " with "+ quantity +" W. You can increase consumption between hours " + timeFormat.format(minTime1) + " - " + timeFormat.format(minTime2);
                    
                     consumerRecommendation.setClientId(c);
                     consumerRecommendation.setCreatedOn(new Date());
                     consumerRecommendation.setEmitter("SYSTEM");
                     consumerRecommendation.setLongDescription(consumerRecommendationStr);
                     consumerRecommendation.setNotifCode("SD-1");
                     consumerRecommendation.setSeverity(1);
                     consumerRecommendation.setReduceIntervalStart(timeFormat.format(maxTime1));
                     consumerRecommendation.setReduceIntervalStop(timeFormat.format(maxTime2));
                     consumerRecommendation.setReduceDayOfWeek(radioSelection);
                     consumerRecommendation.setQuantity(quantity);
                     //if there is no similar recommendation with similar interval values for this client
                    
                    i++;
                     if (!consumerRecommendationFacade.isAlreadyIssued(consumerRecommendation)) {
                         consumerRecommendationFacade.edit(consumerRecommendation);
                         consumerRecommendation.setId(i);
                         specialDayRecommendationList.add(consumerRecommendation);
                     }
                 }
            }
            
        }
        
    }
    public ArrayList<Object[]> getAvgWeeklyByClient(int clientId, Date refDate){
        ArrayList<Object[]> results = new ArrayList<>();
        int i = 0;
        for(String day:weekDays){
            results.add(new Object[2]);
            results.get(i)[0] = cumulClientDFacade.getAvgPerWeekDayByClient(i, clientId, refDate);
            results.get(i)[1] = day;
            i++;
        }
        return results;
    }
    public void issueConsRecOnWeek(){
        hasWeeklyRecomandationData = true;
        
        //avgConsumptionWeekly
        ArrayList<Object[]> orderredDsoSDList =  new ArrayList<>(avgConsumptionWeekly);
        Collections.sort(orderredDsoSDList, (Object[] o1, Object[] o2) -> ((Double)o2[0]).compareTo((Double)o1[0]));
        //get max
        Object[] maxPoint = orderredDsoSDList.get(0);
        //get min 
        Object[] minPoint = orderredDsoSDList.get(orderredDsoSDList.size()-1);
        
        
        String maxDay = (String)maxPoint[1];
        String minDay = (String)minPoint[1];
        
        ArrayList<Object[]> avgConsumptionConsumer = null;
        ArrayList<Object[]> consumerAvgMaxPeaks=null;
       
        weekRecommendationList.clear();
        long i = 1;
        for(ConsumerClient c:consumerClientList){
            //for each consumer get avg consumption(last month consumption avg)
            avgConsumptionConsumer = getAvgWeeklyByClient(c.getId(), new Date());
            consumerAvgMaxPeaks = extractMaxPeeks(avgConsumptionConsumer);
            if(consumerAvgMaxPeaks==null ){
                continue;}
            
           
            for(Object[] peak: consumerAvgMaxPeaks){
                if (maxDay.equals(peak[1])) {
                    consumerRecommendation = new ConsumerRecommendation();
                    Double quantity = (Double) peak[0] * 0.2;
                    String consumerRecommendationStr = "Please consider reducing  consumption on " + maxDay + "s"
                            + " with " + quantity + " W.Instead you can increase consumption on " + minDay + "s.";

                    consumerRecommendation.setClientId(c);
                    consumerRecommendation.setCreatedOn(new Date());
                    consumerRecommendation.setEmitter("SYSTEM");
                    consumerRecommendation.setLongDescription(consumerRecommendationStr);
                    consumerRecommendation.setNotifCode("W-1");
                    consumerRecommendation.setSeverity(1);
                    consumerRecommendation.setReduceDayOfWeek(weekDays.indexOf(maxDay));
                    consumerRecommendation.setQuantity(quantity);
                  
                    i++;
                    if (!consumerRecommendationFacade.isAlreadyIssued(consumerRecommendation)) {
                        consumerRecommendationFacade.edit(consumerRecommendation);
                        consumerRecommendation.setId(i);
                        weekRecommendationList.add(consumerRecommendation);
                    }
                }
            }
            
        }
    }
    
    public void issueConsumerRecommendationsOnDailyConsumption(){
        //get dso avg daily consumption
       
        //get peaks 
//        List<Object[]> peaksDSOList = new ArrayList<>();
//        /////////////////////////////////////////////////////////////////
//        peaksDSOList = extractPeeks(avgConsumptionDaily);
        /////////////////////////////////////////////////////////////////////
        //get ordered avg list
        ArrayList<Object[]> orderredList = (ArrayList)(avgConsumptionDaily).clone();
        Collections.sort(orderredList, (Object[] o1, Object[] o2) -> ((Double)o2[0]).compareTo((Double)o1[0]));
        ///get max peak
        Object[] maxPoint = orderredList.get(0);
        //get min peak
        Object[] minPoint = orderredList.get(orderredList.size()-1);
      
	DateTimeFormatter timeFormat=DateTimeFormatter.ofPattern("HH:mm");
        
        LocalTime maxTime = LocalTime.parse((String)maxPoint[1]);
        LocalTime maxTime1 = maxTime.plusMinutes(-30);
        LocalTime maxTime2 = maxTime.plusMinutes(30);
        
        LocalTime minTime = LocalTime.parse((String)minPoint[1]);
        LocalTime minTime1 = minTime.plusMinutes(-30);
        LocalTime minTime2 = minTime.plusMinutes(30);
        
        ArrayList<Object[]> avgConsumptionConsumer = null;
        ArrayList<Object[]> consumerAvgPeaks=null;
        
        LocalTime crtLocalTime = null;
        consumerRecommendationList.clear();
        
        for(ConsumerClient c:consumerClientList){
            //for each consumer get avg consumption(last month consumption avg)
            avgConsumptionConsumer = new ArrayList<>(consumptionRecordFacade.getAvgDayConsumptonByClient(c.getId(), new Date()));
            consumerAvgPeaks = extractPeaks(avgConsumptionConsumer);
            if(consumerAvgPeaks==null ){
                continue;}
            long i = 1;
           
            for(Object[] peak: consumerAvgPeaks){
                crtLocalTime = LocalTime.parse((String)peak[1]);
                 if(crtLocalTime.isAfter(maxTime1) && crtLocalTime.isBefore(maxTime2)){
                     consumerRecommendation = new ConsumerRecommendation();
                     Double quantity = (Double)peak[0]*0.2;
                     String consumerRecommendationStr = "Please consider reducing daily consumption between hours "+timeFormat.format(maxTime1) +" - " + timeFormat.format(maxTime2)+
                                                    " with "+ quantity +" W. You can increase consumption between hours " + timeFormat.format(minTime1) + " - " + timeFormat.format(minTime2);
                    
                     consumerRecommendation.setClientId(c);
                     consumerRecommendation.setCreatedOn(new Date());
                     consumerRecommendation.setEmitter("SYSTEM");
                     consumerRecommendation.setLongDescription(consumerRecommendationStr);
                     consumerRecommendation.setNotifCode("R-1");
                     consumerRecommendation.setSeverity(1);
                     consumerRecommendation.setReduceIntervalStart(timeFormat.format(maxTime1));
                     consumerRecommendation.setReduceIntervalStop(timeFormat.format(maxTime2));
                     consumerRecommendation.setQuantity(quantity);
                     //if there is no similar recommendation with similar interval values for this client
                    
                     i++;
                     if (!consumerRecommendationFacade.isAlreadyIssued(consumerRecommendation)) {
                         consumerRecommendationFacade.edit(consumerRecommendation);
                         consumerRecommendation.setId(i);
                         consumerRecommendationList.add(consumerRecommendation);
                     }
                 }
            }
            
        }
        
        ////////
        //get avg consumption per hour
//        avgTotalConsumptionCurrent = consumptionRecordFacade.getAvgDayConsumptonByClient(consumer.getId(), new Date());
        
        //get         //create a recommenndation based on local maximimum points and local minimum points
    }

    
    //CRUD Operation
    private ConsumerRecommendationFacade getFacade() {
        return ejbFacade;
    }
    
    public ConsumerRecommendation prepareCreate(){
        selectedRecommendation = new ConsumerRecommendation();
        selectedRecommendation.setCreatedOn(new Date());        
        return selectedRecommendation;
    }
    
    public void create(){
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerRecommendationCreated"));
        
        if (!JsfUtil.isValidationFailed()) {
                consumerRecommendationList = null;    // Invalidate list of items to trigger re-query.
            }
        
    }
    
    public void update(){
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerRecommendationUpdated"));
    }
    
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsersDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedRecommendation = null; // Remove selection
            consumerRecommendationList = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
        public List<ConsumerRecommendation> getConsumerRecommendationList() {
            if (consumerRecommendationList == null) {
                consumerRecommendationList = getFacade().findAll();
            }
            return consumerRecommendationList;
    }
    
     private void persist(PersistAction persistAction, String successMessage) {
        if (selectedRecommendation != null) {
            //setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {                    
                    //currentUser=dashboard.getCurrentUserUsername();
                    getFacade().edit(selectedRecommendation);       
                } else {
                    getFacade().remove(selectedRecommendation);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
     
    
    //--------------------------------------------------------
    
    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    

    public List<String> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<String> weekDays) {
        this.weekDays = weekDays;
    }
    
    public void issueConsumerRecomandationsOnWeeklyConsumption(ConsumerClient consumer){
        //get avg consumption
        //get peaks and optimum
        //create a recomandation based on pozitive peaks and negative peaks
    }

    public ArrayList<Object[]> getAvgConsumptionDaily() {
        return avgConsumptionDaily;
    }

    public void setAvgConsumptionDaily(ArrayList<Object[]> avgConsumptionDaily) {
        this.avgConsumptionDaily = avgConsumptionDaily;
    }

    public LineChartModel getDateModelCurrentAvg() {
        return dateModelCurrentAvg;
    }

    public void setDateModelCurrentAvg(LineChartModel dateModelCurrentAvg) {
        this.dateModelCurrentAvg = dateModelCurrentAvg;
    }

    public List<ConsumerRecommendation> getSelectedConsumerRecommendationList() {
        return selectedConsumerRecommendationList;
    }

    public void setSelectedConsumerRecommendationList(List<ConsumerRecommendation> selectedConsumerRecommendationList) {
        this.selectedConsumerRecommendationList = selectedConsumerRecommendationList;
    }

    public void setConsumerRecommendationList(List<ConsumerRecommendation> consumerRecommendationList) {
        this.consumerRecommendationList = consumerRecommendationList;
    }

    public boolean isNoDataCurrentAvg() {
        return noDataCurrentAvg;
    }

    public void setNoDataCurrentAvg(boolean noDataCurrentAvg) {
        this.noDataCurrentAvg = noDataCurrentAvg;
    }
    
    
    public List<ConsumerClient> getConsumerClientList() {
        return consumerClientList;
    }

    public void setConsumerClientList(List<ConsumerClient> consumerClientList) {
        this.consumerClientList = consumerClientList;
    }

    public List<Object[]> getAvgTotalConsumptionCurrent() {
        return avgTotalConsumptionCurrent;
    }

    public void setAvgTotalConsumptionCurrent(List<Object[]> avgTotalConsumptionCurrent) {
        this.avgTotalConsumptionCurrent = avgTotalConsumptionCurrent;
    }

    public List<Object[]> getAvgTotalConsumptionByDayOfWeek() {
        return avgTotalConsumptionByDayOfWeek;
    }

    public void setAvgTotalConsumptionByDayOfWeek(List<Object[]> avgTotalConsumptionByDayOfWeek) {
        this.avgTotalConsumptionByDayOfWeek = avgTotalConsumptionByDayOfWeek;
    }

    public LineChartModel getDateModelCurrentAvgDow() {
        return dateModelCurrentAvgDow;
    }

    public void setDateModelCurrentAvgDow(LineChartModel dateModelCurrentAvgDow) {
        this.dateModelCurrentAvgDow = dateModelCurrentAvgDow;
    }

    public boolean isNoDataCurrentAvgDow() {
        return noDataCurrentAvgDow;
    }

    public void setNoDataCurrentAvgDow(boolean noDataCurrentAvgDow) {
        this.noDataCurrentAvgDow = noDataCurrentAvgDow;
    }

    public List<ConsumerRecommendation> getSpecialDayRecommendationList() {
        return specialDayRecommendationList;
    }

    public void setSpecialDayRecommendationList(List<ConsumerRecommendation> specialDayRecommendationList) {
        this.specialDayRecommendationList = specialDayRecommendationList;
    }

    public List<ConsumerRecommendation> getWeekRecommendationList() {
        return weekRecommendationList;
    }

    public void setWeekRecommendationList(List<ConsumerRecommendation> weekRecommendationList) {
        this.weekRecommendationList = weekRecommendationList;
    }

    public boolean isHasSpecialDayRecomandationData() {
        return hasSpecialDayRecomandationData;
    }

    public void setHasSpecialDayRecomandationData(boolean hasSpecialDayRecomandationData) {
        this.hasSpecialDayRecomandationData = hasSpecialDayRecomandationData;
    }

    public boolean isHasWeeklyRecomandationData() {
        return hasWeeklyRecomandationData;
    }

    public void setHasWeeklyRecomandationData(boolean hasWeeklyRecomandationData) {
        this.hasWeeklyRecomandationData = hasWeeklyRecomandationData;
    }

    public ConsumerRecommendation getSelectedRecommendation() {
        return selectedRecommendation;
    }

    public void setSelectedRecommendation(ConsumerRecommendation selectedRecommendation) {
        this.selectedRecommendation = selectedRecommendation;
    }

    public ConsumerRecommendationFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ConsumerRecommendationFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    
//        public List<ConsumerClient> getItemsAvailableSelectOne() {
//        return consumerClientFacade.findAll();
//    }
//
//    public void setItemsAvailableSelectOne(List<ConsumerClient> itemsAvailableSelectOne) {
//        this.itemsAvailableSelectOne = itemsAvailableSelectOne;
//    }

    public List<ConsumerClient> getItemsAvailableSelectOne() {
        return itemsAvailableSelectOne;
    }

    public void setItemsAvailableSelectOne(List<ConsumerClient> itemsAvailableSelectOne) {
        this.itemsAvailableSelectOne = itemsAvailableSelectOne;
    }

    public ConsumerClient getSelectedConsumer() {
        return selectedConsumer;
    }

    public void setSelectedConsumer(ConsumerClient selectedConsumer) {
        this.selectedConsumer = selectedConsumer;
    }
    
    
    
    
}
