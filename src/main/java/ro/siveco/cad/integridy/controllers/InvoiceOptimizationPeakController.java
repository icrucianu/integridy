/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

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
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.controllers.util.InvoiceData;
import ro.siveco.cad.integridy.entities.ConsumptionRecord;
import ro.siveco.cad.integridy.entities.SmartMeter;
import ro.siveco.cad.integridy.forecast.utils.ForecastUtils;

/**
 *
 * @author roxanam
 */
@Named("invoiceOptimizationPeakController")
@SessionScoped
public class InvoiceOptimizationPeakController extends ConsumerInvoiceController {
  
     @Override
     public void computeCosts() {
        LocalDateTime refDate = LocalDateTime.ofInstant(getReferenceDate().toInstant(), ZoneId.systemDefault());
        //for current day  
        setConsumptionDayList(consumptionRecordFacade.getByClientAndRefDate(selectedConsumer.getId(), getReferenceDate()));
        
        ChartSeries totalChart = new ChartSeries();
        totalChart.setLabel("Total " + selectedConsumer.getFullName() + " - " + selectedPrice.getPriceDef());

        Map<Date, Double> totalConsumptionPerTimestamp = consumptionDayList.stream().collect(
                Collectors.groupingBy(ConsumptionRecord::getCreatedTime, Collectors.summingDouble(ConsumptionRecord::getConsumedActivePow)));
        TreeMap<Date, Double> treeMap = new TreeMap<Date, Double>(totalConsumptionPerTimestamp);
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
       //acelasi  consum
       //tarif diferentiat
       //se majoreaza consumul cu tarif mic (de noapte)
       //cantitatea cu care se majoreaza tariful de noapte s distribuie in mod egal pe perioada de zi
//        List<ConsumptionRecord> optimizedCostConsumption = getOptimisedConsumption(consumptionDayList);
       
//        Map<Date, Double> totalOptimizedConsumptionPerTimestamp = optimizedCostConsumption.stream().collect(
//                Collectors.groupingBy(ConsumptionRecord::getCreatedTime, Collectors.summingDouble(ConsumptionRecord::getConsumedActivePow)));
        TreeMap<Date, Double> treeOptimMap = getOptimisedConsumption(treeMap);
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
        
         
 
//        monthTotalCost = 0.;
//        double monthOptimTotalCost = 0.;
//        LocalDateTime firsDay = refDate.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0);
//        LocalDateTime lastDay = refDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
//        LocalDateTime crt = firsDay;
//        List<ConsumptionRecord> recListMonth = consumptionRecordFacade.getByClientAndMonth(selectedConsumer.getId(), refDate.getMonthValue(), refDate.getYear());
//        double totalMonthConsumption = recListMonth.stream().map(ConsumptionRecord::getConsumedActivePow).mapToDouble(Double::doubleValue).sum();
//        Map<Date, Double> totalConsumptionMonth = new TreeMap();
//        Map<Date, Double> totalOptimConsumptionMonth = new TreeMap();
//        while (crt.isBefore(lastDay) || crt.isEqual(lastDay)) {
//            Date crtDate = Date.from(crt.toInstant(ZoneOffset.UTC));
//
//            SimpleDateFormat dfDay = new SimpleDateFormat("dd");
//            LocalDate localDate = crt.toLocalDate();
//
//            List<ConsumptionRecord> recList = recListMonth.stream().filter(record -> {
//                return Integer.parseInt(dfDay.format(record.getCreatedTime())) == localDate.getDayOfMonth();
//            }).collect(Collectors.toList());
//
//            Double totalCrtDay = getDayTotal(recList);
//            totalConsumptionMonth.put(crtDate, totalCrtDay);
//            monthTotalCost += totalCrtDay;
//
//            //optim cost
//            List<ConsumptionRecord> recOptimList = getOptimisedConsumption(recList);
//            Double totalOptimDay = getDayTotal(recOptimList);
//            totalOptimConsumptionMonth.put(crtDate,  totalOptimDay);
//            monthOptimTotalCost += totalOptimDay;
//            crt = crt.plusDays(1);
//        }
//        ChartSeries totalSeries = new ChartSeries();
//        totalSeries.setLabel("Day Total " + selectedConsumer.getFullName() + " - " + selectedPrice.getPriceDef());
//        for (Date date : totalConsumptionMonth.keySet()) {
//            totalSeries.set(df.format(date), totalConsumptionMonth.get(date));
//        }
//        dataModelCostTotalMonth.addSeries(totalSeries);
//        ChartSeries totalOptimSeries = new ChartSeries();
//        totalOptimSeries.setLabel("Optim cost "+ selectedConsumer.getFullName() + " - " + selectedPrice.getPriceDef());
//         for (Date date : totalOptimConsumptionMonth.keySet()) {
//            totalOptimSeries.set(df.format(date), totalOptimConsumptionMonth.get(date));
//        }
//        dataModelCostTotalMonth.addSeries(totalOptimSeries);
//        
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        dataModelCostTotalMonth.setTitle("Consumed Active Power Daily Cost between " +  firsDay.format(formatter) + " - " + lastDay.format(formatter));
//        dataModelCostTotalMonth.setZoom(true);
//        dataModelCostTotalMonth.getAxis(AxisType.Y).setLabel("Consumed Active Pow Cost ");
//        dataModelCostTotalMonth.setShadow(false);
//        dataModelCostTotalMonth.setAnimate(true);
//        dataModelCostTotalMonth.setLegendPosition("ne");
//        DateAxis axisMonth = new DateAxis("Dates");
//        axisMonth.setTickAngle(-30);
//        axisMonth.setTickFormat("%d/%m/%y , %H:%M");
//
//        dataModelCostTotalMonth.getAxes().put(AxisType.X, axisMonth);
//        if (dataModelCostTotalMonth != null) {
//            noChartDataMonth = false;
//        } else {
//            noChartDataMonth = true;
//        }
//        //total
//        invoiceDataMonth.add(new InvoiceData(invoiceDataMonth.size(), selectedConsumer, selectedPrice, Date.from(firsDay.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDay.toInstant(ZoneOffset.ofHours(3))), totalMonthConsumption / 1000., monthTotalCost));
//        invoiceDataMonth.add(new InvoiceData(invoiceDataMonth.size(), selectedConsumer, selectedPrice, Date.from(firsDay.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDay.toInstant(ZoneOffset.ofHours(3))), totalMonthConsumption / 1000., monthOptimTotalCost));
//
//
////        // for selected year
////
//        LocalDateTime firsDayOfYear = refDate.with(TemporalAdjusters.firstDayOfYear());
//        LocalDateTime lastDayOfYear = refDate.with(TemporalAdjusters.lastDayOfYear());
//        LocalDateTime crtMonth = firsDayOfYear;
//        double totalYearCost = 0.;
//        double totalOptimYearCost = 0.;
//        double totalYearConsumption = 0.;
//        
//        Map<Date, Double> totalConsumptionCrtYear = new TreeMap();
//        Map<Date, Double> totalOptimConsumptionCrtYear = new TreeMap();
//        for (int i = 1; i <= 12; i++) {
//            LocalDateTime crtLocalDateFirst = LocalDateTime.of(refDate.getYear(), i, 01, 0, 0);
//            LocalDateTime crtLocalDateLast = crtLocalDateFirst.with(TemporalAdjusters.lastDayOfMonth());
//            LocalDateTime crtLocal = crtLocalDateFirst;
//            List<ConsumptionRecord> recListCrtMonth = consumptionRecordFacade.getByClientAndMonth(selectedConsumer.getId(), i, refDate.getYear());
//            totalYearConsumption += recListMonth.stream().map(ConsumptionRecord::getConsumedActivePow).mapToDouble(Double::doubleValue).sum();
//            if (recListCrtMonth == null || recListCrtMonth.isEmpty()) {
//                totalConsumptionCrtYear.put(Date.from(crtLocalDateLast.toInstant(ZoneOffset.UTC)), 0.);
//                totalOptimConsumptionCrtYear.put(Date.from(crtLocalDateLast.toInstant(ZoneOffset.UTC)), 0.);
//                continue;
//            }
//
//            double monthTotalCostLocal = 0.;
//            double monthOptimTotalCostLocal = 0.;
//            while (crtLocal.isBefore(crtLocalDateLast) || crtLocal.isEqual(crtLocalDateLast)) {
//                Date crtDate = Date.from(crtLocal.toInstant(ZoneOffset.UTC));
//
//                SimpleDateFormat dfDay = new SimpleDateFormat("dd");
//                LocalDate localDate = crtLocal.toLocalDate();
//                //             List<ConsumptionRecord> recList = consumptionRecordFacade.getByClientAndRefDate(selectedConsumer.getId(), crtDate);
//                List<ConsumptionRecord> recList = recListCrtMonth.stream().filter(record -> {
//                    return Integer.parseInt(dfDay.format(record.getCreatedTime())) == localDate.getDayOfMonth();
//                }).collect(Collectors.toList());
//
//                Double totalCrtDay = getDayTotal(recList);
//
//                monthTotalCostLocal += totalCrtDay;
//
//                //optim 
//                List<ConsumptionRecord> recOptimList = getOptimisedConsumption(recList);
//                Double totalOptimDay = getDayTotal(recOptimList);
//               
//                monthOptimTotalCostLocal += totalOptimDay;
//                
//                
//                crtLocal = crtLocal.plusDays(1);
//            }
//            totalYearCost += monthTotalCostLocal;
//            totalOptimYearCost += monthOptimTotalCostLocal;
//            totalConsumptionCrtYear.put(Date.from(crtLocal.toInstant(ZoneOffset.ofHours(3))), monthTotalCostLocal);
//            totalOptimConsumptionCrtYear.put(Date.from(crtLocal.toInstant(ZoneOffset.ofHours(3))), monthOptimTotalCostLocal);
//        }
//        ChartSeries totalSeriesYear = new ChartSeries();
//        totalSeriesYear.setLabel("Monthly Total " + selectedConsumer.getFullName()+ " - " + selectedPrice.getPriceDef());
//        for (Date date : totalConsumptionCrtYear.keySet()) {
//            totalSeriesYear.set(df.format(date), totalConsumptionCrtYear.get(date));
//        }
//        dataModelCostTotalYear.addSeries(totalSeriesYear);
//        ChartSeries totalOptimYear = new ChartSeries();
//        totalOptimYear.setLabel("Optimum Monthly Total " + selectedConsumer.getFullName()+ " - " + selectedPrice.getPriceDef());
//        for (Date date : totalOptimConsumptionCrtYear.keySet()) {
//            totalOptimYear.set(df.format(date), totalOptimConsumptionCrtYear.get(date));
//        }
//        dataModelCostTotalYear.addSeries(totalOptimYear);
//        
//        dataModelCostTotalYear.setTitle("Consumed Active Power Monthly Cost between " + firsDayOfYear.format(formatter) + " - " + lastDayOfYear.format(formatter));
//        
//        dataModelCostTotalYear.setZoom(true);
//        dataModelCostTotalYear.getAxis(AxisType.Y).setLabel("Consumed Active Pow Cost ");
//        dataModelCostTotalYear.setShadow(false);
//        dataModelCostTotalYear.setAnimate(true);
//        dataModelCostTotalYear.setLegendPosition("ne");
//        DateAxis axisYear = new DateAxis("Dates");
//        axisYear.setTickAngle(-30);
//        axisYear.setTickFormat("%d/%m/%y");
//
//        dataModelCostTotalYear.getAxes().put(AxisType.X, axisYear);
//        if (dataModelCostTotalYear != null) {
//            noChartDataYear = false;
//        } else {
//            noChartDataYear = true;
//        }
//        
//        invoiceDataYear.add(new InvoiceData(invoiceDataYear.size(), selectedConsumer, selectedPrice, Date.from(firsDayOfYear.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDayOfYear.toInstant(ZoneOffset.ofHours(3))), totalYearConsumption / 1000., totalYearCost));
//        invoiceDataYear.add(new InvoiceData(invoiceDataYear.size(), selectedConsumer, selectedPrice, Date.from(firsDayOfYear.toInstant(ZoneOffset.ofHours(3))), Date.from(lastDayOfYear.toInstant(ZoneOffset.ofHours(3))), totalYearConsumption / 1000., totalOptimYearCost));
//        
         
    }
//     public List<ConsumptionRecord> createOptimumConsumption(List<ConsumptionRecord> realConsumption) {
//        List<ConsumptionRecord> result = new ArrayList<>();
//
//        
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat titleDf = new SimpleDateFormat("yyyy-MM-dd");
//        
//        // For each raw od read data, detect peak, compute optimum
//        // peack positive is considered if previous 4 values and next 4 values are smaller
//        // peack negative is considered if previous 4 values and next 4 values are bigger
//        // optimum is computed by decreasing positive peack and increasing negative peacks
//        // so thta the total amount of power remain the same (MSE is used to compte the error)
//        if (realConsumption != null && realConsumption.size() != 0) {
//
//            int ii = 0;
//            double delta = 0.;
//            int noNeg = 0;
//            int noPoz = 0;
//            double lastPozPeak = 0.;
//            double lastNegPeak = 0.;
//            double difDelta = 0.;
//            
//            
//            
//
//            // for each row in dataset, see if it is a peack, and compute optimum
//            // first and last points are considered both peack and optimum
//            // first 4 and last 4 point can not be peacks or optimum
//            for (ConsumptionRecord res : realConsumption) {
//                
//             
//                if (ii == 0) {
//                    result.add(res);
//                }
//
//                if (ii == (realConsumption.size() - 1)) {
//                        result.add(res);
//                }
//
//                if (ii > 4 && ii < realConsumption.size() - 4) {
//                    // posiotive peek
//
//                    if ((realConsumption.get(ii - 1).getConsumedActivePow() < res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 2).getConsumedActivePow() <res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 3).getConsumedActivePow() < res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 4).getConsumedActivePow() < res.getConsumedActivePow())
//                            && (  realConsumption.get(ii + 1).getConsumedActivePow()  < res.getConsumedActivePow())
//                            &&( realConsumption.get(ii + 2).getConsumedActivePow()  < res.getConsumedActivePow())
//                            && ( realConsumption.get(ii + 3).getConsumedActivePow()  < res.getConsumedActivePow())
//                            && ( realConsumption.get(ii + 4).getConsumedActivePow()  < res.getConsumedActivePow())
//                            ) {
//                        noPoz++;
//                        lastPozPeak = res.getConsumedActivePow();
//                        
//
//                        // do not decrease less that last negative peack
//                        // preserve the amount which was decreased in error computing
//                        if(res.getConsumedActivePow() * Constants.OPTIM_SENZITIVITY > lastNegPeak)
//                        {
//                            delta += res.getConsumedActivePow() * (1.-Constants.OPTIM_SENZITIVITY);
//                            res.setConsumedActivePow( res.getConsumedActivePow() * Constants.OPTIM_SENZITIVITY);
//                        }
//                        else
//                        {
//                            res.setConsumedActivePow((lastNegPeak+res.getConsumedActivePow())/2);
//                            delta += res.getConsumedActivePow()-(Double)((lastNegPeak+res.getConsumedActivePow())/2);
//                        }
//                        result.add(res);
//                    }
//
//                    // negative peak
//                    if ((realConsumption.get(ii - 1).getConsumedActivePow() > res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 2).getConsumedActivePow()> res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 3).getConsumedActivePow()> res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 4).getConsumedActivePow()> res.getConsumedActivePow())
//                            && ( realConsumption.get(ii + 1).getConsumedActivePow() > res.getConsumedActivePow())
//                            && ( realConsumption.get(ii + 2).getConsumedActivePow() > res.getConsumedActivePow())
//                            && ( realConsumption.get(ii + 3).getConsumedActivePow() > res.getConsumedActivePow())
//                            && ( realConsumption.get(ii + 4).getConsumedActivePow() > res.getConsumedActivePow())  ) {
//                        noNeg++;
//                         lastNegPeak = res.getConsumedActivePow();
//                         delta +=(res.getConsumedActivePow())*(1.-Constants.OPTIM_SENZITIVITY);
//
//                        // do not increase more than last pozitive peack
//                        // preserve the amount which was increased in error computing
//                        if ((delta + res.getConsumedActivePow()) > lastPozPeak) {
//                                delta = (lastPozPeak+lastNegPeak)/2-lastNegPeak;
//                            }
//                        Object[] optim = new Object[2];
//                        res.setConsumedActivePow(res.getConsumedActivePow() + delta);
//                        result.add(res);
//
//
//                    }
//
//                } 
//                else if(ii >= realConsumption.size() - 4 && ii>=4){
//                     boolean isMax = true;
//                     boolean isMin = true;
//                     for(int li=ii;li<realConsumption.size()-1;li++ ){
//                         if(res.getConsumedActivePow() < realConsumption.get(li).getConsumedActivePow()){
//                             isMax=false;
//                         } else  if(res.getConsumedActivePow() > realConsumption.get(li).getConsumedActivePow()){
//                             isMin = false;
//                         }
//                     }
//                     //max
//                     if ((realConsumption.get(ii - 1).getConsumedActivePow() < res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 2).getConsumedActivePow() < res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 3).getConsumedActivePow() <res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 4).getConsumedActivePow() < res.getConsumedActivePow())
//                            && isMax){
//                         noPoz++;
//                        lastPozPeak = res.getConsumedActivePow();
//                        
//                     
//                        // do not decrease less that last negative peack
//                        // preserve the amount which was decreased in error computing
//                        if((Double) res.getConsumedActivePow() * Constants.OPTIM_SENZITIVITY > lastNegPeak)
//                        {
//                             delta += res.getConsumedActivePow() * (1.-Constants.OPTIM_SENZITIVITY);
//                             res.setConsumedActivePow(res.getConsumedActivePow() * Constants.OPTIM_SENZITIVITY);
//                           
//                        }
//                        else
//                        {
//                            
//                            double optimv = (lastNegPeak+(Double) res.getConsumedActivePow())/2;
//                            delta += (Double) res.getConsumedActivePow() - optimv;
//                            res.setConsumedActivePow(optimv);
//                        }
//                        result.add(res);
//                        
//
//                     }
//                     //min
//                     if ((realConsumption.get(ii - 1).getConsumedActivePow() > res.getConsumedActivePow())
//                            && ( realConsumption.get(ii - 2).getConsumedActivePow() > res.getConsumedActivePow())
//                             && (realConsumption.get(ii - 3).getConsumedActivePow() > res.getConsumedActivePow())
//                            && (realConsumption.get(ii - 4).getConsumedActivePow() > res.getConsumedActivePow())
//                             && isMin){
//                          noNeg++;
//                         lastNegPeak = res.getConsumedActivePow();
//                         delta +=(res.getConsumedActivePow())*(1.-Constants.OPTIM_SENZITIVITY);
//
//                        // do not increase more than last pozitive peack
//                        // preserve the amount which was increased in error computing
//                        if ((delta + res.getConsumedActivePow()) > lastPozPeak) {
//                                delta = (lastPozPeak+lastNegPeak)/2-lastNegPeak;
//                            }
//                        res.setConsumedActivePow(res.getConsumedActivePow() + delta);
//
//                     }
//                     
//                 } 
//                else if(ii>0 && ii<4 && ii<realConsumption.size()-4 ){
//                     boolean isMax = true;
//                     boolean isMin = true;
//                     for(int li=0;li<ii;li++ ){
//                         if(res.getConsumedActivePow() <  realConsumption.get(li).getConsumedActivePow()){
//                             isMax=false;
//                         } else if(res.getConsumedActivePow() >  realConsumption.get(li).getConsumedActivePow()){
//                             isMin = false;
//                         }
//                     }
//                     if (   isMax
//                            && ((Double) realConsumption.get(ii + 1).getConsumedActivePow() < res.getConsumedActivePow() )
//                            && ((Double) realConsumption.get(ii + 2).getConsumedActivePow() < res.getConsumedActivePow() )
//                             && ((Double) realConsumption.get(ii + 3).getConsumedActivePow() < res.getConsumedActivePow() )
//                            && ((Double) realConsumption.get(ii + 4).getConsumedActivePow() < res.getConsumedActivePow() )
//                             ) {
//                          noPoz++;
//                        lastPozPeak = res.getConsumedActivePow() ;
//                        
////                        Object[] optim = new Object[2];
//                        // do not decrease less that last negative peack
//                        // preserve the amount which was decreased in error computing
//                        if(res.getConsumedActivePow() * Constants.OPTIM_SENZITIVITY > lastNegPeak)
//                        {
//                            delta += res.getConsumedActivePow()  * (1.-Constants.OPTIM_SENZITIVITY);
//                            res.setConsumedActivePow(res.getConsumedActivePow()  * Constants.OPTIM_SENZITIVITY);
//                        }
//                        else
//                        {
//                            double optim = (lastNegPeak+res.getConsumedActivePow())/2;
//                            delta +=  res.getConsumedActivePow()- optim;
//                            res.setConsumedActivePow(optim);
//                        }
//                        result.add(res);
//                        
//
//                    }
//                     //min
//                     if ((realConsumption.get(ii + 1).getConsumedActivePow() > res.getConsumedActivePow())
//                            && (realConsumption.get(ii + 2).getConsumedActivePow() > res.getConsumedActivePow())
//                           && (realConsumption.get(ii + 3).getConsumedActivePow() > res.getConsumedActivePow())
//                            && ( realConsumption.get(ii + 4).getConsumedActivePow() > res.getConsumedActivePow())
//                             && isMin){
//                         noNeg++;
//                         lastNegPeak = res.getConsumedActivePow();
//                         delta += res.getConsumedActivePow()*(1.-Constants.OPTIM_SENZITIVITY);
//
//                        // do not increase more than last pozitive peack
//                        // preserve the amount which was increased in error computing
//                        if ((delta + res.getConsumedActivePow()) > lastPozPeak) {
//                                delta = (lastPozPeak+lastNegPeak)/2-lastNegPeak;
//                            }
//                       
//                        res.setConsumedActivePow(res.getConsumedActivePow()+ delta);
//                        result.add(res);
//
//                     }
//                 }
//
//                ii++;
//            } // for
//        }
//        return result;
//    }
     public TreeMap<Date, Double> getOptimisedConsumption(TreeMap<Date,Double> recList){
//         List<Object[]> optimisedResults = convertMapToList(recList);
//         List<Object[]> optimizedList =createOptimumConsumptionList(optimisedResults);
//         return convertListToTreeMap(ForecastUtils.completeForecastData(createOptimumConsumptionList(convertMapToList(recList)),15));
           return convertListToTreeMap(createOptimumConsumptionList(convertMapToList(recList)));
     }
     private List<Object[]> convertMapToList(TreeMap<Date, Double> treeMap){
         List<Object[]> resultList =  new ArrayList<>();
         for(Date crtDate: treeMap.keySet()){
             Object[] crtObj = new Object[2];
             crtObj[0] = treeMap.get(crtDate);
             crtObj[1] = crtDate;
             
             resultList.add(crtObj);
         }
         return resultList;
     }
     
