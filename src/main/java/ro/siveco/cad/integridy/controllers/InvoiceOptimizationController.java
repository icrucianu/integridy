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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import ro.siveco.cad.integridy.controllers.util.InvoiceData;
import ro.siveco.cad.integridy.entities.ConsumerPrices;
import ro.siveco.cad.integridy.entities.ConsumptionRecord;
import ro.siveco.cad.integridy.entities.SmartMeter;

/**
 *
 * @author roxanam
 */
@Named("invoiceOptimizationController")
@SessionScoped
public class InvoiceOptimizationController extends ConsumerInvoiceController{
    
    private final double optimizingRate = 20.;
    

    
   
    
    
    @Override
    public void computeCosts(){
        LocalDateTime refDate = LocalDateTime.ofInstant(getReferenceDate().toInstant(), ZoneId.systemDefault());
        //for current day  
        setConsumptionDayList(consumptionRecordFacade.getByClientAndRefDate(selectedConsumer.getId(), getReferenceDate()));
        
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
       

       
        //evaluare cost optim
       //get peaks optimum
       //se calculeaza costul pe peaks optimum
       
        List<ConsumptionRecord> optimizedCostConsumption = getOptimisedConsumption(consumptionDayList, selectedPrice);
       
        Map<Date, Double> totalOptimizedConsumptionPerTimestamp = optimizedCostConsumption.stream().collect(
                Collectors.groupingBy(ConsumptionRecord::getCreatedTime, Collectors.summingDouble(ConsumptionRecord::getConsumedActivePow)));
        Map<Date, Double> treeOptimMap = new TreeMap<Date, Double>(totalOptimizedConsumptionPerTimestamp);
        ChartSeries totalOptimChart = new ChartSeries();
        totalOptimChart.setLabel("Total optimized" + getSelectedConsumer().getFullName() + " - " + selectedPrice.getPriceDef());
        Double totalOptimizedCostDay = 0.;
        Double totalOptimizedConsumptionDay = 0.;
        for (Date date : treeOptimMap.keySet()) {
            totalOptimChart.set(df.format(date), hourConsumptionCost(treeOptimMap.get(date), date));
            totalOptimizedCostDay += hourConsumptionCost(treeOptimMap.get(date), date);
            totalOptimizedConsumptionDay += treeOptimMap.get(date);
        }
       
        getDataModelCostTotalDay().addSeries(totalOptimChart);
        getInvoiceDataDay().add(new InvoiceData( getInvoiceDataDay().size(), getSelectedConsumer(), selectedPrice, Date.from(refDate.withHour(0).withMinute(0).toInstant(ZoneOffset.ofHours(3))), Date.from(refDate.withHour(23).withMinute(59).toInstant(ZoneOffset.ofHours(3))), totalOptimizedConsumptionDay / 1000., totalOptimizedCostDay));
        
         
 
        monthTotalCost = 0.;
        double monthOptimTotalCost = 0.;
        LocalDateTime firsDay = refDate.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0);
        LocalDateTime lastDay = refDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
        LocalDateTime crt = firsDay;
        List<ConsumptionRecord> recListMonth = consumptionRecordFacade.getByClientAndMonth(selectedConsumer.getId(), refDate.getMonthValue(), refDate.getYear());
        double totalMonthConsumption = recListMonth.stream().map(ConsumptionRecord::getConsumedActivePow).mapToDouble(Double::doubleValue).sum();
        Map<Date, Double> totalConsumptionMonth = new TreeMap();
        Map<Date, Double> totalOptimConsumptionMonth = new TreeMap();
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

            //optim cost
            List<ConsumptionRecord> recOptimList = getOptimisedConsumption(recList, selectedPrice);
            Double totalOptimDay = getDayTotal(recOptimList);
            totalOptimConsumptionMonth.put(crtDate,  totalOptimDay);
            monthOptimTotalCost += totalOptimDay;
            crt = crt.plusDays(1);
        }
        ChartSeries totalSeries = new ChartSeries();
        totalSeries.setLabel("Day Total " + selectedConsumer.getFullName() + " - " + selectedPrice.getPriceDef());
        for (Date date : totalConsumptionMonth.keySet()) {
            totalSeries.set(df.format(date), totalConsumptionMonth.get(date));
        }
        dataModelCostTotalMonth.addSeries(totalSeries);
        ChartSeries totalOptimSeries = new ChartSeries();
        totalOptimSeries.setLabel("Optim cost "+ selectedConsumer.getFullName() + " - " + selectedPrice.getPriceDef());
         for (Date date : totalOptimConsumptionMonth.keySet()) {
            totalOptimSeries.set(df.format(date), totalOptimConsumptionMonth.get(date));
        }
        dataModelCostTotalMonth.addSeries(totalOptimSeries);
        
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
        invoiceDataMonth.add(new InvoiceData(invoiceDataMonth.size(), selectedConsumer, selectedPrice, Date.from(firsDay.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDay.toInstant(ZoneOffset.ofHours(3))), totalMonthConsumption / 1000., monthOptimTotalCost));


