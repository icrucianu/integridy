package ro.siveco.cad.integridy.controllers;


import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
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
import ro.siveco.cad.integridy.controllers.util.OperatorEnum;
import ro.siveco.cad.integridy.controllers.util.SeverityEnum;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumerSimpleRule;

@Named("consumerSimpleRuleController")
@ViewScoped
public class ConsumerSimpleRuleController implements Serializable {

    @EJB
    private ConsumerSimpleRuleFacade ejbFacade;
    @EJB
    private DashboardService dashboardService;
    private List<ConsumerSimpleRule> items = null;
    private ConsumerSimpleRule selected;
    
    private ConsumerClient currentClient;
    private List<ConsumerSimpleRule> currentConsumerSimpleRuleList=null;
    private List<ConsumerSimpleRule> globalConsumerSimpleRuleList=null;
    
    private ConsumerSimpleRule selectedGlobal;
    
    private String fileName="ConsumerSimpleRules";
    @PostConstruct
    public void init(){
        currentClient = dashboardService.getCurrentConsumerClient();
        if(currentClient!=null){
            currentConsumerSimpleRuleList = ejbFacade.getConsumerSimpleRuleByConsumer(currentClient.getId());
            globalConsumerSimpleRuleList = ejbFacade.getConsumerSimpleRuleGlobal();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    

    public ConsumerSimpleRuleController() {
    }

    public ConsumerSimpleRule getSelected() {
        return selected;
    }

    public void setSelected(ConsumerSimpleRule selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConsumerSimpleRuleFacade getFacade() {
        return ejbFacade;
    }

    public ConsumerSimpleRule prepareCreate() {
        selected = new ConsumerSimpleRule();
        if(currentClient!=null){
            selected.setClientId(currentClient);
        }
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if(selected!=null){
            selected.setCreatedOn(new Date());
            selected.setLastUpdatedOn(new Date());
            selected.setNumberOfRegistrations(1);
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerSimpleRuleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        if(currentClient!=null){
            currentConsumerSimpleRuleList = ejbFacade.getConsumerSimpleRuleByConsumer(currentClient.getId());
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerSimpleRuleUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsumerSimpleRuleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ConsumerSimpleRule> getItems() {
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

    public ConsumerSimpleRule getConsumerSimpleRule(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<ConsumerSimpleRule> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ConsumerSimpleRule> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    public OperatorEnum[] getOperators(){
        return OperatorEnum.values();
    }
    public SeverityEnum[] getSeverities(){
        return SeverityEnum.values();
    }

    public ConsumerClient getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(ConsumerClient currentClient) {
        this.currentClient = currentClient;
    }

    public List<ConsumerSimpleRule> getCurrentConsumerSimpleRuleList() {
        return currentConsumerSimpleRuleList;
    }

    public void setCurrentConsumerSimpleRuleList(List<ConsumerSimpleRule> currentConsumerSimpleRuleList) {
        this.currentConsumerSimpleRuleList = currentConsumerSimpleRuleList;
    }

    public List<ConsumerSimpleRule> getGlobalConsumerSimpleRuleList() {
        return globalConsumerSimpleRuleList;
    }

    public void setGlobalConsumerSimpleRuleList(List<ConsumerSimpleRule> globalConsumerSimpleRuleList) {
        this.globalConsumerSimpleRuleList = globalConsumerSimpleRuleList;
    }

    public ConsumerSimpleRule getSelectedGlobal() {
        return selectedGlobal;
    }

    public void setSelectedGlobal(ConsumerSimpleRule selectedGlobal) {
        this.selectedGlobal = selectedGlobal;
    }
    
    
    @FacesConverter(forClass = ConsumerSimpleRule.class)
    public static class ConsumerSimpleRuleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsumerSimpleRuleController controller = (ConsumerSimpleRuleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consumerSimpleRuleController");
            return controller.getConsumerSimpleRule(getKey(value));
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
            if (object instanceof ConsumerSimpleRule) {
                ConsumerSimpleRule o = (ConsumerSimpleRule) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ConsumerSimpleRule.class.getName()});
                return null;
            }
        }

    }

}