     private TreeMap<Date, Double> convertListToTreeMap(List<Object[]> resList){
         TreeMap<Date,Double> treeMap = new TreeMap<Date,Double>();
         if(resList!=null){
             for(Object[] res: resList){
                 treeMap.put((Date)res[1],(Double)res[0]);
             }
         }
         return treeMap;
     }
     public List<Object[]> createOptimumConsumptionList(List<Object[]> realConsumption) {
         List<Object[]> optimizedResults = new ArrayList<>();
       
        // for dso there is no client id (for the moment)
     
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat titleDf = new SimpleDateFormat("yyyy-MM-dd");
        
        // For each raw od read data, detect peak, compute optimum
        // peack positive is considered if previous 4 values and next 4 values are smaller
        // peack negative is considered if previous 4 values and next 4 values are bigger
        // optimum is computed by decreasing positive peack and increasing negative peacks
        // so thta the total amount of power remain the same (MSE is used to compte the error)
        if (realConsumption != null && realConsumption.size() != 0) {

            int ii = 0;
            double delta = 0.;
            int noNeg = 0;
            int noPoz = 0;
            double lastPozPeak = 0.;
            double lastNegPeak = 0.;
            double difDelta = 0.;
            
           //           for (Object[] res : results) {
//               if(res[0]==null)
//                   res[0] = 0;
//           }
            // for each row in dataset, see if it is a peack, and compute optimum
            // first and last points are considered both peack and optimum
            // first 4 and last 4 point can not be peacks or optimum
            for (Object[] res : realConsumption) {
               
                if (ii == 0) {
                    
                    lastPozPeak = (Double) res[0];
                    lastNegPeak = (Double) res[0];
                    Object[] optim = new Object[2];
                    optim[0] = res[0];
                    optim[1] = res[1];
                    optimizedResults.add(optim);
                }

                if (ii == (realConsumption.size() - 1)) {
                    
                    Object[] optim = new Object[2];
                    optim[0] = res[0];
                    optim[1] = res[1];
                    optimizedResults.add(optim);
                }

                if (ii > 2 && ii < realConsumption.size() - 2) {
                    // positive peek

                    if (((Double) realConsumption.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) realConsumption.get(ii - 2)[0] < (Double) res[0])
//                            && ((Double) realConsumption.get(ii - 3)[0] < (Double) res[0])
//                            && ((Double) realConsumption.get(ii - 4)[0] < (Double) res[0])
                            && ((Double) realConsumption.get(ii + 1)[0] < (Double) res[0])
                            &&((Double) realConsumption.get(ii + 2)[0] < (Double) res[0])
//                            && ((Double) realConsumption.get(ii + 3)[0] < (Double) res[0])
//                            && ((Double) realConsumption.get(ii + 4)[0] < (Double) res[0])
                            ) {
                        noPoz++;
                        lastPozPeak = (Double) res[0];
                        
                        Object[] optim = new Object[2];
                        // do not decrease less that last negative peack
                        // preserve the amount which was decreased in error computing
                        if((Double) res[0] * Constants.OPTIM_SENZITIVITY > lastNegPeak)
                        {
                        optim[0] = (Double) res[0] * Constants.OPTIM_SENZITIVITY;
                        delta += (Double) res[0] * (1.-Constants.OPTIM_SENZITIVITY);
                        }
                        else
                        {
                            optim[0] = (lastNegPeak+(Double) res[0])/2;
                            delta += (Double) res[0]-(Double)optim[0];
                        }
                        
                        optim[1] = res[1];
                        optimizedResults.add(optim);
                        
                    }

                    // negative peak
                    else if (((Double) realConsumption.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) realConsumption.get(ii - 2)[0] > (Double) res[0])
//                            && ((Double) realConsumption.get(ii - 3)[0] > (Double) res[0])
//                            && ((Double) realConsumption.get(ii - 4)[0] > (Double) res[0])
                            && ((Double) realConsumption.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) realConsumption.get(ii + 2)[0] > (Double) res[0])
//                            && ((Double) realConsumption.get(ii + 3)[0] > (Double) res[0])
//                            && ((Double) realConsumption.get(ii + 4)[0] > (Double) res[0]) 
                            ) {
                        noNeg++;
                         lastNegPeak = (Double) res[0];
                         delta +=((Double) res[0])*(1.-Constants.OPTIM_SENZITIVITY);

                        // do not increase more than last pozitive peack
                        // preserve the amount which was increased in error computing
                        if ((delta + (Double) res[0]) > lastPozPeak) {
                                delta = (lastPozPeak+lastNegPeak)/2-lastNegPeak;
                            }
                        Object[] optim = new Object[2];
                        optim[0] = (Double) res[0] + delta;
                        optim[1] = res[1];
                        optimizedResults.add(optim);

                    } else {
                        Object[] optim = new Object[2];
                        optim[0] = (Double) res[0];
                        optim[1] = res[1];
                        optimizedResults.add(optim);
                    }
               

                } 
                if(ii >= realConsumption.size() - 2 && ii>=2){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=ii;li<realConsumption.size()-1;li++ ){
                         if((Double)res[0] < (Double)realConsumption.get(li)[0]){
                             isMax=false;
                         } else  if((Double)res[0] > (Double)realConsumption.get(li)[0]){
                             isMin = false;
                         }
                     }
                     //max
                     if (((Double) realConsumption.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) realConsumption.get(ii - 2)[0] < (Double) res[0])
//                              && ((Double) realConsumption.get(ii - 3)[0] < (Double) res[0])
//                            && ((Double) realConsumption.get(ii - 4)[0] < (Double) res[0])
                             && isMax){
                         noPoz++;
                        lastPozPeak = (Double) res[0];
                        
                        Object[] optim = new Object[2];
                        // do not decrease less that last negative peack
                        // preserve the amount which was decreased in error computing
                        if((Double) res[0] * Constants.OPTIM_SENZITIVITY > lastNegPeak)
                        {
                        optim[0] = (Double) res[0] * Constants.OPTIM_SENZITIVITY;
                        delta += (Double) res[0] * (1.-Constants.OPTIM_SENZITIVITY);
                        }
                        else
                        {
                            optim[0] = (lastNegPeak+(Double) res[0])/2;
                            delta += (Double) res[0]-(Double)optim[0];
                        }
                        
                        optim[1] = res[1];
                        optimizedResults.add(optim);
                        
                     }
                     //min
                     else if (((Double) realConsumption.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) realConsumption.get(ii - 2)[0] > (Double) res[0])
