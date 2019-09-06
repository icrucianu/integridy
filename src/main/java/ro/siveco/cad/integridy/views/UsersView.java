package ro.siveco.cad.integridy.views;

import ro.siveco.cad.integridy.controllers.UsersController;
import ro.siveco.cad.integridy.entities.Users;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="UsersView")
@ViewScoped
public class UsersView {

    private List<Users> usersListList;

    @ManagedProperty("#{usersController}")
    private UsersController service;

    @PostConstruct
    public void init() {
        usersListList = getItems(service.getItems());
    }

    List<Users> getItems(List<Users> list){
        List<Users> items = new ArrayList<>();
        for(Users item: list){
            items.add(item);
        }
        return items;
    }

    public List<Users> getUsersList() {
        return usersListList;
    }

    public void setService(UsersController service) {
        this.service = service;
    }

}
