package ru.budgetapteka.pharmacyecosystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.budgetapteka.pharmacyecosystem.database.entity.CostCategory;
import ru.budgetapteka.pharmacyecosystem.database.entity.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyCostService;
import ru.budgetapteka.pharmacyecosystem.service.pharmacy.PharmacyService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SpringBootTest
public class DataUtil {

    public static final String oneCPath = "src/main/resources/excel_files/Отчет 04.2022.xls";
    public static final String bankStatementPath = "src/main/resources/excel_files/Выписка_01.01.2022-31.01.2022_1543.xlsx";
    public static final BigDecimal expectedCostPrice = new BigDecimal("54100165.53");
    public static final BigDecimal expectedGrossProfit = new BigDecimal("8226444.84");
    public static final BigDecimal expectedTurnOver = new BigDecimal("62326610.37");
    @Autowired
    private static PharmacyCostService costService;
    @Autowired
    private static PharmacyService pharmacyService;



    public static List<Pharmacy> getAllPharmacies() {
        return pharmacyService.getAllPharmacies();
    }

    public static CostCategory getNewCategory() {
        CostCategory costCategory = new CostCategory();
        costCategory.setCategory("Тест");
        costCategory.setType("Постоянные");
        return costCategory;
    }



    public static MultipartFile convertToMultipartFile(String path) {
        Path filePath = Path.of(path);
        String name = "file.txt";
        String originalFileName = "file.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(filePath);
        } catch (final IOException e) {
        }
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    public static boolean changeChecked(Boolean isChecked) {
        isChecked = true;
        return isChecked;
    }
}
