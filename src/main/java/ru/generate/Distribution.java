package ru.generate;

public class Distribution {
    private int numberChance = 20;
    private int variableChance = 50;
    private int constantChance = 30;

    private int total = 100;

    public Distribution() {

    }

    public Distribution(int numberChance, int variableChance, int constantChance) {
        this.numberChance = numberChance;
        this.variableChance = variableChance;
        this.constantChance = constantChance;
        scale();
    }

    private void sum() {
        total = numberChance + variableChance + constantChance;
    }

    private void scale() {
        sum();
        numberChance = (int)((double) numberChance / (double) total * 100D);
        variableChance = (int)((double) variableChance/ (double) total * 100D);
        constantChance = (int)((double) constantChance / (double) total * 100D);
        sum();
    }

    public int getNumberChance() {
        return numberChance;
    }

    public void setNumberChance(int numberChance) {
        this.numberChance = numberChance;
        this.scale();
    }

    public int getVariableChance() {
        return variableChance;
    }

    public void setVariableChance(int variableChance) {
        this.variableChance = variableChance;
        this.scale();
    }

    public int getConstantChance() {
        return constantChance;
    }

    public void setConstantChance(int constantChance) {
        this.constantChance = constantChance;
        this.scale();
    }

    public int[] getAccumulatedChances() {
        return new int[]{numberChance, numberChance + constantChance, numberChance + constantChance + variableChance};
    }

    @Override
    public String toString() {
        return "Distribution{" +
                "numberChance=" + numberChance +
                ", variableChance=" + variableChance +
                ", constantChance=" + constantChance +
                ", total=" + total +
                '}';
    }

    public int getTotal() {
        return total;
    }
}
