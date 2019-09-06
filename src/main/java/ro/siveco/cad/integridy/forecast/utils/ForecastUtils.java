/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.forecast.utils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author roxanam
 */
public class ForecastUtils {
    public static Date getForecastDate(Date refDate){
      if(refDate==null)
          return null;
      LocalDateTime localDate = refDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
      DayOfWeek dow = localDate.getDayOfWeek();
      localDate = localDate.minus(1,ChronoUnit.YEARS );
      localDate = localDate.with(TemporalAdjusters.next(dow));
      return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getNewDate(Date oldDate){
        if(oldDate==null)
          return null;
        LocalDateTime localDate = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek dow = localDate.getDayOfWeek();
        localDate = localDate.plus(1,ChronoUnit.YEARS );
        localDate = localDate.with(TemporalAdjusters.previous(dow));
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }
    
     public static List<Object[]>  createForecastDataModel(List<Object[]> registeredResults){
       
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
     
     public static List<Object[]> completeForecastData(List<Object[]> dataIn){
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
     public static List<Object[]> completeForecastData(List<Object[]> dataIn, int minutes){
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
             
             crtTime = crtTime.plusMinutes(minutes);
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
      
     public static List<Object[]> updateDateInList(List<Object[]> initialList){
        List<Object[]> resultList = new ArrayList<>();
        for(Object[] val:initialList){
            Object[] newVal = new Object[2];
            newVal[0] = val[0];
            newVal[1] = getNewDate((Date)val[1]);
            resultList.add(newVal);
        }
        return resultList;
    }
}
