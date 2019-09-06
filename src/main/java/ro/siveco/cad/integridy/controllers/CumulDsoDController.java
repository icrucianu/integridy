package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.CumulDsoD;
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
import org.apache.poi.ss.usermodel.DateUtil;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import ro.siveco.cad.integridy.controllers.util.DateUtils;

@Named("cumulDsoDController")
@RequestScoped
public class CumulDsoDController  extends ParentRecordController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulDsoDFacade ejbFacade;
    private List<CumulDsoD> items = null;
    private CumulDsoD selected;
    
    private final String CUMUL_DSO_D = "DSO_DailyConsumption_";
    
    
    private LineChartModel dateModelDSOD;
    private boolean noDSODData;
    
    private String fileNameCumulDSO_D;
    
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
        }
    }
    
    public CumulDsoDController() {
    }

    
    public CumulDsoD getSelected() {
        return selected;
    }

    public void setSelected(CumulDsoD selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    @Override
    public CumulDsoDFacade getFacade() {
        return ejbFacade;
    }
    
    /**
     *
     */
    
   

    public CumulDsoD prepareCreate() {
        selected = new CumulDsoD();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoDCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoDUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoDDeleted"));
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

    public boolean isNoDSODData() {
        return noDSODData;
    }

    public void setNoDSODData(boolean noDSODData) {
        this.noDSODData = noDSODData;
    }

    public LineChartModel getDateModelDSOD() {
        return dateModelDSOD;
    }

    public void setDateModelDSOD(LineChartModel dateModelDSOD) {
        this.dateModelDSOD = dateModelDSOD;
    }

    
    
    public CumulDsoD getCumulDsoD(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulDsoD> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulDsoD> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public Date getDayStart(Date crtDate){
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(crtDate);
        calendarStart.add(Calendar.DAY_OF_YEAR, -30);
        return calendarStart.getTime();
    }
    public Date getDayStop(Date crtDate){
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(crtDate);
        calendarStop.add(Calendar.DAY_OF_YEAR, 30);
        return calendarStop.getTime();
    }

    @Override
    public List getItems() {
       if(items==null){
           dateStart = getDayStart(referenceDate);
           dateStop = getDayStop(referenceDate);
           items = ejbFacade.getCumulDsoDbyDay(dateStart, dateStop);
           setFileNameCumulDSO_D(CUMUL_DSO_D+DateUtils.dateDayFormat(dateStart)+"_"+DateUtils.dateDayFormat(dateStop));
       }
       return items;
    }

    
    
    @FacesConverter(forClass = CumulDsoD.class)
    public static class CumulDsoDControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulDsoDController controller = (CumulDsoDController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulDsoDController");
            return controller.getCumulDsoD(getKey(value));
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
            if (object instanceof CumulDsoD) {
                CumulDsoD o = (CumulDsoD) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulDsoD.class.getName()});
                return null;
            }
        }

    }

    public String getFileNameCumulDSO_D() {
        return fileNameCumulDSO_D;
    }

    public void setFileNameCumulDSO_D(String fileNameCumulDSO_D) {
        this.fileNameCumulDSO_D = fileNameCumulDSO_D;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateStop() {
        return dateStop;
    }

    public void setDateStop(Date dateStop) {
        this.dateStop = dateStop;
    }
    

}
