/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.entities.WhatIfParameters;
import ro.siveco.cad.integridy.entities.WhatifScenario;
import ro.siveco.cad.integridy.forecast.utils.ForecastUtils;

/**
 *
 * @author roxanam
 */
@Named("whatIfController")
@SessionScoped
public class WhatIfController implements Serializable{
    @EJB
    private WhatIfParametersFacade whatIfParametersFacade;
    @EJB
    private WhatIfScenarioFacade whatIfScenarioFacade;
    @EJB
    private CumulDsoHFacade cumulDsoHFacade;
    
    private List<WhatifScenario> whatIfScenarioList;
    
    private List<WhatIfParameters> whatIfParametersList;
    
    
    private WhatifScenario selectedScenario; 
    
   private WhatifScenario newScenario;
    private Integer selectedScenarioId;      
    
    private WhatIfParameters selectedWhatIfParameters;
    
    private boolean hasNewParam = false;
    
    private LineChartModel chartDataModel=null;
    private boolean noDataModel=true;
            
    @PostConstruct
    public void init(){
        whatIfScenarioList = whatIfScenarioFacade.findAllOrderById();
        whatIfParametersList = new ArrayList<>();
        chartDataModel = new LineChartModel();
        chartDataModel.setExtender("chartExtender");
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
    public void prepareCreateScenario(){
        newScenario = new WhatifScenario();
    }
    public void createScenario(){
        newScenario.setCreatedOn(new Date());
        chartDataModel.clear();
        noDataModel = true;
        selectedScenario = persistWhatIfScenario(JsfUtil.PersistAction.CREATE, "What-If scenario created!");
        if(selectedScenario!=null){
            selectedScenarioId = selectedScenario.getId();
            
            refreshParametersList();
        }
       if (!JsfUtil.isValidationFailed()) {
            whatIfScenarioList = null;    // Invalidate list of items to trigger re-query.
        }
       
    }
    
    public void onRowEdit(RowEditEvent event) {
        //persist parameter
       
            WhatIfParameters crtParam = (WhatIfParameters) event.getObject();
            if(crtParam.getId()==999999){
                crtParam.setId(null);
                hasNewParam=false;
                persistWhatIfParameter(crtParam, JsfUtil.PersistAction.CREATE, "Parameter created!");
                

            }else{
                  persistWhatIfParameter(crtParam, JsfUtil.PersistAction.UPDATE, "Parameter updated!");
                
            }
           refreshParametersList();
        
        
    }
     
    public void onRowCancel(RowEditEvent event) {
        if(((WhatIfParameters) event.getObject()).getId()==999999){//delete only from list last item
           whatIfParametersList.remove(whatIfParametersList.size()-1);
           hasNewParam=false;
           JsfUtil.addSuccessMessage("Edit Cancelled!");
        }
       refreshParametersList();
        
    }
 
    public void onAddNew() {
        if(!hasNewParam){
            hasNewParam=true;
            // Add one new car to the table:
            WhatIfParameters newParameter = new WhatIfParameters();
            newParameter.setScenarioId(selectedScenario);
            newParameter.setId(999999);
            if(whatIfParametersList==null)
                whatIfParametersList = new ArrayList<>();
            whatIfParametersList.add(newParameter);


           
        }
    }
    public void deleteParameter(int id){
        if(id==999999){//delete only from list last item
           whatIfParametersList.remove(whatIfParametersList.size()-1);
           hasNewParam=false;
        }
        else{
            persistWhatIfParameter(whatIfParametersFacade.find(id), JsfUtil.PersistAction.DELETE, "Parameter deleted!");
            refreshParametersList();
        }
    }
    private WhatifScenario persistWhatIfScenario(JsfUtil.PersistAction persistAction, String successMessage) {
        if (newScenario != null) {
          
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                     WhatifScenario result = whatIfScenarioFacade.edit(newScenario);
                     JsfUtil.addSuccessMessage(successMessage);
                     return result;
                } else {
                    whatIfScenarioFacade.remove(newScenario);
                    JsfUtil.addSuccessMessage(successMessage);
                }
                
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
        return null;
    }
    private WhatIfParameters persistWhatIfParameter(WhatIfParameters parameter, JsfUtil.PersistAction persistAction, String successMessage) {
        if (parameter != null) {
          
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    WhatIfParameters result =  whatIfParametersFacade.edit(parameter);
                    JsfUtil.addSuccessMessage(successMessage);
                     return result;
                } else {
                    whatIfParametersFacade.remove(parameter);
                    JsfUtil.addSuccessMessage(successMessage);
                }
                
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
        return null;
    }
    
    public void refreshParametersList(){
        whatIfParametersList.clear();
        if(selectedScenario!=null)
            whatIfParametersList.addAll(whatIfParametersFacade.findByScenarioId(selectedScenario));
    }
    public void deleteScenario(){
        newScenario= selectedScenario;
        persistWhatIfScenario(JsfUtil.PersistAction.DELETE, "What-If scenario deleted!");
        selectedScenario = null;
       if (!JsfUtil.isValidationFailed()) {
            whatIfScenarioList = null;    // Invalidate list of items to trigger re-query.
        }
    }
    public void updateScenario(){
         newScenario= selectedScenario;
       persistWhatIfScenario(JsfUtil.PersistAction.UPDATE, "What-If scenario updated!");
       if (!JsfUtil.isValidationFailed()) {
            whatIfScenarioList = null;    // Invalidate list of items to trigger re-query.
        }
    }
    public List<WhatifScenario> getWhatIfScenarioList() {
        if(whatIfScenarioList==null)
            whatIfScenarioList = whatIfScenarioFacade.findAllOrderById();
        return whatIfScenarioList;
    }
    public void onSelectScenario(AjaxBehaviorEvent event){
        chartDataModel.clear();
        noDataModel=true;
        selectedScenario = whatIfScenarioFacade.find(selectedScenarioId);
        whatIfParametersList.clear();
        whatIfParametersList.addAll(whatIfParametersFacade.findByScenarioId(selectedScenario));
        
    }
    public void setWhatIfScenarioList(List<WhatifScenario> whatIfScenarioList) {
        this.whatIfScenarioList = whatIfScenarioList;
    }
    
    public void playScenario(){
        //get registered data
        chartDataModel.clear();
         
       Date startDate = ForecastUtils.getForecastDate(selectedScenario.getReferenceStartPeriod());
        Date endDate = ForecastUtils.getForecastDate(selectedScenario.getReferenceEndPeriod());
        
        List<Object[]> allScenarioData = cumulDsoHFacade.getConsumedActivePowByPeriod(startDate, endDate);
        
        //get forecast data
        //getpeakdata //update date with new date
        List<Object[]> forecastData = ForecastUtils.createForecastDataModel(allScenarioData);
        forecastData = ForecastUtils.updateDateInList(forecastData);
        
        
        //forecast with interpolation data
        List<Object[]> completeForecastData = completeForecastData(forecastData);
        chartDataModel = addSerriesToChart(chartDataModel, completeForecastData, "What if Scenario", "Forecast consumption");
        
        //
        List<Object[]> whatIfData = getWhatIfData(completeForecastData,whatIfParametersList);
        
        
        //create data model for chart
//        chartDataModel = addSerriesToChart(chartDataModel, forecastData, "What if Scenario", "Forecast consumption");
        chartDataModel = addSerriesToChart(chartDataModel, whatIfData,  "What if Scenario", "What-If consumption");
        
        if(chartDataModel!=null){
            noDataModel = false;
        }
        
        
    }
    
    
    
    private int minutesOfTheDay(Date date){
        if(date==null)
            return 0;
        LocalDateTime ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return ld.getHour()*60+ld.getMinute();
    }
    private List<Object[]> completeForecastData(List<Object[]> dataIn){
        if(dataIn==null || dataIn.isEmpty())
            return null;
        List<Object[]> result = new ArrayList<>();
        
        Date start = (Date)dataIn.get(0)[1];
        Date stop = (Date)dataIn.get(dataIn.size()-1)[1];
         LocalDateTime localDateStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
         LocalDateTime localDateStop = stop.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
         LocalDateTime crtTime = localDateStart;
         int i = 0;
         Object[] data1 = dataIn.get(i);
         result.add(data1);
         Object[] data2 = dataIn.get(i+1);
         double value = 0.;
         LocalDateTime fdate1 = localDateStart;
         LocalDateTime fDate2 = ((Date)data2[1]).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
         while(crtTime.isBefore(localDateStop)){
             
             crtTime = crtTime.plusHours(1);
             Date crtDate = Date.from(crtTime.atZone(ZoneId.systemDefault()).toInstant());
             if(crtTime.isEqual(fDate2) || crtTime.isAfter(fDate2)){
                 i++;
                 result.add(dataIn.get(i));
                 
                 data1=dataIn.get(i);
                 fdate1 = ((Date)data1[1]).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                 if(i<dataIn.size()-1){
                     data2=dataIn.get(i+1);
                     fDate2 = ((Date)data2[1]).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                 } else {
                     break;
                 }
                 continue;
             }
             
             //calcul noua valoare in crtTime
             double slope = ((double)data2[0]-(double)data1[0])/(double)(((Date)data2[1]).getTime() - ((Date)data1[1]).getTime());
             value = (double)data1[0] + (crtDate.getTime() - ((Date)data1[1]).getTime())*slope;
             Object[] newData = new Object[2];
             newData[0] = value;
             newData[1] = crtDate;
             result.add(newData);
         }
        return result;
    }
    public List<Object[]> getWhatIfData(List<Object[]> dataIn, List<WhatIfParameters> paramList){
        List<Object[]> result = new ArrayList<>();
        double value = 0.;
        Date crtDate = null;
        for(Object[] data:dataIn){
            Object[] newData = new Object[2];
            crtDate = (Date)data[1];
            value = (double)data[0];
            for(WhatIfParameters param: paramList){
               if((minutesOfTheDay(param.getStartTime())<=minutesOfTheDay(crtDate) || param.getStartTime()==null  ) && 
                  (minutesOfTheDay(param.getEndTime())>=minutesOfTheDay(crtDate) || param.getEndTime()==null  ) ){
                    value = value + value*param.getParamDval();
                }
            }
            newData[0] = value;
            newData[1] = crtDate;
            result.add(newData);
        }
        return result;
    }
    public List<WhatIfParameters> getWhatIfParametersList() {
        return whatIfParametersList;
    }

    public void setWhatIfParametersList(List<WhatIfParameters> whatIfParametersList) {
        this.whatIfParametersList = whatIfParametersList;
    }

    public WhatifScenario getSelectedScenario() {
        return selectedScenario;
    }

    public void setSelectedScenario(WhatifScenario selectedScenario) {
        this.selectedScenario = selectedScenario;
    }

    public WhatifScenario getNewScenario() {
        return newScenario;
    }

    public void setNewScenario(WhatifScenario newScenario) {
        this.newScenario = newScenario;
    }

    public Integer getSelectedScenarioId() {
        return selectedScenarioId;
    }

    public void setSelectedScenarioId(Integer selectedScenarioId) {
        this.selectedScenarioId = selectedScenarioId;
    }

    public LineChartModel getChartDataModel() {
        return chartDataModel;
    }

    public void setChartDataModel(LineChartModel chartDataModel) {
        this.chartDataModel = chartDataModel;
    }

    public boolean isNoDataModel() {
        return noDataModel;
    }

    public void setNoDataModel(boolean noDataModel) {
        this.noDataModel = noDataModel;
    }
    
    
    
    
    
}
