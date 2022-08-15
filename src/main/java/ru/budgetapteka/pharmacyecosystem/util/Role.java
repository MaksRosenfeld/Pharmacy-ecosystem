package ru.budgetapteka.pharmacyecosystem.util;

import lombok.Getter;

@Getter
public enum Role {
    PH("фармацевт"),
    ZAV("заведующая"),
    RAZB("разборка");

    private final String fieldName;

    Role(String fieldName) {
        this.fieldName = fieldName;
    }
}
