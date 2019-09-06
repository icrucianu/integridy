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
import ro.siveco.cad.integridy.entities.WhatIfParameters;
import ro.siveco.cad.integridy.entities.WhatifScenario;

/**
 *
 * @author roxanam
 */
@Stateless
public class WhatIfParametersFacade extends AbstractFacade<WhatIfParameters> {

    @PersistenceContext(unitName = "ro.siveco.cad_Integridy_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WhatIfParametersFacade() {
        super(WhatIfParameters.class);
    }
    
    public List<WhatIfParameters> findByScenarioId(WhatifScenario scenario){
        Query query = getEntityManager()
                .createNamedQuery("WhatIfParameters.findByScenario", WhatIfParameters.class)
                .setParameter("scenarioId", scenario);
        return query.getResultList();
    }
    
}
