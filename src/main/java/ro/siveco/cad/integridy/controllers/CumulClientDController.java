package ro.siveco.cad.integridy.controllers;

import org.primefaces.context.RequestContext;
import ro.siveco.cad.integridy.entities.CumulConsumerD;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import ro.siveco.cad.integridy.controllers.util.DateUtils;

@Named("cumulClientDController")
@ViewScoped
public class CumulClientDController extends ParentRecordController implements Serializable {

    

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulClientDFacade ejbFacade;
    
    private Date dateStart, dateStop;

    private List<CumulConsumerD> items = null;
//    private CumulConsumerD selected;
//   private List<CumulClientD> itemsCurrentClient = null;
   @PostConstruct
    @Override
    public void init(){
        super.init();
        getRealConsumptionGraphicData(super.results, "canvasD");
        getOptimumConsumptionGraphicData("canvasD");
         if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
            axis.setTickFormat("%d/%m");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
        }
         // +/- 30 zile reference time
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(referenceDate);
        calendarStart.add(Calendar.DAY_OF_YEAR, -30);
        dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(referenceDate);
        calendarStop.add(Calendar.DAY_OF_YEAR, 30);
        dateStop = calendarStop.getTime();
        setFileName(currentConsumerClient.getFullName().replace(" ", "_")+"_DailyConsumption_"+DateUtils.dateDayFormat(dateStart)+"_"+DateUtils.dateDayFormat(dateStop));

        items  = getItems();
    }


    private Date startDate;
    private Date endDate;
    private List<CumulConsumerD> inBetween;


    public void search() {

        items = null;
        items = getItems();
        if(startDate != null && endDate != null) {
            items = items.stream()
                    .filter(a -> ( a.getCreatedTime() == null || a.getCreatedTime().after(startDate) && ( a.getCreatedTime() == null || a.getCreatedTime().before(endDate))))
                    .collect(Collectors.toList());
        }
        RequestContext.getCurrentInstance().update("datalistM");
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<CumulConsumerD> getInBetween() {
        return inBetween;
    }

    public void setInBetween(List<CumulConsumerD> inBetween) {
        this.inBetween = inBetween;
    }

    public CumulClientDController() {
    }

    public CumulConsumerD getSelected() {
        return (CumulConsumerD)selected;
    }

    public void setSelected(CumulConsumerD selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    
    public CumulClientDFacade getFacade() {
        return ejbFacade;
    }

    public CumulConsumerD prepareCreate() {
        selected = new CumulConsumerD();
        initializeEmbeddableKey();
        return (CumulConsumerD)selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientDCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientDUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulClientDDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CumulConsumerD> getItems() {
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
                    getFacade().edit((CumulConsumerD)selected);
                } else {
                    getFacade().remove((CumulConsumerD)selected);
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

    public CumulConsumerD getCumulClientD(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulConsumerD> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulConsumerD> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulConsumerD.class)
    public static class CumulClientDControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulClientDController controller = (CumulClientDController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulClientDController");
            return controller.getCumulClientD(getKey(value));
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
            if (object instanceof CumulConsumerD) {
                CumulConsumerD o = (CumulConsumerD) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulConsumerD.class.getName()});
                return null;
            }
        }

    }

   @Override
    public List<CumulConsumerD> getItemsCurrentClient() {
        if (itemsCurrentClient == null) {
            itemsCurrentClient = getFacade().getByClientAndRefDate(currentConsumerClient.getId(), referenceDate);
        }
        return itemsCurrentClient;
    }


}
