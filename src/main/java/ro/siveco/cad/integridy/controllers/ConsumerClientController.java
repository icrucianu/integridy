package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import ro.siveco.cad.integridy.entities.ConsumptionPoint;
import ro.siveco.cad.integridy.entities.SmartMeter;

@Named("consumerClientController")
@ViewScoped
public class ConsumerClientController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.ConsumerClientFacade ejbFacade;
    private List<ConsumerClient> items = null;
    private ConsumerClient selected;
    
    @EJB
    private ConsumptionPointFacade consumptionPointFacade;
    private List<ConsumptionPoint> clientConsumptionPointList;
    private ConsumptionPoint selectedConsumptionPoint;
    private boolean noConsumptionPoint;
    
    @EJB
    private SmartMeterFacade smartMeterFacade;
    private List<SmartMeter> smartMeterList;
    private SmartMeter selectedSmartMeter;
    private boolean noSmartMeter;

    private String fileName="Consumers";
    
    public ConsumerClientController() {
    }

    public ConsumerClient getSelected() {
        return selected;
    }

    public void setSelected(ConsumerClient selected) {
        this.selected = selected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConsumerClientFacade getFacade() {
        return ejbFacade;
    }
    
    
    public void onSelectClient(){
       //get consumption points
       clientConsumptionPointList = null;
       if(selected!=null)
            clientConsumptionPointList = consumptionPointFacade.getByClientId(selected.getId());
       selectedConsumptionPoint = null;
       
    }
    public ConsumptionPoint prepareCreateConsumptionPoint() {
        selectedConsumptionPoint = new ConsumptionPoint();
        initializeEmbeddableKey();
        return selectedConsumptionPoint;
    }
    public SmartMeter prepareCreateSmartMeter() {
        selectedSmartMeter = new SmartMeter();
        initializeEmbeddableKey();
        return selectedSmartMeter;
    }
    public void createConsumptionPoint() {
        persistConsumptionPoint(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionPointCreated"));
        if (!JsfUtil.isValidationFailed()) {
           clientConsumptionPointList = null;    // Invalidate list of items to trigger re-query.
        }
    }
    public void createSmartMeter() {
        persistSmartMeter(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SmartMeterCreated"));
        if (!JsfUtil.isValidationFailed()) {
           smartMeterList = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    private void persistConsumptionPoint(PersistAction persistAction, String successMessage) {
        if (selectedConsumptionPoint != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    consumptionPointFacade.edit(selectedConsumptionPoint);
                } else {
                    consumptionPointFacade.remove(selectedConsumptionPoint);
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
    private void persistSmartMeter(PersistAction persistAction, String successMessage) {
        if (selectedSmartMeter != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    smartMeterFacade.edit(selectedSmartMeter);
                } else {
                    smartMeterFacade.remove(selectedSmartMeter);
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
    public void onSelectConsumptionPoint(){
        smartMeterList=null;
        if(selectedConsumptionPoint!=null){
            System.out.println(selected.getUserId());
            smartMeterList = smartMeterFacade.getByConsumptionPointId(selectedConsumptionPoint.getId());
        }
    }
     public void updateConsumptionPoint() {
        persistConsumptionPoint(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumptionPointUpdated"));
    }
     public void updateSmartMeter() {
        persistSmartMeter(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SmartMeterUpdated"));
    }
   public void onUnselectClient(){
       selected = null;
       selectedConsumptionPoint = null;
       selectedSmartMeter = null;
   }
    public void onUnselectConsumptionPoint(){
       selectedConsumptionPoint = null;
       selectedSmartMeter = null;
   }
    public void onSelectSmartMeter(){
        
    }
    
    public ConsumerClient prepareCreate() {
        selected = new ConsumerClient();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerClientCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerClientUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsumerClientDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    public void destroyConsumptionPoint() {
        persistConsumptionPoint(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsumerPointDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedConsumptionPoint = null; // Remove selection
            clientConsumptionPointList = null;    // Invalidate list of items to trigger re-query.
        }
    }
    public void destroySmartMeter() {
        persistSmartMeter(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SmartMeterDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedConsumptionPoint = null; // Remove selection
            clientConsumptionPointList = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ConsumerClient> getItems() {
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

    public ConsumerClient getConsumerClient(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<ConsumerClient> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ConsumerClient> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ConsumerClient.class)
    public static class ConsumerClientControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsumerClientController controller = (ConsumerClientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consumerClientController");
            return controller.getConsumerClient(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ConsumerClient) {
                ConsumerClient o = (ConsumerClient) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ConsumerClient.class.getName()});
                return null;
            }
        }

    }

    public List<ConsumptionPoint> getClientConsumptionPointList() {
        if(clientConsumptionPointList==null)
            if(selected!=null)
                clientConsumptionPointList = consumptionPointFacade.getByClientId(selected.getId());
        return clientConsumptionPointList;
    }

    public void setClientConsumptionPointList(List<ConsumptionPoint> clientConsumptionPointList) {
        this.clientConsumptionPointList = clientConsumptionPointList;
    }

    public ConsumptionPoint getSelectedConsumptionPoint() {
        return selectedConsumptionPoint;
    }

    public void setSelectedConsumptionPoint(ConsumptionPoint selectedConsumptionPoint) {
        this.selectedConsumptionPoint = selectedConsumptionPoint;
    }

    public boolean isNoConsumptionPoint() {
        return noConsumptionPoint;
    }

    public void setNoConsumptionPoint(boolean noConsumptionPoint) {
        this.noConsumptionPoint = noConsumptionPoint;
    }

    public List<SmartMeter> getSmartMeterList() {
        if(smartMeterList==null){
            if(selectedConsumptionPoint!=null)
                smartMeterList = smartMeterFacade.getByConsumptionPointId(selectedConsumptionPoint.getId());
        }
        return smartMeterList;
    }

    public void setSmartMeterList(List<SmartMeter> smartMeterList) {
        this.smartMeterList = smartMeterList;
    }

    public SmartMeter getSelectedSmartMeter() {
        return selectedSmartMeter;
    }

    public void setSelectedSmartMeter(SmartMeter selectedSmartMeter) {
        this.selectedSmartMeter = selectedSmartMeter;
    }

    public boolean isNoSmartMeter() {
        return noSmartMeter;
    }

    public void setNoSmartMeter(boolean noSmartMeter) {
        this.noSmartMeter = noSmartMeter;
    }
    
    

}
