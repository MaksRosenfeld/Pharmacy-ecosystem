package ru.budgetapteka.pharmacyecosystem.database.entity;

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
