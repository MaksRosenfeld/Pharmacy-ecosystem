package ru.budgetapteka.pharmacyecosystem.util;

import lombok.Getter;

@Getter
public enum EmployeeRole {
    PH("Фармацевт"),
    ZAV("Заведующая"),
    RAZB("Разборка"),
    PROV("Провизор");

    private final String fieldName;

    EmployeeRole(String fieldName) {
        this.fieldName = fieldName;
    }
}
