package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.CumulDsoH;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.DateFormat;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;

@Named("cumulDsoHController")
@ViewScoped
public class CumulDsoHController extends ParentRecordController implements Serializable {

    private final String CUMUL_DSO_H = "DSO_HourlyConsumption_";
    private final String dateFormat = "ddMMYYYY";
    private DateFormat df = new SimpleDateFormat(dateFormat);
    @EJB
    private ro.siveco.cad.integridy.controllers.CumulDsoHFacade ejbFacade;
    private List<CumulDsoH> items = null;
    private CumulDsoH selected;
    private String fileNameCumulDSO_H;
    
    
//    @EJB
//    private DashboardService dashboardService;
//    private Date referenceDate;
    
    private LineChartModel dateModelDSOH;
    private boolean noDSOHData;

    public CumulDsoHController() {
        noDSOHData=false;
    }

    public CumulDsoH getSelected() {
        return selected;
    }

    public void setSelected(CumulDsoH selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    @Override
    public CumulDsoHFacade getFacade() {
        return ejbFacade;
    }
    
    public CumulDsoH prepareCreate() {
        selected = new CumulDsoH();
        initializeEmbeddableKey();
        return selected;
    }
    
    
    @PostConstruct
    public void init(){
        super.init();
        if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
            axis.setTickFormat("%d,%H:%M");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
            getRealConsumptionGraphicData(super.results, "canvas");
            getOptimumConsumptionGraphicData("canvas");
           
        }
        
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoHCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoHUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulDsoHDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CumulDsoH> getItems() {
        if (items == null || referenceDate.equals(dashboardService.getCurrentDate())) {
            items = getFacade().getCumulDsoHbyDay(dashboardService.getCurrentDate());
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

    public CumulDsoH getCumulDsoH(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulDsoH> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulDsoH> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulDsoH.class)
    public static class CumulDsoHControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulDsoHController controller = (CumulDsoHController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulDsoHController");
            return controller.getCumulDsoH(getKey(value));
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
            if (object instanceof CumulDsoH) {
                CumulDsoH o = (CumulDsoH) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulDsoH.class.getName()});
                return null;
            }
        }

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

    public String getFileNameCumulDSO_H() {
        return CUMUL_DSO_H+"_" + df.format(dashboardService.getCurrentDate());
    }

    public void setFileNameCumulDSO_H(String fileNameCumulDSO_H) {
        this.fileNameCumulDSO_H = fileNameCumulDSO_H;
    }
    
    
}
