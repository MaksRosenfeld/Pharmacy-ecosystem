package ru.budgetapteka.pharmacyecosystem.service;

import ru.budgetapteka.pharmacyecosystem.exceptions.WrongInnException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cost {

    private String inn;
    private String name;
    private double amount;
    private String description;
    private List<Integer> forPharmacyNumber; // для каких аптек учитывать расход

    public Cost(String inn, String name, double amount, String description) throws WrongInnException {
        if (!inn.isBlank()) {
            this.inn = inn;
            this.name = name;
            this.amount = amount;
            this.description = description;
            this.forPharmacyNumber = parsePharmaciesFromDescription(description);
        } else
            throw new WrongInnException();
    }

    private List<Integer> parsePharmaciesFromDescription(String description) {
        Pattern pattern = Pattern.compile("!!.+!!");
        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            String[] pharmArr = description.substring(matcher.start() + 2, matcher.end() - 2).split(",");
            return Arrays.stream(pharmArr)
                    .map(String::strip)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        return null;
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

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cost cost = (Cost) o;
        return inn.equals(cost.inn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inn);
    }
}
