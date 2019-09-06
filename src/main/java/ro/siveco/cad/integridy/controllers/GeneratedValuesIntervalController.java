package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.GeneratedValuesInterval;
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

@Named("generatedValuesIntervalController")
@SessionScoped
public class GeneratedValuesIntervalController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.GeneratedValuesIntervalFacade ejbFacade;
    private List<GeneratedValuesInterval> items = null;
    private GeneratedValuesInterval selected;

    public GeneratedValuesIntervalController() {
    }

    public GeneratedValuesInterval getSelected() {
        return selected;
    }

    public void setSelected(GeneratedValuesInterval selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private GeneratedValuesIntervalFacade getFacade() {
        return ejbFacade;
    }

    public GeneratedValuesInterval prepareCreate() {
        selected = new GeneratedValuesInterval();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GeneratedValuesIntervalCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GeneratedValuesIntervalUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GeneratedValuesIntervalDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<GeneratedValuesInterval> getItems() {
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

    public GeneratedValuesInterval getGeneratedValuesInterval(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<GeneratedValuesInterval> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<GeneratedValuesInterval> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = GeneratedValuesInterval.class)
    public static class GeneratedValuesIntervalControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GeneratedValuesIntervalController controller = (GeneratedValuesIntervalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "generatedValuesIntervalController");
            return controller.getGeneratedValuesInterval(getKey(value));
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
            if (object instanceof GeneratedValuesInterval) {
                GeneratedValuesInterval o = (GeneratedValuesInterval) object;
                return getStringKey(o.getHourOfDay());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), GeneratedValuesInterval.class.getName()});
                return null;
            }
        }

    }

}
