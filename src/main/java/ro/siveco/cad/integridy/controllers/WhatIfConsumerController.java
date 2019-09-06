/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumptionPoint;
import ro.siveco.cad.integridy.entities.SmartMeter;
import ro.siveco.cad.integridy.entities.WhatIfParameters;
import ro.siveco.cad.integridy.entities.WhatifScenario;
import ro.siveco.cad.integridy.forecast.utils.ForecastUtils;

/**
 *
 * @author roxanam
 */
@Named("whatIfConsumerController")
@SessionScoped
public class WhatIfConsumerController implements Serializable {

    @EJB
    private WhatIfParametersFacade whatIfParametersFacade;

    @EJB
    private WhatIfScenarioFacade whatIfScenarioFacade;

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

    private List<WhatifScenario> whatIfScenarioList;
    private List<WhatIfParameters> whatIfParametersList;
    private WhatifScenario selectedScenario;
    private TreeNode root;
    private TreeNode selectedNode;

    private ConsumerClient selectedConsumer;

    private Integer selectedScenarioId;

    private LineChartModel chartDataModel = null;
    private boolean noDataModel = true;

    @PostConstruct
    public void init() {
        chartDataModel = new LineChartModel();
        chartDataModel.setExtender("chartExtender");
    }

    public void onSelectConsumer(AjaxBehaviorEvent event) {
        root = createTree();
        chartDataModel.clear();
        noDataModel = true;
    }

    public void onSelectScenario(AjaxBehaviorEvent event) {
        selectedScenario = whatIfScenarioFacade.find(selectedScenarioId);
        whatIfParametersList = whatIfParametersFacade.findByScenarioId(selectedScenario);
        chartDataModel.clear();
        noDataModel = true;

    }

    public void playScenario() {
        chartDataModel.clear();

        Date startDate = ForecastUtils.getForecastDate(selectedScenario.getReferenceStartPeriod());
        Date endDate = ForecastUtils.getForecastDate(selectedScenario.getReferenceEndPeriod());

        List<Object[]> allScenarioData = consumptionRecordFacade.getTotalConsumptionByClientIdAndPeriod(selectedConsumer.getId(), startDate, endDate);

        //get forecast data
        //getpeakdata //update date with new date
        List<Object[]> forecastData = ForecastUtils.createForecastDataModel(allScenarioData);
        forecastData = ForecastUtils.updateDateInList(forecastData);

        //forecast with interpolation data
        List<Object[]> completeForecastData = ForecastUtils.completeForecastData(forecastData, 15);
        chartDataModel = addSerriesToChart(chartDataModel, completeForecastData, "What if Scenario ", "Forecast consumption");

        //
        List<Object[]> whatIfData = getWhatIfData(completeForecastData, whatIfParametersList);

        //create data model for chart
//        chartDataModel = addSerriesToChart(chartDataModel, forecastData, "What if Scenario", "Forecast consumption");
        chartDataModel = addSerriesToChart(chartDataModel, whatIfData, "What if Scenario", "What-If consumption");

        if (chartDataModel != null) {
            noDataModel = false;
        }

    }

    public List<Object[]> getWhatIfData(List<Object[]> dataIn, List<WhatIfParameters> paramList) {
        List<Object[]> result = new ArrayList<>();
        double value = 0.;
        Date crtDate = null;
        for (Object[] data : dataIn) {
            Object[] newData = new Object[2];
            crtDate = (Date) data[1];
            value = (double) data[0];
            for (WhatIfParameters param : paramList) {
                if ((minutesOfTheDay(param.getStartTime()) <= minutesOfTheDay(crtDate) || param.getStartTime() == null)
                        && (minutesOfTheDay(param.getEndTime()) >= minutesOfTheDay(crtDate) || param.getEndTime() == null)) {
                    value = value + value * param.getParamDval();
                }
            }
            newData[0] = value;
            newData[1] = crtDate;
            result.add(newData);
        }
        return result;
    }

    private int minutesOfTheDay(Date date) {
        if (date == null) {
            return 0;
        }
        LocalDateTime ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return ld.getHour() * 60 + ld.getMinute();
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

    public WhatIfScenarioFacade getWhatIfScenarioFacade() {
        return whatIfScenarioFacade;
    }

    public void setWhatIfScenarioFacade(WhatIfScenarioFacade whatIfScenarioFacade) {
        this.whatIfScenarioFacade = whatIfScenarioFacade;
    }

    public List<WhatifScenario> getWhatIfScenarioList() {
        if (whatIfScenarioList == null) {
            whatIfScenarioList = whatIfScenarioFacade.findAllOrderById();
        }
        return whatIfScenarioList;
    }

    public void setWhatIfScenarioList(List<WhatifScenario> whatIfScenarioList) {
        this.whatIfScenarioList = whatIfScenarioList;
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

    public ConsumerClient getSelectedConsumer() {
        return selectedConsumer;
    }

    public void setSelectedConsumer(ConsumerClient selectedConsumer) {
        this.selectedConsumer = selectedConsumer;
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
