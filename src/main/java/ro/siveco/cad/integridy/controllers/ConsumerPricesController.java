package ro.siveco.cad.integridy.controllers;

import ro.siveco.cad.integridy.entities.ConsumerPrices;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;
import ro.siveco.cad.integridy.controllers.util.PriceType;

@Named("consumerPricesController")
@SessionScoped
public class ConsumerPricesController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.ConsumerPricesFacade ejbFacade;
    private List<ConsumerPrices> items = null;
    private List<ConsumerPrices> pricesInBetween;
    private List<ConsumerPrices> pricesInBetweenFirst;
    private List<ConsumerPrices> pricesInBetweenSecond;
    private ConsumerPrices selected;
    private Date validityStart;
    private Date validityEnd;

    private Date firstStart;
    private Date firstEnd;

    private Date secondStart;
    private Date secondEnd;
    
    private boolean showDiffControls=false;
    
    private String fileName="Prices";

    public ConsumerPricesController() {
    }
    public void priceTypeSelectionChanged(final AjaxBehaviorEvent event)  {
        if(selected.getPriceType().equals(PriceType.DIFFERENTIATED.toString())){
            showDiffControls=true;
        } else {
            showDiffControls=false;
        }
    }
    public void onRowSelect(SelectEvent event){
        if(selected.getPriceType().equals(PriceType.DIFFERENTIATED.toString())){
            showDiffControls=true;
        } else {
            showDiffControls=false;
        }
    }
    public PriceType[] getPriceTypes(){
        return PriceType.values();
    }

    public ConsumerPrices getSelected() {
        return selected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    
    public void setSelected(ConsumerPrices selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConsumerPricesFacade getFacade() {
        return ejbFacade;
    }

    public ConsumerPrices prepareCreate() {
        
        selected = new ConsumerPrices();
        showDiffControls=false;
        selected.setCreatedOn(new Date());
        initializeEmbeddableKey();
        return selected;
    }

    public boolean isShowDiffControls() {
        return showDiffControls;
    }

    public void setShowDiffControls(boolean showDiffControls) {
        this.showDiffControls = showDiffControls;
    }
    
    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerPricesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConsumerPricesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConsumerPricesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ConsumerPrices> getItems() {
        if (items == null) {
            items = getFacade().findAll();
            pricesInBetween  = items;
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

    public ConsumerPrices getConsumerPrices(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<ConsumerPrices> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ConsumerPrices> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void filter() {
        items = null;
        items = getItems();
        if(validityEnd != null && validityStart != null) {
            pricesInBetween = items.stream()
                    .filter(a -> ( a.getValidityStart() == null || a.getValidityStart().after(validityStart) && ( a.getValidityEnd() == null || a.getValidityEnd().before(validityEnd))))
                    .collect(Collectors.toList());
        } else{
            pricesInBetween = items.stream()
                    .filter(a -> (validityStart == null || a.getValidityStart() == null || a.getValidityStart().after(validityStart) && (validityEnd == null || a.getValidityEnd() == null || a.getValidityEnd().before(validityEnd))))
                    .collect(Collectors.toList());
        }
    }

    public void filterFirst() {
        items = null;
        items = getItems();
        if(firstStart != null && firstEnd != null) {
            pricesInBetweenFirst = items.stream()
                    .filter(a -> ( a.getValidityStart() == null || a.getValidityStart().after(firstStart) && ( a.getValidityEnd() == null || a.getValidityEnd().before(firstEnd))))
                    .collect(Collectors.toList());
        } else{
            pricesInBetweenFirst = items.stream()
                    .filter(a -> (firstStart == null || a.getValidityStart() == null || a.getValidityStart().after(firstStart) && (firstEnd == null || a.getValidityEnd() == null || a.getValidityEnd().before(firstEnd))))
                    .collect(Collectors.toList());
        }
    }

    public void filterSecond() {
        items = null;
        items = getItems();
        if(secondStart != null && secondEnd != null) {
            pricesInBetweenSecond = items.stream()
                    .filter(a -> ( a.getValidityStart() == null || a.getValidityStart().after(secondStart) && ( a.getValidityEnd() == null || a.getValidityEnd().before(secondEnd))))
                    .collect(Collectors.toList());
        } else{
            pricesInBetweenSecond = items.stream()
                    .filter(a -> (secondStart == null || a.getValidityStart() == null || a.getValidityStart().after(secondStart) && (secondEnd == null || a.getValidityEnd() == null || a.getValidityEnd().before(secondEnd))))
                    .collect(Collectors.toList());
        }
    }

    public Date getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(Date validityStart) {
        this.validityStart = validityStart;
    }

    public Date getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }

    public Date getFirstStart() {
        return firstStart;
    }

    public void setFirstStart(Date firstStart) {
        this.firstStart = firstStart;
    }

    public Date getFirstEnd() {
        return firstEnd;
    }

    public void setFirstEnd(Date firstEnd) {
        this.firstEnd = firstEnd;
    }

    public Date getSecondStart() {
        return secondStart;
    }

    public void setSecondStart(Date secondStart) {
        this.secondStart = secondStart;
    }

    public Date getSecondEnd() {
        return secondEnd;
    }

    public void setSecondEnd(Date secondEnd) {
        this.secondEnd = secondEnd;
    }

    public List<ConsumerPrices> getPricesInBetween() {
        return pricesInBetween;
    }

    public void setPricesInBetween(List<ConsumerPrices> pricesInBetween) {
        this.pricesInBetween = pricesInBetween;
    }

    public List<ConsumerPrices> getPricesInBetweenFirst() {
        return pricesInBetweenFirst;
    }

    public void setPricesInBetweenFirst(List<ConsumerPrices> pricesInBetweenFirst) {
        this.pricesInBetweenFirst = pricesInBetweenFirst;
    }

    public List<ConsumerPrices> getPricesInBetweenSecond() {
        return pricesInBetweenSecond;
    }

    public void setPricesInBetweenSecond(List<ConsumerPrices> pricesInBetweenSecond) {
        this.pricesInBetweenSecond = pricesInBetweenSecond;
    }

    @FacesConverter(forClass = ConsumerPrices.class)
    public static class ConsumerPricesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConsumerPricesController controller = (ConsumerPricesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "consumerPricesController");
            return controller.getConsumerPrices(getKey(value));
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
            if (object instanceof ConsumerPrices) {
                ConsumerPrices o = (ConsumerPrices) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ConsumerPrices.class.getName()});
                return null;
            }
        }

    }

}
