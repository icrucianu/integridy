package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.CumulConsumerY;
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

@Named("cumulClientYController")
@SessionScoped
public class CumulClientYController  extends ParentRecordController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulClientYFacade ejbFacade;
    @PostConstruct
    public void init() {
        super.init();
        if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickFormat("%Y");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
        }
        setFileName(currentConsumerClient.getFullName().replace(" ", "_")+"_YearlyConsumption");
    }
   
  
    public CumulClientYController() {
    }

    
    public void setSelected(CumulConsumerY selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    @Override
    public CumulClientYFacade getFacade() {
        return ejbFacade;
    }

    public CumulConsumerY prepareCreate() {
        selected = new CumulConsumerY();
        initializeEmbeddableKey();
        return (CumulConsumerY)selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientYCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientYUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulClientYDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CumulConsumerY> getItems() {
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
                    getFacade().edit((CumulConsumerY)selected);
                } else {
                    getFacade().remove((CumulConsumerY)selected);
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

    public CumulConsumerY getCumulClientY(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulConsumerY> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulConsumerY> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulConsumerY.class)
    public static class CumulClientYControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulClientYController controller = (CumulClientYController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulClientYController");
            return controller.getCumulClientY(getKey(value));
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
            if (object instanceof CumulConsumerY) {
                CumulConsumerY o = (CumulConsumerY) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulConsumerY.class.getName()});
                return null;
            }
        }

    }
     @Override
     public List<CumulConsumerY> getItemsCurrentClient() {
        if (itemsCurrentClient == null) {
              itemsCurrentClient = getFacade().getByClientAndRefDate(currentConsumerClient.getId(), referenceDate);

        }
        return itemsCurrentClient;
    }

}
