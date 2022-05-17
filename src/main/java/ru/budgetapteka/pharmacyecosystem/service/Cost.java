package ru.budgetapteka.pharmacyecosystem.service;

import ru.budgetapteka.pharmacyecosystem.exceptions.WrongInnException;

import java.util.List;

public class Cost {

    private String inn;
    private double amount;
    private String description;
    private List<Integer> forPharmacyNumber; // для каких аптек учитывать расход

    public Cost(String inn, double amount, String description) throws WrongInnException {
        if (!inn.isBlank()) {
            this.inn = inn;
            this.amount = amount;
            this.description = description;
        } else
            throw new WrongInnException();


    }

    @Override
    public String toString() {
        return "Cost{" +
                "inn='" + inn + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    public String getInn() {
        return inn;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public List<Integer> getForPharmacyNumber() {
        return forPharmacyNumber;
    }
}
