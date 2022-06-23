package ru.budgetapteka.pharmacyecosystem.service.excelparsing;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.budgetapteka.pharmacyecosystem.service.Pharmacy;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsTo;
import ru.budgetapteka.pharmacyecosystem.to.FinancialResultsToImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Getter
public class ExcelParserImpl implements ExcelParser {

    private static final Logger log = getLogger(ExcelParserImpl.class);

    private final Parseable parseable;
    private List<Row> rowsWithTypos;

    public ExcelParserImpl(Parseable parseable) {
        this.parseable = parseable;
        log.info("Парсер создан");
    }

    @Override
    public void parse1CStatement() {
        ExcelFile1C excel = (ExcelFile1C) this.parseable;
        Sheet sheet = excel.getWorkbook().getSheetAt(0);
        List<Pharmacy> pharmaciesWithDataFrom1C = new ArrayList<>();
        Cell cellWithDate = sheet.getRow(excel.getDATE_ROW()).getCell(excel.getDATE_COLUMN());
        ParsedResults.setDate(DataExtractor.extractDate(cellWithDate)); // дата выписки
        for (Row row : sheet) {
            // исключаем ненужные ряды
            if (row.getRowNum() >= excel.getSTART_ROW()
                    && row.getRowNum() < sheet.getLastRowNum()) {
                Pharmacy pharmacyWithData = parseDataForEachPharmacy_1C(excel, row);
                pharmaciesWithDataFrom1C.add(pharmacyWithData);
            } else if (row.getRowNum() == sheet.getLastRowNum()) {
                parseLastRow_1C(excel, row);
            }
        }
        ParsedResults.setPharmaciesWithData(pharmaciesWithDataFrom1C);
    }

    @Override
    public void parseBankStatement() {
        ExcelFileBankStatement excel = (ExcelFileBankStatement) this.parseable;
        Sheet sheet = excel.getWorkbook().getSheetAt(0);
        List<Cost> costs = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() >= excel.getCOST_START() // расходы начинаются стандартно с 13 ряда
                    && row.getCell(excel.getDEBIT_COLUMN()).getCellType() == CellType.NUMERIC) {
                Cost cost = parseForCosts_BS(excel, row);
                costs.add(cost);
            }
        }
        ParsedResults.setCosts(costs);
        log.info("Количество расходов: {}", ParsedResults.getCosts().size());
//       Опечатки сохраняем только в случае, если они были
        if (rowsWithTypos.size() > 0) {
            Map<Workbook, List<Row>> mapWithTypos = new HashMap<>();
            mapWithTypos.put(excel.getWorkbook(), rowsWithTypos);
            ParsedResults.setCellsWithTypos(mapWithTypos);
        }

    }

    //    Собираем данные из 1С выписки по аптекам
    private Pharmacy parseDataForEachPharmacy_1C(ExcelFile1C oneC, Row row) {
        Cell cellWithName = row.getCell(oneC.getNAME_COLUMN());
        Integer id = DataExtractor.extractNumberOfPharmacy(cellWithName); // Забираем номер аптеки
        log.info("Парсим данные для аптеки номер {}", id);
        Pharmacy pharmacy = new Pharmacy(id);
        BigDecimal turnOver = BigDecimal.valueOf(row.getCell(oneC.getTURN_OVER_COLUMN()).getNumericCellValue());
        pharmacy.setTurnOverForMonth(turnOver);
        BigDecimal grossProfit = BigDecimal.valueOf(row.getCell(oneC.getGROSS_PROFIT_COLUMN()).getNumericCellValue());
        pharmacy.setGrossProfit(grossProfit);
        BigDecimal costPrice = BigDecimal.valueOf(row.getCell(oneC.getCOST_PRICE_COLUMN()).getNumericCellValue());
        pharmacy.setCostPrice(costPrice);
        return pharmacy;
    }
// Парсим последний ряд для общих данных (выручка, валовая прибыль, себестоимость)
    private void parseLastRow_1C(ExcelFile1C oneC, Row lastRow) {
        log.info("Парсим последний ряд из 1С выписки");
        BigDecimal totalTurnOver = BigDecimal.valueOf(lastRow.getCell(oneC.getTURN_OVER_COLUMN()).getNumericCellValue());
        ParsedResults.setTotalTurnOver(totalTurnOver);
        BigDecimal totalGrossProfit = BigDecimal.valueOf(lastRow.getCell(oneC.getGROSS_PROFIT_COLUMN()).getNumericCellValue());
        ParsedResults.setTotalGrossProfit(totalGrossProfit);
        BigDecimal totalCostPrice = BigDecimal.valueOf(lastRow.getCell(oneC.getCOST_PRICE_COLUMN()).getNumericCellValue());
        ParsedResults.setTotalCostPrice(totalCostPrice);
    }

    private Cost parseForCosts_BS(ExcelFileBankStatement bs, Row row) {
        this.rowsWithTypos = new ArrayList<>();
        String inn = row.getCell(bs.getINN_COLUMN()).getStringCellValue();
        String name = row.getCell(bs.getNAME_COLUMN()).getStringCellValue();
        BigDecimal amount = BigDecimal.valueOf(row.getCell(bs.getDEBIT_COLUMN()).getNumericCellValue());
        Cell description = row.getCell(bs.getDESCRIPTION_COLUMN());
        Cost cost = null;
        if (!inn.isBlank() && !amount.toString().isBlank()) {
            cost = new Cost(Long.parseLong(inn));
            cost.setName(name);
            cost.setAmount(amount);
            cost.setBelongingCosts(DataExtractor.extractPharmacyNumbers(description));
        } else {
            log.error("Ошибка в банковской выписке в ряде: {}", row.getRowNum());
            rowsWithTypos.add(row);
        }
        return cost;

    }
}
