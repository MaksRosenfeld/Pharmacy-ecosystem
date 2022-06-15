package ru.budgetapteka.pharmacyecosystem.service;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.budgetapteka.pharmacyecosystem.exceptions.WrongInnException;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class Cost {

    private static final Logger log = LoggerFactory.getLogger(Cost.class);

    private Long inn;
    private String name;
    private BigDecimal amount;
    private String description;
    private CostType type;
    private List<Pharmacy> belongingCosts; // для каких аптек учитывать расход

    public Cost(String inn, String name, BigDecimal amount, String description, List<Pharmacy> belongingCosts) {
        this.inn = Long.parseLong(inn);
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.belongingCosts = belongingCosts;
    }

    // для удаления из множества ИННов
    public Cost(Long inn) {
        log.info("Удаляем из списка отсутсвующих ИНН: {}", inn);
        this.inn = inn;
    }


    @Override
    public String toString() {
        return "Cost{" +
                "inn=" + inn +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", belongingCosts=" + belongingCosts +
                '}';
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
