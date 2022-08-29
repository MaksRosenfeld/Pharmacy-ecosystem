package ru.budgetapteka.pharmacyecosystem.util;

import lombok.Getter;

@Getter
public enum Role {
    PH("Фармацевт"),
    ZAV("Заведующая"),
    RAZB("Разборка"),
    PROV("Провизор");

    private final String fieldName;

    Role(String fieldName) {
        this.fieldName = fieldName;
    }
}
