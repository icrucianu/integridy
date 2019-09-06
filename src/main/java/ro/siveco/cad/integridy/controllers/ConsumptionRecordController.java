package ro.siveco.cad.integridy.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.siveco.cad.integridy.entities.ConsumptionRecord;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import ro.siveco.cad.integridy.controllers.util.DateUtils;
import ro.siveco.cad.integridy.controllers.util.ValueOnChart;

@Named("consumptionRecordController")
@RequestScoped
public class ConsumptionRecordController extends ParentRecordController implements Serializable {

    
    @EJB
    private ro.siveco.cad.integridy.controllers.ConsumptionRecordFacade ejbFacade;
    
    private List<ConsumptionRecord> items = null;
    private List<ConsumptionRecord> itemsCurrentClient = null;
    private ConsumptionRecord selected;
    
    
    
    @PostConstruct
    @Override
    public void init(){
        super.init();
        getRealConsumptionGraphicData(super.results, "canvas");
        getOptimumConsumptionGraphicData("canvas");
        if(dateModelCurrentTotal!=null){
            DateAxis axis = new DateAxis("Dates");
            axis.setTickAngle(-30);
            axis.setTickFormat("%d/%m,%H:%M");
            dateModelCurrentTotal.getAxes().put(AxisType.X, axis);
        }
        
        setFileName(currentConsumerClient.getFullName().replace(" ", "_")+"_HourlyConsumption_"+DateUtils.dateDayFormat(referenceDate));
    }
   
    
    @Override
    protected void populateDateModel(LineChartModel lcm, List itemsCC){
      LineChartSeries series1 = null;
       int deviceNumber = 0;
      for (Object orec : itemsCC) {
          ConsumptionRecord rec = (ConsumptionRecord) orec;
         if (rec.getDeviceNumber() != deviceNumber) {
             if (series1 != null) {
                 lcm.addSeries(series1);
             }
             deviceNumber = rec.getDeviceNumber();
             series1 = new LineChartSeries();
             series1.setLabel("Client consumption device " + rec.getDeviceNumber());
         }
//                if(rec.getDeviceNumber()==3)
         series1.set(df.format(rec.getCreatedTime()), rec.getConsumedActivePow());
     }
     if (series1 != null) {
         lcm.addSeries(series1);
     }
 }


    public ConsumptionRecord prepareCreate() {
        selected = new ConsumptionRecord();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionRecordCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionRecordUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionRecordDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ConsumptionRecord> getItems() {
        if (items == null) {
//            items = getFacade().findAll();
            items = getFacade().getByUsernameRefDate(currenUsername, referenceDate);
        }
        return items;
    }

    public List<ConsumptionRecord> getItemsCurrentClient() {
        if (itemsCurrentClient == null) {
            itemsCurrentClient = getFacade().getByClientAndRefDate(currentConsumerClient.getId(), referenceDate);
        }
        return itemsCurrentClient;
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

    public ConsumptionRecord getConsumptionRecord(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ConsumptionRecord> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ConsumptionRecord> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    /**
     * @return the ejbFacade
     */
    @Override
    public ro.siveco.cad.integridy.controllers.ConsumptionRecordFacade getFacade() {
        return ejbFacade;
    }

    /**
     * @param ejbFacade the ejbFacade to set
     */
    public void setFacade(ro.siveco.cad.integridy.controllers.ConsumptionRecordFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    @FacesConverter(forClass = ConsumptionRecord.class)
    public static class ConsumptionRecordControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsumptionRecordController controller = (ConsumptionRecordController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consumptionRecordController");
            return controller.getConsumptionRecord(getKey(value));
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
            if (object instanceof ConsumptionRecord) {
                ConsumptionRecord o = (ConsumptionRecord) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ConsumptionRecord.class.getName()});
                return null;
            }
        }

    }

}
