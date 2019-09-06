/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.Users;

/**
 *
 * @author roxanam
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }
    
    public Users findByUserName(String username){
        Query query = getEntityManager().createNamedQuery("Users.findByUsername", Users.class);
        query.setParameter("username", username);
        return (Users)query.getSingleResult();
    }
    
    public List<String> getUserNames(){
        Query query = getEntityManager().createNamedQuery("Users.getUserNames",Users.class);
        return query.getResultList();
    }
    
    public Users getUserByToken(String token){
        Query query = getEntityManager().createNamedQuery("Users.findByToken", Users.class);
        query.setParameter("token", token);
        return (Users)query.getSingleResult();
    }
}
