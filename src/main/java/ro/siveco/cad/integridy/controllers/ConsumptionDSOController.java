/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.entities.CumulDsoD;
import ro.siveco.cad.integridy.entities.CumulDsoH;
import ro.siveco.cad.integridy.entities.CumulDsoM;
import ro.siveco.cad.integridy.entities.CumulDsoW;
import ro.siveco.cad.integridy.entities.CumulDsoY;

/**
 *
 * @author roxanam
 */
@Named("consumptionDSOController")
@RequestScoped
public class ConsumptionDSOController implements Serializable{
    
    @EJB
    private DashboardService dashboardService;
    private Date referenceDate;

    
    @EJB
    private CumulDsoHFacade cumulDsoHFacade;
    private List<CumulDsoH> cumulDsoHList;
    private CumulDsoH cumulDsoHSelected;
    private LineChartModel dateModelDSOH;
    private boolean noDSOHData;
    
    @EJB
    private CumulDsoDFacade cumulDsoDFacade;
    private List<CumulDsoD> cumulDsoDList;
    private CumulDsoD cumulDsoDSelected;
    
    
    @EJB
    private CumulDsoWFacade cumulDsoWFacade;
    private List<CumulDsoW> cumulDsoWList;
    private CumulDsoW cumulDsoWSelected;
    
    @EJB
    private CumulDsoMFacade cumulDsoFacade;
    private List<CumulDsoM> cumulDsoMList;
    private CumulDsoM cumulDsoMSelected;
    
    @EJB
    private CumulDsoYFacade cumulDsoYFacade;
    private List<CumulDsoY> cumulDsoYList;
    private CumulDsoY cumulDsoYSelected;

    
    @PostConstruct
    public void init(){
        setNoDSOHData(true);
        setReferenceDate(dashboardService.getCurrentDate());
        createModelDSO_H();
    }
    public void createModelDSO_H(){
        cumulDsoHList = getCumulDsoHList();
        if(cumulDsoHList == null || cumulDsoHList.size()==0){
            setNoDSOHData(true);
            return;
        } else
            setNoDSOHData(false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateModelDSOH = new LineChartModel();
        LineChartSeries serie = new LineChartSeries();
        serie.setLabel("Consum dso");
        
        for(CumulDsoH rec: cumulDsoHList){
            serie.set(df.format(rec.getCreatedTime()), rec.getConsumedActivePow());
        }
        dateModelDSOH.addSeries(serie);
        
        dateModelDSOH.setTitle("Consumed active power " );
        dateModelDSOH.setZoom(true);
        dateModelDSOH.getAxis(AxisType.Y).setLabel("Consumed Active Pow ");
        dateModelDSOH.setShadow(false);
        dateModelDSOH.setAnimate(true);
       
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-30);
        axis.setTickFormat("%#d/%#m,%H:%#M");
        dateModelDSOH.getAxes().put(AxisType.X, axis);
        
    }
    public List<CumulDsoH> getCumulDsoHList() {
        if(cumulDsoHList==null || cumulDsoHList.size()==0|| !dashboardService.getCurrentDate().equals(referenceDate)){
            cumulDsoHList = cumulDsoHFacade.getCumulDsoHbyDay(referenceDate);
            
        }
        return cumulDsoHList;
    }

    public void setCumulDsoHList(List<CumulDsoH> cumulDsoHList) {
        this.cumulDsoHList = cumulDsoHList;
    }

    public CumulDsoH getCumulDsoHSelected() {
        return cumulDsoHSelected;
    }

    public void setCumulDsoHSelected(CumulDsoH cumulDsoHSelected) {
        this.cumulDsoHSelected = cumulDsoHSelected;
    }

    public List<CumulDsoD> getCumulDsoDList() {
        return cumulDsoDList;
    }

    public void setCumulDsoDList(List<CumulDsoD> cumulDsoDList) {
        this.cumulDsoDList = cumulDsoDList;
    }

    public CumulDsoD getCumulDsoDSelected() {
        return cumulDsoDSelected;
    }

    public void setCumulDsoDSelected(CumulDsoD cumulDsoDSelected) {
        this.cumulDsoDSelected = cumulDsoDSelected;
    }

    public List<CumulDsoW> getCumulDsoWList() {
        return cumulDsoWList;
    }

    public void setCumulDsoWList(List<CumulDsoW> cumulDsoWList) {
        this.cumulDsoWList = cumulDsoWList;
    }

    public CumulDsoW getCumulDsoWSelected() {
        return cumulDsoWSelected;
    }

    public void setCumulDsoWSelected(CumulDsoW cumulDsoWSelected) {
        this.cumulDsoWSelected = cumulDsoWSelected;
    }

    public List<CumulDsoM> getCumulDsoMList() {
        return cumulDsoMList;
    }

    public void setCumulDsoMList(List<CumulDsoM> cumulDsoMList) {
        this.cumulDsoMList = cumulDsoMList;
    }

    public CumulDsoM getCumulDsoMSelected() {
        return cumulDsoMSelected;
    }

    public void setCumulDsoMSelected(CumulDsoM cumulDsoMSelected) {
        this.cumulDsoMSelected = cumulDsoMSelected;
    }

    public List<CumulDsoY> getCumulDsoYList() {
        return cumulDsoYList;
    }

    public void setCumulDsoYList(List<CumulDsoY> cumulDsoYList) {
        this.cumulDsoYList = cumulDsoYList;
    }

    public CumulDsoY getCumulDsoYSelected() {
        return cumulDsoYSelected;
    }

    public void setCumulDsoYSelected(CumulDsoY cumulDsoYSelected) {
        this.cumulDsoYSelected = cumulDsoYSelected;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public LineChartModel getDateModelDSOH() {
        return dateModelDSOH;
    }

    public void setDateModelDSOH(LineChartModel dateModelDSOH) {
        this.dateModelDSOH = dateModelDSOH;
    }

    public boolean isNoDSOHData() {
        return noDSOHData;
    }

    public void setNoDSOHData(boolean noDSOHData) {
        this.noDSOHData = noDSOHData;
    }
    
    
    
    
    
    
    
    
}
