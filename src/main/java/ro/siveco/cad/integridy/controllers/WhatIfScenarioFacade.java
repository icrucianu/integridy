/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ro.siveco.cad.integridy.entities.WhatifScenario;

/**
 *
 * @author roxanam
 */

@Stateless
public class WhatIfScenarioFacade extends AbstractFacade<WhatifScenario>{
     @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WhatIfScenarioFacade() {
        super(WhatifScenario.class);
    }
    
    public List<WhatifScenario> findAllOrderById(){
        Query query = getEntityManager().createNamedQuery("WhatifScenario.findAllOrederedbyID", WhatifScenario.class);
        return query.getResultList();
    }
}
