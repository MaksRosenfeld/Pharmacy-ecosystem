package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

public enum CostType {
    VARIABLE("переменные"),
    FIXED("постоянные");

    private String name;
    CostType(String name) {
        this.name = name;

    }
    public String getName() {
        return this.name;
    }
}
