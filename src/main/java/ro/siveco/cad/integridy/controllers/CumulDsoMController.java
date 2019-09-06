package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.CumulDsoM;
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
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import ro.siveco.cad.integridy.controllers.util.DateUtils;

@Named("cumulDsoMController")
@ViewScoped
public class CumulDsoMController extends ParentRecordController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulDsoMFacade ejbFacade;
    private List<CumulDsoM> items = null;
    private CumulDsoM selected;

    private LineChartModel dateModelDSOM;
    private boolean noDSOMData;
    private String fileNameCumulDSO_M = "DSO_MonthlyConsumption_";
    private Date  dateStart, dateStop;
    
    @PostConstruct
    public void init(){
        super.init();
        if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
            axis.setTickFormat("%d/%m");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
            getRealConsumptionGraphicData(super.results, "canvasM");
            getOptimumConsumptionGraphicData("canvasM");
        }
        dateStart=getStartDate(referenceDate);
        dateStop=getStopDate(referenceDate);
        setFileNameCumulDSO_M(fileNameCumulDSO_M+DateUtils.dateDayFormat(dateStart)+"_"+DateUtils.dateDayFormat(dateStop));
    }
    public CumulDsoMController() {
        noDSOMData = false;
    }
    public Date getStartDate(Date refDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.add(Calendar.MONTH, -12);
        return calendar.getTime();
    }
    public Date getStopDate(Date refDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.add(Calendar.MONTH, 12);
        return calendar.getTime();
    }
    public String getFileNameCumulDSO_M() {
        return fileNameCumulDSO_M;
    }

    public void setFileNameCumulDSO_M(String fileNameCumulDSO_M) {
        this.fileNameCumulDSO_M = fileNameCumulDSO_M;
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

    
    public CumulDsoM getSelected() {
        return selected;
    }

    public void setSelected(CumulDsoM selected) {
        this.selected = selected;
    }

    @Override
    protected void setEmbeddableKeys() {
    }

//        @PostConstruct
//    @Override
//    public void init(){
//        super.init();
//        DateAxis axis = new DateAxis("Dates");
//        axis.setTickAngle(-30);
//        axis.setTickFormat("%d/%m,%H:%M");
//        dateModelDSOM.getAxes().put(AxisType.X, axis);
//    }
    
    @Override
    public CumulDsoMFacade getFacade() {
        return ejbFacade;
    }

    public CumulDsoM prepareCreate() {
        selected = new CumulDsoM();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoMCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoMUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoMDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CumulDsoM> getItems() {
        if (items == null) {
//            items = getFacade().findAll();
            items = getFacade().getConsumptionByDay(referenceDate);
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

    public CumulDsoM getCumulDsoM(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulDsoM> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulDsoM> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulDsoM.class)
    public static class CumulDsoMControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulDsoMController controller = (CumulDsoMController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulDsoMController");
            return controller.getCumulDsoM(getKey(value));
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
            if (object instanceof CumulDsoM) {
                CumulDsoM o = (CumulDsoM) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulDsoM.class.getName()});
                return null;
            }
        }

    }

}