//                             && ((Double) realConsumption.get(ii - 3)[0] > (Double) res[0])
//                            && ((Double) realConsumption.get(ii - 4)[0] > (Double) res[0])
                             && isMin){
                          noNeg++;
                         lastNegPeak = (Double) res[0];
                         delta +=((Double) res[0])*(1.-Constants.OPTIM_SENZITIVITY);

                        // do not increase more than last pozitive peack
                        // preserve the amount which was increased in error computing
                        if ((delta + (Double) res[0]) > lastPozPeak) {
                                delta = (lastPozPeak+lastNegPeak)/2-lastNegPeak;
                            }
                        Object[] optim = new Object[2];
                        optim[0] = (Double) res[0] + delta;
                        optim[1] = res[1];
                        optimizedResults.add(optim);

                        
                     }else {
                        Object[] optim = new Object[2];
                        optim[0] = (Double) res[0];
                        optim[1] = res[1];
                        optimizedResults.add(optim);
                    }
                     
                     
                 } 
                if(ii>0 && ii<2 && ii<realConsumption.size()-2 ){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=0;li<ii;li++ ){
                         if((Double)res[0] < (Double)realConsumption.get(li)[0]){
                             isMax=false;
                         } else if((Double)res[0] > (Double)realConsumption.get(li)[0]){
                             isMin = false;
                         }
                     }
                     if (   isMax
                            && ((Double) realConsumption.get(ii + 1)[0] < (Double) res[0])
                            && ((Double) realConsumption.get(ii + 2)[0] < (Double) res[0])
//                             && ((Double) realConsumption.get(ii + 3)[0] < (Double) res[0])
//                            && ((Double) realConsumption.get(ii + 4)[0] < (Double) res[0])
                             ) {
                          noPoz++;
                        lastPozPeak = (Double) res[0];
                        
                        Object[] optim = new Object[2];
                        // do not decrease less that last negative peack
                        // preserve the amount which was decreased in error computing
                        if((Double) res[0] * Constants.OPTIM_SENZITIVITY > lastNegPeak)
                        {
                        optim[0] = (Double) res[0] * Constants.OPTIM_SENZITIVITY;
                        delta += (Double) res[0] * (1.-Constants.OPTIM_SENZITIVITY);
                        }
                        else
                        {
                            optim[0] = (lastNegPeak+(Double) res[0])/2;
                            delta += (Double) res[0]-(Double)optim[0];
                        }
                        
                        optim[1] = res[1];
                        optimizedResults.add(optim);
                        
                    }
                     //min
                     else if (((Double) realConsumption.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) realConsumption.get(ii + 2)[0] > (Double) res[0])
//                           && ((Double) realConsumption.get(ii + 3)[0] > (Double) res[0])
//                            && ((Double) realConsumption.get(ii + 4)[0] > (Double) res[0])
                             && isMin){
                         noNeg++;
                         lastNegPeak = (Double) res[0];
                         delta +=((Double) res[0])*(1.-Constants.OPTIM_SENZITIVITY);

                        // do not increase more than last pozitive peack
                        // preserve the amount which was increased in error computing
                        if ((delta + (Double) res[0]) > lastPozPeak) {
                                delta = (lastPozPeak+lastNegPeak)/2-lastNegPeak;
                            }
                        Object[] optim = new Object[2];
                        optim[0] = (Double) res[0] + delta;
                        optim[1] = res[1];
                        optimizedResults.add(optim);

                        
                     }else {
                        Object[] optim = new Object[2];
                        optim[0] = (Double) res[0];
                        optim[1] = res[1];
                        optimizedResults.add(optim);
                    }
                     
                 } 

                ii++;
            } // for

          
        }
        return optimizedResults;
    }
    
}
