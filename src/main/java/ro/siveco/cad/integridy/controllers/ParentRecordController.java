package ro.siveco.cad.integridy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.primefaces.model.chart.*;
import ro.siveco.cad.integridy.entities.ConsumptionRecord;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.controllers.util.ValueOnChart;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumptionPoint;
import ro.siveco.cad.integridy.entities.SmartMeter;


/**
 * This class is the partent class for all controllers used to display charts
 * This class gets data from tables, then computes the peack and also the optimum
 * Data is different depending on the ejbFacade used.
 * @author iacobc
 * @param <T> T is the clas derived from it
 */
//@Named("parentRecordController")
//@RequestScoped
public abstract class ParentRecordController<T> implements Serializable {

    /**
     * @param forecastedDataSeries the forecastedDataSeries to set
     */
    public void setForecastedDataSeries(List<Object[]> forecastedDataSeries) {
        this.forecastedDataSeries = forecastedDataSeries;
    }

    
    // this is the eEjbFacade used globaly in function
     // it is initialized with the curent facade
    protected ro.siveco.cad.integridy.controllers.AbstractFacade ejbFacade;
   
    protected List<T> items = null;
    protected List<T> itemsCurrentClient = null;
    protected T selected;
    private String fileName="Consumption";

    // Link to dashboard
    @EJB
    protected DashboardService dashboardService;
    protected String currenUsername;
    protected Date referenceDate;

    // link to current consumer
    @EJB
    protected ConsumerClientFacade consumerClientFacade;
    protected ConsumerClient currentConsumerClient;

    // link to consumption points
    @EJB
    protected ConsumptionPointFacade consumptionPointFacade;
    protected List<ConsumptionPoint> currentClientConsumptionPointList;

    // Link to smart meter
    @EJB
    protected SmartMeterFacade smartMeterFacade;
    protected List<SmartMeter> smartMeterList;

    protected Integer selectedConsumptionPointId;
    protected List<SelectItem> consumptionPoints;

    protected LineChartModel dateModelCurrent;
    protected boolean noDataCurrent;

    protected LineChartModel dateModelCurrentTotal;
    protected boolean noDataCurrentTotal;

    protected List<Object[]> fullDataSeries;
    
    protected List<Object[]> results;

    protected List<Object[]> peackDataSeries;

    protected List<Object[]> optimumDataSeries;

    protected List<Object[]> forecastedDataSeries;

    protected SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        noDataCurrent = true;
        noDataCurrentTotal = true;
        setCurrenUsername(dashboardService.getUsername());
        setReferenceDate(dashboardService.getCurrentDate());
       
        setCurrentConsumerClient(dashboardService.getCurrentConsumerClient());
        
        // in case it is a dso do not initialize client data
        if(getCurrentConsumerClient()!=null){
            setCurrentClientConsumptionPointList(consumptionPointFacade.getByClientId(getCurrentConsumerClient().getId()));

            initConsumptionPoints();
            //getItemsCurrentClient();
        }

        if (consumptionPoints != null && consumptionPoints.size() > 0) {
            selectedConsumptionPointId = (Integer) consumptionPoints.get(0).getValue();
        }
        noDataCurrentTotal = true;

        fullDataSeries = new ArrayList();

        peackDataSeries = new ArrayList();

        optimumDataSeries = new ArrayList();

