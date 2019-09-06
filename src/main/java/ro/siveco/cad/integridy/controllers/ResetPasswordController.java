/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import ro.siveco.cad.integridy.entities.Users;

/**
 *
 * @author roxanam
 */
@Named("resetPasswordController")
@ViewScoped
public class ResetPasswordController implements  Serializable{
    @EJB
    private UsersFacade usersFacade;
    
    private Users user;
    private String userName;
    private String email;
    private String appToken;
    
    
    
    public String sendResetPasswordMail(){
        return "";
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }
    
    
    
    
}
