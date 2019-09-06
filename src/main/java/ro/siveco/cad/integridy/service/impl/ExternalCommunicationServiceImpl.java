package ro.siveco.cad.integridy.service.impl;


import ro.siveco.cad.integridy.service.api.ExternalCommunicationService;
import ro.siveco.cad.integridy.controllers.util.EnergyProductionEnum;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ExternalCommunicationServiceImpl implements ExternalCommunicationService {
//     private Client client;
//    /**
//     *
//     * @return
//     * @throws Exception
//     */
    @Override
    public Map<EnergyProductionEnum, String> getNewestRomanianEnergyInfo() throws Exception {

        Map<Short, EnergyProductionEnum> idxMap = new HashMap<>();
        Map<EnergyProductionEnum, String> valuesMap = new HashMap<>();
//         addDefaultValues4Demo(valuesMap);
        LocalDateTime dt = LocalDateTime.now(ZoneId.systemDefault());
        int currentYear = dt.getYear();
        int currentMonth = dt.getMonthValue();
        int currentDay = dt.getDayOfMonth();
        int currentHours = dt.getHour();
        int currentMinutes = dt.getMinute();

        dt = dt.minusHours(1l);
        int pastYear = dt.getYear();
        int pastMonth = dt.getMonthValue();
        int pastDay = dt.getDayOfMonth();
        int pastHours = dt.getHour();
        int pastMinutes = dt.getMinute();
        String url = "http://version1.sistemulenergetic.ro/statistics/show_graph/" + pastYear + "/" + pastMonth + "/" + pastDay + "/" + pastHours + "/" + pastMinutes + "/" + currentYear + "/" + currentMonth + "/" + currentDay + "/" + currentHours + "/" + currentMinutes;
//        client = javax.ws.rs.client.ClientBuilder.newClient();
//        WebTarget webTarget = client.target(url);
        try {
            org.jsoup.nodes.Document doc =  Jsoup.connect(url).get();;
            org.jsoup.select.Elements rows = doc.select("tr");
            int rownr = 0;
            for (org.jsoup.nodes.Element row : rows) {
                short idx = 0;
                if (rownr == 2) {
                    break;
                }
                rownr++;
                org.jsoup.select.Elements columns = row.select("td");
                for (org.jsoup.nodes.Element column : columns) {
                    idx++;
                    if (column.text().toLowerCase().contains(EnergyProductionEnum.DATE.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.DATE);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.CONSUMPTION.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.CONSUMPTION);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.PRODUCTION.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.PRODUCTION);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.NUCLEAR.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.NUCLEAR);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.WIND.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.WIND);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.HYDROCARBS.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.HYDROCARBS);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.HYDRO.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.HYDRO);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.COAL.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.COAL);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.PV.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.PV);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.BIOMASS.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.BIOMASS);
                    } else if (column.text().toLowerCase().contains(EnergyProductionEnum.BALANCE.getId())) {
                        idxMap.put(idx, EnergyProductionEnum.BALANCE);
                    } else {
                        valuesMap.put(idxMap.get(idx), column.text());
                    }
                    System.out.print(column.text());
                }
                System.out.println();
            }
            if (valuesMap.size() == 0) {
                addDefaultValues4Demo(valuesMap);
            }
        } catch (Exception ex) {
            addDefaultValues4Demo(valuesMap);
        }
        System.out.println(valuesMap.toString());
        return valuesMap;
    }

    private void addDefaultValues4Demo(Map<EnergyProductionEnum, String> valuesMap) {
        valuesMap.put(EnergyProductionEnum.BIOMASS, "36");
        valuesMap.put(EnergyProductionEnum.PRODUCTION, "7841");
        valuesMap.put(EnergyProductionEnum.WIND, "780");
        valuesMap.put(EnergyProductionEnum.PV, "548");
        valuesMap.put(EnergyProductionEnum.DATE, "2018-07-16 16:13:15");
        valuesMap.put(EnergyProductionEnum.COAL, "1681");
        valuesMap.put(EnergyProductionEnum.CONSUMPTION, "7238");
        valuesMap.put(EnergyProductionEnum.HYDRO, "890");
        valuesMap.put(EnergyProductionEnum.NUCLEAR, "1325");
        valuesMap.put(EnergyProductionEnum.BALANCE, "-603");
    }

    
}
