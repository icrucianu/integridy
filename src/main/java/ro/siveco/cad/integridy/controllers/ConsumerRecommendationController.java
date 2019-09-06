package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
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
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumerRecommendation;

@Named("consumerRecommendationController")
@ViewScoped
public class ConsumerRecommendationController implements Serializable {

    
    @EJB
    private DashboardService dashboardService;
    
    private ConsumerClient currentConsumerClient;
    
    @EJB
    private ConsumerRecommendationFacade ejbFacade;
    private List<ConsumerRecommendation> items = null;
    private ConsumerRecommendation selected;

    
    @PostConstruct
    public void init(){
         
        currentConsumerClient = dashboardService.getCurrentConsumerClient();
//        currentConsumerClient = dashboardService.getCurrentConsumerClient();
        items = ejbFacade.getByClientId(currentConsumerClient.getId());
        
    }
    
    public ConsumerRecommendationController() {
    }

    public ConsumerRecommendation getSelected() {
        return selected;
    }

    public void setSelected(ConsumerRecommendation selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConsumerRecommendationFacade getFacade() {
        return ejbFacade;
    }

    public ConsumerRecommendation prepareCreate() {
        selected = new ConsumerRecommendation();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerRecommendationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerRecommendationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsumerRecommendationDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ConsumerRecommendation> getItems() {
        if (items == null) {
//            items = getFacade().findAll();
            items = getFacade().getByClientId(currentConsumerClient.getId());
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

    public ConsumerRecommendation getConsumerRecommendation(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ConsumerRecommendation> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ConsumerRecommendation> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ConsumerRecommendation.class)
    public static class ConsumerRecommendationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsumerRecommendationController controller = (ConsumerRecommendationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consumerRecommendationController");
            return controller.getConsumerRecommendation(getKey(value));
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
            if (object instanceof ConsumerRecommendation) {
                ConsumerRecommendation o = (ConsumerRecommendation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ConsumerRecommendation.class.getName()});
                return null;
            }
        }

    }

}
