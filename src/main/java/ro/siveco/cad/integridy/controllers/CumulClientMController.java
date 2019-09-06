package ro.siveco.cad.integridy.controllers;


import org.primefaces.context.RequestContext;
import ro.siveco.cad.integridy.entities.CumulConsumerM;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIData;
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

@Named("cumulClientMController")
@SessionScoped
public class CumulClientMController extends ParentRecordController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.CumulClientMFacade ejbFacade;
    private List<CumulConsumerM> items = null;
//    private CumulConsumerM selected;
    private Date dateStart, dateStop;

    private Date startDate;
    private Date endDate;
    private List<CumulConsumerM> inBetween;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        getRealConsumptionGraphicData(super.results, "canvasM");
        getOptimumConsumptionGraphicData("canvasM");
        if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
            axis.setTickFormat("%d/%m");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
            
        }
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(referenceDate);
        calendarStart.add(Calendar.MONTH, -12);
        dateStart = calendarStart.getTime();
        
        Calendar calendarStop = Calendar.getInstance();
        calendarStop.setTime(referenceDate);
        calendarStop.add(Calendar.MONTH, 12);
        dateStop = calendarStop.getTime();
        
        setFileName(currentConsumerClient.getFullName().replace(" ", "_")+"MonthlyConsumption_"+DateUtils.dateDayFormat(dateStart)+"_"+DateUtils.dateDayFormat(dateStop));

        items  = getItems();
    }


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

    public List<CumulConsumerM> getInBetween() {
        return inBetween;
    }

    public void setInBetween(List<CumulConsumerM> inBetween) {
        this.inBetween = inBetween;
    }

    private boolean noDataCumulClientM;

    public boolean isNoDataCumulClientM() {
        return noDataCumulClientM;
    }

    public void setNoDataCumulClientM(boolean noDataCumulClientM) {
        this.noDataCumulClientM = noDataCumulClientM;
    }

    public CumulConsumerM getSelected() {
        return (CumulConsumerM) selected;
    }

    public void setSelected(CumulConsumerM selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    @Override
    public CumulClientMFacade getFacade() {
        return ejbFacade;
    }

    public CumulConsumerM prepareCreate() {
        selected = new CumulConsumerM();
        initializeEmbeddableKey();
        return (CumulConsumerM) selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientMCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CumulClientMUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CumulClientMDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<CumulConsumerM> getItems() {
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
                    getFacade().edit((CumulConsumerM) selected);
                } else {
                    getFacade().remove((CumulConsumerM) selected);
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

    public CumulConsumerM getCumulClientM(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CumulConsumerM> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CumulConsumerM> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CumulConsumerM.class)
    public static class CumulClientMControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CumulClientMController controller = (CumulClientMController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cumulClientMController");
            return controller.getCumulClientM(getKey(value));
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
            if (object instanceof CumulConsumerM) {
                CumulConsumerM o = (CumulConsumerM) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CumulConsumerM.class.getName()});
                return null;
            }
        }

    }

    @Override
    public List<CumulConsumerM> getItemsCurrentClient() {
        if (itemsCurrentClient == null) {
            itemsCurrentClient = getFacade().getByClientAndRefDate(currentConsumerClient.getId(), referenceDate);
        }
        return itemsCurrentClient;
    }

}
