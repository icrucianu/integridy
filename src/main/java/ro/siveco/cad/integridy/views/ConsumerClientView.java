package ro.siveco.cad.integridy.views;

import ro.siveco.cad.integridy.controllers.ConsumerClientController;
import ro.siveco.cad.integridy.controllers.UsersController;
import ro.siveco.cad.integridy.entities.ConsumerClient;
import ro.siveco.cad.integridy.entities.Users;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="ConsumerClientView")
@ViewScoped
public class ConsumerClientView {

    private List<ConsumerClient> consumerClientList;

    @ManagedProperty("#{consumerClientController}")
    private ConsumerClientController service;

    @PostConstruct
    public void init() {
        consumerClientList = getItems(service.getItems());
    }

    List<ConsumerClient> getItems(List<ConsumerClient> list){
        List<ConsumerClient> items = new ArrayList<>();
        for(ConsumerClient item: list){
            items.add(item);
        }
        return items;
    }

    public List<ConsumerClient> getConsumerClientList() {
        return consumerClientList;
    }

    public void setService(ConsumerClientController service) {
        this.service = service;
    }

}
