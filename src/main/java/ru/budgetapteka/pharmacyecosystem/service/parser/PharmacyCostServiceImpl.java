package ru.budgetapteka.pharmacyecosystem.service.parser;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.CategoryNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.ContragentNew;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyCost;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static ru.budgetapteka.pharmacyecosystem.rest.util.Util.PhInfo.OFFICE_NUMBER;
import static ru.budgetapteka.pharmacyecosystem.service.parser.DataExtractor.extractPharmacyNumbers;

/**
 * Класс обрабатывает RawCost, проверяет наличие
 * ИНН, которые не в базе данных. Создает PharmacyCost из RawCost,
 * добавляя все данные
 */
@Slf4j
@Data
@Service
@Scope("session")
public class PharmacyCostServiceImpl implements PharmacyCostService {

    private final ContragentService contragentService;
    private final PharmacyService pharmacyService;


    /*
    Перед тем, как создавать PharmacyCost, необходимо
    данным методом проверить наличие ИНН, которые не в базе
     */
    public Set<RawCost> findMissedInn(List<RawAbstract> rawAbstracts) {
        log.info("Ищем недостающие ИНН");
        List<RawCost> rawCosts = rawAbstracts.stream().map(ra -> (RawCost) ra).toList();
        List<ContragentNew> allContragents = contragentService.getAllContragents();
        Set<RawCost> missedInns = rawCosts.stream()
                .filter(rawCost ->
                        allContragents.stream().noneMatch(
                                contr -> rawCost.getInn().equals(contr.getInn())))
                .collect(Collectors.toSet());
        log.info("Кол-во недостающих ИНН: {}", missedInns.size());
        return missedInns;
    }


    /*
    Только после того, как метод findMissedInn вернет сет
    с размером 0, можно запускать данный метод, иначе
    подсчет будет неверным
     */
    public List<PharmacyCost> convertToPharmacyCosts(List<RawAbstract> rawAbstract, LocalDate date) {
        log.info("Создаем PharmacyCosts");
        List<Pharmacy> allPharmacies = pharmacyService.getAllPharmacies();
        List<ContragentNew> allContragents = contragentService.getAllContragents();
        List<RawCost> rawCosts = rawAbstract.stream().map(ra -> (RawCost) ra).toList();
        List<PharmacyCost> pharmacyCosts = new ArrayList<>();
        rawCosts.forEach(rawCost -> {
            if (possibleToHandel(rawCost, allContragents)) {
                List<PharmacyCost> somePharmacyCosts = convertToPharmacyCost(rawCost, allPharmacies);
                distributeCostToEachPharmacy(rawCost, somePharmacyCosts);
                setMainDataFromRawCost(rawCost, somePharmacyCosts, allContragents, date);
                pharmacyCosts.addAll(somePharmacyCosts);
            }
        });
        return pharmacyCosts;
    }

    /*
    Создает из RawCost -> PharmacyCost, при этом
    добавляет только аптеку. Если цифры в описании
    нет, то расходы идут на офис
     */
    private List<PharmacyCost> convertToPharmacyCost(RawCost rawCost,
                                                     List<Pharmacy> allPharmacies) {
        List<Integer> pharmacyNumbers = extractPharmacyNumbers(rawCost.getDescription());
        if (pharmacyNumbers.isEmpty()) {
            Pharmacy office = pharmacyService.findByNumber(OFFICE_NUMBER, allPharmacies);
            PharmacyCost officeCost = PharmacyCost.builder()
                    .pharmacy(office).build();
            return List.of(officeCost);
        } else {
            return pharmacyNumbers.stream()
                    .map(number -> PharmacyCost.builder()
                            .pharmacy(pharmacyService.findByNumber(number, allPharmacies))
                            .build()).toList();
        }


    }

    private void distributeCostToEachPharmacy(RawCost rawCost, List<PharmacyCost> pharmacyCosts) {
        BigDecimal totalAmount = rawCost.getAmount();
        int amountOfPharmacies = pharmacyCosts.size();
        pharmacyCosts
                .forEach(phCost ->
                        phCost.setAmount(totalAmount.divide(BigDecimal.valueOf(amountOfPharmacies), 2, RoundingMode.HALF_UP)));
    }

    private void setMainDataFromRawCost(RawCost rawCost, List<PharmacyCost> pharmacyCosts,
                                        List<ContragentNew> allContragents, LocalDate date) {

        pharmacyCosts.forEach(phCost -> {
            CategoryNew category = allContragents.stream()
                    .filter(contragent -> rawCost.getInn().equals(contragent.getInn()))
                    .map(ContragentNew::getCategoryId)
                    .findFirst().orElseThrow();
            phCost.setDate(Date.valueOf(date));
            phCost.setInn(rawCost.getInn());
            phCost.setName(rawCost.getName());
            phCost.setCategoryId(category);
        });
    }


    /*
   Проверяем, есть ли данный ИНН в базе и нужно ли его исключать из
   подсчета
    */
    private boolean possibleToHandel(RawCost rawCost, List<ContragentNew> allContragents) {
        return allContragents
                .stream()
                .anyMatch(contragent -> contragent.getInn().equals(rawCost.getInn())
                        && !contragent.getExclude());
    }


}
