package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.DataUtil;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyCostService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CostTest {

    @Autowired
    private ExcelParser excelParser;
    @Autowired
    private ParsedResults parsedResults;


    @Test
    void distributeToPharmacies() {
        MultipartFile oneC = DataUtil.convertToMultipartFile(DataUtil.oneCPath);
        MultipartFile bS = DataUtil.convertToMultipartFile(DataUtil.bankStatementPath);
        excelParser.parse1CStatement(oneC);
        excelParser.parseBankStatement(bS);
        assertNotNull(parsedResults.getPharmacyCosts());


    }
}