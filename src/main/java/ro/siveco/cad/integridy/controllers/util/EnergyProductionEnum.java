package ro.siveco.cad.integridy.controllers.util;

public enum EnergyProductionEnum {
    DATE("data"),
    CONSUMPTION("consum"),
    PRODUCTION("productie"),
    NUCLEAR("nuclear"),
    WIND("eolian"),
    HYDRO("hidro"),
    HYDROCARBS("hidrocarburi"),
    COAL("carbune"),
    PV("fotovolt"),
    BIOMASS("biomasa"),
    BALANCE("sold");

    private String id;

    EnergyProductionEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
