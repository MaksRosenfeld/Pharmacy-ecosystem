package ru.budgetapteka.pharmacyecosystem.service;

import java.util.List;

public class Cost {

    private int inn;
    private int amount;
    private List<Integer> forPharmacyNum;

    public Cost(int inn, int amount, List<Integer> forPharmacyNum) {
        this.inn = inn;
        this.amount = amount;
        this.forPharmacyNum = forPharmacyNum;
    }

    public int getInn() {
        return inn;
    }

    public void setInn(int inn) {
        this.inn = inn;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Integer> getForPharmacyNum() {
        return forPharmacyNum;
    }

    public void setForPharmacyNum(List<Integer> forPharmacyNum) {
        this.forPharmacyNum = forPharmacyNum;
    }
}
