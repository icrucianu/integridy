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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.controllers.util.ValueOnChart;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumptionPoint;
import ro.siveco.cad.integridy.entities.ConsumptionRecord;
import ro.siveco.cad.integridy.entities.SmartMeter;
import ro.siveco.cad.integridy.forecast.utils.ForecastUtils;

/**
 *
 * @author roxanam
 */
@Named("forecastConsumerController")
@SessionScoped
public class ForecastConsumerController implements Serializable{
    
    @EJB
    private DashboardService dashboardService;
    @EJB
    private ro.siveco.cad.integridy.controllers.ConsumptionRecordFacade consumptionRecordFacade;
    
    @EJB
    private CumulClientDFacade cumulClientDFacade;
    @EJB
    private ConsumptionPointFacade consumptionPointFacade;
    @EJB
    private SmartMeterFacade smartMeterFacade;
    
//    private List<ConsumptionRecord> items = null;
//    private List<ConsumptionRecord> itemsCurrentClient = null;
//    private ConsumptionRecord selected;
//    
    //daily
    private List<Object[]> dailyConsumptionRecords;
    private boolean  noDataDaily=true;
    private List<Object[]> forecastDailyConsumption;
   
    private boolean noDataWeekly=true;
    private boolean noDataMonthly=true;
    
    private LineChartModel dailyForecastModel;
    private LineChartModel weeklyForecastModel;
    private LineChartModel monthlyForecastModel;
    
    
    
    private TreeNode root;
    private TreeNode selectedNode;
    
    private ConsumerClient selectedConsumer;

    @PostConstruct
    public void init(){
       referenceDate = dashboardService.getCurrentDate();
       dailyForecastModel = new LineChartModel();
       weeklyForecastModel = new LineChartModel();
       monthlyForecastModel = new LineChartModel();
       dailyForecastModel.setExtender("chartExtender");
       weeklyForecastModel.setExtender("chartExtender");
       monthlyForecastModel.setExtender("chartExtender");
        
    }
    
