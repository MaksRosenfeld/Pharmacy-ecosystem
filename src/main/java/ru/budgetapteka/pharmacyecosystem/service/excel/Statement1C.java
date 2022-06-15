package ru.budgetapteka.pharmacyecosystem.service.excel;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@Getter
@Setter
@Component
public class Statement1C extends AbstractExcelFile {

    private static final Logger log = LoggerFactory.getLogger(Statement1C.class);

    private final int ROW_START = 5; // строка начала выручек у аптек
    private final int COST_PRICE_COLUMN = 4; // № столбца с себестоимостью продукции
    private final int TURN_OVER_COLUMN = 6; // № столбца с выручкой аптеки за месяц
    private final int GROSS_PROFIT_COLUMN = 7; // № столбца с ИНН
    private final int NAME_COLUMN = 0; // № столбца с описанием

    private final int DATE_ROW = 3; // ряд даты
    private final int DATE_COLUMN = 2; // колонка даты

    @Autowired
    private FinanceResultTo financeResults;

    // парсит данные с файла и вносит результаты
    @Override
    public void parse(InputStream inputStream) {
        BigDecimal totalTurnOver = BigDecimal.ZERO;
        BigDecimal totalGrossProfit = BigDecimal.ZERO;
        BigDecimal totalCostPrice = BigDecimal.ZERO;
        try {

            super.setWorkbook(new HSSFWorkbook(inputStream));
            Sheet sheetForParsing = super.createSheetForParsing();
            log.info("Читаем excel файл: {}", sheetForParsing.getSheetName());
            Cell cellWithDate = sheetForParsing.getRow(DATE_ROW).getCell(DATE_COLUMN); // строка с датой
            financeResults.setDateOfStatements(DataExtractor.extractDate(cellWithDate));
            for (Row row : sheetForParsing) {
                // исключаем последний ряд (у него итог), а также все, что до нужного ряда
                if (row.getRowNum() > ROW_START && row.getRowNum() < sheetForParsing.getLastRowNum()) {
                    BigDecimal costPrice = BigDecimal.valueOf(row.getCell(COST_PRICE_COLUMN).getNumericCellValue());
                    BigDecimal turnOver = BigDecimal.valueOf(row.getCell(TURN_OVER_COLUMN).getNumericCellValue());
                    BigDecimal grossProfit = BigDecimal.valueOf(row.getCell(GROSS_PROFIT_COLUMN).getNumericCellValue());
                    Integer id = DataExtractor.extractNumberOfPharmacy(row.getCell(NAME_COLUMN));
                    // TODO: Доделать парсинг для аптек
                    totalTurnOver = totalTurnOver.add(turnOver);
                    totalGrossProfit = totalGrossProfit.add(grossProfit);
                    totalCostPrice = totalCostPrice.add(costPrice);
                }
            }
            log.info("Данные считаны, выставляем Выручка: {}, Валовую прибыль: {}, Себестоимость продаж: {}",
                    totalTurnOver, totalGrossProfit, totalCostPrice);
            financeResults.setTurnOver(totalTurnOver);
            financeResults.setGrossProfit(totalGrossProfit);
            financeResults.setCostPrice(totalCostPrice);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
