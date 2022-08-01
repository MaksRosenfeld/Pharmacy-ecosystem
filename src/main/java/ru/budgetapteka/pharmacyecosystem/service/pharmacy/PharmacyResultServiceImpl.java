package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.database.entity.PharmacyResult;
import ru.budgetapteka.pharmacyecosystem.database.repository.PharmacyResultRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@Service
public class PharmacyResultServiceImpl implements PharmacyResultService{

    private static final Logger log = LoggerFactory.getLogger(PharmacyResultServiceImpl.class);

    private final PharmacyResultRepository pharmacyResultRepository;
    private final PharmacyService pharmacyService;

    public PharmacyResultServiceImpl(PharmacyResultRepository pharmacyResultRepository,
                                     PharmacyService pharmacyService) {
        this.pharmacyResultRepository = pharmacyResultRepository;
        this.pharmacyService = pharmacyService;
    }


    @Override
    public PharmacyResult createPharmacyResult(Pharmacy pharmacy,
                                               LocalDate date,
                                               BigDecimal turnOver,
                                               BigDecimal grossProfit,
                                               BigDecimal costPrice) {
        PharmacyResult pharmacyResult = new PharmacyResult();
        pharmacyResult.setPharmacy(pharmacy);
        pharmacyResult.setDate(Date.valueOf(date));
        pharmacyResult.setTurnover(turnOver);
        pharmacyResult.setGrossProfit(grossProfit);
        pharmacyResult.setCostPrice(costPrice);
        return pharmacyResult;
    }

    @Override
    public void saveResultsForEachPharmacy(List<PharmacyResult> pharmacyResults) {
        log.info("Сохраняем результаты аптек в базе");
        pharmacyResultRepository.saveAll(pharmacyResults);
    }


}
