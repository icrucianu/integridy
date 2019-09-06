package ro.siveco.cad.integridy.controllers.util;

public enum GraphicalStepEnum {
    STEP_15m(0, 15), STEP_1H(10, 60), STEP_6H(20, 360), STEP_12H(30, 720), STEP_1D(40, 1440), STEP_1W(50, 10080), STEP_1M(60, 40320), STEP_3M(70, 120960), STEP_6M(80, 241920), STEP_1Y(90, 483840);
    private int stepValue;
    private long inMinutes;

    private GraphicalStepEnum(int stepVal, long inMinutes) {
        this.stepValue = stepVal;
        this.inMinutes = inMinutes;
    }

    public static GraphicalStepEnum getByValue(int value) {
        for (GraphicalStepEnum gse : values()) {
            if (gse.stepValue == value) {
                return gse;
            }
        }
        return null;
    }

    public int getStepValue() {
        return stepValue;
    }

    public long getInMinutes() {
        return inMinutes;
    }
}
