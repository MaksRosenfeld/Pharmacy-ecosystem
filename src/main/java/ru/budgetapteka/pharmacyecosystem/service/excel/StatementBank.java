package ru.budgetapteka.pharmacyecosystem.service.excel;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Cost;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
public class StatementBank extends AbstractExcelFile {

    private static final Logger log = LoggerFactory.getLogger(StatementBank.class);

    private final int COST_START = 13; // строка начала расходов в листе
    private final int DEBET_COLUMN = 7; // № столбца с суммой расходов
    private final int NAME_COLUMN = 3; // № столбца с названием фирмы
    private final int INN_COLUMN = 4; // № столбца с ИНН
    private final int DESCRIPTION_COLUMN = 9; // № столбца с описанием

    @Autowired
    private FinanceResultTo financeResults; // все результаты после парсинга

    // парсит данные с файла и вносит результаты
    @Override
    public void parse(InputStream inputStream) {
        try {
            super.setWorkbook(new XSSFWorkbook(inputStream));
            Sheet sheetForParsing = super.createSheetForParsing();
            log.info("Читаем excel файл: {}", sheetForParsing.getSheetName());
            List<Cost> costList = new ArrayList<>();
            List<Cell> cellWithTypos = new ArrayList<>();
            Map<AbstractExcelFile, List<Cell>> mapCellsWithTypos = new HashMap<>();
            for (Row row: sheetForParsing) {
                if (row.getRowNum() >= this.COST_START // расходы начинаются стандартно с 13 ряда
                        && row.getCell(this.DEBET_COLUMN).getCellType() == CellType.NUMERIC) {
                    String name = (row.getCell(this.NAME_COLUMN).getStringCellValue());
                    String inn = row.getCell(this.INN_COLUMN).getStringCellValue();
                    BigDecimal amount = BigDecimal.valueOf(row.getCell(this.DEBET_COLUMN).getNumericCellValue());
                    Cell description = row.getCell(this.DESCRIPTION_COLUMN);
                    if (!inn.isBlank() && !amount.toString().isBlank()) {
                        List<Pharmacy> belongingCosts = DataExtractor.extractPharmacyNumbers(description);
                        costList.add(new Cost(inn, name, amount, description.getStringCellValue(), belongingCosts));
                    } else {
                        Cell typoCell = row.getCell(this.INN_COLUMN);
                        cellWithTypos.add(typoCell); // добавляем клетки с ошибками
                    }
                }
            }
            log.info("Найдено записей с ошибками: {}", cellWithTypos.size());
            mapCellsWithTypos.put(this, cellWithTypos);
            financeResults.setCellsWithTypos(mapCellsWithTypos);
            log.info("Создаем список расходов");
            financeResults.setCostList(costList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
