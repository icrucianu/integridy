/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.ValueOnChart;
import ro.siveco.cad.integridy.entities.CumulDsoD;
import ro.siveco.cad.integridy.entities.CumulDsoH;
import ro.siveco.cad.integridy.entities.CumulDsoM;
import ro.siveco.cad.integridy.entities.ForecastDso;
import ro.siveco.cad.integridy.entities.ForecastDsoW;

@Named("forecastController")
@ViewScoped
public class ForecastController implements Serializable {
    @EJB
    private DashboardService dashboardService;
    
    @EJB
    private CumulDsoHFacade cumulDsoHFacade;
    
    @EJB
    private CumulDsoDFacade cumulDsoDFacade;
    
    @EJB
    private ForecastDsoFacade forecastDsoFacade;
    
    @EJB
    private ForecastDsoWFacade forecastDsoWFacade;
    
    @EJB
    private CumulDsoMFacade cumulDsoMFacade;
    private List<CumulDsoH> cumulDSOHList;
    private List<Object[]> dailyResults;
    private List<Object[]> forecastDaily;
    private List<CumulDsoH> forecastDailyData;
    
    private List<Object[]> weeklyResults;
    private List<CumulDsoD> cumulDsoDList;
    private List<Object[]> forecastWeekly;
    private List<CumulDsoD> forecastWeekLyData;
    
    //monthly
    private List<CumulDsoD> forecastMonthlyData;
    private List<Object[]> monthlyResults;
     private List<Object[]> forecastMonthly;
    
    
    
    private List<CumulDsoM> cumulDsoMList;
    
    private Date refDate = null;
    private Date forecastDate=null;
    
    private LineChartModel dataModelDaily=null;
    private boolean noDataModelDaily=true;
    
    private LineChartModel dataModelWeekly=null;
    private boolean noDataModelWeekly=true;
    
    private LineChartModel dataModelMonthly=null;
    private boolean noDataModelMonthly=true;
    
    
    @PostConstruct
    public void init(){
        
        
        forecastDailyData = new ArrayList<>();
        forecastWeekLyData=new ArrayList<>();
        
        
        refDate = dashboardService.getCurrentDate();
        forecastDate = getForecastDate();
        

        
        
        //dailyResults
        cumulDSOHList = cumulDsoHFacade.getCumulDsoHbyDay(forecastDate);
        
        
        dailyResults = cumulDsoHFacade.getConsumedActivePowByDay(forecastDate);
        forecastDaily = createForecastDataModel(dailyResults);
       
        
        //weeklyresults
        cumulDsoDList = getWeeklyForecastResults(forecastDate);
        forecastWeekly = createForecastDataModel(weeklyResults);
        
        //monthly
        forecastMonthlyData = getMonthlyForecastResults(forecastDate);
        forecastMonthly = createForecastDataModel(monthlyResults);
        
        //results to insert in forecast tables 
        //data is not inserted anymore in data tables
//        forecastDailyData = getForecastDataDaily();
//        forecastWeekLyData = getForecastDataWeekly();
        

//        updateForcastDsoTable(forecastDailyData);
//        updateForecastDsoWTable(forecastWeekLyData);
       
        
        //replace date with future date in forecastDaily(for chart)
        //replace date with future date in forecastWeekly(for chart)
        forecastDaily=updateDateInList(forecastDaily);
        forecastWeekly=updateDateInList(forecastWeekly);
        forecastMonthly = updateDateInList(forecastMonthly);
        
        
        
        
        //chart data models (charts primefaces)
        dataModelDaily = addSerriesToChart(dataModelDaily, forecastDaily, "Daily Forecast ", "Forecast");
        dataModelWeekly = addSerriesToChart(dataModelWeekly, forecastWeekly, "Week forecast", "Forecast");
        dataModelMonthly = addSerriesToChart(dataModelMonthly, monthlyResults, "Month forecast", "Forecast");
        
        dataModelDaily.setExtender("chartExtender");;
        dataModelWeekly.setExtender("chartExtender");
        dataModelMonthly.setExtender("chartExtender");
        if(dataModelDaily!=null){
            noDataModelDaily = false;
        }
        if(dataModelWeekly!=null){
            noDataModelWeekly = false;
        }
        if(dataModelMonthly!=null){
            noDataModelMonthly = false;
        }
        
        //for chart js
        getForecastConsumptionGraphicData(forecastDaily, "canvas");
        getForecastConsumptionGraphicData(forecastWeekly, "canvasW");
        getForecastConsumptionGraphicData(forecastMonthly, "canvasM");
        
        
    }
    public List<Object[]> updateDateInList(List<Object[]> initialList){
        List<Object[]> resultList = new ArrayList<>();
        for(Object[] val:initialList){
            Object[] newVal = new Object[2];
            newVal[0] = val[0];
            newVal[1] = getNewDate((Date)val[1]);
            resultList.add(newVal);
        }
        return resultList;
    }
    