        forecastedDataSeries = new ArrayList();

       
        createTotalConsumptionDataModel();

//         getOptimumConsumptionGraphicData();
    }

    public void getRealConsumptionGraphicData(List<Object[]> results) {

        List<ValueOnChart> pointstList = new ArrayList<>();
        if (results != null && results.size() > 0) {
            for (Object[] o : results) {
                pointstList.add(new ValueOnChart(df.format((Date) o[1]), (Double) o[0]));
            }
        }
        //Call js function to populate chart with data
        ObjectMapper objectMapper = new ObjectMapper();
        String optimumDataJSON;
        try {
            optimumDataJSON = objectMapper.writeValueAsString(pointstList);
            RequestContext.getCurrentInstance().execute("getRealData('" + optimumDataJSON + "')");
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ParentRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getOptimumConsumptionGraphicData() {

        List<ValueOnChart> pointstList = new ArrayList<>();
        if (optimumDataSeries != null && optimumDataSeries.size() > 0) {
            for (Object[] o : optimumDataSeries) {
                pointstList.add(new ValueOnChart(df.format((Date) o[1]), (Double) o[0]));
            }
        }
        //Call js function to populate chart with data
        ObjectMapper objectMapper = new ObjectMapper();
        String optimumDataJSON;
        try {
            optimumDataJSON = objectMapper.writeValueAsString(pointstList);
            RequestContext.getCurrentInstance().execute("getOptimumData('" + optimumDataJSON + "')");
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ParentRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getRealConsumptionGraphicData(List<Object[]> results, String canvasName) {

        List<ValueOnChart> pointstList = new ArrayList<>();
        if (results != null && results.size() > 0) {
            for (Object[] o : results) {
                pointstList.add(new ValueOnChart(df.format((Date) o[1]), (Double) o[0]));
            }
        }
        //Call js function to populate chart with data
        ObjectMapper objectMapper = new ObjectMapper();
        String optimumDataJSON;
        try {
            optimumDataJSON = objectMapper.writeValueAsString(pointstList);
            RequestContext.getCurrentInstance().execute("getRealData('" + optimumDataJSON + "','" + canvasName + "')");
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ParentRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   public void getOptimumConsumptionGraphicData(String canvasName) {

        List<ValueOnChart> pointstList = new ArrayList<>();
        if (optimumDataSeries != null && optimumDataSeries.size() > 0) {
            for (Object[] o : (List<Object[]>)optimumDataSeries) {
                pointstList.add(new ValueOnChart(df.format((Date) o[1]), (Double) o[0]));
            }
        }
        //Call js function to populate chart with data
        ObjectMapper objectMapper = new ObjectMapper();
        String optimumDataJSON;
        try {
            optimumDataJSON = objectMapper.writeValueAsString(pointstList);
            RequestContext.getCurrentInstance().execute("getOptimumData('" + optimumDataJSON  + "','" + canvasName + "')");
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ParentRecordController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
     * This is the main function
     * It establish the correct facade to data
     * Then it reads the data
     * then computes the peak and optimum
     * Finally constructs the data series for charts
     * And establish chart formats
     */
    public void createTotalConsumptionDataModel() {
        noDataCurrentTotal = true;
        ejbFacade = getFacade();
       
        // for dso there is no client id (for the moment)
        int clientID=0;
        if(currentConsumerClient !=null){
            clientID = currentConsumerClient.getId();
        }
        
        // read data from tables
       results = ejbFacade.getTotalConsumptionByClient(clientID, referenceDate);
//        getRealConsumptionGraphicData(results);
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat titleDf = new SimpleDateFormat("yyyy-MM-dd");
        
        // For each raw od read data, detect peak, compute optimum
        // peack positive is considered if previous 4 values and next 4 values are smaller
        // peack negative is considered if previous 4 values and next 4 values are bigger
        // optimum is computed by decreasing positive peack and increasing negative peacks
        // so thta the total amount of power remain the same (MSE is used to compte the error)
        if (results != null && results.size() != 0) {

            int ii = 0;
            double delta = 0.;
            int noNeg = 0;
            int noPoz = 0;
            double lastPozPeak = 0.;
            double lastNegPeak = 0.;
            double difDelta = 0.;
            
            fullDataSeries.clear();
            peackDataSeries.clear();
            optimumDataSeries.clear();
            forecastedDataSeries.clear();

            noDataCurrentTotal = false;
            dateModelCurrentTotal = new LineChartModel();
            dateModelCurrentTotal.setExtender("chartExtender");
            LineChartSeries series1 = new LineChartSeries();
            LineChartSeries series2 = new LineChartSeries();
            LineChartSeries series3 = new LineChartSeries();
            BarChartSeries series4 = new BarChartSeries();
            BarChartSeries series5 = new BarChartSeries();

//           for (Object[] res : results) {
//               if(res[0]==null)
//                   res[0] = 0;
//           }
            // for each row in dataset, see if it is a peack, and compute optimum
            // first and last points are considered both peack and optimum
            // first 4 and last 4 point can not be peacks or optimum
            for (Object[] res : results) {
                series1.set(df.format((Date) res[1]), (Double) res[0]);
                fullDataSeries.add(res);
                if (ii == 0) {
                    series2.set(df.format((Date) res[1]), (Double) res[0]);
                    series3.set(df.format((Date) res[1]), (Double) res[0]);
                    series4.set(df.format((Date) res[1]), (Double) res[0]);
                    series5.set(df.format((Date) res[1]), (Double) res[0]);
                    lastPozPeak = (Double) res[0];
                    lastNegPeak = (Double) res[0];
                    Object[] optim = new Object[2];
                    optim[0] = res[0];
                    optim[1] = res[1];
                    optimumDataSeries.add(optim);
                }

                if (ii == (results.size() - 1)) {
                    series2.set(df.format((Date) res[1]), (Double) res[0]);
                    series3.set(df.format((Date) res[1]), (Double) res[0]);
                    series4.set(df.format((Date) res[1]), (Double) res[0]);
                    series5.set(df.format((Date) res[1]), (Double) res[0]);
                    Object[] optim = new Object[2];
                    optim[0] = res[0];
                    optim[1] = res[1];
                    optimumDataSeries.add(optim);
                }

                if (ii > 4 && ii < results.size() - 4) {
                    // posiotive peek

                    if (((Double) results.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] < (Double) res[0])
                            //                            && ((Double) results.get(ii - 5)[0] < (Double) res[0])
                            //                            && ((Double) results.get(ii - 6)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 1)[0] < (Double) res[0])
                            &&((Double) results.get(ii + 2)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] < (Double) res[0]) //                            && ((Double) results.get(ii + 5)[0] < (Double) res[0])
                            //                            && ((Double) results.get(ii + 6)[0] < (Double) res[0])
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
                        optimumDataSeries.add(optim);
                        Object[] forecast = new Object[2];
                        forecast[0] = (Double) res[0] * Constants.OPTIM_SENZITIVITY;
                        forecast[1] = res[1];
                        peackDataSeries.add(res);
                        series2.set(df.format((Date) res[1]), (Double) res[0]);
                        series3.set(df.format((Date) res[1]), (Double)optim[0]);
                        series4.set(df.format((Date) res[1]), (Double)optim[0]);
                        series5.set(df.format((Date) res[1]), (Double) res[0]);

                        forecastedDataSeries.add(optim);
                        //System.out.println("Positive " + ss + " ii " + ii + " date " + res[1] + " val " + res[0] + " optim " + optim[0] + " delta " + delta + "las pos peak " + lastPozPeak + " ");
                    }

                    // negative peak
                    if (((Double) results.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] > (Double) res[0])
                            //                            && ((Double) results.get(ii - 5)[0] > (Double) res[0])
                            //                            && ((Double) results.get(ii - 6)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] > (Double) res[0]) //                             && ((Double) results.get(ii + 5)[0] > (Double) res[0])
                            //                            && ((Double) results.get(ii + 6)[0] > (Double) res[0])
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
                        optimumDataSeries.add(optim);

                        Object[] forecast = new Object[2];
                        forecast[0] = (Double) res[0] + delta / 2;
                        forecast[1] = res[1];
                        optimumDataSeries.add(optim);
                        forecastedDataSeries.add(optim);
                        //System.out.println("Negative " + ss + " ii " + ii + " date " + res[1] + " val " + res[0] + " optim " + optim[0] + " delta " + delta + "las pos peak " + lastPozPeak + " ");
                        peackDataSeries.add(res);
                        series2.set(df.format((Date) res[1]), (Double) res[0]);
                        series3.set(df.format((Date) res[1]), (Double) res[0] + delta);
                        series4.set(df.format((Date) res[1]), (Double) res[0] + delta);
                        series5.set(df.format((Date) res[1]), (Double) res[0]);

                    }

                } 
                else if(ii >= results.size() - 4 && ii>=4){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=ii;li<results.size()-1;li++ ){
                         if((Double)res[0] < (Double)results.get(li)[0]){
                             isMax=false;
                         } else  if((Double)res[0] > (Double)results.get(li)[0]){
                             isMin = false;
                         }
                     }
                     //max
                     if (((Double) results.get(ii - 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] < (Double) res[0])
                              && ((Double) results.get(ii - 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] < (Double) res[0])
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
                        optimumDataSeries.add(optim);
                        Object[] forecast = new Object[2];
                        forecast[0] = (Double) res[0] * Constants.OPTIM_SENZITIVITY;
                        forecast[1] = res[1];
                        peackDataSeries.add(res);
                        series2.set(df.format((Date) res[1]), (Double) res[0]);
                        series3.set(df.format((Date) res[1]), (Double)optim[0]);
                        series4.set(df.format((Date) res[1]), (Double)optim[0]);
                        series5.set(df.format((Date) res[1]), (Double) res[0]);

                        forecastedDataSeries.add(optim);
                     }
                     //min
                     if (((Double) results.get(ii - 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 2)[0] > (Double) res[0])
                             && ((Double) results.get(ii - 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii - 4)[0] > (Double) res[0])
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
                        optimumDataSeries.add(optim);

                        Object[] forecast = new Object[2];
                        forecast[0] = (Double) res[0] + delta / 2;
                        forecast[1] = res[1];
                        optimumDataSeries.add(optim);
                        forecastedDataSeries.add(optim);
                        //System.out.println("Negative " + ss + " ii " + ii + " date " + res[1] + " val " + res[0] + " optim " + optim[0] + " delta " + delta + "las pos peak " + lastPozPeak + " ");
                        peackDataSeries.add(res);
                        series2.set(df.format((Date) res[1]), (Double) res[0]);
                        series3.set(df.format((Date) res[1]), (Double) res[0] + delta);
                        series4.set(df.format((Date) res[1]), (Double) res[0] + delta);
                        series5.set(df.format((Date) res[1]), (Double) res[0]);
                     }
                     
                 } 
                else if(ii>0 && ii<4 && ii<results.size()-4 ){
                     boolean isMax = true;
                     boolean isMin = true;
                     for(int li=0;li<ii;li++ ){
                         if((Double)res[0] < (Double)results.get(li)[0]){
                             isMax=false;
                         } else if((Double)res[0] > (Double)results.get(li)[0]){
                             isMin = false;
                         }
                     }
                     if (   isMax
                            && ((Double) results.get(ii + 1)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] < (Double) res[0])
                             && ((Double) results.get(ii + 3)[0] < (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] < (Double) res[0])
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
                        optimumDataSeries.add(optim);
                        Object[] forecast = new Object[2];
                        forecast[0] = (Double) res[0] * Constants.OPTIM_SENZITIVITY;
                        forecast[1] = res[1];
                        peackDataSeries.add(res);
                        series2.set(df.format((Date) res[1]), (Double) res[0]);
                        series3.set(df.format((Date) res[1]), (Double)optim[0]);
                        series4.set(df.format((Date) res[1]), (Double)optim[0]);
                        series5.set(df.format((Date) res[1]), (Double) res[0]);

                        forecastedDataSeries.add(optim);
                    }
                     //min
                     if (((Double) results.get(ii + 1)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 2)[0] > (Double) res[0])
                           && ((Double) results.get(ii + 3)[0] > (Double) res[0])
                            && ((Double) results.get(ii + 4)[0] > (Double) res[0])
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
                        optimumDataSeries.add(optim);

                        Object[] forecast = new Object[2];
                        forecast[0] = (Double) res[0] + delta / 2;
                        forecast[1] = res[1];
                        optimumDataSeries.add(optim);
                        forecastedDataSeries.add(optim);
                        //System.out.println("Negative " + ss + " ii " + ii + " date " + res[1] + " val " + res[0] + " optim " + optim[0] + " delta " + delta + "las pos peak " + lastPozPeak + " ");
                        peackDataSeries.add(res);
                        series2.set(df.format((Date) res[1]), (Double) res[0]);
                        series3.set(df.format((Date) res[1]), (Double) res[0] + delta);
                        series4.set(df.format((Date) res[1]), (Double) res[0] + delta);
                        series5.set(df.format((Date) res[1]), (Double) res[0]);
                     }
                 }

                ii++;
            } // for

            // define chart characteristics
            series1.setLabel("Consumed active power " + titleDf.format(referenceDate));
            series2.setLabel("Peack active power " + titleDf.format(referenceDate));
            series3.setLabel("Optimum active power " + titleDf.format(referenceDate));
            series4.setLabel("optimum"+titleDf.format(referenceDate));
            series5.setLabel("Peack  " + titleDf.format(referenceDate));
            dateModelCurrentTotal.addSeries(series1);
            dateModelCurrentTotal.addSeries(series2);
            dateModelCurrentTotal.addSeries(series3);
            dateModelCurrentTotal.addSeries(series4);
            dateModelCurrentTotal.addSeries(series5);
            dateModelCurrentTotal.setTitle("Consumed active power " + df.format(referenceDate));
            dateModelCurrentTotal.setZoom(true);
            dateModelCurrentTotal.getAxis(AxisType.Y).setLabel("Consumed Active Pow ");
            dateModelCurrentTotal.setShadow(false);
            dateModelCurrentTotal.setAnimate(true);
            dateModelCurrentTotal.setLegendPosition("nw");
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
           // axis.setTickFormat("%#d,%H:%M");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
        }
    }

    public void consumptionPointChanged() {

    }

    public boolean isOptimumDataReady() {
        return optimumDataSeries != null && optimumDataSeries.size() > 0;
    }

    protected void populateDateModel(LineChartModel lcm, List itemsCC) {
        return;
    }

//    {
//         LineChartSeries series1 = null;
//          int deviceNumber = 0;
//         for (T rect : itemsCC) {
//            
////            if (rect.getDeviceNumber() != deviceNumber) {
////                if (series1 != null) {
////                    dateModelCurrent.addSeries(series1);
////                }
////                deviceNumber = rect.getDeviceNumber();
////                series1 = new LineChartSeries();
////                series1.setLabel("Client consumption device " + rec.getDeviceNumber());
////            }
//////                if(rec.getDeviceNumber()==3)
////            series1.set(df.format(rect.getCreatedTime()), rec.getConsumedActivePow());
//        }
//        if (series1 != null) {
//            dateModelCurrent.addSeries(series1);
//        }
//    }
    protected void createDateModel() {
        itemsCurrentClient = getItemsCurrentClient();
        if (itemsCurrentClient == null || itemsCurrentClient.size() == 0) {
            noDataCurrent = true;
            return;
        } else {
            noDataCurrent = false;
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

        LineChartSeries series1 = null;

//           series1= new LineChartSeries();
//           series1.setLabel("Client consumption device " + 3);
        int deviceNumber = 0;

        populateDateModel(dateModelCurrent, itemsCurrentClient);

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

    private void initConsumptionPoints() {
        if (currentClientConsumptionPointList == null) {
            return;
        }
        consumptionPoints = new ArrayList<SelectItem>();
        for (ConsumptionPoint cp : currentClientConsumptionPointList) {
            consumptionPoints.add(new SelectItem(cp.getId(), cp.getPointName()));
        }
    }

    public ParentRecordController() {
    }

    public LineChartModel getDateModelCurrent() {
        return dateModelCurrent;
    }

    public void setDateModelCurrent(LineChartModel dateModelCurrent) {
        this.dateModelCurrent = dateModelCurrent;
    }

    public boolean isNoDataCurrent() {
        return noDataCurrent;
    }

    public void setNoDataCurrent(boolean noDataCurrent) {
        this.noDataCurrent = noDataCurrent;
    }

    public LineChartModel getDateModelCurrentTotal() {
        return dateModelCurrentTotal;
    }

    public void setDateModelCurrentTotal(LineChartModel dateModelCurrentTotal) {
        this.dateModelCurrentTotal = dateModelCurrentTotal;
    }

    public boolean isNoDataCurrentTotal() {
        return noDataCurrentTotal;
    }

    public void setNoDataCurrentTotal(boolean noDataCurrentTotal) {
        this.noDataCurrentTotal = noDataCurrentTotal;
    }

    public List<SelectItem> getConsumptionPoints() {
        return consumptionPoints;
    }

    public void setConsumptionPoints(List<SelectItem> consumptionPoints) {
        this.consumptionPoints = consumptionPoints;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public Integer getSelectedConsumptionPointId() {
        return selectedConsumptionPointId;
    }

    public void setSelectedConsumptionPointId(Integer selectedConsumptionPointId) {
        this.selectedConsumptionPointId = selectedConsumptionPointId;
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public abstract AbstractFacade getFacade(); 

    public String getCurrenUsername() {
        return currenUsername;
    }

    public void setCurrenUsername(String currenUsername) {
        this.currenUsername = currenUsername;
    }

    public ConsumerClient getCurrentConsumerClient() {
        return currentConsumerClient;
    }

    public void setCurrentConsumerClient(ConsumerClient currentConsumerClient) {
        this.currentConsumerClient = currentConsumerClient;
    }

    public List<ConsumptionPoint> getCurrentClientConsumptionPointList() {
        return currentClientConsumptionPointList;
    }

    public void setCurrentClientConsumptionPointList(List<ConsumptionPoint> currentClientConsumptionPointList) {
        this.currentClientConsumptionPointList = currentClientConsumptionPointList;
    }

    public List<SmartMeter> getSmartMeterList() {
        return smartMeterList;
    }

    public void setSmartMeterList(List<SmartMeter> smartMeterList) {
        this.smartMeterList = smartMeterList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    
    /**
     * @return the fullDataSeries
     */
    public List<Object[]> getFullDataSeries() {
        return fullDataSeries;
    }

    /**
     * @param fullDataSeries the fullDataSeries to set
     */
    public void setFullDataSeries(List<Object[]> fullDataSeries) {
        this.fullDataSeries = fullDataSeries;
    }

    /**
     * @return the peackDataSeries
     */
    public List<Object[]> getPeackDataSeries() {
        return peackDataSeries;
    }

    /**
     * @param peackDataSeries the peackDataSeries to set
     */
    public void setPeackDataSeries(List<Object[]> peackDataSeries) {
        this.peackDataSeries = peackDataSeries;
    }

    /**
     * @return the optimumDataSeries
     */
    public List<Object[]> getOptimumDataSeries() {
        return optimumDataSeries;
    }

    /**
     * @param optimumDataSeries the optimumDataSeries to set
     */
    public void setOptimumDataSeries(List<Object[]> optimumDataSeries) {
        this.optimumDataSeries = optimumDataSeries;
    }

    /**
     * @return the forecastedDataSeries
     */
    public List<Object[]> getForecastedDataSeries() {
        return forecastedDataSeries;
    }

//
//    public ConsumptionRecord prepareCreate() {
//        selected = new ConsumptionRecord();
//        initializeEmbeddableKey();
//        return selected;
//    }
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionRecordCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionRecordUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionRecordDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<T> getItems() {
        if (items == null) {
            items = getFacade().findAll();
//            items = getFacade().getBycli(currenUsername, referenceDate);
        }
        return items;
    }
//
    public List<T> getItemsCurrentClient() {
        if (itemsCurrentClient == null) {
//              itemsCurrentClient = getFacade().getByUsernameRefDate(currenUsername, referenceDate);
            itemsCurrentClient = getFacade().getByClientAndRefDate(currentConsumerClient.getId(), referenceDate);
  
        }
        return itemsCurrentClient;
    }

    public void setItemsCurrentClient(List<T> itemsCurrentClient) {
        this.itemsCurrentClient = itemsCurrentClient;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
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

//    public ConsumptionRecord getConsumptionRecord(java.lang.Long id) {
//        return getFacade().find(id);
//    }
    public List<ConsumptionRecord> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ConsumptionRecord> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }


}
