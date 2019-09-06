package ro.siveco.cad.integridy.controllers;

import org.primefaces.context.RequestContext;
import ro.siveco.cad.integridy.entities.CumulConsumerW;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.controllers.util.DateUtils;

@Named("cumulClientWController")
@ViewScoped
public class CumulClientWController extends ParentRecordController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulClientWFacade ejbFacade;
    
    private Date dateStart, dateStop;
    private List<CumulConsumerW> items = null;
//    private CumulConsumerW selected;
    
    @PostConstruct
     public void init() {
        super.init();
        getRealConsumptionGraphicData(super.results, "canvasW");
        getOptimumConsumptionGraphicData("canvasW");
        if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
            axis.setTickFormat("%d/%m");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
        }
        LocalDate refDate = referenceDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
        dateStart = Date.from(refDate.plus(-20, ChronoUnit.WEEKS).atStartOfDay(ZoneId.systemDefault()).toInstant());
        dateStop = Date.from(refDate.plus(20, ChronoUnit.WEEKS).atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        setFileName(currentConsumerClient.getFullName().replace(" ", "_")+"_WeeklyConsumption_"+DateUtils.dateDayFormat(dateStart)+"_"+DateUtils.dateDayFormat(dateStop));

        items  = getItems();

    }



    private Date startDate;
    private Date endDate;
    private List<CumulConsumerW> inBetween;

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

    public List<CumulConsumerW> getInBetween() {
        return inBetween;
    }

    public void setInBetween(List<CumulConsumerW> inBetween) {
        this.inBetween = inBetween;
    }

    private boolean noDataCumulClientW;
    
       public CumulClientWController() {
    }
       
    public boolean isNoDataCumulClientW() {
        return noDataCumulClientW;
    }

    public void setNoDataCumulClientW(boolean noDataCumulClientW) {
        this.noDataCumulClientW = noDataCumulClientW;
    }

    
    @Override
    public CumulClientWFacade getFacade() {
        return ejbFacade;
    }

    public CumulConsumerW prepareCreate() {
        selected = new CumulConsumerW();
        initializeEmbeddableKey();
        return (CumulConsumerW)selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientWCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientWUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulClientWDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CumulConsumerW> getItems() {
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
                    getFacade().edit((CumulConsumerW)selected);
                } else {
                    getFacade().remove((CumulConsumerW)selected);
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

    public CumulConsumerW getCumulClientW(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulConsumerW> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulConsumerW> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulConsumerW.class)
    public static class CumulClientWControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulClientWController controller = (CumulClientWController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulClientWController");
            return controller.getCumulClientW(getKey(value));
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
            if (object instanceof CumulConsumerW) {
                CumulConsumerW o = (CumulConsumerW) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulConsumerW.class.getName()});
                return null;
            }
        }

    }
     @Override
     public List<CumulConsumerW> getItemsCurrentClient() {
        if (itemsCurrentClient == null ) {
            itemsCurrentClient=getFacade().getByClientAndRefDate(currentConsumerClient.getId(), referenceDate);
        }
        return itemsCurrentClient;
    }

}