    private void updateForcastDsoTable(List<CumulDsoH> cumulDsoHList){
        for(CumulDsoH rec: cumulDsoHList){
            ForecastDso forecastData = new ForecastDso();
            forecastData.setActivePow(rec.getActivePow());
            forecastData.setCo2(rec.getCo2());
            forecastData.setConsumedActivePow(rec.getConsumedActivePow());
            forecastData.setConsumedReactivePow(rec.getConsumedReactivePow());
//            forecastData.setCreatedTime(refDate);
            forecastData.setDayCost(rec.getDayCost());
            forecastData.setDeviceNumber(rec.getDeviceNumber());
            forecastData.setDownTime(rec.getDownTime());
            forecastData.setEnergyMismatch(rec.getEnergyMismatch());
            forecastData.setEnergyMismatchRatio(rec.getEnergyMismatch());
            forecastData.setIdx12hours(rec.getIdx12hours());
            forecastData.setIdx15minutes(rec.getIdx15minutes());
            forecastData.setIdx1day(rec.getIdx1day());
            forecastData.setIdx1hour(rec.getIdx1hour());
            forecastData.setIdx1month(rec.getIdx1month());
            forecastData.setIdx1week(rec.getIdx1week());
            forecastData.setIdx1year(rec.getIdx1year());
            forecastData.setIdx3month(rec.getIdx3month());
            forecastData.setIdx6hours(rec.getIdx6hours());
            forecastData.setIdx6month(rec.getIdx6month());
            forecastData.setNightCost(rec.getNightCost());
            forecastData.setPhaseVoltage(rec.getPhaseVoltage());
            forecastData.setReactivePow(rec.getReactivePow());
            forecastData.setSaidi(rec.getSaidi());
            forecastData.setSendInterval(rec.getSendInterval());
            forecastData.setThdd(rec.getThdd());
//            forecastData.setUpTime(rec.getUpTime());
            try{
                forecastDsoFacade.create(forecastData);
            }catch(Exception ex){
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
            
        }
    }
    
    private void updateForecastDsoWTable(List<CumulDsoD> cumulDsoDlist){
        for(CumulDsoD rec: cumulDsoDlist){
            ForecastDsoW forecastData = new ForecastDsoW();
            forecastData.setActivePow(rec.getActivePow());
            forecastData.setCo2(rec.getCo2());
            forecastData.setConsumedActivePow(rec.getConsumedActivePow());
            forecastData.setConsumedReactivePow(rec.getConsumedReactivePow());
//            forecastData.setCreatedTime(getNewDate(rec.getCreatedTime()));
            forecastData.setDayCost(rec.getDayCost());
            forecastData.setDeviceNumber(rec.getDeviceNumber());
            forecastData.setDownTime(rec.getDownTime());
            forecastData.setEnergyMismatch(rec.getEnergyMismatch());
            forecastData.setEnergyMismatchRatio(rec.getEnergyMismatch());
            forecastData.setIdx12hours(rec.getIdx12hours());
            forecastData.setIdx15minutes(rec.getIdx15minutes());
            forecastData.setIdx1day(rec.getIdx1day());
            forecastData.setIdx1hour(rec.getIdx1hour());
            forecastData.setIdx1month(rec.getIdx1month());
            forecastData.setIdx1week(rec.getIdx1week());
            forecastData.setIdx1year(rec.getIdx1year());
            forecastData.setIdx3month(rec.getIdx3month());
            forecastData.setIdx6hours(rec.getIdx6hours());
            forecastData.setIdx6month(rec.getIdx6month());
            forecastData.setNightCost(rec.getNightCost());
            forecastData.setPhaseVoltage(rec.getPhaseVoltage());
            forecastData.setReactivePow(rec.getReactivePow());
            forecastData.setSaidi(rec.getSaidi());
            forecastData.setSendInterval(rec.getSendInterval());
            forecastData.setThdd(rec.getThdd());
//            forecastData.setUpTime(rec.getUpTime());
            try{
                forecastDsoWFacade.create(forecastData);
            }catch(Exception e){
                 Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
            
        }
    }
    
    public List<CumulDsoH> getForecastDataDaily(){
        List<CumulDsoH> result= new ArrayList<>();
        int n=0;
        for(Object[] val:forecastDaily){
            for(int i=n;n<cumulDSOHList.size()-1; i++){
                if(cumulDSOHList.get(i).getCreatedTime().equals((Date)val[1])){
                    result.add(cumulDSOHList.get(i));
                    //insert in forecast_dso table
                    n=i+1;
                    break;
                }
            }
        }
        return result;
    }
    
     public List<CumulDsoD> getForecastDataWeekly(){
        List<CumulDsoD> result= new ArrayList<>();
        int n=0;
        for(Object[] val:forecastWeekly){
            for(int i=n;n<cumulDsoDList.size()-1; i++){
                if(cumulDsoDList.get(i).getCreatedTime().equals((Date)val[1])){
                    result.add(cumulDsoDList.get(i));
                              
                    n=i+1;
                    break;
                }
            }
        }
        return result;
    }

    
    
    public void getForecastConsumptionGraphicData(List<Object[]> results, String canvasName) {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        List<ValueOnChart> pointstList = new ArrayList<>();
        if (results != null && results.size() > 0) {
            for (Object[] o : results) {
                pointstList.add(new ValueOnChart(df.format((Date) o[1]), (Double) o[0]));
            }
        }
        //Call js function to populate chart with data
        ObjectMapper objectMapper = new ObjectMapper();
        String forecastDataJSON;
        try {
            forecastDataJSON = objectMapper.writeValueAsString(pointstList);
            RequestContext.getCurrentInstance().execute("getForecastData('" + forecastDataJSON + "','" + canvasName + "')");
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ParentRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public List<Object[]>  createForecastDataModel(List<Object[]> registeredResults){
       
//        getRealConsumptionGraphicData(results);
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat titleDf = new SimpleDateFormat("yyyy-MM-dd");
        List<Object[]> forecastedDataSeries = new ArrayList<>();
        //Forcast is based on peaks detection for a simila dow from previous year
        // For each raw  read data, detect peak
        // peak positive is considered if previous 2 values and next 2 values are smaller
        // peak negative is considered if previous 2 values and next 2 values are bigger
         // so thta the total amount of power remain the same (MSE is used to compte the error)
        if (registeredResults != null && !registeredResults.isEmpty()) {

            int ii = 0;
            double delta = 0.;
            int noNeg = 0;
            int noPoz = 0;
            double lastPozPeak = 0.;
            double lastNegPeak = 0.;
            double difDelta = 0.;
          
            // for each row in dataset, see if it is a peack, and compute optimum
            // first and last points are considered both peack and optimum
            // first 4 and last 4 point can not be peacks or optimum
            for (Object[] res : registeredResults) {
                if (ii == 0) {
                    lastPozPeak = (Double) res[0];
                    lastNegPeak = (Double) res[0];
                    Object[] first = new Object[2];
                    first[0] = res[0];
                    first[1] = res[1];
                    forecastedDataSeries.add(first);
                   
                }
                if (ii == (registeredResults.size() - 1)) {
                    Object[] last = new Object[2];
                    last[0] = res[0];
                    last[1] = res[1];
                    forecastedDataSeries.add(last);
                }
                if (ii >= 2 && ii < registeredResults.size() - 2) {
                    // maximum local peak
                    if (((Double) registeredResults.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) registeredResults.get(ii - 2)[0] < (Double) res[0])
                            && ((Double) registeredResults.get(ii + 1)[0] < (Double) res[0])
                            &&((Double) registeredResults.get(ii + 2)[0] < (Double) res[0])
                            ) {
                        noPoz++;
                        forecastedDataSeries.add(res);
                    }
                    // minimum local peak
                    if (((Double) registeredResults.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) registeredResults.get(ii - 2)[0] > (Double) res[0])
                            && ((Double) registeredResults.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) registeredResults.get(ii + 2)[0] > (Double) res[0])
                            ) {
                        noNeg++;
                         forecastedDataSeries.add(res);
                    }
                } // if ii 4
                if(ii>=registeredResults.size() - 2){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=ii;li<registeredResults.size()-1;li++ ){
                         if((Double)res[0] < (Double)registeredResults.get(li)[0]){
                             isMax=false;
                         } else  if((Double)res[0] > (Double)registeredResults.get(li)[0]){
                             isMin = false;
                         }
                     }
                     //max
                     if (((Double) registeredResults.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) registeredResults.get(ii - 2)[0] < (Double) res[0])
                             && isMax){
                         forecastedDataSeries.add(res);
                     }
                     //min
                     if (((Double) registeredResults.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) registeredResults.get(ii - 2)[0] > (Double) res[0])
                             && isMin){
                         forecastedDataSeries.add(res);
                     }
                     
                 }
         
                 if(ii>0 && ii<2){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=0;li<2;li++ ){
                         if((Double)res[0] < (Double)registeredResults.get(li)[0]){
                             isMax=false;
                         } else if((Double)res[0] > (Double)registeredResults.get(li)[0]){
                             isMin = false;
                         }
                     }
                     if (   isMax
                            && ((Double) registeredResults.get(ii + 1)[0] < (Double) res[0])
                            && ((Double) registeredResults.get(ii + 2)[0] < (Double) res[0])
                             ) {
                        forecastedDataSeries.add(res);
                    }
                     //min
                     if (((Double) registeredResults.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) registeredResults.get(ii + 2)[0] > (Double) res[0])
                             && isMin){
                         forecastedDataSeries.add(res);
                     }
                 }
                ii++;
            } // for

           
        }
        return forecastedDataSeries;
    }
    
    
    public LineChartModel addSerriesToChart(LineChartModel dataModel, List<Object[]> resultsSerie, String setChartTitle, String seriesLabel) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LineChartModel ret;
        if (dataModel == null) {
            ret = new LineChartModel();
        } else {
            ret = dataModel;
        }
        LineChartSeries series1 = new LineChartSeries();
        for (Object[] val : resultsSerie) {
            series1.set(df.format((Date) val[1]), (Double) val[0]);
        }
        series1.setLabel(seriesLabel);
        ret.addSeries(series1);
        ret.setTitle(setChartTitle);
        ret.setZoom(true);
        ret.getAxis(AxisType.Y).setLabel("Consumed Active Pow ");
        ret.setShadow(false);
        ret.setAnimate(true);
        ret.setLegendPosition("ne");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-30);
        ret.getAxes().put(AxisType.X, axis);
        return ret;
    }
    public Date getNewDate(Date oldDate){
        
        if(refDate==null)
          return null;
        LocalDateTime localDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek dow = localDate.getDayOfWeek();
        localDate = localDate.plus(1,ChronoUnit.YEARS );
        localDate = localDate.with(TemporalAdjusters.previous(dow));
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }
    // get first dow with a   year before   
    
    public Date getForecastDate(){
      if(refDate==null)
          return null;
      LocalDateTime localDate = this.refDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
      DayOfWeek dow = localDate.getDayOfWeek();
      localDate = localDate.minus(1,ChronoUnit.YEARS );
      localDate = localDate.with(TemporalAdjusters.next(dow));
      return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public List<CumulDsoD> getWeeklyForecastResults(Date dtForecast){
        List<CumulDsoD> result = new ArrayList<>();
        LocalDateTime ld = dtForecast.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
       
        LocalDateTime ldWeekStart = null;
        if(ld.getDayOfWeek()!=DayOfWeek.MONDAY)
            ldWeekStart = ld.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        else
            ldWeekStart = ld;
        
        LocalDateTime ldWeekStop = null;
        if(ld.getDayOfWeek()!=DayOfWeek.SUNDAY)
            ldWeekStop = ld.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        else 
            ldWeekStop = ld;
        
        Date dateWeekStart = Date.from(ldWeekStart.atZone(ZoneId.systemDefault()).toInstant());
        Date dateWeekStop = Date.from(ldWeekStop.atZone(ZoneId.systemDefault()).toInstant());
        result = cumulDsoDFacade.getCumulDsoDByTimeInterval(dateWeekStart, dateWeekStop);
        weeklyResults=cumulDsoDFacade.getConsumedActivePowByTimeInterval(dateWeekStart, dateWeekStop);
        return result;
    }
    public List<CumulDsoD> getMonthlyForecastResults(Date dtForecast){
        List<CumulDsoD> result = new ArrayList<>();
        LocalDateTime ld = dtForecast.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
       
        LocalDateTime ldMonthStart = null;
        ldMonthStart = ld.with(TemporalAdjusters.firstDayOfMonth());
                
        LocalDateTime ldMonthStop = null;
        ldMonthStop = ld.with(TemporalAdjusters.lastDayOfMonth());
        
        Date dateMonthStart = Date.from(ldMonthStart.atZone(ZoneId.systemDefault()).toInstant());
        Date dateMonthStop = Date.from(ldMonthStop.atZone(ZoneId.systemDefault()).toInstant());
        result = cumulDsoDFacade.getCumulDsoDByTimeInterval(dateMonthStart, dateMonthStop);
        monthlyResults=cumulDsoDFacade.getConsumedActivePowByTimeInterval(dateMonthStart, dateMonthStop);
        return result;
    }
    public List<Object[]> getDailyResults() {
        return dailyResults;
    }

    public void setDailyResults(List<Object[]> dailyResults) {
        this.dailyResults = dailyResults;
    }

    public List<Object[]> getWeeklyResults() {
        return weeklyResults;
    }

    public void setWeeklyResults(List<Object[]> weeklyResults) {
        this.weeklyResults = weeklyResults;
    }

    public List<Object[]> getMonthlyResults() {
        return monthlyResults;
    }

    public void setMonthlyResults(List<Object[]> monthlyResults) {
        this.monthlyResults = monthlyResults;
    }

    public Date getRefDate() {
        return refDate;
    }

    public void setRefDate(Date refDate) {
        this.refDate = refDate;
    }

    public LineChartModel getDataModelDaily() {
        return dataModelDaily;
    }

    public void setDataModelDaily(LineChartModel dataModelDaily) {
        this.dataModelDaily = dataModelDaily;
    }

    public boolean isNoDataModelDaily() {
        return noDataModelDaily;
    }

    public void setNoDataModelDaily(boolean noDataModelDaily) {
        this.noDataModelDaily = noDataModelDaily;
    }

    public LineChartModel getDataModelWeekly() {
        return dataModelWeekly;
    }

    public void setDataModelWeekly(LineChartModel dataModelWeekly) {
        this.dataModelWeekly = dataModelWeekly;
    }

    public boolean isNoDataModelWeekly() {
        return noDataModelWeekly;
    }

    public void setNoDataModelWeekly(boolean noDataModelWeekly) {
        this.noDataModelWeekly = noDataModelWeekly;
    }

    public LineChartModel getDataModelMonthly() {
        return dataModelMonthly;
    }

    public void setDataModelMonthly(LineChartModel dataModelMonthly) {
        this.dataModelMonthly = dataModelMonthly;
    }

    public boolean isNoDataModelMonthly() {
        return noDataModelMonthly;
    }

    public void setNoDataModelMonthly(boolean noDataModelMonthly) {
        this.noDataModelMonthly = noDataModelMonthly;
    }
    
    
}
