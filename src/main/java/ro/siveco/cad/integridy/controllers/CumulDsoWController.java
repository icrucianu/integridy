package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.CumulDsoW;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import ro.siveco.cad.integridy.controllers.util.DateUtils;


@Named("cumulDsoWController")
@RequestScoped
public class CumulDsoWController extends ParentRecordController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulDsoWFacade ejbFacade;
    private List<CumulDsoW> items = null;
    private CumulDsoW selected;
    
    private LineChartModel dateModelDSOW;
    private boolean noDSOWData;
    private final String CUMUL_DSO_W="DSO_WeeklyConsumption_";
    private String fileNameCumulDSO_W = CUMUL_DSO_W;
    private Date dateStart;
    private Date dateStop; 
    

    @PostConstruct
    public void init(){
        super.init();
        if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
            axis.setTickFormat("%d/%m");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
            getRealConsumptionGraphicData(super.results, "canvasW");
            getOptimumConsumptionGraphicData("canvasW");
        }
        dateStart = getDateStart(referenceDate);
        dateStop = getDateStop(referenceDate);
        fileNameCumulDSO_W = fileNameCumulDSO_W + DateUtils.dateDayFormat(dateStart)+"_"+DateUtils.dateDayFormat(dateStop);
    }
    
    public LineChartModel getDateModelDSOW() {
        return dateModelDSOW;
    }

    public void setDateModelDSOW(LineChartModel dateModelDSOW) {
        this.dateModelDSOW = dateModelDSOW;
    }
    
    public void setNoDSOWData(boolean noDSOWData) {
        this.noDSOWData = noDSOWData;
    }

    public boolean isNoDSOWData() {
        return noDSOWData;
    }
  
   
    public CumulDsoWController() {
    }

    
    public CumulDsoW getSelected() {
        return selected;
    }
    
    
    public void setSelected(CumulDsoW selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }
    //date +/- 6 luni
    public Date getDateStart(Date refDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.add(Calendar.MONTH, -6);
        return calendar.getTime();
    }
    public Date getDateStop(Date refDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.add(Calendar.MONTH, 6);
        return calendar.getTime();
    }
    @Override
    public CumulDsoWFacade getFacade() {
        return ejbFacade;
    }
    
//        @PostConstruct
//    public void init(){
//        super.init();
//        DateAxis axis = new DateAxis("Dates");
//        axis.setTickAngle(-30);
//        axis.setTickFormat("%d/%m,%H:%M");
//        dateModelDSOW.getAxes().put(AxisType.X, axis);
//    }

    public CumulDsoW prepareCreate() {
        selected = new CumulDsoW();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoWCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoWUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoWDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
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

    public CumulDsoW getCumulDsoW(java.lang.Long id) {
        return getFacade().find(id);
    }

    
    public List<CumulDsoW> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulDsoW> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulDsoW.class)
    public static class CumulDsoWControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulDsoWController controller = (CumulDsoWController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulDsoWController");
            return controller.getCumulDsoW(getKey(value));
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
            if (object instanceof CumulDsoW) {
                CumulDsoW o = (CumulDsoW) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulDsoW.class.getName()});
                return null;
            }
        }

    }

    public String getFileNameCumulDSO_W() {
        return fileNameCumulDSO_W;
    }

    public void setFileNameCumulDSO_W(String fileNameCumulDSO_W) {
        this.fileNameCumulDSO_W = fileNameCumulDSO_W;
    }
    
}
