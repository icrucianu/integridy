package ro.siveco.cad.integridy.service.api;

import ro.siveco.cad.integridy.controllers.util.EnergyProductionEnum;
import java.io.Serializable;
import java.util.Map;

public interface ExternalCommunicationService extends Serializable{
    String BEAN_ID = "externalCommunicationService";

    Map<EnergyProductionEnum, String> getNewestRomanianEnergyInfo() throws Exception;

}