    public void onSelectConsumer(AjaxBehaviorEvent event){
        root = createTree();
        
        noDataDaily = true;
        noDataWeekly = true;
        noDataMonthly=true;
        
        dailyForecastModel.clear();
        weeklyForecastModel.clear();
        monthlyForecastModel.clear();
        
        if(selectedConsumer!=null){
            referenceDate = dashboardService.getCurrentDate();
            dailyConsumptionRecords = consumptionRecordFacade.getTotalConsumptionByClient(selectedConsumer.getId(), ForecastUtils.getForecastDate(referenceDate));
            forecastDailyConsumption = ForecastUtils.createForecastDataModel(dailyConsumptionRecords);
            forecastDailyConsumption = ForecastUtils.updateDateInList(forecastDailyConsumption);
            forecastDailyConsumption = ForecastUtils.completeForecastData(forecastDailyConsumption, 15);
            dailyForecastModel = addSerriesToChart(dailyForecastModel, forecastDailyConsumption, "Forecast consumption ", "Forecast consumption");
            
            if(dailyForecastModel!=null ){
                noDataDaily = false;
            }
            
            //weekly
             LocalDateTime ld = ForecastUtils.getForecastDate(referenceDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
       
            LocalDateTime ldWeekStart = null;
            if(ld.getDayOfWeek()!=DayOfWeek.MONDAY)
                ldWeekStart = ld.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            else
                ldWeekStart = ld;
             Date dateWeekStart = Date.from(ldWeekStart.atZone(ZoneId.systemDefault()).toInstant());

            LocalDateTime ldWeekStop = null;
//            if(ld.getDayOfWeek()!=DayOfWeek.SUNDAY)
                ldWeekStop = ld.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
//            else 
//                ldWeekStop = ld;
            Date dateWeekStop = Date.from(ldWeekStop.atZone(ZoneId.systemDefault()).toInstant());

            List<Object[]> dataWeek = cumulClientDFacade.getTotalConsumptionByClientAndPeriod(selectedConsumer.getId(), dateWeekStart, dateWeekStop);
            List<Object[]> forecastweek = ForecastUtils.createForecastDataModel(dataWeek);
            forecastweek = ForecastUtils.updateDateInList(forecastweek);
            
            weeklyForecastModel =  addSerriesToChart(weeklyForecastModel, forecastweek, "Week forecast ", "Forecast dayly consumption");

            if(weeklyForecastModel!=null){
                noDataWeekly=false;
            }
        
            //monthly
//             LocalDateTime ld = ForecastUtils.getForecastDate(referenceDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
       
            LocalDateTime ldMonthStart = null;
            ldMonthStart = ld.with(TemporalAdjusters.firstDayOfMonth());

            LocalDateTime ldMonthStop = null;
            ldMonthStop = ld.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

            Date dateMonthStart = Date.from(ldMonthStart.atZone(ZoneId.systemDefault()).toInstant());
            Date dateMonthStop = Date.from(ldMonthStop.atZone(ZoneId.systemDefault()).toInstant());
            
            
             List<Object[]> dataMonth = cumulClientDFacade.getTotalConsumptionByClientAndPeriod(selectedConsumer.getId(), dateMonthStart, dateMonthStop);
            List<Object[]> forecastmonth = ForecastUtils.createForecastDataModel(dataMonth);
            forecastmonth = ForecastUtils.updateDateInList(forecastmonth);
            
            monthlyForecastModel =  addSerriesToChart(monthlyForecastModel, forecastmonth, "Month forecast ", "Forecast daily consumption");

            if(monthlyForecastModel!=null){
                noDataMonthly=false;
            }
            
             //for chart js
        getForecastConsumptionGraphicData(forecastDailyConsumption, "canvasConsumerD");
        getForecastConsumptionGraphicData(forecastweek, "canvasConsumerW");
        getForecastConsumptionGraphicData(forecastmonth, "canvasConsumerM");
        }
        
        
       
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
    
    private  LineChartModel addSerriesToChart(LineChartModel dataModel, List<Object[]> resultsSerie, String setChartTitle, String seriesLabel) {
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
    private Date referenceDate;
    
    public TreeNode createTree() {
       
        TreeNode root = new DefaultTreeNode(new DevicesNode(0, "", "root", "root"), null);
        List<ConsumptionPoint> consumptionPointList = consumptionPointFacade.getByClientId(selectedConsumer.getId());
        for (ConsumptionPoint cp : consumptionPointList) {
            TreeNode cpNode = new DefaultTreeNode("CP", new DevicesNode(cp.getId(), cp.getPointType(), "CP", cp.getPointName()), root);
            List<SmartMeter> smartMeters = smartMeterFacade.getByConsumptionPointId(cp.getId());

            for (SmartMeter sm : smartMeters) {
                TreeNode smNode = new DefaultTreeNode("SM", new DevicesNode(sm.getId(), sm.getDeviceType(), "SM", sm.getDeviceName()), cpNode);
            }
        }
        return root;
    }

    public boolean isNoDataMonthly() {
        return noDataMonthly;
    }

    public void setNoDataMonthly(boolean noDataMonthly) {
        this.noDataMonthly = noDataMonthly;
    }

    
    public boolean isNoDataDaily() {
        return noDataDaily;
    }

    public void setNoDataDaily(boolean noDataDaily) {
        this.noDataDaily = noDataDaily;
    }
    
//    public List<ConsumptionRecord> getItems() {
//        return items;
//    }
//
//    public void setItems(List<ConsumptionRecord> items) {
//        this.items = items;
//    }
//
//    public List<ConsumptionRecord> getItemsCurrentClient() {
//        return itemsCurrentClient;
//    }
//
//    public void setItemsCurrentClient(List<ConsumptionRecord> itemsCurrentClient) {
//        this.itemsCurrentClient = itemsCurrentClient;
//    }
//
//    public ConsumptionRecord getSelected() {
//        return selected;
//    }
//
//    public void setSelected(ConsumptionRecord selected) {
//        this.selected = selected;
//    }

    public ConsumerClient getSelectedConsumer() {
        return selectedConsumer;
    }

    public void setSelectedConsumer(ConsumerClient selectedConsumer) {
        this.selectedConsumer = selectedConsumer;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public List<Object[]> getDailyConsumptionRecords() {
        return dailyConsumptionRecords;
    }

    public void setDailyConsumptionRecords(List<Object[]> dailyConsumptionRecords) {
        this.dailyConsumptionRecords = dailyConsumptionRecords;
    }

    public LineChartModel getDailyForecastModel() {
        return dailyForecastModel;
    }

    public void setDailyForecastModel(LineChartModel dailyForecastModel) {
        this.dailyForecastModel = dailyForecastModel;
    }

    public LineChartModel getWeeklyForecastModel() {
        return weeklyForecastModel;
    }

    public void setWeeklyForecastModel(LineChartModel weeklyForecastModel) {
        this.weeklyForecastModel = weeklyForecastModel;
    }

    public LineChartModel getMonthlyForecastModel() {
        return monthlyForecastModel;
    }

    public void setMonthlyForecastModel(LineChartModel monthlyForecastModel) {
        this.monthlyForecastModel = monthlyForecastModel;
    }

    public boolean isNoDataWeekly() {
        return noDataWeekly;
    }

    public void setNoDataWeekly(boolean noDataWeekly) {
        this.noDataWeekly = noDataWeekly;
    }
    
    
}
