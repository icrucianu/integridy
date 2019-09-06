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
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import ro.siveco.cad.integridy.entities.ConsumerClient;

/**
 *
 * @author roxanam
 */
@Named("consumerDataController")
@SessionScoped
public class ConsumerDataController  implements Serializable   {

    @EJB
    private ro.siveco.cad.integridy.controllers.ConsumerClientFacade consumerFacade;
    private List<ConsumerClient> consumers = null;
    private ConsumerClient selectedConsumer;
    private boolean showConsumerGraph=false;
    
    @EJB
    private DashboardService dashboardService;
    
    private List<ConsumerClient> selectedConsumers;
    @PostConstruct
    public void init(){
        getConsumers();
    }
    
    
    public void onSelectConsumer(AjaxBehaviorEvent event){
       dashboardService.setCurrentConsumerClient(selectedConsumer);
        setShowConsumerGraph(selectedConsumer!=null);
    }

    public ConsumerClientFacade getConsumerFacade() {
        return consumerFacade;
    }

    public boolean isShowConsumerGraph() {
        return showConsumerGraph;
    }

    public void setShowConsumerGraph(boolean showConsumerGraph) {
        this.showConsumerGraph = showConsumerGraph;
    }
    
    public void setConsumerFacade(ConsumerClientFacade consumerFacade) {
        this.consumerFacade = consumerFacade;
    }

    public List<ConsumerClient> getConsumers() {
        if(consumers==null)
            consumers = consumerFacade.findAll();
        return consumers;
    }

    public void setConsumers(List<ConsumerClient> consumers) {
        this.consumers = consumers;
    }

    public ConsumerClient getSelectedConsumer() {
        return selectedConsumer;
    }

    public void setSelectedConsumer(ConsumerClient selectedConsumer) {
        this.selectedConsumer = selectedConsumer;
    }

    public List<ConsumerClient> getSelectedConsumers() {
        return selectedConsumers;
    }

    public void setSelectedConsumers(List<ConsumerClient> selectedConsumers) {
        this.selectedConsumers = selectedConsumers;
    }
    
    
    
    
    
   
    
}
