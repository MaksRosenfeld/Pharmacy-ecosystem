package ru.budgetapteka.pharmacyecosystem.rest;

import lombok.Getter;

@Getter
public enum Status {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    SUCCESS("SUCCESS"),
    ERROR("ERROR"),
    RECEIVED("RECEIVED");

    private final String value;

    Status(String value) {
        this.value = value;
    }

}