//        // for selected year
//
        LocalDateTime firsDayOfYear = refDate.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime lastDayOfYear = refDate.with(TemporalAdjusters.lastDayOfYear());
        LocalDateTime crtMonth = firsDayOfYear;
        double totalYearCost = 0.;
        double totalOptimYearCost = 0.;
        double totalYearConsumption = 0.;
        
        Map<Date, Double> totalConsumptionCrtYear = new TreeMap();
        Map<Date, Double> totalOptimConsumptionCrtYear = new TreeMap();
        for (int i = 1; i <= 12; i++) {
            LocalDateTime crtLocalDateFirst = LocalDateTime.of(refDate.getYear(), i, 01, 0, 0);
            LocalDateTime crtLocalDateLast = crtLocalDateFirst.with(TemporalAdjusters.lastDayOfMonth());
            LocalDateTime crtLocal = crtLocalDateFirst;
            List<ConsumptionRecord> recListCrtMonth = consumptionRecordFacade.getByClientAndMonth(selectedConsumer.getId(), i, refDate.getYear());
            totalYearConsumption += recListMonth.stream().map(ConsumptionRecord::getConsumedActivePow).mapToDouble(Double::doubleValue).sum();
            if (recListCrtMonth == null || recListCrtMonth.isEmpty()) {
                totalConsumptionCrtYear.put(Date.from(crtLocalDateLast.toInstant(ZoneOffset.UTC)), 0.);
                totalOptimConsumptionCrtYear.put(Date.from(crtLocalDateLast.toInstant(ZoneOffset.UTC)), 0.);
                continue;
            }

            double monthTotalCostLocal = 0.;
            double monthOptimTotalCostLocal = 0.;
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

                //optim 
                List<ConsumptionRecord> recOptimList = getOptimisedConsumption(recList, selectedPrice);
                Double totalOptimDay = getDayTotal(recOptimList);
               
                monthOptimTotalCostLocal += totalOptimDay;
                
                
                crtLocal = crtLocal.plusDays(1);
            }
            totalYearCost += monthTotalCostLocal;
            totalOptimYearCost += monthOptimTotalCostLocal;
            totalConsumptionCrtYear.put(Date.from(crtLocal.toInstant(ZoneOffset.ofHours(3))), monthTotalCostLocal);
            totalOptimConsumptionCrtYear.put(Date.from(crtLocal.toInstant(ZoneOffset.ofHours(3))), monthOptimTotalCostLocal);
        }
        ChartSeries totalSeriesYear = new ChartSeries();
        totalSeriesYear.setLabel("Monthly Total " + selectedConsumer.getFullName()+ " - " + selectedPrice.getPriceDef());
        for (Date date : totalConsumptionCrtYear.keySet()) {
            totalSeriesYear.set(df.format(date), totalConsumptionCrtYear.get(date));
        }
        dataModelCostTotalYear.addSeries(totalSeriesYear);
        ChartSeries totalOptimYear = new ChartSeries();
        totalOptimYear.setLabel("Optimum Monthly Total " + selectedConsumer.getFullName()+ " - " + selectedPrice.getPriceDef());
        for (Date date : totalOptimConsumptionCrtYear.keySet()) {
            totalOptimYear.set(df.format(date), totalOptimConsumptionCrtYear.get(date));
        }
        dataModelCostTotalYear.addSeries(totalOptimYear);
        
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
        invoiceDataYear.add(new InvoiceData(invoiceDataYear.size(), selectedConsumer, selectedPrice, Date.from(firsDayOfYear.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDayOfYear.toInstant(ZoneOffset.ofHours(3))), totalYearConsumption / 1000., totalOptimYearCost));
        
         
    }
    private List<ConsumptionRecord> getOptimisedConsumption(List<ConsumptionRecord> recList, ConsumerPrices price){
       
        double dayRecNr = 0.;
        double nightRecNr = 0.;
        double dayConsumption = 0.;
        double nightConsumption = 0.;
        
        for(ConsumptionRecord rec: recList){
            if(isDayInterval(rec.getCreatedTime(), price.getStartTimePeriod(), price.getEndTimePeriod())){
                dayConsumption += rec.getConsumedActivePow();
                dayRecNr = dayRecNr + 1;
            } else {
                nightConsumption += rec.getConsumedActivePow();
                nightRecNr = nightRecNr + 1;
            }
        }
        double dayHours = (int)(selectedPrice.getEndTimePeriod().getTime() - selectedPrice.getStartTimePeriod().getTime())/3600000;
        
        double increaseNightAmount = nightConsumption * (optimizingRate / 100.);
        
        double decreaseStepDay = increaseNightAmount / dayRecNr;
        double increaseStepNight = increaseNightAmount / nightRecNr;
        
        List<ConsumptionRecord> optimizedCostConsumption = new ArrayList<>();
        for(ConsumptionRecord rec: recList){
            if(isDayInterval(rec.getCreatedTime(), selectedPrice.getStartTimePeriod(), selectedPrice.getEndTimePeriod())){
                rec.setConsumedActivePow(rec.getConsumedActivePow()-decreaseStepDay);
                
            } else {
                rec.setConsumedActivePow(rec.getConsumedActivePow()+increaseStepNight);
            }
            optimizedCostConsumption.add(rec);
        }
        return optimizedCostConsumption;
    }
    
    
}
