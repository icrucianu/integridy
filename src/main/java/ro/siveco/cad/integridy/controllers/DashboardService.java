/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.shiro.subject.Subject;
import ro.siveco.cad.integridy.controllers.util.Constants;
import ro.siveco.cad.integridy.entities.ConsumerClient;

/**
 *
 * @author roxanam
 */
@Stateless
public class DashboardService {
    private Date currentDate;
    private String username ;
    private ConsumerClient currentConsumerClient;

    @EJB
    private ConsumerClientFacade consumerClientFacade;
    
    @PostConstruct
    public void init(){
        setCurrentConsumerClient(consumerClientFacade.getClientByUserName(getUsername()));
    }
    

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getUsername() {
        Subject subject = org.apache.shiro.SecurityUtils.getSubject();
        if(subject!=null){
            return subject.getPrincipal().toString();
        }else{
            return "User";
        }
    }
    
//    public ConsumerClient getCurrentConsumerClient(){
//        return consumerClientFacade.getClientByUserName(getUsername());
//    }
    
     public ConsumerClient getCurrentConsumerClient(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        return (ConsumerClient) session.getAttribute(Constants.CURRENT_CONSUMER);
    }
    public void setCurrentConsumerClient(ConsumerClient consumer){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Constants.CURRENT_CONSUMER, consumer);
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    
}
