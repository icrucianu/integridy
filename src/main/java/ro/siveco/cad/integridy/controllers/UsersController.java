package ro.siveco.cad.integridy.controllers;

import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import ro.siveco.cad.integridy.entities.Users;
import ro.siveco.cad.integridy.controllers.util.JsfUtil;
import ro.siveco.cad.integridy.controllers.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.Role;
import ro.siveco.cad.integridy.mail.Mail;
import ro.siveco.cad.integridy.mail.MailClient;
import ro.siveco.cad.integridy.mail.messages.MailMessages;

@Named("usersController")
@ViewScoped
public class UsersController implements Serializable {

    @EJB
    private ro.siveco.cad.integridy.controllers.UsersFacade ejbFacade;
    private List<Users> items = null;
    private Users selected;
    private String currentUser;
    @EJB
    private DashboardService dashboard;
    @EJB
    private UsersFacade userFacade;
    @EJB
    private RoleFacade roleFacade;
    
    private List<String> userNames;
    private List<String> roles;
    
    private String fileName="Users";
    
    

    @PostConstruct
    public void init(){
        items=getItems();
        userNames = new ArrayList<>();
        currentUser=dashboard.getUsername();
        roles = roleFacade.getRoleNames();
    }
    
    public UsersController() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    

    public Users getSelected() {
        return selected;
    }

    public void setSelected(Users selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsersFacade getFacade() {
        return ejbFacade;
    }

    public Users prepareCreate() {
        selected = new Users();
        selected.setCreatedBy(currentUser);
        selected.setCreationDate(new Date());
        selected.setVersion(BigInteger.ONE);
        selected.setEnabled(false);
        selected.setActive(false);
        initializeEmbeddableKey();
        return selected;
    }
    public MailjetResponse sendConfirmationEmail(Users user) throws MailjetException, MailjetSocketTimeoutException{
       List<String> recipients = new ArrayList<>();
        recipients.add(user.getEmail());

        Mail mail = new Mail();
        mail.setFromAddress(Constants.USER_APPLICATION_FROM_EMAIL);
        mail.setFromName(Constants.USER_APPLICATION_FROM_NAME);
        mail.setRecipients(recipients);
        mail.setSubject(Constants.USER_APPLICATION_FROM_SUBJECT);
        mail.setHtmlContent(MailMessages.userConfirmation(user));
        MailClient mailClient = MailClient.getInstance();

        return mailClient.send(mail);
    }
    public void create() {        
          
        userNames = userFacade.getUserNames();
        
        if(!userNames.contains(selected.getUsername())){//check username and email unique
           Users user = persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsersCreated"));      
           if(user!=null){
               
               try {
                   sendConfirmationEmail(user);
               } catch (MailjetException ex) {
                   Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
               } catch (MailjetSocketTimeoutException ex) {
                   Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
               }
               
           }
        }else{
             FacesMessage invalidInput = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate username", "Please choose another one");
             FacesContext.getCurrentInstance().addMessage(null, invalidInput);
        }
        
        if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
            }
        
    }

    public void update() {        
        selected.setVersion(selected.getVersion().add(BigInteger.ONE));
        selected.setModifiedBy(currentUser);
        selected.setModificationDate(new Date());
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsersUpdated"));        
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsersDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

        public List<Users> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    List<Users> tableItems = null;
    
    public List<Users> getTableItems(){
        items = getItems();
        List<Users> newList = new ArrayList<>();
        items.forEach((Users obj) -> {
            newList.add(obj);
        });
        return newList;
    }
    
    private Users persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {                    
                    //currentUser=dashboard.getCurrentUserUsername();
                    if(persistAction==PersistAction.CREATE){
                        selected.setToken(UUID.randomUUID().toString());
                    }
                    JsfUtil.addSuccessMessage(successMessage);
                    return getFacade().edit(selected);   
                    
                } else {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage);
                    return null;
                }
                
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
                return null;
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                return null;
            }
        }
        return null;
    }

    public Users getUsers(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Users> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Users> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Users.class)
    public static class UsersControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsersController controller = (UsersController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usersController");
            return controller.getUsers(getKey(value));
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
            if (object instanceof Users) {
                Users o = (Users) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Users.class.getName()});
                return null;
            }
        }

    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    

    

}
