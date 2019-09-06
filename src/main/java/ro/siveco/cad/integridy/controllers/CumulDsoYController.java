package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.CumulDsoY;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

@Named("cumulDsoYController")
@SessionScoped
public class CumulDsoYController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulDsoYFacade ejbFacade;
    private List<CumulDsoY> items = null;
    private CumulDsoY selected;
    private String fileNameCumulDSO_Y = "DSO_Yearly_consumption";
   
    
    @EJB
    private DashboardService dashboardService;
    private Date referenceDate;
    
    private LineChartModel dateModelDSOY;
    private boolean noDSOYData;

    public CumulDsoYController() {
    }

    public String getFileNameCumulDSO_Y() {
        return fileNameCumulDSO_Y;
    }

    public void setFileNameCumulDSO_Y(String fileNameCumulDSO_Y) {
        this.fileNameCumulDSO_Y = fileNameCumulDSO_Y;
    }

    
    public CumulDsoY getSelected() {
        return selected;
    }

    public void setSelected(CumulDsoY selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }
    
//        @PostConstruct
//    @Override
//    public void init(){
//        super.init();
//        DateAxis axis = new DateAxis("Dates");
//        axis.setTickAngle(-30);
//        axis.setTickFormat("%d/%m,%H:%M");
//        dateModelDSOY.getAxes().put(AxisType.X, axis);
//    }

    protected void initializeEmbeddableKey() {
    }

    private CumulDsoYFacade getFacade() {
        return ejbFacade;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public LineChartModel getDateModelDSOY() {
        return dateModelDSOY;
    }

    public boolean isNoDSOYData() {
        return noDSOYData;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public void setDateModelDSOY(LineChartModel dateModelDSOY) {
        this.dateModelDSOY = dateModelDSOY;
    }

    public void setNoDSOYData(boolean noDSOHData) {
        this.noDSOYData = noDSOHData;
    }
    
    @PostConstruct
    public void init(){
        setNoDSOYData(true);
        setReferenceDate(dashboardService.getCurrentDate());
        createModelDSO_Y();
    }
    
    
    public void createModelDSO_Y(){
        items = getItems();
        if(items == null || items.isEmpty()){
            setNoDSOYData(noDSOYData);
            return;
        }else
            setNoDSOYData(false);
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateModelDSOY = new LineChartModel();
        dateModelDSOY.setExtender("chartExtender2");
        LineChartSeries serie = new LineChartSeries();
        serie.setLabel("Consum dso");
        
        for(CumulDsoY rec: items){
            serie.set(df.format(rec.getCreatedTime()), rec.getConsumedActivePow());
        }
        dateModelDSOY.addSeries(serie);
        
        dateModelDSOY.setTitle("Consumed active power " );
        dateModelDSOY.setZoom(true);
        dateModelDSOY.getAxis(AxisType.Y).setLabel("Consumed Active Pow ");
        dateModelDSOY.setShadow(false);
        dateModelDSOY.setAnimate(true);
       
        DateAxis axis = new DateAxis("Dates");
        axis.setTickFormat("%Y");
        dateModelDSOY.getAxes().put(AxisType.X, axis);
    }

    public CumulDsoY prepareCreate() {
        selected = new CumulDsoY();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoYCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoYUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoYDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CumulDsoY> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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

    public CumulDsoY getCumulDsoY(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulDsoY> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulDsoY> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulDsoY.class)
    public static class CumulDsoYControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulDsoYController controller = (CumulDsoYController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulDsoYController");
            return controller.getCumulDsoY(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CumulDsoY) {
                CumulDsoY o = (CumulDsoY) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulDsoY.class.getName()});
                return null;
            }
        }

    }

}
