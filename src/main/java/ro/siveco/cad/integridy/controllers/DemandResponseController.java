/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.ConsumerRecommendation;

/**
 *
 * @author roxanam
 */
@Named("demandResponseController")
@ViewScoped
public class DemandResponseController implements Serializable{
    
    @EJB
    private ConsumerRecommendationFacade consumerRecommendationFacade;
    @EJB
    private DashboardService dashboardService;
    
    private ConsumerClient currentConsumer;
    
    
    private List<ConsumerRecommendation> consumerRecommendationsList;
    
    @PostConstruct
    public void init(){
        currentConsumer = dashboardService.getCurrentConsumerClient();
    }

    public List<ConsumerRecommendation> getConsumerRecommendationsList() {
        return consumerRecommendationsList;
    }

    public void setConsumerRecommendationsList(List<ConsumerRecommendation> consumerRecommendationsList) {
        this.consumerRecommendationsList = consumerRecommendationsList;
    }
    
    
    
}
