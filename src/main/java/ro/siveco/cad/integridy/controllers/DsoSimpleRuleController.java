package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.DsoSimpleRule;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;

@Named("dsoSimpleRuleController")
@ViewScoped
public class DsoSimpleRuleController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.DsoSimpleRuleFacade ejbFacade;
    private List<DsoSimpleRule> items = null;
    private DsoSimpleRule selected;
    
    private String fileName = "DsoSimpleRules" ;

    public DsoSimpleRuleController() {
    }

    public DsoSimpleRule getSelected() {
        return selected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    
    public void setSelected(DsoSimpleRule selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DsoSimpleRuleFacade getFacade() {
        return ejbFacade;
    }

    public DsoSimpleRule prepareCreate() {
        selected = new DsoSimpleRule();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DsoSimpleRuleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DsoSimpleRuleUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DsoSimpleRuleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<DsoSimpleRule> getItems() {
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

    public DsoSimpleRule getDsoSimpleRule(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<DsoSimpleRule> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DsoSimpleRule> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = DsoSimpleRule.class)
    public static class DsoSimpleRuleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DsoSimpleRuleController controller = (DsoSimpleRuleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "dsoSimpleRuleController");
            return controller.getDsoSimpleRule(getKey(value));
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
            if (object instanceof DsoSimpleRule) {
                DsoSimpleRule o = (DsoSimpleRule) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DsoSimpleRule.class.getName()});
                return null;
            }
        }

    }

}
