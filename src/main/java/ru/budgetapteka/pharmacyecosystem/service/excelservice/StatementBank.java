package ru.budgetapteka.pharmacyecosystem.service.excelservice;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    private final int COST_START = 13; // строка начала расходов в листе
    private final int DEBET_COLUMN = 7; // № столбца с суммой расходов
    private final int NAME_COLUMN = 3; // № столбца с названием фирмы
    private final int INN_COLUMN = 4; // № столбца с ИНН
    private final int DESCRIPTION_COLUMN = 9; // № столбца с описанием

    @Autowired
    private ExcelResults excelResults; // все результаты после парсинга

    // парсит данные с файла и вносит результаты
    @Override
    public void parse(InputStream inputStream) {
        try {
            super.setWorkbook(new XSSFWorkbook(inputStream));
            Sheet sheetForParsing = super.createSheetForParsing();
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
            mapCellsWithTypos.put(this, cellWithTypos);
            excelResults.setCellsWithTypos(mapCellsWithTypos);
            excelResults.setCostList(costList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
