package ro.siveco.cad.integridy.auth;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import ro.siveco.cad.integridy.entities.Users;
import ro.siveco.cad.integridy.controllers.UsersFacade;

/**
 *
 * @author SIVECO
 */
@Named("changePasswordController")
@ViewScoped
public class ChangePasswordController implements Serializable{
    
    @EJB
    private UsersFacade usersFacade;
    private String username;
    private String password;
    private String newPassword;
    private String newPasswordConfirmation;

    private String appToken;
    public void onLoadPage(){
        // appp token 
    }
    public void changePassword(){
        //check input is not empty
        if(username.trim().isEmpty() || password.trim().isEmpty() 
                || newPassword.trim().isEmpty() || newPasswordConfirmation.trim().isEmpty()){
            FacesMessage invalidInput = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Input fields cannot be empty", "Please fill all the required fields");
            FacesContext.getCurrentInstance().addMessage(null, invalidInput);
            return;
        }
        
        Users user = null;
        try{
            user = (Users)usersFacade.getEntityManager().createNamedQuery("Users.findByUserName")
                .setParameter("userName", username).getSingleResult();
        }catch(NoResultException nre){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, nre);
        }catch(NonUniqueResultException nure){
             Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, nure);
        }catch(Exception ex){
             Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        if(user==null){
            return;
        }
//        //check old password
//        if(!user.getPassword().equals(password)){
//            FacesMessage oldPasswordIncorrect = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password incorrect", "");
//            FacesContext.getCurrentInstance().addMessage(null, oldPasswordIncorrect);
//            return;
//        }
        
        //check new password confirmation
        if(!newPassword.equals(newPasswordConfirmation)){
            FacesMessage passwordsDoNotMatch = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!","");
            FacesContext.getCurrentInstance().addMessage(null, passwordsDoNotMatch);
            return;
        }
        
//        //check if new password is equal to old password
//        if(newPassword.equals(password)){
//            FacesMessage oldPasswordDif = new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password cannot be the same as your old password", "");
//            FacesContext.getCurrentInstance().addMessage(null, oldPasswordDif);
//            return;
//        }
        
        //change password
        try{
            user.setPassword(newPassword);
            usersFacade.edit(user);
            FacesMessage changePasswordSuccess = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully!",
                    "Your password has been changed successfully!");
            FacesContext.getCurrentInstance().addMessage(null, changePasswordSuccess);
        }catch(Exception ex){
             Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }
    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }
    
    
    
}
