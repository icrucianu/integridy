/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.auth;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import ro.siveco.cad.integridy.controllers.ActionLogFacade;
import ro.siveco.cad.integridy.controllers.UsersFacade;
import ro.siveco.cad.integridy.entities.ActionLog;

/**
 *
 * @author roxanam
 */
@Named("loginController")
@ViewScoped
public class LoginController implements Serializable {

    private String username;
    private String password;

    @EJB
    private ActionLogFacade actionLogFacade;

    @EJB
    private UsersFacade usersFacade;

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

    public String login() {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(getUsername(), getPassword());

//        try{
//            String userToken = cognitoAuthentication(getUsername(), getPassword());
//        }catch(Exception ex){}
//        
        try {
            subject.login(usernamePasswordToken);

            actionLogRegister();

            return "index.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            FacesMessage invalidCredentials = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", "Your username/password combination is incorrect!");
            FacesContext.getCurrentInstance().addMessage(null, invalidCredentials);
            ex.printStackTrace();
        }
        return null;
    }

    private void actionLogRegister() {
        try {
            //login action to be logged
            ActionLog action = new ActionLog();
            action.setActionDate(new Date());
            action.setActionName("Login");
            action.setPage("index.xhtml");
            action.setUserId(usersFacade.findByUserName(getUsername()));
            actionLogFacade.create(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
