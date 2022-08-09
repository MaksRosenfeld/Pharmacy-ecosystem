package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.database.repository.PharmacyResultRepository;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.parser.FinCounterService;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PharmacyResultServiceImplTest {

    @Autowired
    private PharmacyResultRepository resultRepository;
    @Autowired
    private PharmacyResultService resultService;
    @Autowired
    private FinancialResultsTo finResults;
//    @Autowired
//    private ExcelParser excelParser;
    @Autowired
    private FinCounterService finCounterService;
    @Autowired
    private ContragentService contragentService;


}