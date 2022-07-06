package ru.budgetapteka.pharmacyecosystem.service.pharmacy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.budgetapteka.pharmacyecosystem.DataUtil;
import ru.budgetapteka.pharmacyecosystem.database.repository.PharmacyResultRepository;
import ru.budgetapteka.pharmacyecosystem.service.contragent.ContragentService;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ExcelParser;
import ru.budgetapteka.pharmacyecosystem.service.finance.FinanceCounter;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.budgetapteka.pharmacyecosystem.DataUtil.*;

@SpringBootTest
class PharmacyResultServiceImplTest {

    @Autowired
    private PharmacyResultRepository resultRepository;
    @Autowired
    private PharmacyResultService resultService;
    @Autowired
    private FinancialResultsTo finResults;
    @Autowired
    private ExcelParser excelParser;
    @Autowired
    private FinanceCounter financeCounter;
    @Autowired
    private ContragentService contragentService;

    @Transactional
    @Test
    void saveResultsForEachPharmacy() {
        excelParser.parse1CStatement(convertToMultipartFile(oneCPath));
        excelParser.parseBankStatement(convertToMultipartFile(bankStatementPath));
        if (!contragentService.hasMissingInn()) {
            financeCounter
                    .countCosts()
                    .countNetProfit()
                    .countRoS()
                    .countResultsForEachPharmacy()
                    .sendResults();
        }
        resultRepository.saveAll(finResults.getPharmaciesWithMonthResults());
        assertThat(resultRepository.findAll()).isNotNull();


    }
}