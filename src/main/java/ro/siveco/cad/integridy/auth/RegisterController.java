package ro.siveco.cad.integridy.auth;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;

import ro.siveco.cad.integridy.entities.Users;
import ro.siveco.cad.integridy.controllers.UsersFacade;

/**
 *
 * @author SIVECO
 */
@Named("registerController")
@ViewScoped
public class RegisterController implements Serializable{

    @EJB
    private UsersFacade usersFacade;
   
    private String username;
    private String password;
    private String passwordConfirmation;
    private Long cardNumber;
    
    private String usernameAvailability = "fa fa-times-circle";
    private boolean usernameTaken;
    private boolean invalidUsername;
    private String cardIdAvailability = "fa fa-times-circle";
    
    public RegisterController(){}
    
    /**
     * Creates a User account with a role of 'User'.
     */
    public void registerAccount(){
        //check username availability
        if(usernameTaken){
            FacesMessage invalidUsernameMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username",
                "This username is already taken. Please choose another one..");
            FacesContext.getCurrentInstance().addMessage(null, invalidUsernameMsg);
            return;
        }
        
        if(invalidUsername){
            FacesMessage invalidUsernameMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username",
                "Username cannot contain whitespaces!");
            FacesContext.getCurrentInstance().addMessage(null, invalidUsernameMsg);
            return;
        }
        
        //check password matching
        if(!password.equals(passwordConfirmation)){
            FacesMessage noMatchingPasswords = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match","");
            FacesContext.getCurrentInstance().addMessage(null, noMatchingPasswords);
        }
        
        
        //register
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        
       
        usersFacade.create(user);
        
        FacesMessage userCreated = new FacesMessage(FacesMessage.SEVERITY_INFO, "Account registered", 
                "Your account was successfully registered. You can now log in");
        FacesContext.getCurrentInstance().addMessage(null, userCreated);
        
        clearInput();
    }
    
    private void clearInput(){
        username = null;
        cardNumber = null;
    }
    
    public void checkUsernameAvailability(AjaxBehaviorEvent event) throws ValidatorException{
        username = ((InputText)event.getSource()).getValue().toString();
        if(username.trim().equals("")){
             usernameAvailability = "fa fa-times-circle";
             return;
        }
        List<Users> usernameCheck = null;
        try{
            usernameCheck = usersFacade.getEntityManager().createNamedQuery("Users.findByUserName")
                .setParameter("userName", username).getResultList();
        }catch(Exception ex){}
        if(usernameCheck==null || usernameCheck.isEmpty()){
            usernameAvailability = "fa fa-check-circle";
            usernameTaken = false;
        }else{
            usernameAvailability = "fa fa-times-circle";
            usernameTaken = true;
        }
        
        //check if username contains spaces
        Pattern pattern = Pattern.compile("^\\S*$");
        Matcher matcher = pattern.matcher(username);
        if(!matcher.find()){
            //whitespace found
            invalidUsername = true;
        }else {
            invalidUsername = false;
        }
        RequestContext.getCurrentInstance().update("registerForm:usernameCheck");
    }
    
    public String getStatusColor(){
        if(usernameAvailability.equals("fa fa-check-circle")){
            return "green";
        }else{
            return "#EE2F41";
        }
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
    public Long getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getUsernameAvailability() {
        return usernameAvailability;
    }
    public void setUsernameAvailability(String usernameAvailability) {
        this.usernameAvailability = usernameAvailability;
    }
    public String getCardIdAvailability() {
        return cardIdAvailability;
    }
    public void setCardIdAvailability(String cardIdAvailability) {
        this.cardIdAvailability = cardIdAvailability;
    }
    public UsersFacade getUsersFacade() {
        return usersFacade;
    }
    public void setUsersFacade(UsersFacade usersFacade) {
        this.usersFacade = usersFacade;
    }
    public boolean isUsernameTaken() {
        return usernameTaken;
    }
    public void setUsernameTaken(boolean usernameTaken) {
        this.usernameTaken = usernameTaken;
    }
    public boolean isInvalidUsername() {
        return invalidUsername;
    }
    public void setInvalidUsername(boolean invalidUsername) {
        this.invalidUsername = invalidUsername;
    }
    
    
}
