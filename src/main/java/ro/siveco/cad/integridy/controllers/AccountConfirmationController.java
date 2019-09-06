/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import ro.siveco.cad.integridy.entities.Users;

/**
 *
 * @author roxanam
 */
@Named("accountConfirmationController")
@ViewScoped
public class AccountConfirmationController implements Serializable{
    
    private String accountConfirmationToken;
    @EJB
    private UsersFacade usersFacade;
    
    private Users crtUser;
    
    @PostConstruct
    public void init(){
        if(accountConfirmationToken!=null)
            crtUser = usersFacade.getUserByToken(accountConfirmationToken);
    }
    public void onLoadPage(){
         if(accountConfirmationToken!=null)
            crtUser = usersFacade.getUserByToken(accountConfirmationToken);
    }
    public String getAccountConfirmationToken() {
        return accountConfirmationToken;
    }

    public void setAccountConfirmationToken(String accountConfirmationToken) {
        this.accountConfirmationToken = accountConfirmationToken;
    }

    public Users getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(Users crtUser) {
        this.crtUser = crtUser;
    }
    
    public String activateAccount(){
        try{
            crtUser.setEnabled(true);
            crtUser.setActive(true);
            crtUser.setModificationDate(new Date());
            crtUser.setModifiedBy(crtUser.getUsername());
           
            usersFacade.edit(crtUser);
            
            return "/faces/login.xhtml?faces-redirect=true";
        }catch(Exception e){
             Logger.getLogger(AccountConfirmationController.class.getName()).log(Level.SEVERE, null, e);
              FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!" ,"An error occured while activating your account!");
             FacesContext.getCurrentInstance().addMessage(null, error);
             return null;
        }
        
    }
    
}
