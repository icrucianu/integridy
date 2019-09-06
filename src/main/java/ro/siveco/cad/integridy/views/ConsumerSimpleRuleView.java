package ro.siveco.cad.integridy.views;

import ro.siveco.cad.integridy.controllers.ConsumerSimpleRuleController;
import ro.siveco.cad.integridy.controllers.UsersController;
import ro.siveco.cad.integridy.entities.ConsumerSimpleRule;
import ro.siveco.cad.integridy.entities.Users;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="ConsumerSimpleRuleView")
@ViewScoped
public class ConsumerSimpleRuleView {

    private List<ConsumerSimpleRule> consumerSimpleRuleList;

    @ManagedProperty("#{consumerSimpleRuleController}")
    private ConsumerSimpleRuleController service;

    @PostConstruct
    public void init() {
        consumerSimpleRuleList = getItems(service.getItems());
    }

    List<ConsumerSimpleRule> getItems(List<ConsumerSimpleRule> list){
        List<ConsumerSimpleRule> items = new ArrayList<>();
        for(ConsumerSimpleRule item: list){
            items.add(item);
        }
        return items;
    }

    public List<ConsumerSimpleRule> getConsumerSimpleRuleList() {
        return consumerSimpleRuleList;
    }

    public void setService(ConsumerSimpleRuleController service) {
        this.service = service;
    }

}
