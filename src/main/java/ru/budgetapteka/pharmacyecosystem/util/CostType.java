package ru.budgetapteka.pharmacyecosystem.util;

public enum CostType {
    VARIABLE("переменные"),
    FIXED("постоянные");

    private final String name;
    CostType(String name) {
        this.name = name;

    }
    public String getName() {
        return this.name;
    }
}
