package ru.budgetapteka.pharmacyecosystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ExcelFile1C;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.ExcelFileBankStatement;
import ru.budgetapteka.pharmacyecosystem.service.excelparsing.Parseable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@SpringBootTest
public class DataUtil {

    @Value("${path.1CFile}")
    public static final String oneCPath = "src/main/resources/excel_files/Отчет 04.2022.xls";
    public static final String bankStatementPath = "src/main/resources/excel_files/Выписка_01.01.2022-31.01.2022_1543.xlsx";
    public static final BigDecimal expectedCostPrice = new BigDecimal("54100165.53");
    public static final BigDecimal expectedGrossProfit = new BigDecimal("8226444.84");
    public static final BigDecimal expectedTurnOver = new BigDecimal("62326610.37");


    public static Parseable get1CFile() {
        try(InputStream file = new FileInputStream(oneCPath)) {
            return new ExcelFile1C(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Parseable getBStatement() {
        try(InputStream file = new FileInputStream(bankStatementPath)) {
            return new ExcelFileBankStatement(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
