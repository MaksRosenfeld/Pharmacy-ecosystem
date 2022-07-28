package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;


import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyCostService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Cost {

    private static final Logger log = LoggerFactory.getLogger(Cost.class);

    private Long inn;
    private String name;
    private BigDecimal amount;
    private String description;
    private CategoryNew category;
    private List<Integer> belongingCosts; // для каких аптек учитывать расход


    // для удаления из множества ИННов
    public Cost(Long inn) {
        this.inn = inn;
    }

    public Cost(BigDecimal amount, Long inn, String name, String description) {
        this.amount = amount;
        this.inn = inn;
        this.name = name;
        this.description = description;
    }

    //    Отправляет разделенный расход на кол-во аптек

    void distributeToPharmacies(List<Pharmacy> pharmacies,
                                List<ContragentNew> allContragents,
                                ParsedResults parsedResults,
                                PharmacyCostService costService) {
        // находим совпадение по ИНН в списке контрагентов, исключаем всех с флагом "Исключить"
        Optional<ContragentNew> ctg = allContragents.stream()
                .filter(cg -> cg.getInn().equals(this.inn) && !cg.getExclude())
                .findFirst();
        // расходы без аптек относятся к офису, поэтому добавляем 0 - индекс офиса
        if (this.belongingCosts.isEmpty()) this.belongingCosts.add(0);
        if (ctg.isPresent()) {
            log.info("Распределяем по аптекам: {}", this.belongingCosts);
            log.info("Сумма расхода: {}", this.amount);
            pharmacies.stream()
                    .filter(p -> this.belongingCosts.contains(p.getPharmacyNumber()))
                    .forEach(p -> {
                        BigDecimal eachPharmacyCost = this.amount.divide(
                                BigDecimal.valueOf(this.belongingCosts.size()), 2, RoundingMode.HALF_UP);
                        PharmacyCost pharmacyCost = costService.createPharmacyCost(p,
                                parsedResults.getDate(),
                                ctg.get().getCategoryId(),
                                eachPharmacyCost);
                        log.info("Ушло на аптеку: {}", pharmacyCost.getAmount());
                        parsedResults.savePharmacyCost(pharmacyCost);
                    });

        }


    }

    public void setBelongingCosts(List<Integer> belongingCosts) {
        this.belongingCosts = Objects.requireNonNullElseGet(belongingCosts, ArrayList::new);

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
