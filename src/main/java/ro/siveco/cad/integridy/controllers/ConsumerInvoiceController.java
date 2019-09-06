/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import ro.siveco.cad.integridy.controllers.util.InvoiceData;
import ro.siveco.cad.integridy.controllers.util.PriceType;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumerPrices;
import ro.siveco.cad.integridy.entities.ConsumptionPoint;
import ro.siveco.cad.integridy.entities.ConsumptionRecord;
import ro.siveco.cad.integridy.entities.SmartMeter;

@Named("consumerInvoiceController")
@SessionScoped
public class ConsumerInvoiceController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.ConsumerClientFacade consumerFacade;
    private List<ConsumerClient> consumers = null;
    protected ConsumerClient selectedConsumer;
    private boolean showConsumerGraph = false;

    private boolean viewDetailedChart;
    
    @EJB
    private DashboardService dashboardService;
    

    @EJB
    private ConsumptionPointFacade consumptionPointFacade;
    private List<ConsumptionPoint> consumptionPointList;

    @EJB
    private SmartMeterFacade smartMeterFacade;
    private List<SmartMeter> smartMeterList;

    @EJB
    private ConsumerPricesFacade consumerPricesFacade;
    private List<ConsumerPrices> validPricesList;
    protected ConsumerPrices selectedPrice;
    
    
    @EJB
    private UsersFacade usersFacade;

  

    //current data
    @EJB
    protected ConsumptionRecordFacade consumptionRecordFacade;
    protected List<ConsumptionRecord> consumptionDayList;

    private List<SelectItem> consumerDevices = null;

    private List<ConsumerClient> selectedConsumers;
    private TreeNode root;
    private TreeNode selectedNode;

    private Map<Integer, String> devicesNames;

    private String fileName = "Report";

    protected LineChartModel dataModelCostTotalDay;
    protected boolean noChartDataDay = true;
    protected Double totalDayCost;

    protected LineChartModel dataModelCostTotalMonth;
    protected boolean noChartDataMonth = true;

    protected double monthTotalCost;

    protected LineChartModel dataModelCostTotalYear;
    protected boolean noChartDataYear = true;

    private Double yearTotalCost;

    protected List<InvoiceData> invoiceDataDay;
    protected List<InvoiceData> invoiceDataMonth;
    protected List<InvoiceData> invoiceDataYear;
    
    private boolean isConsumer=false;

    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @PostConstruct
    public void init() {
         consumerDevices = new ArrayList<SelectItem>();
        if (validPricesList != null && !validPricesList.isEmpty()) {
            selectedPrice = validPricesList.get(0);
        }
        smartMeterList = new ArrayList<>();
        devicesNames = new HashMap<>();
        dataModelCostTotalDay = new LineChartModel();
        dataModelCostTotalMonth = new LineChartModel();
        dataModelCostTotalYear = new LineChartModel();
        dataModelCostTotalDay.setExtender("myLineChartExtender");
        dataModelCostTotalMonth.setExtender("myLineChartExtender");
        dataModelCostTotalYear.setExtender("myLineChartExtender");
        invoiceDataDay = new ArrayList<>();
        invoiceDataMonth = new ArrayList<>();
        invoiceDataYear = new ArrayList<>();
        dataModelCostTotalDay.setLegendPosition("nw");
        selectedConsumer=dashboardService.getCurrentConsumerClient();
        isConsumer = "consumer".equalsIgnoreCase(usersFacade.findByUserName(dashboardService.getUsername()).getRole());
        if(selectedConsumer==null){
            getConsumers();
           
        } else {
           
            onSelectConsumer(null);
        }
       
    }

    public List<ConsumptionPoint> getConsumptionPoints(ConsumerClient consumer) {
        return consumptionPointFacade.getByClientId(consumer.getId());
    }

    public TreeNode createTree() {
        if (dashboardService.getCurrentConsumerClient() == null) {
            return null;
        }
        TreeNode root = new DefaultTreeNode(new DevicesNode(0, "", "root", "root"), null);
        List<ConsumptionPoint> consumptionPointList = consumptionPointFacade.getByClientId(dashboardService.getCurrentConsumerClient().getId());
        for (ConsumptionPoint cp : consumptionPointList) {
            TreeNode cpNode = new DefaultTreeNode("CP", new DevicesNode(cp.getId(), cp.getPointType(), "CP", cp.getPointName()), root);
            List<SmartMeter> smartMeters = smartMeterFacade.getByConsumptionPointId(cp.getId());

            for (SmartMeter sm : smartMeters) {
                TreeNode smNode = new DefaultTreeNode("SM", new DevicesNode(sm.getId(), sm.getDeviceType(), "SM", sm.getDeviceName()), cpNode);
            }
        }
        return root;
    }

    public Double hourConsumptionCost(ConsumptionRecord cumulH, ConsumerPrices price) {
        Double cost = 0.;
        if (price.getPriceType().equals(PriceType.UNDIFFERENTIATED)) {
            return cumulH.getConsumedActivePow() * price.getPriceVal() / 1000.;
        } else {
            if (isDayInterval(cumulH.getCreatedTime(), price.getStartTimePeriod(), price.getEndTimePeriod())) {
                return cumulH.getConsumedActivePow() * price.getPriceVal() / 1000.;
            } else {
                return cumulH.getConsumedActivePow() * price.getNightPriceVal() / 1000.;
            }
        }
    }

    public Double hourConsumptionCost(ConsumptionRecord cumulH) {
        Double cost = 0.;
        if (selectedPrice.getPriceType().equalsIgnoreCase(PriceType.UNDIFFERENTIATED.toString())) {
            return cumulH.getConsumedActivePow() * selectedPrice.getPriceVal() / 1000.;
        } else {
            if (isDayInterval(cumulH.getCreatedTime(), selectedPrice.getStartTimePeriod(), selectedPrice.getEndTimePeriod())) {
                return cumulH.getConsumedActivePow() * selectedPrice.getPriceVal() / 1000.;
            } else {
                return cumulH.getConsumedActivePow() * selectedPrice.getNightPriceVal() / 1000.;
            }
        }
    }

    public Double hourConsumptionCost(Double consumedActivePower, Date timeStamp) {
        if (selectedPrice == null) {
            return null;
        }
        Double cost = 0.;
        if (PriceType.UNDIFFERENTIATED.toString().equalsIgnoreCase(selectedPrice.getPriceType())) {
            return consumedActivePower * selectedPrice.getPriceVal() / 1000.;
        } else {
            if (isDayInterval(timeStamp, selectedPrice.getStartTimePeriod(), selectedPrice.getEndTimePeriod())) {
                return consumedActivePower * selectedPrice.getPriceVal() / 1000.;
            } else {
                return consumedActivePower * selectedPrice.getNightPriceVal() / 1000.;
            }
        }
    }

    protected boolean isDayInterval(Date createdDate, Date startDay, Date endDay) {
        LocalTime createdTime = LocalDateTime.ofInstant(createdDate.toInstant(), ZoneId.systemDefault()).toLocalTime();
        LocalTime t1 = LocalDateTime.ofInstant(startDay.toInstant(), ZoneId.systemDefault()).toLocalTime();
        LocalTime t2 = LocalDateTime.ofInstant(endDay.toInstant(), ZoneId.systemDefault()).toLocalTime();
        if (t1.isBefore(createdTime) && t2.isAfter(createdTime)) {
            return true;
        } else {
            return false;
        }

    }

    public void onSelectConsumer(AjaxBehaviorEvent event) {
        dashboardService.setCurrentConsumerClient(selectedConsumer);
        setShowConsumerGraph(selectedConsumer != null);
        consumerDevices.clear();
        root = createTree();
        smartMeterList = smartMeterFacade.getByConsumerId(selectedConsumer.getId());

    }

    public String getDeviceName(int deviceNumber) {
        for (SmartMeter sm : smartMeterList) {
            if (sm.getId() == deviceNumber) {
                return sm.getDeviceName() + "(" + sm.getDeviceType() + ") - " + sm.getSerialNo();
            }
        }
        return "" + deviceNumber;
    }

    public void clearChart() {
        dataModelCostTotalDay.clear();
        noChartDataDay = true;
        dataModelCostTotalMonth.clear();
        noChartDataMonth = true;
        dataModelCostTotalYear.clear();
        noChartDataYear = true;
        invoiceDataDay.clear();
        invoiceDataMonth.clear();
        invoiceDataYear.clear();
    }

    public void computeCosts() {
        LocalDateTime refDate = LocalDateTime.ofInstant(getReferenceDate().toInstant(), ZoneId.systemDefault());
        //for current day  
        consumptionDayList = consumptionRecordFacade.getByClientAndRefDate(selectedConsumer.getId(), getReferenceDate());
        
        ChartSeries totalChart = new ChartSeries();
        totalChart.setLabel("Total " + selectedConsumer.getFullName() + " - " + selectedPrice.getPriceDef());

        Map<Date, Double> totalConsumptionPerTimestamp = consumptionDayList.stream().collect(
                Collectors.groupingBy(ConsumptionRecord::getCreatedTime, Collectors.summingDouble(ConsumptionRecord::getConsumedActivePow)));
        Map<Date, Double> treeMap = new TreeMap<Date, Double>(totalConsumptionPerTimestamp);
        Double totalDay = 0.;
        for (Date date : treeMap.keySet()) {
            totalChart.set(df.format(date), hourConsumptionCost(treeMap.get(date), date));
            totalDay += hourConsumptionCost(treeMap.get(date), date);
        }
        totalDayCost = totalDay;
        dataModelCostTotalDay.addSeries(totalChart);
        double totalDayConsumption = 0.;
        
        totalDayConsumption = consumptionDayList.stream().map(ConsumptionRecord::getConsumedActivePow).mapToDouble(Double::doubleValue).sum();
        if (viewDetailedChart) {
            for (SmartMeter sm : smartMeterList) {
                //filter main list for each device
                List<ConsumptionRecord> listDeviceConsumption = consumptionDayList.stream()
                        .filter(rec -> rec.getDeviceNumber().equals(sm.getId()))
                        .collect(Collectors.toList());

                ChartSeries deviceSeries = new ChartSeries();
                deviceSeries.setLabel(getDeviceName(sm.getId()));
                for (ConsumptionRecord rec : listDeviceConsumption) {
                    deviceSeries.set(df.format(rec.getCreatedTime()), hourConsumptionCost(rec.getConsumedActivePow(), rec.getCreatedTime()));
                }
                dataModelCostTotalDay.addSeries(deviceSeries);

            }
        }
        SimpleDateFormat titleDFDay = new SimpleDateFormat("dd/MM/YYYY");
        dataModelCostTotalDay.setTitle("Consumed Active Power Cost - " + titleDFDay.format(getReferenceDate()));
        dataModelCostTotalDay.setZoom(true);
        dataModelCostTotalDay.getAxis(AxisType.Y).setLabel("Consumed Active Pow Cost ");
        dataModelCostTotalDay.setShadow(false);
        dataModelCostTotalDay.setAnimate(true);
        dataModelCostTotalDay.setLegendPosition("ne");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-30);
        axis.setTickFormat("%#d,%H:%M");
        dataModelCostTotalDay.getAxes().put(AxisType.X, axis);
        if (dataModelCostTotalDay != null) {
            noChartDataDay = false;
        } else {
            noChartDataDay = true;
        }

        invoiceDataDay.add(new InvoiceData(invoiceDataDay.size(), selectedConsumer, selectedPrice, Date.from(refDate.withHour(0).withMinute(0).toInstant(ZoneOffset.ofHours(3))), Date.from(refDate.withHour(23).withMinute(59).toInstant(ZoneOffset.ofHours(3))), totalDayConsumption / 1000., totalDayCost));

 
        monthTotalCost = 0.;

        LocalDateTime firsDay = refDate.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0);
        LocalDateTime lastDay = refDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
        LocalDateTime crt = firsDay;
        List<ConsumptionRecord> recListMonth = consumptionRecordFacade.getByClientAndMonth(selectedConsumer.getId(), refDate.getMonthValue(), refDate.getYear());
        double totalMonthConsumption = recListMonth.stream().map(ConsumptionRecord::getConsumedActivePow).mapToDouble(Double::doubleValue).sum();
        Map<Date, Double> totalConsumptionMonth = new TreeMap();
        while (crt.isBefore(lastDay) || crt.isEqual(lastDay)) {
            Date crtDate = Date.from(crt.toInstant(ZoneOffset.UTC));

            SimpleDateFormat dfDay = new SimpleDateFormat("dd");
            LocalDate localDate = crt.toLocalDate();

            List<ConsumptionRecord> recList = recListMonth.stream().filter(record -> {
                return Integer.parseInt(dfDay.format(record.getCreatedTime())) == localDate.getDayOfMonth();
            }).collect(Collectors.toList());

            Double totalCrtDay = getDayTotal(recList);
            totalConsumptionMonth.put(crtDate, totalCrtDay);
            monthTotalCost += totalCrtDay;

            //details per device ???
            crt = crt.plusDays(1);
        }
        ChartSeries totalSeries = new ChartSeries();
        totalSeries.setLabel("Day Total " + selectedConsumer.getFullName() + " - " + selectedPrice.getPriceDef());
        for (Date date : totalConsumptionMonth.keySet()) {
            totalSeries.set(df.format(date), totalConsumptionMonth.get(date));
        }
        dataModelCostTotalMonth.addSeries(totalSeries);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataModelCostTotalMonth.setTitle("Consumed Active Power Daily Cost between " +  firsDay.format(formatter) + " - " + lastDay.format(formatter));
        dataModelCostTotalMonth.setZoom(true);
        dataModelCostTotalMonth.getAxis(AxisType.Y).setLabel("Consumed Active Pow Cost ");
        dataModelCostTotalMonth.setShadow(false);
        dataModelCostTotalMonth.setAnimate(true);
        dataModelCostTotalMonth.setLegendPosition("ne");
        DateAxis axisMonth = new DateAxis("Dates");
        axisMonth.setTickAngle(-30);
        axisMonth.setTickFormat("%d/%m/%y , %H:%M");

        dataModelCostTotalMonth.getAxes().put(AxisType.X, axisMonth);
        if (dataModelCostTotalMonth != null) {
            noChartDataMonth = false;
        } else {
            noChartDataMonth = true;
        }
        //total
        invoiceDataMonth.add(new InvoiceData(invoiceDataMonth.size(), selectedConsumer, selectedPrice, Date.from(firsDay.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDay.toInstant(ZoneOffset.ofHours(3))), totalMonthConsumption / 1000., monthTotalCost));
      
        // for selected year

        LocalDateTime firsDayOfYear = refDate.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime lastDayOfYear = refDate.with(TemporalAdjusters.lastDayOfYear());
        LocalDateTime crtMonth = firsDayOfYear;
        double totalYearCost = 0.;
        double totalYearConsumption = 0.;
        
        Map<Date, Double> totalConsumptionCrtYear = new TreeMap();
        for (int i = 1; i <= 12; i++) {
            LocalDateTime crtLocalDateFirst = LocalDateTime.of(refDate.getYear(), i, 01, 0, 0);
            LocalDateTime crtLocalDateLast = crtLocalDateFirst.with(TemporalAdjusters.lastDayOfMonth());
            LocalDateTime crtLocal = crtLocalDateFirst;
            List<ConsumptionRecord> recListCrtMonth = consumptionRecordFacade.getByClientAndMonth(selectedConsumer.getId(), i, refDate.getYear());
            totalYearConsumption += recListMonth.stream().map(ConsumptionRecord::getConsumedActivePow).mapToDouble(Double::doubleValue).sum();
            if (recListCrtMonth == null || recListCrtMonth.isEmpty()) {
                totalConsumptionCrtYear.put(Date.from(crtLocalDateLast.toInstant(ZoneOffset.UTC)), 0.);
                continue;
            }

            double monthTotalCostLocal = 0.;
            while (crtLocal.isBefore(crtLocalDateLast) || crtLocal.isEqual(crtLocalDateLast)) {
                Date crtDate = Date.from(crtLocal.toInstant(ZoneOffset.UTC));

                SimpleDateFormat dfDay = new SimpleDateFormat("dd");
                LocalDate localDate = crtLocal.toLocalDate();
                //             List<ConsumptionRecord> recList = consumptionRecordFacade.getByClientAndRefDate(selectedConsumer.getId(), crtDate);
                List<ConsumptionRecord> recList = recListCrtMonth.stream().filter(record -> {
                    return Integer.parseInt(dfDay.format(record.getCreatedTime())) == localDate.getDayOfMonth();
                }).collect(Collectors.toList());

                Double totalCrtDay = getDayTotal(recList);

                monthTotalCostLocal += totalCrtDay;

                //details per device ???
                crtLocal = crtLocal.plusDays(1);
            }
            totalYearCost += monthTotalCostLocal;
            totalConsumptionCrtYear.put(Date.from(crtLocal.toInstant(ZoneOffset.ofHours(3))), monthTotalCostLocal);

        }
        ChartSeries totalSeriesYear = new ChartSeries();
        totalSeriesYear.setLabel("Monthly Total " + selectedConsumer.getFullName()+ " - " + selectedPrice.getPriceDef());
        for (Date date : totalConsumptionCrtYear.keySet()) {
            totalSeriesYear.set(df.format(date), totalConsumptionCrtYear.get(date));
        }
        dataModelCostTotalYear.addSeries(totalSeriesYear);

        dataModelCostTotalYear.setTitle("Consumed Active Power Monthly Cost between " + firsDayOfYear.format(formatter) + " - " + lastDayOfYear.format(formatter));
        
        dataModelCostTotalYear.setZoom(true);
        dataModelCostTotalYear.getAxis(AxisType.Y).setLabel("Consumed Active Pow Cost ");
        dataModelCostTotalYear.setShadow(false);
        dataModelCostTotalYear.setAnimate(true);
        dataModelCostTotalYear.setLegendPosition("ne");
        DateAxis axisYear = new DateAxis("Dates");
        axisYear.setTickAngle(-30);
        axisYear.setTickFormat("%d/%m/%y");

        dataModelCostTotalYear.getAxes().put(AxisType.X, axisYear);
        if (dataModelCostTotalYear != null) {
            noChartDataYear = false;
        } else {
            noChartDataYear = true;
        }
        
        invoiceDataYear.add(new InvoiceData(invoiceDataYear.size(), selectedConsumer, selectedPrice, Date.from(firsDayOfYear.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDayOfYear.toInstant(ZoneOffset.ofHours(3))), totalYearConsumption / 1000., totalYearCost));
        
    }
    
    

    public double getMonthTotal() {
        return 0.;
    }

    public double getDayTotal(List<ConsumptionRecord> listRecords) {
        double total = 0.;
        Map<Date, Double> totalConsumptionPerTimestamp = listRecords.stream().collect(
                Collectors.groupingBy(ConsumptionRecord::getCreatedTime, Collectors.summingDouble(ConsumptionRecord::getConsumedActivePow)));
        Map<Date, Double> treeMap = new TreeMap<Date, Double>(totalConsumptionPerTimestamp);
        for (Date date : treeMap.keySet()) {
            total += hourConsumptionCost(treeMap.get(date), date);
        }

        return total;

    }

    public LineChartModel getDataModelCostTotalDay() {
        return dataModelCostTotalDay;
    }

    public void setDataModelCostTotalDay(LineChartModel dataModelCostTotalDay) {
        this.dataModelCostTotalDay = dataModelCostTotalDay;
    }

    public boolean isNoChartDataDay() {
        return noChartDataDay;
    }

    public void setNoChartDataDay(boolean noChartDataDay) {
        this.noChartDataDay = noChartDataDay;
    }

    public ConsumerClientFacade getConsumerFacade() {
        return consumerFacade;
    }

    public boolean isShowConsumerGraph() {
        return showConsumerGraph;
    }

    public void setShowConsumerGraph(boolean showConsumerGraph) {
        this.showConsumerGraph = showConsumerGraph;
    }

    public void setConsumerFacade(ConsumerClientFacade consumerFacade) {
        this.consumerFacade = consumerFacade;
    }

    public List<ConsumerClient> getConsumers() {
        if (consumers == null) {
            consumers = consumerFacade.findAll();
        }
        return consumers;
    }

    public void setConsumers(List<ConsumerClient> consumers) {
        this.consumers = consumers;
    }

    public ConsumerClient getSelectedConsumer() {
        return selectedConsumer;
    }

    public void setSelectedConsumer(ConsumerClient selectedConsumer) {
        this.selectedConsumer = selectedConsumer;
    }

    public List<ConsumerClient> getSelectedConsumers() {
        return selectedConsumers;
    }

    public void setSelectedConsumers(List<ConsumerClient> selectedConsumers) {
        this.selectedConsumers = selectedConsumers;
    }

    private List<ConsumerPrices> getValidPricesList(Date refDate) {
        return consumerPricesFacade.getValidConsumerPrices(refDate);
    }

    public List<ConsumerPrices> getValidPricesList() {

        return consumerPricesFacade.getValidConsumerPrices(dashboardService.getCurrentDate());
    }

    public Date getReferenceDate() {
        return dashboardService.getCurrentDate();
    }

    public void setValidPricesList(List<ConsumerPrices> validPricesList) {
        this.validPricesList = validPricesList;
    }

    public List<ConsumptionPoint> getConsumptionPointList() {
        return consumptionPointList;
    }

    public void setConsumptionPointList(List<ConsumptionPoint> consumptionPointList) {
        this.consumptionPointList = consumptionPointList;
    }

    public List<SmartMeter> getSmartMeterList() {
        return smartMeterList;
    }

    public void setSmartMeterList(List<SmartMeter> smartMeterList) {
        this.smartMeterList = smartMeterList;
    }

    public ConsumerPrices getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedPrice(ConsumerPrices selectedPrice) {
        this.selectedPrice = selectedPrice;
    }

    public List<ConsumptionRecord> getConsumptionDayList() {
        return consumptionDayList;
    }

    public void setConsumptionDayList(List<ConsumptionRecord> consumptionDayList) {
        this.consumptionDayList = consumptionDayList;
    }

    public List<SelectItem> getConsumerDevices() {
        return consumerDevices;
    }

    public void setConsumerDevices(List<SelectItem> consumerDevices) {
        this.consumerDevices = consumerDevices;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isViewDetailedChart() {
        return viewDetailedChart;
    }

    public void setViewDetailedChart(boolean viewDetailedChart) {
        this.viewDetailedChart = viewDetailedChart;
    }

    public Double getTotalDayCost() {
        return totalDayCost;
    }

    public void setTotalDayCost(Double totalDayCost) {
        this.totalDayCost = totalDayCost;
    }

    public double getMonthTotalCost() {
        return monthTotalCost;
    }

    public void setMonthTotalCost(double monthTotalCost) {
        this.monthTotalCost = monthTotalCost;
    }

    public LineChartModel getDataModelCostTotalMonth() {
        return dataModelCostTotalMonth;
    }

    public void setDataModelCostTotalMonth(LineChartModel dataModelCostTotalMonth) {
        this.dataModelCostTotalMonth = dataModelCostTotalMonth;
    }

    public boolean isNoChartDataMonth() {
        return noChartDataMonth;
    }

    public void setNoChartDataMonth(boolean noChartDataMonth) {
        this.noChartDataMonth = noChartDataMonth;
    }

    public LineChartModel getDataModelCostTotalYear() {
        return dataModelCostTotalYear;
    }

    public void setDataModelCostTotalYear(LineChartModel dataModelCostTotalYear) {
        this.dataModelCostTotalYear = dataModelCostTotalYear;
    }

    public boolean isNoChartDataYear() {
        return noChartDataYear;
    }

    public void setNoChartDataYear(boolean noChartDataYear) {
        this.noChartDataYear = noChartDataYear;
    }

    public List<InvoiceData> getInvoiceDataDay() {
        return invoiceDataDay;
    }

    public void setInvoiceDataDay(List<InvoiceData> invoiceDataDay) {
        this.invoiceDataDay = invoiceDataDay;
    }

    public List<InvoiceData> getInvoiceDataMonth() {
        return invoiceDataMonth;
    }

    public void setInvoiceDataMonth(List<InvoiceData> invoiceDataMonth) {
        this.invoiceDataMonth = invoiceDataMonth;
    }

    public List<InvoiceData> getInvoiceDataYear() {
        return invoiceDataYear;
    }

    public void setInvoiceDataYear(List<InvoiceData> invoiceDataYear) {
        this.invoiceDataYear = invoiceDataYear;
    }

    public boolean isIsConsumer() {
        return isConsumer;
    }

    public void setIsConsumer(boolean isConsumer) {
        this.isConsumer = isConsumer;
    }
    

}
