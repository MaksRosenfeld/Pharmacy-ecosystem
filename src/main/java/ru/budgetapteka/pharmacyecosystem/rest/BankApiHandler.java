package ru.budgetapteka.pharmacyecosystem.rest;

public interface BankApiHandler {

    public void orderBankStatement(String dateFrom, String dateTo);
    public void getBankStatement();
    public Status getBankStatementStatus();
}
