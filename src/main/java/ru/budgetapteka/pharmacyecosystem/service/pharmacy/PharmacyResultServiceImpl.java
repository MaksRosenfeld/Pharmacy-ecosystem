package ru.budgetapteka.pharmacyecosystem.service.pharmacy;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawAbstract;
import ru.budgetapteka.pharmacyecosystem.service.parsing.RawResult;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyResultService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static ru.budgetapteka.pharmacyecosystem.util.Util.PhInfo.OFFICE_NUMBER;

@Slf4j
@Data
@Service
@Scope("session")
public class PharmacyResultServiceImpl implements PharmacyResultService {

    private final PharmacyService pharmacyService;

    public List<PharmacyResult> convertToPharmacyResults(List<RawAbstract> rawAbstracts, LocalDate date) {
        List<Pharmacy> allPharmacies = pharmacyService.getAllPharmacies();
        return rawAbstracts.stream()
                .map(ra -> (RawResult) ra)
                .map(rr -> PharmacyResult.builder()
                        .pharmacy(pharmacyService.findByNumber(rr.getPharmacyNumber(), allPharmacies))
                        .turnOver(rr.getTurnOver())
                        .grossProfit(rr.getGrossProfit())
                        .costPrice(rr.getCostPrice())
                        .date(Date.valueOf(date))
                        .build())
                .toList();
    }

    public PharmacyResult getOffice(List<PharmacyResult> pharmacyResults) {
        return pharmacyResults.stream()
                .filter(pr -> OFFICE_NUMBER.equals(pr.getPharmacy().getPharmacyNumber()))
                .findFirst().orElseThrow();
    }

}
